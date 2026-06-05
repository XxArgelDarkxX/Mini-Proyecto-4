// API Client para comunicarse con el backend FastAPI
const API_URL = process.env.NEXT_PUBLIC_API_URL || 'http://localhost:8000/api';

export interface RegisterData {
  username: string;
  password: string;
}

export interface LoginData {
  username: string;
  password: string;
}

export interface TrainerData {
  name: string;
}

export interface AuthResponse {
  access_token: string;
  token_type: string;
}

export interface Trainer {
  suid: number;
  username: string;
  name: string;
  sprite: number;
  wins: number;
  losses: number;
  points: number;
  team: number[];
  inventory: number[];
  team_detail?: Pokemon[];
  inventory_detail?: Pokemon[];
}

export interface Pokemon {
  pokedex_id: number;
  name: string;
  level?: number;
  experience?: number;
  next_level_exp?: number;
  types: string[];
  base_hp?: number;
  base_attack?: number;
  base_defense?: number;
  base_special_attack?: number;
  base_special_defense?: number;
  base_speed?: number;
  base_stats: {
    hp: number;
    attack: number;
    defense: number;
    special_attack: number;
    special_defense: number;
    speed: number;
  };
  moves: Move[];
  sprites: {
    front_default: string;
    back_default: string;
  };
}

export interface Move {
  name: string;
  type: string;
  power: number;
  accuracy: number;
}

export interface CaptureSpinInfo {
  duration_ms: number;
  sprites: string[];
  final_sprite: string;
}

export interface CapturedPokemon extends Pokemon {
  level_range?: {
    min: number;
    max: number;
  };
}

export interface CapturePokemonResponse {
  status: string;
  trainer_suid: number;
  points_remaining: number;
  points_spent: number;
  team_count: number;
  inventory_count: number;
  spin: CaptureSpinInfo;
  captured_pokemon: CapturedPokemon;
}

export interface RedeemCouponResponse {
  status: string;
  trainer_suid: number;
  points_remaining: number;
  points_redeemed: number;
  coupon_code: string;
  expiration_date: string;
  redirect_url: string;
  message: string;
}

// Función auxiliar para obtener el token almacenado
function getToken(): string | null {
  if (typeof window === 'undefined') return null;
  return localStorage.getItem('token');
}

// Función auxiliar para headers con autenticación
function getAuthHeaders(): HeadersInit {
  const token = getToken();
  return {
    'Content-Type': 'application/json',
    ...(token && { 'Authorization': `Bearer ${token}` })
  };
}

// AUTH ENDPOINTS
export async function register(data: RegisterData): Promise<{ message: string }> {
  const response = await fetch(`${API_URL}/auth/register`, {
    method: 'POST',
    headers: { 'Content-Type': 'application/json' },
    body: JSON.stringify(data)
  });

  if (!response.ok) {
    const error = await response.json();
    throw new Error(error.detail || 'Error al registrarse');
  }

  return response.json();
}

export async function login(data: LoginData): Promise<AuthResponse> {
  const response = await fetch(`${API_URL}/auth/login`, {
    method: 'POST',
    headers: { 'Content-Type': 'application/json' },
    body: JSON.stringify(data)
  });

  if (!response.ok) {
    const error = await response.json();
    throw new Error(error.detail || 'Error al iniciar sesión');
  }

  const authData = await response.json();
  
  // Guardar token en localStorage
  if (typeof window !== 'undefined') {
    localStorage.setItem('token', authData.access_token);
  }

  return authData;
}

export function logout(): void {
  if (typeof window !== 'undefined') {
    localStorage.removeItem('token');
  }
}

export function isAuthenticated(): boolean {
  return getToken() !== null;
}

// TRAINER ENDPOINTS
export async function createTrainer(data: TrainerData): Promise<{ suid: number; status: string; trainer: Trainer }> {
  const response = await fetch(`${API_URL}/trainers/`, {
    method: 'POST',
    headers: getAuthHeaders(),
    body: JSON.stringify(data)
  });

  if (!response.ok) {
    const error = await response.json();
    throw new Error(error.detail || 'Error al crear entrenador');
  }

  return response.json();
}

