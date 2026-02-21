from fastapi import FastAPI
from fastapi.middleware.cors import CORSMiddleware
from app.routes import pokemons, trainers, battles, auth
from app.db.mongo import connect_db, close_db

app = FastAPI(title="Pokemon Battle API")

app.add_middleware(
    CORSMiddleware,
    allow_origins=["*"],
    allow_credentials=True,
    allow_methods=["*"],
    allow_headers=["*"],
)

@app.on_event("startup")
async def startup_event() -> None:
    await connect_db()

@app.on_event("shutdown")
async def shutdown_event() -> None:
    await close_db()

@app.get("/health")
async def health() -> dict:
    return {"status": "ok"}

app.include_router(pokemons.router, prefix="/api/pokemons", tags=["Pokemons"])
app.include_router(trainers.router, prefix="/api/trainers", tags=["Trainers"])
app.include_router(battles.router, prefix="/api/battles", tags=["Battles"])
app.include_router(auth.router, prefix="/api/auth", tags=["Auth"])
