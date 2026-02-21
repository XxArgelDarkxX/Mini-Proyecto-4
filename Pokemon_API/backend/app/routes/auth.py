from fastapi import APIRouter, HTTPException
from pydantic import BaseModel
import app.db.mongo as mongo
from app.auth.security import hash_password, verify_password, create_access_token

router = APIRouter()


class UserCreate(BaseModel):
    username: str
    password: str


class TokenResponse(BaseModel):
    access_token: str
    token_type: str = "bearer"


class UserResponse(BaseModel):
    id: str
    username: str


@router.post("/register", response_model=TokenResponse)
async def register(payload: UserCreate) -> TokenResponse:
    if mongo.db is None:
        raise HTTPException(status_code=500, detail="Database not initialized")

    existing = await mongo.db.users.find_one({"username": payload.username})
    if existing:
        raise HTTPException(status_code=400, detail="Username already exists")

    user_doc = {
        "username": payload.username,
        "password_hash": hash_password(payload.password)
    }
    result = await mongo.db.users.insert_one(user_doc)
    user_doc["_id"] = str(result.inserted_id)

    token = create_access_token(user_doc)
    return TokenResponse(access_token=token)


@router.post("/login", response_model=TokenResponse)
async def login(payload: UserCreate) -> TokenResponse:
    if mongo.db is None:
        raise HTTPException(status_code=500, detail="Database not initialized")

    user = await mongo.db.users.find_one({"username": payload.username})
    if not user:
        raise HTTPException(status_code=401, detail="Invalid credentials")

    if not verify_password(payload.password, user.get("password_hash", "")):
        raise HTTPException(status_code=401, detail="Invalid credentials")

    user["_id"] = str(user["_id"])
    token = create_access_token(user)
    return TokenResponse(access_token=token)
