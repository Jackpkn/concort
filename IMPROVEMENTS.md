# 🚀 Concort — Improvements Roadmap

This document outlines recommended improvements across **Security**, **UI/UX**, **Features**, **Performance**, and **DevOps**.

---

## 🔴 Critical — Security

| Priority | Issue | Current State | Recommendation |
|----------|-------|---------------|----------------|
| 🔴 HIGH | **Hardcoded Secret Key** | `SECRET_KEY = "dev-secret-key-change-in-production"` in `config.py` | Fail if `SECRET_KEY` not set in production |
| 🔴 HIGH | **Profile Setup Auth** | `user_id: str = None` — not getting from JWT | Implement proper JWT auth middleware for all protected routes |
| 🔴 HIGH | **No Rate Limiting** | OTP endpoint has no rate limiting | Add rate limiting (5 attempts/minute per phone) |
| 🔴 HIGH | **Token Storage** | Using DataStore (plaintext) | Use EncryptedSharedPreferences or Android Keystore |
| 🟠 MED | **OTP in Response (DEV)** | OTP returned in response in dev mode | Never return OTP, only log to console |
| 🟠 MED | **No Token Refresh** | Only access token with 7-day expiry | Implement refresh token rotation |
| 🟠 MED | **CORS Wildcard** | `allow_origins=["*"]` | Restrict to specific domains in production |
| 🟡 LOW | **No Password Hashing Use** | `verify_password`/`get_password_hash` unused | Remove or implement password-based auth option |

---

## 🎨 UI/UX Improvements

### Missing Screens & Features

| Screen | Status | What to Add |
|--------|--------|-------------|
| **Onboarding Carousel** | ❌ Missing | Welcome slides explaining how Concort works |
| **Profile Photo** | ❌ Missing | Profile image upload + display |
| **Discovery/Explore** | ❌ Missing | See people nearby / explore profiles |
| **Notification Center** | ❌ Missing | Match notifications, messages, system alerts |
| **Block/Report User** | ❌ Missing | Safety feature to block/report matches |
| **Delete Account** | ❌ Missing | GDPR-compliant account deletion |
| **Dark/Light Theme Toggle** | ❌ Missing | Settings option for theme preference |

### UI Enhancements

| Area | Current | Improvement |
|------|---------|-------------|
| **Loading States** | Basic | Add skeleton loaders and shimmer effects |
| **Empty States** | Text only | Add illustrations for empty matches, chats |
| **Error Handling** | Generic errors | User-friendly error messages with retry buttons |
| **Pull-to-Refresh** | Not implemented | Add pull-to-refresh on Home, Chat screens |
| **Haptic Feedback** | None | Add haptics for button presses, swipes |
| **Animations** | Basic fade | Add Lottie animations for match found, queue join |
| **Accessibility** | Not audited | Add content descriptions, proper focus handling |
| **Chat Typing Indicator** | Not implemented | Show "Partner is typing..." |
| **Read Receipts** | Not implemented | Show message read status |
| **Message Reactions** | Not implemented | Add emoji reactions to messages |

---

## ⚙️ Features to Add

### Android App

| Feature | Priority | Description |
|---------|----------|-------------|
| **Push Notifications** | 🔴 HIGH | Firebase Cloud Messaging for match/message alerts |
| **Biometric Login** | 🟠 MED | Auto-login with fingerprint/face after first auth |
| **Profile Verification** | 🟠 MED | Photo verification to prevent catfishing |
| **Preferences Screen** | 🟠 MED | Age range, distance, gender preferences |
| **Match Expiry Timer** | 🟠 MED | Visual countdown for match decision window |
| **Share Profile** | 🟡 LOW | Generate shareable profile link |
| **App Lock** | 🟡 LOW | PIN/pattern lock for privacy |

### Backend API

| Feature | Priority | Description |
|---------|----------|-------------|
| **Twilio Integration** | 🔴 HIGH | Real SMS OTP sending (partially implemented) |
| **Push Notification Service** | 🔴 HIGH | FCM integration for server-side push |
| **User Blocking API** | 🔴 HIGH | `/api/v1/users/{id}/block` endpoint |
| **Report User API** | 🔴 HIGH | `/api/v1/users/{id}/report` endpoint |
| **Image Upload** | 🟠 MED | Profile photo upload with S3/CloudFlare R2 |
| **Pagination** | 🟠 MED | Paginate chat messages, match history |
| **Caching Layer** | 🟠 MED | Redis caching for queue status, user profiles |
| **WebSocket Heartbeat** | 🟠 MED | Keep-alive ping/pong for connection health |
| **Analytics Events** | 🟡 LOW | Track user engagement, match rates |

---

## 🚀 Performance & Optimization

| Area | Improvement |
|------|-------------|
| **Database Indexing** | Add indexes on `phone_number`, `queue_position`, `match_id` |
| **Query Optimization** | Use eager loading for user-match relationships |
| **Image Optimization** | Compress/resize profile images before upload |
| **Lazy Loading** | Lazy load chat messages (20 at a time) |
| **Connection Pooling** | Configure SQLAlchemy connection pool for production |
| **Proguard/R8** | Enable code shrinking for release builds |

---

## 🛠️ DevOps & Infrastructure

| Task | Description |
|------|-------------|
| **CI/CD Pipeline** | GitHub Actions for automated testing & deployment |
| **Unit Tests** | Add pytest tests for backend, JUnit for Android |
| **E2E Tests** | Maestro or Espresso for Android UI testing |
| **API Tests** | Postman/Newman collection for API testing |
| **Docker Production Config** | Multi-stage Dockerfile, health checks |
| **Logging** | Structured JSON logging with request IDs |
| **Monitoring** | Sentry for error tracking, Prometheus for metrics |
| **Secrets Management** | Use AWS Secrets Manager or HashiCorp Vault |
| **Database Migrations** | Add Alembic for SQLAlchemy migrations |

---

## 📋 Immediate Action Items

```text
Phase 1 — Security Fixes (Critical)
├── [ ] Add JWT auth middleware for protected routes
├── [ ] Implement rate limiting on auth endpoints
├── [ ] Migrate to EncryptedSharedPreferences
├── [ ] Remove OTP from API responses
└── [ ] Block wildcard CORS in production

Phase 2 — Core Features
├── [ ] Push notifications with FCM
├── [ ] Profile photo upload
├── [ ] Block/Report functionality
├── [ ] Real SMS via Twilio
└── [ ] Refresh token implementation

Phase 3 — UI/UX Polish
├── [ ] Onboarding carousel
├── [ ] Skeleton loaders
├── [ ] Typing indicators
├── [ ] Pull-to-refresh
└── [ ] Empty state illustrations

Phase 4 — DevOps
├── [ ] GitHub Actions CI/CD
├── [ ] Unit & integration tests
├── [ ] Alembic migrations
└── [ ] Monitoring setup
```

---

## 📚 Resources

- [Android Security Best Practices](https://developer.android.com/topic/security/best-practices)
- [FastAPI Security Guide](https://fastapi.tiangolo.com/tutorial/security/)
- [OWASP Mobile Top 10](https://owasp.org/www-project-mobile-top-10/)
- [Firebase Cloud Messaging](https://firebase.google.com/docs/cloud-messaging)
- [Twilio Python SDK](https://www.twilio.com/docs/libraries/python)
