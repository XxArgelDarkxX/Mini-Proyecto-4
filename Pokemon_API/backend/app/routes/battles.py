from fastapi import APIRouter, HTTPException, Depends
import app.db.mongo as mongo
from app.auth.deps import get_current_user

router = APIRouter()

@router.post("/")
async def save_battle(payload: dict, _user: dict = Depends(get_current_user)) -> dict:
    if mongo.db is None:
        raise HTTPException(status_code=500, detail="Database not initialized")
    result = await mongo.db.battles.insert_one(payload)
    return {"id": str(result.inserted_id), "status": "saved"}
