import { Trainer } from '@/lib/api';
import styles from './TrainerCard.module.css';

interface TrainerCardProps {
  trainer: Trainer;
}

export default function TrainerCard({ trainer }: TrainerCardProps) {
  // Obtener URL del sprite del entrenador (1-8)
  const getTrainerSprite = (spriteNum: number) => {
    return `https://raw.githubusercontent.com/PokeAPI/sprites/master/sprites/pokemon/other/official-artwork/${spriteNum}.png`;
  };

  // Obtener sprite de Pokémon
  const getPokemonSprite = (pokedexId: number) => {
    return `https://raw.githubusercontent.com/PokeAPI/sprites/master/sprites/pokemon/${pokedexId}.png`;
  };

  const getExpProgress = (experience?: number, nextLevelExp?: number) => {
    const exp = typeof experience === 'number' ? experience : 0;
    const next = typeof nextLevelExp === 'number' && nextLevelExp > 0 ? nextLevelExp : 100;
    return Math.min(100, Math.max(0, (exp / next) * 100));
  };

  return (
    <div className={styles.card}>
      {/* Header con información básica */}
      <div className={styles.header}>
        <div className={styles.trainerInfo}>
          <img 
            src={getTrainerSprite(trainer.sprite)} 
            alt={trainer.name}
            className={styles.trainerSprite}
          />
          <div className={styles.trainerDetails}>
            <h2>{trainer.name}</h2>
            <p className={styles.username}>@{trainer.username}</p>
            <p className={styles.suid}>ID: #{trainer.suid}</p>
          </div>
        </div>

        <div className={styles.stats}>
          <div className={styles.statBox}>
            <span className={styles.statValue}>{trainer.wins}</span>
            <span className={styles.statLabel}>Victorias</span>
          </div>
          <div className={styles.statBox}>
            <span className={styles.statValue}>{trainer.losses}</span>
            <span className={styles.statLabel}>Derrotas</span>
          </div>
          <div className={styles.statBox}>
            <span className={styles.statValue}>
              {trainer.wins + trainer.losses > 0 
                ? ((trainer.wins / (trainer.wins + trainer.losses)) * 100).toFixed(0)
                : 0}%
            </span>
            <span className={styles.statLabel}>Ratio</span>
          </div>
        </div>
      </div>

      {/* Equipo de batalla */}
      <div className={styles.section}>
        <h3>⚔️ Equipo de Batalla</h3>
        <div className={styles.team}>
          {trainer.team_detail && trainer.team_detail.length > 0 ? (
            trainer.team_detail.map((pokemon) => (
              <div key={pokemon.pokedex_id} className={styles.pokemonCard}>
                <span className={styles.levelBadge}>Lv. {pokemon.level ?? 50}</span>
                <img 
                  src={getPokemonSprite(pokemon.pokedex_id)}
                  alt={pokemon.name}
                  className={styles.pokemonSprite}
                />
                <div className={styles.pokemonInfo}>
                  <span className={styles.pokemonName}>
                    {pokemon.name.charAt(0).toUpperCase() + pokemon.name.slice(1)}
                  </span>
                  <span className={styles.pokemonId}>#{pokemon.pokedex_id}</span>
                  <div className={styles.expRow}>
                    <div className={styles.expBar}>
                      <div
                        className={styles.expFill}
                        style={{ width: `${getExpProgress(pokemon.experience, pokemon.next_level_exp)}%` }}
                      ></div>
                    </div>
                    <span className={styles.expLabel}>
                      {pokemon.experience ?? 0} / {pokemon.next_level_exp ?? 100} EXP
                    </span>
                  </div>
                  <div className={styles.types}>
                    {pokemon.types && pokemon.types.length > 0 ? pokemon.types.map((type) => (
                      <span key={type} className={`${styles.type} ${styles[type]}`}>
                        {type.toUpperCase()}
                      </span>
                    )) : null}
                  </div>
                </div>
              </div>
            ))
          ) : (
            <div className={styles.empty}>
              <p>No tienes Pokémon en tu equipo</p>
            </div>
          )}
          
          {/* Slots vacíos */}
          {(trainer.team?.length || 0) < 3 && Array.from({ length: 3 - (trainer.team?.length || 0) }).map((_, i) => (
            <div key={`empty-${i}`} className={styles.emptySlot}>
              <span>Slot vacío</span>
            </div>
          ))}
        </div>
      </div>

      {/* Inventario */}
      <div className={styles.section}>
        <h3>📦 Inventario ({trainer.inventory?.length || 0} Pokémon)</h3>
        <div className={styles.inventory}>
          {trainer.inventory_detail && trainer.inventory_detail.length > 0 ? (
            trainer.inventory_detail.map((pokemon) => (
              <div key={pokemon.pokedex_id} className={styles.inventoryItem}>
                <img 
                  src={getPokemonSprite(pokemon.pokedex_id)}
                  alt={pokemon.name}
                  title={pokemon.name.charAt(0).toUpperCase() + pokemon.name.slice(1)}
                />
                <span>#{pokemon.pokedex_id}</span>
              </div>
            ))
          ) : (
            <div className={styles.empty}>
              <p>Tu inventario está vacío</p>
            </div>
          )}
        </div>
      </div>
    </div>
  );
}