export async function getMyTrainer(): Promise<Trainer> {
  const response = await fetch(`${API_URL}/trainers/me`, {
    method: 'GET',
    headers: getAuthHeaders()
  });

  if (!response.ok) {
    const error = await response.json();
    const err: any = new Error(error.detail || 'Error al obtener entrenador');
    err.status = response.status;
    throw err;
  }

  const trainer = await response.json();
  
  // Función auxiliar para normalizar Pokémon
  const normalizePokemon = (pokemon: any) => ({
    ...pokemon,
    level: typeof pokemon.level === 'number' ? pokemon.level : 5,
    experience: typeof pokemon.experience === 'number' ? pokemon.experience : 0,
    next_level_exp: typeof pokemon.next_level_exp === 'number' ? pokemon.next_level_exp : 100,
    types: Array.isArray(pokemon.types) 
      ? pokemon.types 
      : pokemon.type 
        ? [pokemon.type] 
        : []
  });
  
  // Normalizar datos para evitar undefined
  return {
    ...trainer,
    team: trainer.team || [],
    inventory: trainer.inventory || [],
    team_detail: (trainer.team_detail || []).map(normalizePokemon),
    inventory_detail: (trainer.inventory_detail || []).map(normalizePokemon)
  };
}

export async function getTrainer(suid: number): Promise<Trainer> {
  const response = await fetch(`${API_URL}/trainers/${suid}`, {
    method: 'GET',
    headers: getAuthHeaders()
  });

  if (!response.ok) {
    const error = await response.json();
    throw new Error(error.detail || 'Error al obtener entrenador');
  }

  return response.json();
}

export async function addPokemonToTrainer(suid: number, pokedexId: string): Promise<Trainer> {
  const response = await fetch(`${API_URL}/trainers/${suid}/pokemon`, {
    method: 'POST',
    headers: getAuthHeaders(),
    body: JSON.stringify({ pokedex_id: pokedexId })
  });

  if (!response.ok) {
    const error = await response.json();
    throw new Error(error.detail || 'Error al agregar Pokémon');
  }

  return response.json();
}

export async function getTrainerTeam(suid: number): Promise<Pokemon[]> {
  const response = await fetch(`${API_URL}/trainers/${suid}/team`, {
    method: 'GET',
    headers: getAuthHeaders()
  });

  if (!response.ok) {
    const error = await response.json();
    throw new Error(error.detail || 'Error al obtener equipo');
  }

  return response.json();
}

export async function getTrainerInventory(suid: number): Promise<Pokemon[]> {
  const response = await fetch(`${API_URL}/trainers/${suid}/inventory`, {
    method: 'GET',
    headers: getAuthHeaders()
  });

  if (!response.ok) {
    const error = await response.json();
    throw new Error(error.detail || 'Error al obtener inventario');
  }

  return response.json();
}

// POKEMON ENDPOINTS
export async function getPokemon(pokedexId: number): Promise<Pokemon> {
  const response = await fetch(`${API_URL}/pokemons/${pokedexId}`, {
    method: 'GET',
    headers: getAuthHeaders()
  });

  if (!response.ok) {
    const error = await response.json();
    throw new Error(error.detail || 'Error al obtener Pokémon');
  }

  return response.json();
}

export async function getAllPokemons(): Promise<Pokemon[]> {
  const response = await fetch(`${API_URL}/pokemons`, {
    method: 'GET',
    headers: getAuthHeaders()
  });

  if (!response.ok) {
    const error = await response.json();
    throw new Error(error.detail || 'Error al obtener Pokémon');
  }

  return response.json();
}

export async function captureRandomPokemon(suid: number): Promise<CapturePokemonResponse> {
  const response = await fetch(`${API_URL}/trainers/${suid}/capture`, {
    method: 'POST',
    headers: getAuthHeaders()
  });

  if (!response.ok) {
    const error = await response.json();
    throw new Error(error.detail || 'Error al capturar Pokémon');
  }

  return response.json();
}

export async function redeemCoupon(suid: number): Promise<RedeemCouponResponse> {
  const response = await fetch(`${API_URL}/trainers/points/${suid}/redeem_coupon`, {
    method: 'POST',
    headers: getAuthHeaders()
  });

  if (!response.ok) {
    const error = await response.json();
    throw new Error(error.detail || 'Error al canjear cupón');
  }

  return response.json();
}

export async function getTrainerPoints(suid: number): Promise<{ trainer_suid: number; points: number }> {
  const response = await fetch(`${API_URL}/trainers/points/${suid}`, {
    method: 'GET',
    headers: getAuthHeaders()
  });

  if (!response.ok) {
    const error = await response.json();
    throw new Error(error.detail || 'Error al obtener puntos');
  }

  return response.json();
}
