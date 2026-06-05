import { Trainer, Pokemon } from '@/lib/api';
import { useState } from 'react';
import PokemonModal from './PokemonModal';
import styles from './TrainerCard.module.css';

interface TrainerCardProps {
  trainer: Trainer;
  onTeamUpdate?: () => void;
}

export default function TrainerCard({ trainer, onTeamUpdate }: TrainerCardProps) {
  const [selectedPokemon, setSelectedPokemon] = useState<Pokemon | null>(null);
  const [showModal, setShowModal] = useState(false);
  const [toastMessage, setToastMessage] = useState<string | null>(null);
  const [isLoading, setIsLoading] = useState(false);

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

  const handleShowToast = (message: string) => {
    setToastMessage(message);
    setTimeout(() => setToastMessage(null), 3000);
  };

  const handlePokemonClick = (pokemon: Pokemon) => {
    setSelectedPokemon(pokemon);
    setShowModal(true);
  };

  const handleAddToTeam = async (pokemon: Pokemon) => {
    const teamSize = trainer.team?.length || 0;
    if (teamSize >= 3) {
      handleShowToast('❌ Tu equipo está completo (máximo 3 pokémon)');
      return;
    }

    setIsLoading(true);
    try {
      const response = await fetch(
        `${process.env.NEXT_PUBLIC_API_URL}/trainers/${trainer.suid}/team`,
        {
          method: 'POST',
          headers: {
            'Content-Type': 'application/json',
            'Authorization': `Bearer ${localStorage.getItem('token')}`
          },
          body: JSON.stringify({ pokedex_id: pokemon.pokedex_id })
        }
      );

      if (!response.ok) {
        throw new Error('Error al agregar pokémon al equipo');
      }

      handleShowToast(`✅ ${pokemon.name} agregado al equipo`);
      onTeamUpdate?.();
    } catch (error) {
      handleShowToast(`❌ Error: ${(error as Error).message}`);
    } finally {
      setIsLoading(false);
    }
  };

  const handleRemoveFromTeam = async (pokemon: Pokemon) => {
    setIsLoading(true);
    try {
      const teamIndex = trainer.team_detail?.findIndex(p => p.pokedex_id === pokemon.pokedex_id);
      if (teamIndex === undefined || teamIndex === -1) {
        throw new Error('Pokémon no encontrado en el equipo');
      }

      const response = await fetch(
        `${process.env.NEXT_PUBLIC_API_URL}/trainers/${trainer.suid}/team/${teamIndex}`,
        {
          method: 'DELETE',
          headers: {
            'Authorization': `Bearer ${localStorage.getItem('token')}`
          }
        }
      );

      if (!response.ok) {
        throw new Error('Error al remover pokémon del equipo');
      }

      handleShowToast(`✅ ${pokemon.name} removido del equipo`);
      onTeamUpdate?.();
    } catch (error) {
      handleShowToast(`❌ Error: ${(error as Error).message}`);
    } finally {
      setIsLoading(false);
    }
  };

  const hasTeamSpace = (trainer.team?.length || 0) < 3;

  return (
    <>
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
            <div className={styles.statBox}>
              <span className={styles.statValue}>{trainer.points}</span>
              <span className={styles.statLabel}>Puntos</span>
            </div>
          </div>
        </div>

        {/* Equipo de batalla */}
        <div className={styles.section}>
          <h3>⚔️ Equipo de Batalla</h3>
          <div className={styles.team}>
            {trainer.team_detail && trainer.team_detail.length > 0 ? (
              trainer.team_detail.map((pokemon) => (
                <div 
                  key={pokemon.pokedex_id} 
                  className={styles.pokemonCard}
                  onClick={() => handlePokemonClick(pokemon)}
                >
                  <span className={styles.levelBadge}>Lv. {pokemon.level ?? 5}</span>
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
                        {pokemon.experience ?? 0} / {pokemon.next_level_exp ?? 100}
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

                  {/* Overlay con opciones */}
                  <div className={styles.cardOverlay}>
                    <div className={styles.cardActions}>
                      <button 
                        className={styles.actionBtn}
                        onClick={(e) => {
                          e.stopPropagation();
                          handleRemoveFromTeam(pokemon);
                        }}
                        disabled={isLoading}
                      >
                        Desequipar
                      </button>
                      <button 
                        className={`${styles.actionBtn} ${styles.secondary}`}
                        onClick={(e) => {
                          e.stopPropagation();
                          handlePokemonClick(pokemon);
                        }}
                      >
                        Ver detalles
                      </button>
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
                <div 
                  key={pokemon.pokedex_id} 
                  className={styles.inventoryItem}
                  onClick={() => handlePokemonClick(pokemon)}
                >
                  <img 
                    src={getPokemonSprite(pokemon.pokedex_id)}
                    alt={pokemon.name}
                    title={pokemon.name.charAt(0).toUpperCase() + pokemon.name.slice(1)}
                  />
                  <span>#{pokemon.pokedex_id}</span>

                  {/* Overlay con opciones */}
                  <div className={styles.inventoryOverlay}>
                    <div className={styles.inventoryActions}>
                      {hasTeamSpace ? (
                        <button 
                          className={styles.addBtn}
                          onClick={(e) => {
                            e.stopPropagation();
                            handleAddToTeam(pokemon);
                          }}
                          disabled={isLoading}
                        >
                          Agregar al equipo
                        </button>
                      ) : (
                        <div className={styles.teamFullBadge}>
                          Equipo lleno
                        </div>
                      )}
                      <button 
                        className={`${styles.addBtn} ${styles.secondary}`}
                        onClick={(e) => {
                          e.stopPropagation();
                          handlePokemonClick(pokemon);
                        }}
                      >
                        Ver detalles
                      </button>
                    </div>
                  </div>
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

      {/* Modal de Pokémon */}
      {selectedPokemon && (
        <PokemonModal 
          pokemon={selectedPokemon}
          isOpen={showModal}
          onClose={() => {
            setShowModal(false);
            setSelectedPokemon(null);
          }}
        />
      )}

      {/* Toast notification */}
      {toastMessage && (
        <div className={styles.toast}>
          {toastMessage}
        </div>
      )}
    </>
  );
}
