# Concort Backend

A dating platform built on fairness, patience, and real connections.

## Quick Start

```bash
# Create virtual environment
python -m venv venv
source venv/bin/activate

# Install dependencies
pip install -r requirements.txt

# Run with Docker (recommended)
docker-compose up -d

# Or run locally
uvicorn app.main:app --reload
```

## API Documentation

- Swagger UI: http://localhost:8000/docs
- ReDoc: http://localhost:8000/redoc

## Environment Variables

Copy `.env.example` to `.env` and configure:

```
DATABASE_URL=postgresql+asyncpg://user:pass@localhost:5432/concort
SECRET_KEY=your-secret-key
```
