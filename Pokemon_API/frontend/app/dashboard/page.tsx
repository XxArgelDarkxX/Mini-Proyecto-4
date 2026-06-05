'use client';

import { useEffect, useRef, useState } from 'react';
import { useRouter } from 'next/navigation';
import { captureRandomPokemon, getMyTrainer, logout, isAuthenticated, redeemCoupon, type CapturedPokemon, type Trainer } from '@/lib/api';
import TrainerCard from '@/components/TrainerCard';
import styles from './dashboard.module.css';

export default function DashboardPage() {
  const router = useRouter();
  const [trainer, setTrainer] = useState<Trainer | null>(null);
  const [loading, setLoading] = useState(true);
  const [error, setError] = useState('');
  const [isCapturing, setIsCapturing] = useState(false);
  const [showGachaModal, setShowGachaModal] = useState(false);
  const [spinSprite, setSpinSprite] = useState<string | null>(null);
  const [capturedPokemon, setCapturedPokemon] = useState<CapturedPokemon | null>(null);
  const [captureError, setCaptureError] = useState('');
  const [redeemingCoupon, setRedeemingCoupon] = useState(false);
  const [couponError, setCouponError] = useState('');
  const gachaTimeoutRef = useRef<ReturnType<typeof setTimeout> | null>(null);

  // Cargar datos del entrenador
  const loadTrainer = async () => {
    try {
      setLoading(true);
      const data = await getMyTrainer();
      setTrainer(data);
    } catch (err: any) {
      // Si no tiene entrenador (404), redirigir al onboarding
      if (err.status === 404) {
        router.push('/onboarding');
      } else {
        setError(err.message || 'Error al cargar datos');
      }
    } finally {
      setLoading(false);
    }
  };

  useEffect(() => {
    // Verificar autenticación
    if (!isAuthenticated()) {
      router.push('/login');
      return;
    }

    loadTrainer();
  }, [router]);

  useEffect(() => {
    return () => {
      if (gachaTimeoutRef.current) {
        clearTimeout(gachaTimeoutRef.current);
      }
    };
  }, []);

  const handleLogout = () => {
    logout();
    router.push('/login');
  };

  const runGachaSpin = async (sprites: string[], finalSprite: string, durationMs: number) => {
    return new Promise<void>((resolve) => {
      const start = Date.now();
      const pool = sprites.length > 0 ? sprites : [finalSprite];

      const tick = () => {
        const elapsed = Date.now() - start;
        const progress = Math.min(1, elapsed / durationMs);
        const randomIndex = Math.floor(Math.random() * pool.length);
        setSpinSprite(pool[randomIndex] || finalSprite);

        if (progress >= 1) {
          setSpinSprite(finalSprite);
          resolve();
          return;
        }

        const delay = Math.floor(45 + progress * 280);
        gachaTimeoutRef.current = setTimeout(tick, delay);
      };

      tick();
    });
  };

  const handleCapturePokemon = async () => {
    if (!trainer || isCapturing) return;

    setCaptureError('');
    setCapturedPokemon(null);
    setShowGachaModal(true);
    setIsCapturing(true);

    try {
      const result = await captureRandomPokemon(trainer.suid);
      await runGachaSpin(result.spin.sprites, result.spin.final_sprite, result.spin.duration_ms);
      setCapturedPokemon(result.captured_pokemon);
      await loadTrainer();
    } catch (err: any) {
      setCaptureError(err.message || 'Error al capturar Pokémon');
    } finally {
      setIsCapturing(false);
    }
  };

  const handleRedeemCoupon = async () => {
    if (!trainer || redeemingCoupon) return;

    const currentPoints = trainer.points || 0;
    if (currentPoints < 1000) {
      setCouponError(`Puntos insuficientes. Tienes ${currentPoints} puntos, pero se requieren 1000.`);
      setTimeout(() => setCouponError(''), 5000);
      return;
    }

    const confirmed = window.confirm(
      `¿Deseas canjear 1000 puntos por un cupón de comida gratis?\n\nTus puntos actuales: ${currentPoints}`
    );

    if (!confirmed) return;

    setRedeemingCoupon(true);
    setCouponError('');

    try {
      const result = await redeemCoupon(trainer.suid);
      alert(`¡Cupón canjeado exitosamente!\n\nCódigo: ${result.coupon_code}\nExpira: ${new Date(result.expiration_date).toLocaleDateString()}\n\nRedirigiendo al restaurante...`);
      
      // Redirigir a la página del restaurante
      window.location.href = result.redirect_url;
    } catch (err: any) {
      setCouponError(err.message || 'Error al canjear cupón');
      setTimeout(() => setCouponError(''), 5000);
    } finally {
      setRedeemingCoupon(false);
    }
  };

  if (loading) {
    return (
      <div className={styles.container}>
        <div className={styles.loading}>
          <div className={styles.spinner}></div>
          <p>Cargando datos del entrenador...</p>
        </div>
      </div>
    );
  }

  if (error) {
    return (
      <div className={styles.container}>
        <div className={styles.error}>
          <h2>Error</h2>
          <p>{error}</p>
          <button onClick={() => router.push('/login')} className={styles.btn}>
            Volver al inicio
          </button>
        </div>
      </div>
    );
  }

  if (!trainer) {
    return null; // Ya redirigió al onboarding
  }

  return (
    <div className={styles.container}>
      <header className={styles.header}>
        <h1>🎮 Pokémon Battle</h1>
        <button onClick={handleLogout} className={styles.logoutBtn}>
          Cerrar Sesión
        </button>
      </header>

      <main className={styles.main}>
        <TrainerCard 
          trainer={trainer}
          onTeamUpdate={() => {
            // Recargar datos del entrenador
            loadTrainer();
          }}
        />

        <div className={styles.pointsDisplay}>
          <h3>🏆 Puntos: {trainer.points || 0}</h3>
          <p>• Gana 5 puntos por cada victoria</p>
        </div>

        {couponError && (
          <div className={styles.errorBanner}>
            ❌ {couponError}
          </div>
        )}

        <div className={styles.actions}>
          <button className={styles.actionBtn} disabled>
            🎯 Buscar Batalla
          </button>
          <button 
            className={styles.actionBtn} 
            onClick={handleCapturePokemon} 
            disabled={isCapturing || (trainer.points || 0) < 15}
            title={(trainer.points || 0) < 15 ? 'Necesitas al menos 15 puntos' : 'Captura un Pokémon aleatorio'}
          >
            📦 Capturar Pokémon (15 pts)
          </button>
          <button 
            className={styles.actionBtn} 
            onClick={handleRedeemCoupon} 
            disabled={redeemingCoupon || (trainer.points || 0) < 1000}
            title={(trainer.points || 0) < 1000 ? 'Necesitas 1000 puntos' : 'Canjea un cupón de comida gratis'}
          >
            🍕 Cupón de Comida (1000 pts)
          </button>
          <button className={styles.actionBtn} disabled>
            📊 Historial
          </button>
        </div>

        <div className={styles.info}>
          <p>💡 <strong>Cómo ganar puntos:</strong> Gana 5 puntos por cada batalla ganada</p>
          <p>🎮 <strong>Usa tus puntos:</strong> Captura Pokémon (15 pts) o canjea cupones de comida (1000 pts)</p>
        </div>
      </main>

      {showGachaModal && (
        <div className={styles.gachaOverlay}>
          <div className={styles.gachaModal}>
            <h2>🎰 Captura Gacha</h2>

            <div className={styles.gachaSpriteBox}>
              {spinSprite ? (
                <img src={spinSprite} alt="Gacha sprite" className={styles.gachaSprite} />
              ) : (
                <div className={styles.gachaPlaceholder}>Iniciando giro...</div>
              )}
            </div>

            {isCapturing && <p className={styles.gachaStatus}>Girando...</p>}
            {captureError && <p className={styles.gachaError}>❌ {captureError}</p>}

            {capturedPokemon && !isCapturing && (
              <div className={styles.captureResult}>
                <p>
                  🎉 Te tocó <strong>{capturedPokemon.name}</strong> (#{capturedPokemon.pokedex_id})
                </p>
                <p>
                  Nivel: <strong>{capturedPokemon.level}</strong>
                  {capturedPokemon.level_range
                    ? ` (rango ${capturedPokemon.level_range.min}-${capturedPokemon.level_range.max})`
                    : ''}
                </p>
              </div>
            )}

            <button
              className={styles.gachaCloseBtn}
              onClick={() => setShowGachaModal(false)}
              disabled={isCapturing}
            >
              Cerrar
            </button>
          </div>
        </div>
      )}
    </div>
  );
}
