# 💫 Concort

> A dating platform built on **fairness**, **patience**, and **real connections**.  
> No swipes. No chaos. Just your turn.

[![Android](https://img.shields.io/badge/Android-Kotlin-green?logo=android)](https://developer.android.com/)
[![Backend](https://img.shields.io/badge/Backend-FastAPI-009688?logo=fastapi)](https://fastapi.tiangolo.com/)
[![License](https://img.shields.io/badge/License-MIT-blue.svg)](LICENSE)

---

## 🎯 What is Concort?

Concort reimagines online dating by replacing the chaotic swipe culture with a **queue-based matching system**. Every user gets a fair chance to connect based on when they joined, not on algorithms or popularity metrics.

### ✨ Key Features

- **🎰 Fair Queue System** — Matches are made based on queue position, ensuring everyone gets an equal opportunity
- **💬 Real-Time Chat** — WebSocket-powered messaging for instant communication
- **🔒 Secure Authentication** — Phone-based OTP verification with JWT tokens
- **🎨 Modern UI** — Beautiful Jetpack Compose interface with smooth animations
- **📊 Queue Statistics** — See your position, estimated wait time, and daily match stats

---

## 🏗️ Project Structure

```
concort/
├── app/                          # Android Application
│   └── src/main/java/com/example/concort/
│       ├── data/                 # API clients, repositories
│       ├── di/                   # Dependency injection (Hilt)
│       ├── domain/               # Models and enums
│       ├── navigation/           # Navigation graph
│       └── ui/                   # Compose screens, themes, viewmodels
│
└── backend/                      # FastAPI Backend
    └── app/
        ├── api/v1/endpoints/     # REST & WebSocket endpoints
        ├── core/                 # Config, security, database
        ├── models/               # SQLAlchemy models
        ├── schemas/              # Pydantic schemas
        └── services/             # Business logic
```

---

## 🚀 Quick Start

### Prerequisites

- **Android Studio** Hedgehog or later
- **Python 3.10+**
- **Docker** (optional, for containerized backend)

### Backend Setup

```bash
cd backend

# Create virtual environment
python -m venv venv
source venv/bin/activate  # On Windows: venv\Scripts\activate

# Install dependencies
pip install -r requirements.txt

# Copy environment variables
cp .env.example .env

# Run the server
uvicorn app.main:app --reload --host 0.0.0.0 --port 8000
```

**Or with Docker:**

```bash
cd backend
docker-compose up -d
```

📖 **API Documentation:** http://localhost:8000/docs

### Android Setup

1. Open the project in **Android Studio**
2. Sync Gradle dependencies
3. Update the API base URL in `AppModule.kt` if needed
4. Run on emulator or physical device

---

## 📱 App Screens

| Screen | Description |
|--------|-------------|
| **Welcome** | Onboarding with phone number entry |
| **OTP Verification** | Secure 6-digit code verification |
| **Profile Setup** | Set up your name, bio, and preferences |
| **Home** | Queue status, position, and daily stats |
| **Chat** | Real-time messaging with your match |
| **Profile** | View and edit your profile |
| **Settings** | App preferences and account management |

---

## 🔌 API Endpoints

### Authentication
| Method | Endpoint | Description |
|--------|----------|-------------|
| `POST` | `/api/v1/auth/start-verification` | Send OTP to phone |
| `POST` | `/api/v1/auth/verify-otp` | Verify OTP and get token |
| `POST` | `/api/v1/auth/refresh` | Refresh access token |

### Users
| Method | Endpoint | Description |
|--------|----------|-------------|
| `GET` | `/api/v1/users/me` | Get current user profile |
| `PUT` | `/api/v1/users/me` | Update profile |
| `GET` | `/api/v1/users/{id}` | Get user by ID |

### Matching
| Method | Endpoint | Description |
|--------|----------|-------------|
| `POST` | `/api/v1/matching/join-queue` | Join the matching queue |
| `GET` | `/api/v1/matching/queue-status` | Get queue position & stats |
| `POST` | `/api/v1/matching/respond` | Accept or decline a match |

### Chat
| Method | Endpoint | Description |
|--------|----------|-------------|
| `GET` | `/api/v1/chat/{match_id}/messages` | Get chat history |
| `WS` | `/ws/chat/{match_id}` | Real-time chat WebSocket |

---

## 🛠️ Tech Stack

### Android
- **Kotlin** — Primary language
- **Jetpack Compose** — Modern declarative UI
- **Hilt** — Dependency injection
- **Retrofit** — HTTP client
- **OkHttp** — WebSocket support
- **Coroutines & Flow** — Async programming
- **Navigation Compose** — Screen navigation

### Backend
- **FastAPI** — High-performance Python framework
- **SQLAlchemy** — ORM with async support
- **PostgreSQL** — Production database
- **SQLite** — Development database
- **Pydantic** — Data validation
- **JWT** — Secure authentication
- **WebSockets** — Real-time communication

---

## 🔐 Environment Variables

Create a `.env` file in the `backend/` directory:

```env
# Database
DATABASE_URL=postgresql+asyncpg://user:password@localhost:5432/concort

# Security
SECRET_KEY=your-super-secret-key-here
ACCESS_TOKEN_EXPIRE_MINUTES=1440

# Development
DEV_MODE=true
```

---

## 🤝 Contributing

1. Fork the repository
2. Create your feature branch (`git checkout -b feature/amazing-feature`)
3. Commit your changes (`git commit -m 'Add amazing feature'`)
4. Push to the branch (`git push origin feature/amazing-feature`)
5. Open a Pull Request

---

## 📄 License

This project is licensed under the MIT License - see the [LICENSE](LICENSE) file for details.

---

<p align="center">
  Made with ❤️ for meaningful connections
</p>
