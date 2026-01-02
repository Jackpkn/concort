from app.api.v1.endpoints import auth, chat, matching, users
from fastapi import APIRouter

api_router = APIRouter()

# Include all endpoint routers
api_router.include_router(auth.router)
api_router.include_router(users.router)
api_router.include_router(matching.router)
api_router.include_router(chat.router)
