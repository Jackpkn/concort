"""
WebSocket Chat Handler for Real-time Messaging
Zero cost - built into FastAPI!
"""

from typing import Dict, List

from fastapi import WebSocket


class ConnectionManager:
    """Manages WebSocket connections for real-time chat."""

    def __init__(self):
        # matchId -> list of connected WebSockets
        self.active_connections: Dict[str, List[WebSocket]] = {}
        # userId -> WebSocket (for direct notifications)
        self.user_connections: Dict[str, WebSocket] = {}

    async def connect(self, websocket: WebSocket, match_id: str, user_id: str):
        """Accept and register a new WebSocket connection."""
        await websocket.accept()

        # Add to match room
        if match_id not in self.active_connections:
            self.active_connections[match_id] = []
        self.active_connections[match_id].append(websocket)

        # Register user connection
        self.user_connections[user_id] = websocket

        print(f"User {user_id} connected to match {match_id}")

    def disconnect(self, websocket: WebSocket, match_id: str, user_id: str):
        """Remove a WebSocket connection."""
        if match_id in self.active_connections:
            if websocket in self.active_connections[match_id]:
                self.active_connections[match_id].remove(websocket)
            if not self.active_connections[match_id]:
                del self.active_connections[match_id]

        if user_id in self.user_connections:
            del self.user_connections[user_id]

        print(f"User {user_id} disconnected from match {match_id}")

    async def send_personal_message(self, message: dict, websocket: WebSocket):
        """Send message to a specific connection."""
        await websocket.send_json(message)

    async def broadcast_to_match(
        self, match_id: str, message: dict, exclude: WebSocket = None
    ):
        """Broadcast message to all users in a match."""
        if match_id in self.active_connections:
            for connection in self.active_connections[match_id]:
                if connection != exclude:
                    try:
                        await connection.send_json(message)
                    except:
                        pass

    async def notify_user(self, user_id: str, message: dict):
        """Send notification to a specific user."""
        if user_id in self.user_connections:
            try:
                await self.user_connections[user_id].send_json(message)
            except:
                pass


# Global connection manager
manager = ConnectionManager()
