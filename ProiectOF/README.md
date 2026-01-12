# 🚚 FleetOps Brașov GPS — Fleet Management System

<div align="center">

![Java](https://img.shields.io/badge/Java-21-orange?style=for-the-badge&logo=java)
![Spring Boot](https://img.shields.io/badge/Spring%20Boot-3.5.9-brightgreen?style=for-the-badge&logo=springboot)
![C++](https://img.shields.io/badge/C++-17-blue?style=for-the-badge&logo=cplusplus)
![PostgreSQL](https://img.shields.io/badge/PostgreSQL-15-blue?style=for-the-badge&logo=postgresql)
![Docker](https://img.shields.io/badge/Docker-Compose-2496ED?style=for-the-badge&logo=docker)

**Un sistem modern de dispecerizare flotă cu tracking GPS în timp real** 🌍

[Demo](#-demo-rapid) • [Instalare](#-instalare-rapidă) • [Documentație](#-documentație-api) • [Tehnologii](#-stack-tehnologic)

</div>

---

## 📋 Cuprins

- [Despre Proiect](#-despre-proiect)
- [Features](#-features)
- [Stack Tehnologic](#-stack-tehnologic)
- [Arhitectură](#-arhitectură)
- [Instalare Rapidă](#-instalare-rapidă)
- [Configurare](#-configurare)
- [Utilizare](#-utilizare)
- [Documentație API](#-documentație-api)
- [Exemple Practice](#-exemple-practice)
- [Dezvoltare Locală](#-dezvoltare-locală)
- [Troubleshooting](#-troubleshooting)

---

## 🎯 Despre Proiect

**FleetOps** este o aplicație demo pentru **dispecerizare flotă auto** cu tracking GPS în timp real, dezvoltată folosind arhitectură **microservicii**.

Sistemul permite:
- 📍 Urmărirea vehiculelor pe hartă în timp real
- 🛣️ Calcularea automată a rutelor
- 📦 Gestionarea comenzilor și curselor
- 🔐 Autentificare securizată
- 📸 Upload documente/poze pentru vehicule
- 📊 Monitoring și metrici (Actuator + Prometheus)

---

## ✨ Features

| Feature | Descriere | Status |
|---------|-----------|--------|
| 🗺️ **Hartă Interactivă** | Vizualizare vehicule pe Leaflet Maps | ✅ |
| ⚡ **Real-time Updates** | WebSocket (STOMP) pentru poziții live | ✅ |
| 🧮 **Calcul Rute** | Microserviciu C++ pentru rutare optimă | ✅ |
| 🔒 **Autentificare** | Basic Auth cu BCrypt | ✅ |
| 💾 **Persistență** | PostgreSQL cu JPA/Hibernate | ✅ |
| 📦 **Stocare Fișiere** | MinIO (S3-compatible) | ✅ |
| 📖 **API Documentation** | Swagger/OpenAPI integrat | ✅ |
| 🐳 **Containerizare** | Docker Compose full-stack | ✅ |
| 📈 **Monitoring** | Spring Actuator + Prometheus metrics | ✅ |

---

## 🛠️ Stack Tehnologic

### Backend

- **☕ Java 21** — Limbaj principal
- **🍃 Spring Boot 3.5.9** — Framework aplicație
  - Spring Web (REST API)
  - Spring Security (autentificare Basic Auth)
  - Spring Data JPA (persistență)
  - Spring WebSocket (real-time comunicare)
  - Spring Actuator (health checks, metrics)
- **🗄️ PostgreSQL 15** — Bază de date relațională
- **⚙️ C++ 17 + Crow Framework** — Microserviciu routing
- **📦 MinIO** — Object storage (compatibil S3)

### Frontend

- **🌐 HTML5 + JavaScript (Vanilla)**
- **🗺️ Leaflet.js** — Mapare interactivă
- **🔌 SockJS + STOMP.js** — WebSocket client
- **🎨 CSS3** — UI modern dark theme
- **🌐 Nginx Alpine** — Web server

### DevOps & Tools

- **🐳 Docker & Docker Compose** — Orchestrare containere
- **📚 Maven** — Build tool (Java)
- **🔨 CMake** — Build tool (C++)
- **📖 Swagger/OpenAPI** — Documentație API
- **📊 Prometheus** — Format metrici

---

## 🏗️ Arhitectură

```
┌─────────────────────────────────────────────────────────────────┐
│                         🌐 FRONTEND                              │
│                    Nginx + Leaflet Maps                          │
│                  (http://localhost:3000)                         │
└────────────┬────────────────────────────────────────────────────┘
             │ REST API + WebSocket
             ▼
┌─────────────────────────────────────────────────────────────────┐
│                    🚪 GATEWAY SERVICE                            │
│              Spring Boot (Java) - Port 8080                      │
│  • REST Controllers (Vehicles, Orders)                           │
│  • WebSocket STOMP (real-time broadcasts)                        │
│  • Security (Basic Auth + BCrypt)                                │
│  • Swagger UI Documentation                                      │
└──────┬──────────────────┬─────────────────┬─────────────────────┘
       │                  │                 │
       │ JPA/Hibernate    │ HTTP POST       │ MinIO SDK
       ▼                  ▼                 ▼
┌─────────────┐   ┌──────────────┐   ┌──────────────┐
│ 🗄️ POSTGRES │   │ ⚙️ ROUTING   │   │ 📦 MINIO     │
│   Port 5432 │   │   SERVICE    │   │  Ports 9000  │
│             │   │  C++ (Crow)  │   │       9001   │
│  fleet_db   │   │  Port 8081   │   │              │
└─────────────┘   └──────────────┘   └──────────────┘
```

### 🔄 Flow-ul unei comenzi

1. **Frontend** → Utilizatorul selectează vehicul și destinație
2. **Gateway** → Validează request-ul și extrage coordonatele
3. **Routing Service (C++)** → Calculează ruta optimă
4. **Gateway** → Salvează comanda în Postgres cu ruta calculată
5. **MovementSimulator** → Simulează deplasarea vehiculului
6. **WebSocket** → Broadcast poziții actualizate către frontend
7. **Frontend** → Actualizează markerul pe hartă în timp real

---

## 🚀 Instalare Rapidă

### Prerequisite

- ✅ **Docker Desktop** instalat și pornit
- ✅ **Git** (opțional, pentru clone)

### Pași

1️⃣ **Clonează sau deschide repo-ul**

```powershell
cd "C:\Users\Gabriel\Documents\AN 2\OF\ProiectOF\ProiectOF"
```

2️⃣ **Pornește toate serviciile**

```powershell
docker compose up -d --build
```

3️⃣ **Verifică starea serviciilor**

```powershell
docker compose ps
```

Toate serviciile ar trebui să fie `healthy` sau `running`.

4️⃣ **Accesează aplicația**

| Serviciu | URL | Credențiale |
|----------|-----|-------------|
| 🌐 **Frontend** | http://localhost:3000 | `Capitanu` / `123` |
| 📖 **Swagger UI** | http://localhost:8080/swagger-ui.html | Same |
| 💚 **Health Check** | http://localhost:8080/actuator/health | Public |
| 📦 **MinIO Console** | http://localhost:9001 | `admin` / `password123` |

🎉 **Gata! Aplicația rulează!**

---

## ⚙️ Configurare

Toate configurările sunt centralizate în fișierul **`.env`** din rădăcina proiectului:

```env
# 🗄️ Database Configuration
POSTGRES_USER=postgres
POSTGRES_PASSWORD=1q2w3e
POSTGRES_DB=fleet_db

# 🔗 Microservices Communication
ROUTING_SERVICE_URL=http://routing-service:8081/

# 📦 MinIO Storage
MINIO_ROOT_USER=admin
MINIO_ROOT_PASSWORD=password123

# 👤 Initial Admin User
INITIAL_ADMIN_USERNAME=Capitanu
INITIAL_ADMIN_PASSWORD=123
```

💡 **Tip:** Modifică `.env` pentru credențiale personalizate, apoi restart:

```powershell
docker compose down -v
docker compose up -d --build
```

---

## 🎮 Utilizare

### 1️⃣ Autentificare

Deschide http://localhost:3000 și autentifică-te cu:
- **User:** `Capitanu`
- **Parola:** `123`

![Login Screen](https://img.shields.io/badge/🔐-Login-blue)

### 2️⃣ Creează un Vehicul (via Swagger)

Accesează http://localhost:8080/swagger-ui.html

**POST** `/api/vehicles`:
```json
{
  "name": "Duba Brașov 01",
  "status": "IDLE",
  "currentX": 0,
  "currentY": 0
}
```

### 3️⃣ Lansează o Cursă

Din UI (localhost:3000):
- Selectează vehiculul
- Introdu coordonate **Pickup** (ex: `X=10`, `Y=20`)
- Introdu coordonate **Destinație** (ex: `X=50`, `Y=60`)
- Click **"LANSEAZĂ CURSA"** 🚀

Vehiculul se va deplasa automat, iar poziția se actualizează în timp real pe hartă!

### 4️⃣ Monitorizare

- **Status vehicule:** Vizibil pe hartă cu culori diferite
- **Comenzi active:** Salvate în baza de date
- **Metrici:** http://localhost:8080/actuator/prometheus

---

## 📖 Documentație API

### 🚗 Vehicles Endpoints

| Method | Endpoint | Descriere | Auth |
|--------|----------|-----------|------|
| `GET` | `/api/vehicles` | Listează toate vehiculele | ✅ |
| `POST` | `/api/vehicles` | Creează vehicul nou | ✅ |
| `GET` | `/api/vehicles/{id}/calculate-path` | Calculează rută fără lansare | ✅ |
| `POST` | `/api/vehicles/{id}/start-trip` | Lansează cursă nouă | ✅ |
| `POST` | `/api/vehicles/{id}/upload-photo` | Upload poză vehicul | ✅ |

### 🔌 WebSocket

**Endpoint:** `ws://localhost:8080/ws-fleet` (SockJS)

**Topic:** `/topic/vehicles` (broadcasts)

Primești update-uri automate:
```json
{
  "id": 1,
  "name": "Duba Brașov 01",
  "status": "MOVING",
  "currentX": 25.5,
  "currentY": 40.2
}
```

### 📊 Actuator & Monitoring

| Endpoint | Descriere |
|----------|-----------|
| `/actuator/health` | Health status (public) |
| `/actuator/metrics` | Metrici disponibile |
| `/actuator/prometheus` | Export Prometheus |

---

## 💻 Exemple Practice

### PowerShell — Setup Auth Header

```powershell
$auth = "Basic " + [Convert]::ToBase64String([Text.Encoding]::ASCII.GetBytes("Capitanu:123"))
$headers = @{ Authorization = $auth }
```

### 📋 Listează Vehicule

```powershell
Invoke-RestMethod -Uri "http://localhost:8080/api/vehicles" -Headers $headers
```

### ➕ Creează Vehicul

```powershell
$body = @{
    name = "Autoutilitară 2"
    status = "IDLE"
    currentX = 0
    currentY = 0
} | ConvertTo-Json

Invoke-RestMethod -Uri "http://localhost:8080/api/vehicles" `
    -Headers $headers `
    -Method POST `
    -ContentType 'application/json' `
    -Body $body
```

### 🚀 Lansează Cursă

```powershell
$trip = @{
    startX = 10
    startY = 20
    endX = 50
    endY = 60
} | ConvertTo-Json

Invoke-RestMethod -Uri "http://localhost:8080/api/vehicles/1/start-trip" `
    -Headers $headers `
    -Method POST `
    -ContentType 'application/json' `
    -Body $trip
```

### 📸 Upload Fotografie

```powershell
curl.exe -X POST "http://localhost:8080/api/vehicles/1/upload-photo" `
    -H "Authorization: Basic Q2FwaXRhbnU6MTIz" `
    -F "file=@C:\path\to\photo.jpg"
```

---

## 🧑‍💻 Dezvoltare Locală

Vrei să dezvolți fără rebuild constant al containerelor?

### 1️⃣ Pornește doar infrastructura

```powershell
docker compose up -d db minio routing-service
```

### 2️⃣ Rulează Gateway local

```powershell
cd gateway-service
.\mvnw.cmd spring-boot:run
```

Gateway-ul va porni pe **8080** și se va conecta la Postgres și Routing Service din Docker.

### 3️⃣ Dezvoltă Routing Service (C++)

```powershell
cd routing-service
cmake .
make
./routing_service
```

### 4️⃣ Frontend Static

Deschide direct `frontend/index.html` în browser sau folosește Live Server.

---

## 🐛 Troubleshooting

### ❌ Port-ul 8080 e ocupat

**Soluție:** Modifică în `docker-compose.yml`:
```yaml
gateway-service:
  ports:
    - "8081:8080"  # Acum gateway e pe 8081
```

### ❌ Gateway nu pornește (unhealthy)

**Verifică:**
```powershell
docker compose logs gateway-service
```

**Cauze posibile:**
- Postgres nu e ready → așteaptă 30s
- Routing service down → verifică `docker compose ps`

### ❌ Autentificare eșuată

**Soluție:** Reset utilizator admin:
```powershell
docker compose down -v
docker compose up -d --build
```

Aceasta recreează DB-ul cu credențialele din `.env`.

### ❌ MinIO nu funcționează

**Verifică:**
```powershell
docker compose logs minio
```

Accesează consolă: http://localhost:9001 (`admin` / `password123`)

### ❌ Upload foto eșuat

**Cauze:**
- Bucket nu există → Gateway creează automat `fleet-storage`
- Fișier prea mare → verifică limits în Nginx (default 1MB)

### 🔍 Logs utile

```powershell
# Toate serviciile
docker compose logs -f

# Doar gateway
docker compose logs -f gateway-service

# Doar routing
docker compose logs -f routing-service
```

---

## 📦 Structură Proiect

```
ProiectOF/
├── 📁 gateway-service/          # 🍃 Spring Boot App
│   ├── src/main/java/
│   │   └── com/fleetops/gateway/
│   │       ├── config/          # Security, WebSocket, MinIO
│   │       ├── controller/      # REST endpoints
│   │       ├── dto/             # Request/Response objects
│   │       ├── model/           # JPA entities
│   │       ├── repository/      # Database access
│   │       └── service/         # Business logic
│   ├── Dockerfile
│   └── pom.xml
│
├── 📁 routing-service/          # ⚙️ C++ Microservice
│   ├── main.cpp                 # Crow HTTP server
│   ├── CMakeLists.txt
│   └── Dockerfile
│
├── 📁 frontend/                 # 🌐 Static UI
│   └── index.html               # Single-page app
│
├── 🐳 docker-compose.yml         # Orchestration
├── ⚙️ .env                       # Configuration
└── 📖 README.md                  # Acest fișier
```

---

## 🎓 Concepte Învățate

- ✅ Arhitectură **microservicii** (Java + C++)
- ✅ **REST API** design cu Spring Boot
- ✅ **WebSocket** real-time cu STOMP
- ✅ **Autentificare** Basic Auth + BCrypt
- ✅ **Persistență** cu JPA/Hibernate
- ✅ **Object storage** cu MinIO (S3)
- ✅ **Containerizare** full-stack cu Docker
- ✅ **Health checks** și monitoring
- ✅ **API Documentation** cu Swagger/OpenAPI

---

## 🤝 Contribuții

Proiect educațional/demonstrativ dezvoltat pentru cursul de **Optimizare și Funcții**.

---

## 📝 Licență

Acest proiect este open-source și disponibil ca material educațional.

---

<div align="center">

**Dezvoltat cu ☕ & ❤️ în Brașov**

🚚 **FleetOps** — *Modern Fleet Management*

</div>
