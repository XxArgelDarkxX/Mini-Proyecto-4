'use client';

import { Suspense, useState, useEffect } from 'react';
import { useRouter, useSearchParams } from 'next/navigation';
import { createTrainer, login, addPokemonToTrainer, getMyTrainer } from '@/lib/api';
import styles from './onboarding.module.css';

function OnboardingContent() {
  const router = useRouter();
  const searchParams = useSearchParams();
  const [step, setStep] = useState(0);
  const [trainerName, setTrainerName] = useState('');
  const [loading, setLoading] = useState(false);
  const [error, setError] = useState('');
  const [trainerSuid, setTrainerSuid] = useState<number | null>(null);

  const username = searchParams.get('username');
  const password = searchParams.get('password');

  useEffect(() => {
    if (!username || !password) {
      router.push('/register');
    }
  }, [username, password, router]);

  const handleCreateTrainer = async (e: React.FormEvent) => {
    e.preventDefault();
    setError('');
    setLoading(true);

    try {
      // Primero hacer login para obtener el token
      if (username && password) {
        await login({ username, password });
        
        // Crear el perfil del entrenador
        const result = await createTrainer({ name: trainerName });
        setTrainerSuid(result.suid);
        
        setStep(1); // Ir al tutorial
      }
    } catch (err: any) {
      setError(err.message || 'Error al crear entrenador');
    } finally {
      setLoading(false);
    }
  };

  const handleAddStarterPokemon = async () => {
    if (!trainerSuid) return;
    
    setLoading(true);
    try {
      // Agregar 3 Pokémon iniciales populares (Bulbasaur, Charmander, Squirtle)
      await addPokemonToTrainer(trainerSuid, "1");  // Bulbasaur
      await addPokemonToTrainer(trainerSuid, "4");  // Charmander  
      await addPokemonToTrainer(trainerSuid, "7");  // Squirtle
      
      setStep(2);
    } catch (err: any) {
      setError(err.message || 'Error al agregar Pokémon');
    } finally {
      setLoading(false);
    }
  };

  const handleFinish = () => {
    router.push('/dashboard');
  };

  if (step === 0) {
    return (
      <div className={styles.container}>
        <div className={styles.card}>
          <div className={styles.welcome}>
            <h1>🎉 ¡Bienvenido a Pokémon Battle!</h1>
            <p>Estás a punto de comenzar tu aventura como entrenador Pokémon</p>
          </div>

          <form onSubmit={handleCreateTrainer} className={styles.form}>
            <div className={styles.inputGroup}>
              <label htmlFor="trainerName">¿Cómo te gustaría que te llamen?</label>
              <input
                id="trainerName"
                type="text"
                value={trainerName}
                onChange={(e) => setTrainerName(e.target.value)}
                required
                placeholder="Tu apodo de entrenador"
                disabled={loading}
                minLength={2}
                maxLength={20}
              />
              <small>Este será tu nombre visible en las batallas</small>
            </div>

            {error && <div className={styles.error}>{error}</div>}

            <button type="submit" className={styles.btn} disabled={loading}>
              {loading ? 'Creando perfil...' : 'Continuar'}
            </button>
          </form>
        </div>
      </div>
    );
  }

  if (step === 1) {
    return (
      <div className={styles.container}>
        <div className={styles.card}>
          <div className={styles.tutorial}>
            <h2>📚 Tutorial Rápido</h2>
            
            <div className={styles.tutorialSection}>
              <h3>⚔️ Tu Equipo de Batalla</h3>
              <p>
                Puedes tener hasta <strong>3 Pokémon</strong> en tu equipo de batalla.
                Estos serán los que uses en combate contra otros entrenadores.
              </p>
            </div>

            <div className={styles.tutorialSection}>
              <h3>📦 Tu Inventario</h3>
              <p>
                Almacena todos los Pokémon que captures. Puedes intercambiarlos
                con tu equipo de batalla en cualquier momento.
              </p>
            </div>

            <div className={styles.tutorialSection}>
              <h3>🎯 Batallas</h3>
              <p>
                Gana batallas para aumentar tu ratio de victorias y demostrar
                que eres el mejor entrenador Pokémon.
              </p>
            </div>

            <div className={styles.tutorialSection}>
              <h3>🎁 Para empezar...</h3>
              <p>
                Te regalaremos <strong>3 Pokémon iniciales</strong> para que comiences
                tu aventura: ¡Bulbasaur, Charmander y Squirtle!
              </p>
            </div>

            {error && <div className={styles.error}>{error}</div>}

            <button 
              onClick={handleAddStarterPokemon} 
              className={styles.btn}
              disabled={loading}
            >
              {loading ? 'Preparando Pokémon...' : '¡Recibir Pokémon iniciales!'}
            </button>
          </div>
        </div>
      </div>
    );
  }

  if (step === 2) {
    return (
      <div className={styles.container}>
        <div className={styles.card}>
          <div className={styles.success}>
            <h2>✨ ¡Felicidades, {trainerName}!</h2>
            
            <div className={styles.starterPokemon}>
              <div className={styles.pokemonStarter}>
                <img 
                  src="https://raw.githubusercontent.com/PokeAPI/sprites/master/sprites/pokemon/1.png" 
                  alt="Bulbasaur"
                />
                <span>Bulbasaur</span>
              </div>
              <div className={styles.pokemonStarter}>
                <img 
                  src="https://raw.githubusercontent.com/PokeAPI/sprites/master/sprites/pokemon/4.png" 
                  alt="Charmander"
                />
                <span>Charmander</span>
              </div>
              <div className={styles.pokemonStarter}>
                <img 
                  src="https://raw.githubusercontent.com/PokeAPI/sprites/master/sprites/pokemon/7.png" 
                  alt="Squirtle"
                />
                <span>Squirtle</span>
              </div>
            </div>

            <p className={styles.successText}>
              Has recibido tus 3 Pokémon iniciales. ¡Ya forman parte de tu equipo de batalla!
            </p>

            <button onClick={handleFinish} className={styles.btn}>
              Ir a mi Dashboard
            </button>
          </div>
        </div>
      </div>
    );
  }

  return null;
}

export default function OnboardingPage() {
  return (
    <Suspense fallback={
      <div className={styles.container}>
        <div className={styles.loading}>
          <div className={styles.spinner}></div>
          <p>Cargando...</p>
        </div>
      </div>
    }>
      <OnboardingContent />
    </Suspense>
  );
}


