import os
from dotenv import load_dotenv
from motor.motor_asyncio import AsyncIOMotorClient

load_dotenv()

MONGODB_URL = os.getenv("MONGODB_URL", "mongodb://mongo:27017")
DB_NAME = os.getenv("DB_NAME", "pokemon_battle")

client: AsyncIOMotorClient | None = None

db = None

async def connect_db() -> None:
    global client, db
    client = AsyncIOMotorClient(MONGODB_URL)
    db = client[DB_NAME]

async def close_db() -> None:
    global client
    if client is not None:
        client.close()
