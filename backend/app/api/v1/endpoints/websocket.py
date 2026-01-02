"""
WebSocket endpoints for real-time chat
"""

from uuid import UUID

from fastapi import APIRouter, Query, WebSocket, WebSocketDisconnect
from sqlalchemy import select

from app.core.database import async_session_maker
from app.core.security import decode_token
from app.core.websocket_manager import manager
from app.models.match import Match
from app.models.message import Message

router = APIRouter(tags=["WebSocket"])


@router.websocket("/ws/chat/{match_id}")
async def websocket_chat(websocket: WebSocket, match_id: str, token: str = Query(...)):
    """
    WebSocket endpoint for real-time chat.

    Connect with: ws://host:port/ws/chat/{match_id}?token=your_jwt_token

    Message format (send):
    {
        "type": "message",
        "content": "Hello!"
    }

    Message format (receive):
    {
        "type": "message",
        "id": "uuid",
        "sender_id": "uuid",
        "content": "Hello!",
        "sent_at": "2024-01-01T12:00:00",
        "is_sent_by_me": false
    }
    """
    # Validate token
    user_id = decode_token(token)
    if not user_id:
        await websocket.close(code=4001, reason="Invalid token")
        return

    # Verify user has access to this match
    async with async_session_maker() as db:
        match_query = select(Match).where(Match.id == UUID(match_id))
        result = await db.execute(match_query)
        match = result.scalar_one_or_none()

        if not match:
            await websocket.close(code=4004, reason="Match not found")
            return

        if str(match.male_user_id) != user_id and str(match.female_user_id) != user_id:
            await websocket.close(code=4003, reason="Not authorized")
            return

    # Connect
    await manager.connect(websocket, match_id, user_id)

    try:
        while True:
            # Receive message
            data = await websocket.receive_json()

            if data.get("type") == "message":
                content = data.get("content", "").strip()
                if not content:
                    continue

                # Save message to database
                async with async_session_maker() as db:
                    message = Message(
                        match_id=UUID(match_id),
                        sender_id=UUID(user_id),
                        content=content,
                    )
                    db.add(message)
                    await db.commit()
                    await db.refresh(message)

                    # Prepare response
                    response = {
                        "type": "message",
                        "id": str(message.id),
                        "match_id": match_id,
                        "sender_id": user_id,
                        "content": content,
                        "sent_at": message.sent_at.isoformat(),
                        "is_read": False,
                    }

                # Send confirmation to sender
                await manager.send_personal_message(
                    {**response, "is_sent_by_me": True}, websocket
                )

                # Broadcast to other users in match
                await manager.broadcast_to_match(
                    match_id, {**response, "is_sent_by_me": False}, exclude=websocket
                )

            elif data.get("type") == "typing":
                # Broadcast typing indicator
                await manager.broadcast_to_match(
                    match_id, {"type": "typing", "user_id": user_id}, exclude=websocket
                )

            elif data.get("type") == "read":
                # Mark messages as read
                async with async_session_maker() as db:
                    from sqlalchemy import update

                    await db.execute(
                        update(Message)
                        .where(Message.match_id == UUID(match_id))
                        .where(Message.sender_id != UUID(user_id))
                        .where(Message.is_read == False)
                        .values(is_read=True)
                    )
                    await db.commit()

                # Notify sender that messages were read
                await manager.broadcast_to_match(
                    match_id, {"type": "read", "by_user_id": user_id}, exclude=websocket
                )

    except WebSocketDisconnect:
        manager.disconnect(websocket, match_id, user_id)
    except Exception as e:
        print(f"WebSocket error: {e}")
        manager.disconnect(websocket, match_id, user_id)
