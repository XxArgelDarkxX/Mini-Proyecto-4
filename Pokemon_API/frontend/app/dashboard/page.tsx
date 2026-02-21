'use client';

import { useEffect, useState } from 'react';
import { useRouter } from 'next/navigation';
import { getMyTrainer, logout, isAuthenticated, type Trainer } from '@/lib/api';
import TrainerCard from '@/components/TrainerCard';
import styles from './dashboard.module.css';

export default function DashboardPage() {
  const router = useRouter();
  const [trainer, setTrainer] = useState<Trainer | null>(null);
  const [loading, setLoading] = useState(true);
  const [error, setError] = useState('');

  useEffect(() => {
    // Verificar autenticación
    if (!isAuthenticated()) {
      router.push('/login');
      return;
    }

    // Cargar datos del entrenador
    const loadTrainer = async () => {
      try {
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

    loadTrainer();
  }, [router]);

  const handleLogout = () => {
    logout();
    router.push('/login');
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
        <TrainerCard trainer={trainer} />

        <div className={styles.actions}>
          <button className={styles.actionBtn} disabled>
            🎯 Buscar Batalla
          </button>
          <button className={styles.actionBtn} disabled>
            📦 Capturar Pokémon
          </button>
          <button className={styles.actionBtn} disabled>
            📊 Historial
          </button>
        </div>

        <div className={styles.info}>
          <p>💡 <strong>Próximamente:</strong> Sistema de batallas, capturas y más funciones</p>
        </div>
      </main>
    </div>
  );
}
