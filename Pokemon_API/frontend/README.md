# Pokémon Battle - Frontend

Frontend desarrollado en **Next.js 14** para el juego de batallas Pokémon.

## 🚀 Características

- ✅ **Autenticación completa**: Login y registro de usuarios
- ✅ **Tutorial interactivo**: Onboarding para nuevos entrenadores
- ✅ **Dashboard del entrenador**: Visualización de perfil con:
  - Sprite personalizado
  - Estadísticas (victorias, derrotas, ratio)
  - Equipo de batalla (máximo 3 Pokémon)
  - Inventario completo de Pokémon capturados
- ✅ **Integración con API**: Conexión con backend FastAPI
- ✅ **Diseño responsive**: Optimizado para diferentes tamaños de pantalla
- ✅ **Pokémon iniciales**: Los nuevos usuarios reciben Bulbasaur, Charmander y Squirtle

## 📁 Estructura del Proyecto

```
frontend/
├── app/                      # Rutas de Next.js (App Router)
│   ├── page.tsx             # Página principal / Landing
│   ├── login/               # Pantalla de inicio de sesión
│   ├── register/            # Pantalla de registro
│   ├── dashboard/           # Dashboard del entrenador
│   ├── onboarding/          # Tutorial para nuevos usuarios
│   ├── layout.tsx           # Layout principal
│   └── globals.css          # Estilos globales
├── components/              # Componentes reutilizables
│   └── TrainerCard.tsx      # Tarjeta del entrenador con equipo e inventario
├── lib/                     # Utilidades y servicios
│   └── api.ts              # Cliente API para comunicación con backend
├── .env.local              # Variables de entorno
└── package.json
```

## 🛠️ Tecnologías

- **Next.js 14**: Framework de React con App Router
- **TypeScript**: Tipado estático
- **CSS Modules**: Estilos con scope local
- **Docker**: Containerización

## 🎮 Flujos de Usuario

### 1. Nuevo Usuario (Registro)
1. Accede a `/register`
2. Crea credenciales (username + password)
3. Es redirigido a `/onboarding`
4. Elige su nombre de entrenador
5. Ve el tutorial explicativo
6. Recibe 3 Pokémon iniciales (Bulbasaur, Charmander, Squirtle)
7. Accede al dashboard

### 2. Usuario Existente (Login)
1. Accede a `/login`
2. Ingresa credenciales
3. Es redirigido a `/dashboard`
4. Ve su tarjeta de entrenador con:
   - Información personal
   - Estadísticas de batallas
   - Equipo actual (máximo 3)
   - Inventario completo

## 🔑 Variables de Entorno

```env
NEXT_PUBLIC_API_URL=http://localhost:8000/api
```

## 🐳 Ejecutar con Docker

```bash
# Desde la raíz del proyecto Pokemon_API
docker compose up -d frontend

# Ver logs
docker compose logs -f frontend

# Reconstruir después de cambios
docker compose up -d --build frontend
```

## 💻 Desarrollo Local

```bash
# Instalar dependencias
npm install

# Ejecutar en modo desarrollo
npm run dev

# Visitar http://localhost:3000
```

## 📡 API Endpoints Utilizados

El frontend consume los siguientes endpoints del backend:

### Auth
- `POST /api/auth/register` - Crear cuenta de usuario
- `POST /api/auth/login` - Iniciar sesión (retorna JWT token)

### Trainers
- `POST /api/trainers` - Crear perfil de entrenador
- `GET /api/trainers/me` - Obtener perfil propio (requiere auth)
- `GET /api/trainers/{suid}` - Obtener perfil por SUID
- `POST /api/trainers/{suid}/pokemon` - Agregar Pokémon al entrenador
- `GET /api/trainers/{suid}/team` - Ver equipo de batalla
- `GET /api/trainers/{suid}/inventory` - Ver inventario completo

### Pokemon
- `GET /api/pokemons` - Listar todos los Pokémon
- `GET /api/pokemons/{pokedex_id}` - Obtener Pokémon específico

## 🎨 Componentes Principales

### TrainerCard
Muestra la información completa del entrenador:
- **Header**: Sprite, nombre, username, SUID, estadísticas
- **Equipo**: Grid con los 3 Pokémon de batalla (con sprites, nombres, tipos)
- **Inventario**: Grid scrollable con todos los Pokémon capturados

### Auth Forms (Login/Register)
- Validación de campos
- Manejo de errores
- Estados de carga
- Diseño responsive

### Onboarding (Tutorial)
- 3 pasos guiados:
  1. Crear nombre de entrenador
  2. Tutorial explicativo
  3. Recibir Pokémon iniciales

## 🔐 Autenticación

El sistema usa **JWT (JSON Web Tokens)**:
- Los tokens se almacenan en `localStorage`
- Se envían en el header `Authorization: Bearer <token>`
- El dashboard verifica autenticación y redirige si no hay token
- El logout simplemente elimina el token del localStorage

## 🎯 Características Futuras (Deshabilitadas)

En el dashboard hay botones para funciones futuras:
- 🎯 Buscar Batalla
- 📦 Capturar Pokémon
- 📊 Historial de batallas

## 🐛 Debugging

```bash
# Ver logs del contenedor
docker logs pokemon-frontend -f

# Acceder al contenedor
docker exec -it pokemon-frontend sh

# Verificar variables de entorno
docker exec pokemon-frontend env | grep NEXT_PUBLIC
```

## 📱 Responsive Design

La UI está optimizada para:
- 📱 **Mobile**: < 768px
- 💻 **Tablet**: 768px - 1024px
- 🖥️ **Desktop**: > 1024px

## 🎨 Paleta de Colores

```css
Primary: #667eea → #764ba2 (gradient)
Secondary: #f5f7fa → #c3cfe2
Success: #78C850 (green)
Error: #c33 (red)
Background: Linear gradients con animaciones
```

## ✨ Animaciones

- **Fade In**: Apariciones suaves de componentes
- **Slide In**: Entrada de tarjetas desde abajo
- **Bounce**: Pokémon iniciales
- **Hover Effects**: Transform y box-shadow en interact elements

## 📄 Licencia

Proyecto educativo - Mini Proyecto #4
