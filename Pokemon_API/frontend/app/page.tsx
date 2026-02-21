'use client';

import { useEffect } from 'react';
import { useRouter } from 'next/navigation';
import { isAuthenticated } from '@/lib/api';
import styles from './home.module.css';

export default function Home() {
  const router = useRouter();

  useEffect(() => {
    // Si ya está autenticado, redirigir al dashboard
    if (isAuthenticated()) {
      router.push('/dashboard');
    }
  }, [router]);

  return (
    <div className={styles.container}>
      <div className={styles.hero}>
        <h1 className={styles.title}>🎮 Pokémon Battle</h1>
        <p className={styles.subtitle}>
          Conviértete en el mejor entrenador Pokémon
        </p>
        
        <div className={styles.features}>
          <div className={styles.feature}>
            <span className={styles.featureIcon}>⚔️</span>
            <h3>Batallas Épicas</h3>
            <p>Enfrenta a otros entrenadores en combates estratégicos</p>
          </div>
          
          <div className={styles.feature}>
            <span className={styles.featureIcon}>📦</span>
            <h3>Captura Pokémon</h3>
            <p>Expande tu colección con los 151 Pokémon de Gen 1</p>
          </div>
          
          <div className={styles.feature}>
            <span className={styles.featureIcon}>🏆</span>
            <h3>Sube de Rango</h3>
            <p>Demuestra tu habilidad y alcanza la cima</p>
          </div>
        </div>

        <div className={styles.actions}>
          <button 
            onClick={() => router.push('/register')} 
            className={styles.primaryBtn}
          >
            Comenzar Aventura
          </button>
          <button 
            onClick={() => router.push('/login')} 
            className={styles.secondaryBtn}
          >
            Ya tengo cuenta
          </button>
        </div>

        <div className={styles.info}>
          <p>💡 <strong>Tip:</strong> Cada entrenador comienza con 3 Pokémon iniciales</p>
        </div>
      </div>
    </div>
  );
}

