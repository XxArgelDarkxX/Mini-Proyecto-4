# API Endpoints Documentation

## Authentication Endpoints

### Register User
**POST** `/api/auth/register`
```json
{
  "username": "username",
  "password": "password"
}
```
**Response:**
```json
{
  "access_token": "jwt_token",
  "token_type": "bearer"
}
```

### Login User
**POST** `/api/auth/login`
```json
{
  "username": "username",
  "password": "password"
}
```
**Response:**
```json
{
  "access_token": "jwt_token",
  "token_type": "bearer"
}
```

---

## Pokemon Endpoints

### Get All Pokemons
**GET** `/api/pokemons/`
**Auth:** Not required
**Response:**
```json
{
  "pokemons": [
    {
      "_id": "id",
      "pokedex_id": "1",
      "name": "bulbasaur",
      "type": "GRASS",
      "base_hp": 45,
      "base_attack": 49,
      "base_defense": 49,
      "base_special_attack": 65,
      "base_special_defense": 65,
      "base_speed": 45,
      "front_sprite": "url",
      "back_sprite": "url",
      "icon": "url",
      "moves": [
        {
          "name": "Razor Wind",
          "type": "NORMAL",
          "power": 80,
          "accuracy": 100
        }
      ]
    }
  ]
}
```

### Get Pokemon by Pokedex ID
**GET** `/api/pokemons/{pokedex_id}`
**Auth:** Not required
**Response:** Single pokemon object

### Seed Pokemon from PokeAPI
**POST** `/api/pokemons/seed/{pokedex_id}`
**Auth:** Required (Bearer token)
**Description:** Fetches pokemon data from PokeAPI including stats, moves, and sprites
**Response:**
```json
{
  "status": "seeded",
  "upserted": true,
  "pokemon": { ... }
}
```

### Proxy PokeAPI
**GET** `/api/pokemons/pokeapi/{pokedex_id}`
**Auth:** Not required
**Description:** Direct proxy to PokeAPI

---

## Trainer Endpoints

### Create Trainer
**POST** `/api/trainers/`
**Auth:** Required (Bearer token)
**Body:**
```json
{
  "name": "Trainer Name"
}
```
**Response:**
```json
{
  "id": "trainer_id",
  "status": "created",
  "trainer": {
    "username": "username",
    "name": "Trainer Name",
    "sprite": 3,
    "wins": 0,
    "losses": 0,
    "team": [],
    "inventory": [],
    "_id": "trainer_id"
  }
}
```

### Get Current User's Trainer Profile
**GET** `/api/trainers/me`
**Auth:** Required (Bearer token)
**Description:** Get the authenticated user's trainer profile with full pokemon data
**Response:**
```json
{
  "_id": "trainer_id",
  "username": "username",
  "name": "Trainer Name",
  "sprite": 3,
  "wins": 1,
  "losses": 0,
  "team": ["1", "25", "4"],
  "inventory": ["1", "25", "4"],
  "team_detail": [
    {
      "name": "bulbasaur",
      "type": "GRASS",
      "base_hp": 45,
      ...
    }
  ],
  "inventory_detail": [
    {
      "name": "bulbasaur",
      ...
    }
  ]
}
```

### Get Trainer by ID
**GET** `/api/trainers/{trainer_id}`
**Auth:** Required (Bearer token)
**Description:** Get any trainer's profile with full pokemon data
**Response:** Same as `/api/trainers/me`

### Add Pokemon to Trainer's Inventory
**POST** `/api/trainers/{trainer_id}/pokemon`
**Auth:** Required (Bearer token)
**Body:**
```json
{
  "pokedex_id": "1"
}
```
**Logic:**
- Adds pokemon to inventory
- **Automatically assigns to team if team has < 3 slots**
- Once team has 3 pokemon, new pokemon only go to inventory
**Response:**
```json
{
  "status": "added",
  "pokemon_id": "1",
  "team_count": 1,
  "inventory_count": 1
}
```

### Get Trainer's Battle Team
**GET** `/api/trainers/{trainer_id}/team`
**Auth:** Required (Bearer token)
**Description:** Get only the current battle team (max 3 pokemon)
**Response:**
```json
{
  "trainer_id": "trainer_id",
  "trainer_name": "Trainer Name",
  "team": [
    { pokemon_data },
    { pokemon_data },
    { pokemon_data }
  ],
  "team_size": 3
}
```

### Get Trainer's Inventory
**GET** `/api/trainers/{trainer_id}/inventory`
**Auth:** Required (Bearer token)
**Description:** Get all pokemon in the trainer's inventory
**Response:**
```json
{
  "trainer_id": "trainer_id",
  "trainer_name": "Trainer Name",
  "inventory": [
    { pokemon_data },
    { pokemon_data },
    ...
  ],
  "inventory_size": 5
}
```

### Add Win to Trainer
**POST** `/api/trainers/{trainer_id}/stats/win`
**Auth:** Required (Bearer token)
**Response:**
```json
{
  "status": "updated",
  "wins": 2,
  "losses": 1
}
```

### Add Loss to Trainer
**POST** `/api/trainers/{trainer_id}/stats/loss`
**Auth:** Required (Bearer token)
**Response:**
```json
{
  "status": "updated",
  "wins": 1,
  "losses": 2
}
```

---

## Battle Endpoints

### Save Battle Result
**POST** `/api/battles/`
**Auth:** Required (Bearer token)
**Body:**
```json
{
  "trainer1_id": "trainer_id",
  "trainer2_id": "trainer_id",
  "winner_id": "trainer_id",
  "loser_id": "trainer_id",
  "moves_used": ["move1", "move2"],
  "duration": 120
}
```

---

## Usage Example (Frontend)

```javascript
// 1. Register
const token = await fetch('/api/auth/register', {
  method: 'POST',
  body: JSON.stringify({ username, password })
}).then(r => r.json()).then(r => r.access_token);

// 2. Create Trainer
const trainer = await fetch('/api/trainers/', {
  method: 'POST',
  headers: { 'Authorization': `Bearer ${token}` },
  body: JSON.stringify({ name: 'Ash' })
}).then(r => r.json());

// 3. Seed Pokemons
await fetch('/api/pokemons/seed/1', {
  method: 'POST',
  headers: { 'Authorization': `Bearer ${token}` }
});

// 4. Add Pokemon to Team
await fetch(`/api/trainers/${trainer.id}/pokemon`, {
  method: 'POST',
  headers: { 'Authorization': `Bearer ${token}` },
  body: JSON.stringify({ pokedex_id: '1' })
});

// 5. Get Trainer Profile
const profile = await fetch('/api/trainers/me', {
  headers: { 'Authorization': `Bearer ${token}` }
}).then(r => r.json());

console.log(profile.team_detail); // [Bulbasaur, ...]
console.log(profile.inventory_detail); // [...]

// 6. Update Stats After Battle
await fetch(`/api/trainers/${trainer.id}/stats/win`, {
  method: 'POST',
  headers: { 'Authorization': `Bearer ${token}` }
});
```

---

## Notes

- **All trainer endpoints require authentication** except pokemon list endpoints
- **Team is limited to 3 pokemon** - first 3 added automatically go to team
- **Sprite is randomly assigned** (1-8) when trainer is created
- **Pokemon moves** are automatically extracted from PokeAPI with power and accuracy
- **All timestamps are in UTC** and stored in MongoDB
