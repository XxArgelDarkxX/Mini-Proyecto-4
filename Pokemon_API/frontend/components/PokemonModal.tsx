'use client';

import React, { useState } from 'react';
import { Pokemon } from '@/lib/api';
import styles from './PokemonModal.module.css';

interface PokemonModalProps {
  pokemon: Pokemon;
  isOpen: boolean;
  onClose: () => void;
}

export default function PokemonModal({ pokemon, isOpen, onClose }: PokemonModalProps) {
  const [showMoves, setShowMoves] = useState(false);

  if (!isOpen) return null;

  const getPokemonSprite = (pokedexId: number) => {
    return `https://raw.githubusercontent.com/PokeAPI/sprites/master/sprites/pokemon/${pokedexId}.png`;
  };

  const getTypeColor = (type: string) => {
    const colors: { [key: string]: string } = {
      'FIRE': '#FF6B6B',
      'WATER': '#4ECDC4',
      'GRASS': '#78e47d',
      'GROUND': '#D7997A',
      'ELECTRIC': '#FFE66D',
      'FLYING': '#B8E0F1',
      'PSYCHIC': '#ee76a4',
      'ICE': '#ABE5E5',
      'BUG': '#459545',
      'ROCK': '#B8B8B8',
      'GHOST': '#4e0467',
      'DRAGON': '#C4A0F0',
      'FIGHTING': '#D47A3A',
      'FAIRY': '#ffb4e6',
      'DARK': '#8C7563',
      'NORMAL': '#DDD',
      'STEEL': '#C5C5C5',
      'POISON': '#b730dc'
    };
    return colors[type] || '#999';
  };

  return (
    <div className={styles.overlay} onClick={onClose}>
      <div className={styles.modal} onClick={(e) => e.stopPropagation()}>
        {/* Header con botón cerrar */}
        <button className={styles.closeBtn} onClick={onClose}>✕</button>

        {/* Selector de vistas */}
        <div className={styles.viewToggle}>
          <button
            className={`${styles.tabBtn} ${!showMoves ? styles.active : ''}`}
            onClick={() => setShowMoves(false)}
          >
            Stats
          </button>
          <button
            className={`${styles.tabBtn} ${showMoves ? styles.active : ''}`}
            onClick={() => setShowMoves(true)}
          >
            Movimientos
          </button>
        </div>

        {/* Vista de Stats */}
        {!showMoves && (
          <div className={styles.statsView}>
            {/* Sprite frontal en la izquierda */}
            <div className={styles.spriteContainer}>
              <div className={styles.levelDisplay}>
                Lv. {pokemon.level ?? 5}
              </div>
              <img
                src={getPokemonSprite(pokemon.pokedex_id)}
                alt={pokemon.name}
                className={styles.frontSprite}
              />
            </div>

            {/* Stats en la derecha */}
            <div className={styles.statsContent}>
              <div className={styles.pokemonHeader}>
                <h2>{pokemon.name.charAt(0).toUpperCase() + pokemon.name.slice(1)}</h2>
                <div className={styles.typesTags}>
                  {pokemon.types && pokemon.types.length > 0 
                    ? pokemon.types.map((type) => (
                        <span
                          key={type}
                          className={styles.typeTag}
                          style={{ backgroundColor: getTypeColor(type) }}
                        >
                          {type}
                        </span>
                      ))
                    : null
                  }
                </div>
              </div>

              {/* Experience bar */}
              <div className={styles.expSection}>
                <div className={styles.expLabel}>
                  <span>EXP</span>
                  <span>
                    {pokemon.experience ?? 0} / {pokemon.next_level_exp ?? 100}
                  </span>
                </div>
                <div className={styles.expBar}>
                  <div
                    className={styles.expFill}
                    style={{
                      width: `${Math.min(
                        100,
                        ((pokemon.experience ?? 0) / (pokemon.next_level_exp ?? 100)) * 100
                      )}%`
                    }}
                  ></div>
                </div>
              </div>

              {/* Stats Grid */}
              <div className={styles.statsGrid}>
                <div className={styles.statItem}>
                  <span className={styles.statLabel}>HP</span>
                  <span className={styles.statValue}>
                    {pokemon.base_hp ?? 45}
                  </span>
                </div>
                <div className={styles.statItem}>
                  <span className={styles.statLabel}>ATK</span>
                  <span className={styles.statValue}>
                    {pokemon.base_attack ?? 49}
                  </span>
                </div>
                <div className={styles.statItem}>
                  <span className={styles.statLabel}>DEF</span>
                  <span className={styles.statValue}>
                    {pokemon.base_defense ?? 49}
                  </span>
                </div>
                <div className={styles.statItem}>
                  <span className={styles.statLabel}>SP.ATK</span>
                  <span className={styles.statValue}>
                    {pokemon.base_special_attack ?? 65}
                  </span>
                </div>
                <div className={styles.statItem}>
                  <span className={styles.statLabel}>SP.DEF</span>
                  <span className={styles.statValue}>
                    {pokemon.base_special_defense ?? 65}
                  </span>
                </div>
                <div className={styles.statItem}>
                  <span className={styles.statLabel}>SPD</span>
                  <span className={styles.statValue}>
                    {pokemon.base_speed ?? 45}
                  </span>
                </div>
              </div>
            </div>
          </div>
        )}

        {/* Vista de Movimientos */}
        {showMoves && (
          <div className={styles.movesView}>
            {/* Sprite frontal en la izquierda */}
            <div className={styles.spriteContainer}>
              <div className={styles.levelDisplay}>
                Lv. {pokemon.level ?? 5}
              </div>
              <img
                src={getPokemonSprite(pokemon.pokedex_id)}
                alt={pokemon.name}
                className={styles.frontSprite}
              />
            </div>

            {/* Movimientos en la derecha */}
            <div className={styles.movesContent}>
              <h3>Movimientos</h3>
              <div className={styles.movesGrid}>
                {pokemon.moves && pokemon.moves.length > 0 ? (
                  pokemon.moves.map((move, index) => (
                    <div key={index} className={styles.moveCard}>
                      <div className={styles.moveLeft}>
                        <div className={styles.moveName}>
                          {move.name.charAt(0).toUpperCase() + move.name.slice(1)}
                        </div>
                        <div
                          className={styles.moveType}
                          style={{ backgroundColor: getTypeColor(move.type || 'NORMAL') }}
                        >
                          {move.type}
                        </div>
                      </div>
                      <div className={styles.movePower}>
                        {move.power || '-'}
                      </div>
                    </div>
                  ))
                ) : (
                  <div className={styles.noMoves}>
                    Este Pokémon no tiene movimientos
                  </div>
                )}
              </div>
            </div>
          </div>
        )}
      </div>
    </div>
  );
}
