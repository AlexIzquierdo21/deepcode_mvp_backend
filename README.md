# DeepCode Backend - Programming Challenges Platform API

[![Spring Boot](https://img.shields.io/badge/Spring%20Boot-3.2+-brightgreen.svg)](https://spring.io/projects/spring-boot)
[![Java](https://img.shields.io/badge/Java-17+-orange.svg)](https://www.oracle.com/java/)
[![MySQL](https://img.shields.io/badge/MySQL-8.0+-blue.svg)](https://www.mysql.com/)
[![Docker](https://img.shields.io/badge/Docker-Ready-2496ED.svg)](https://www.docker.com/)
[![License](https://img.shields.io/badge/License-MIT-yellow.svg)](LICENSE)

> **RESTful API** para una plataforma de retos de programación con sistema de autenticación JWT, gestión de usuarios y seguimiento de progreso. Desarrollada como parte de un proyecto académico para demostrar habilidades en desarrollo backend con Spring Boot.

---

## Tabla de Contenidos

- [Características](#-características)
- [Stack Tecnológico](#️-stack-tecnológico)
- [Arquitectura](#️-arquitectura)
- [Instalación Rápida con Docker](#-instalación-rápida-con-docker)
- [Probar la API con Postman](#-probar-la-api-con-postman)
- [Endpoints de la API](#-endpoints-de-la-api)
- [Datos de Prueba](#-datos-de-prueba)
- [Estructura del Proyecto](#-estructura-del-proyecto)
- [Roadmap](#️-roadmap)
- [Contacto](#-contacto)

---

## Características

### **Autenticación y Seguridad**
- Sistema de autenticación con **JWT (JSON Web Tokens)**
- Tokens con expiración de 7 días
- Passwords encriptados con **BCrypt**
- Validación de email case-insensitive

### **Gestión de Retos**
- CRUD completo de retos de programación
- Soporte para múltiples lenguajes: **Python, Java, Kotlin, HTML/CSS/JS**
- Niveles de dificultad: **Beginner, Intermediate**
- Filtros por lenguaje y nivel
- Sistema de autoría (cada reto tiene un creador)

### **Sistema de Progreso**
- Los usuarios pueden marcar retos como completados
- Historial de progreso por usuario
- Notas personales en cada reto completado

### **Validaciones de Negocio**
- No se permiten retos duplicados (mismo título + lenguaje + nivel)
- Solo el creador puede eliminar sus propios retos
- Validación de email único en registro

---

## 🛠Stack Tecnológico

| Tecnología | Versión | Descripción |
|------------|---------|-------------|
| **Java** | 17+ | Lenguaje de programación |
| **Spring Boot** | 3.2+ | Framework backend |
| **Spring Security** | 6.x | Autenticación y autorización |
| **Spring Data JPA** | 3.x | Persistencia de datos |
| **MySQL** | 8.0+ | Base de datos relacional |
| **JWT (jjwt)** | 0.11+ | Tokens de autenticación |
| **Lombok** | 1.18+ | Reducción de código boilerplate |
| **Maven** | 3.9+ | Gestión de dependencias |
| **Docker** | 20.x+ | Contenedorización |

---

## 🏗Arquitectura
```
┌─────────────────┐
│  Spring Boot    │
│   (REST API)    │
└────────┬────────┘
         │
    ┌────▼────┐
    │  JWT    │
    │ Filter  │
    └────┬────┘
         │
    ┌────▼──────────┐
    │  Controllers  │
    └────┬──────────┘
         │
    ┌────▼──────────┐
    │   Services    │  ← Lógica de negocio
    └────┬──────────┘
         │
    ┌────▼──────────┐
    │ Repositories  │  ← JPA
    └────┬──────────┘
         │
    ┌────▼──────────┐
    │    MySQL      │
    └───────────────┘
```

**Patrón de diseño:** Arquitectura en capas (Controller → Service → Repository)

---

## Instalación Rápida con Docker

### **Requisitos:**
- [Docker](https://www.docker.com/get-started) instalado
- [Docker Compose](https://docs.docker.com/compose/install/) instalado

### **Pasos:**
```bash
# 1. Clonar el repositorio
git clone https://github.com/AlexIzquierdo21/deepcode_mvp_backend.git
cd deepcode_mvp_backend

# 2. Construir y ejecutar con Docker Compose
docker-compose up --build -d

# 3. Esperar 30-60 segundos a que se inicialice MySQL

# 4. Ver logs para verificar que arrancó correctamente
docker-compose logs -f app

# Deberías ver:
# Iniciando seeders de datos de prueba...
# Usuarios creados: 4
# Challenges creados: 19
# 🚀 API lista en: http://localhost:8080
```

### **Detener la aplicación:**
```bash
# Detener contenedores (conserva datos)
docker-compose down

# Detener y eliminar TODO (incluye base de datos)
docker-compose down -v
```

### **Acceso:**
- **API Backend:** http://localhost:8080
- **MySQL:** localhost:3307 (usuario: `root`, password: `root`)

---

## Probar la API con Postman

### **Importar colección (¡Token automático incluido!)**

La colección de Postman viene preconfigurada con **gestión automática del token JWT**. No necesitas copiar y pegar tokens manualmente.

**Pasos:**

1. **Abrir Postman**
2. Click en **"Import"** (arriba a la izquierda)
3. **Arrastrar o seleccionar** los archivos de la carpeta `postman/`:
    - `DeepCode-API.postman_collection.json`
    - `DeepCode-Local.postman_environment.json`
4. Seleccionar el environment **"DeepCode Local"** (dropdown arriba a la derecha)
5. **¡Listo para probar!**

---

### **Flujo de prueba automático:**

#### **1️-Login (el token se guarda automáticamente)**
```
POST {{base_url}}/auth/login
```

**Body ya incluido:**
```json
{
  "email": "alex@test.com",
  "password": "test123"
}
```

**Magic:** Después del login, el token se guarda automáticamente en la variable `{{token}}` y se usa en todas las peticiones siguientes.

---

#### **2️-Ver todos los challenges (token ya incluido)**
```
GET {{base_url}}/challenges
```

**Sin hacer nada más**, el token se envía automáticamente en el header `Authorization`.

**Resultado:** 19 challenges con seeders

---

#### **3️-Filtrar challenges por lenguaje**
```
GET {{base_url}}/challenges?language=PYTHON
```

**Resultado:** 6 challenges de Python

---

#### **4️-Crear un nuevo challenge**
```
POST {{base_url}}/challenges
```

**Body ya incluido:**
```json
{
  "title": "Nuevo Reto de Ejemplo",
  "description": "Este es un reto creado desde Postman",
  "language": "PYTHON",
  "level": "BEGINNER"
}
```

**Resultado:** Challenge creado con tu usuario

---

#### **5️-Marcar challenge como completado**
```
POST {{base_url}}/progress
```

**Body ya incluido:**
```json
{
  "challengeId": 1,
  "notes": "Completado desde Postman"
}
```

**Resultado:** Progreso registrado

---

#### **6️-Ver mi progreso**
```
GET {{base_url}}/progress
```

**Resultado:** Lista de challenges completados

---

### **Características automáticas de la colección:**

✅ **Token automático:** Se extrae del login y se usa en todas las peticiones  
✅ **Variables de entorno:** `{{base_url}}` y `{{token}}` preconfiguradas  
✅ **Requests pre-llenados:** Todos los bodies de ejemplo ya están incluidos  
✅ **Headers automáticos:** Authorization y Content-Type configurados  
✅ **Scripts de test:** Validaciones automáticas en las respuestas

---

### **Cambiar de usuario:**

Para probar con otro usuario, simplemente cambia el body en el endpoint de login:
```json
{
  "email": "maria@test.com",
  "password": "test123"
}
```

**El nuevo token se guardará automáticamente.**

---

### **Documentación completa:**

Ver [Postman README](./postman/README.md) para más detalles sobre endpoints y ejemplos.

---

## Endpoints de la API

### **Base URL:** `http://localhost:8080`

---

### **Autenticación**

| Método | Endpoint | Descripción | Auth |
|--------|----------|-------------|------|
| POST | `/auth/register` | Registrar nuevo usuario | No |
| POST | `/auth/login` | Iniciar sesión | No |
| GET | `/auth/me` | Ver perfil del usuario autenticado | Sí |

---

### **Challenges**

| Método | Endpoint | Descripción | Auth |
|--------|----------|-------------|------|
| GET | `/challenges` | Listar todos los challenges | Sí |
| GET | `/challenges?language=PYTHON` | Filtrar por lenguaje | Sí |
| GET | `/challenges?level=BEGINNER` | Filtrar por nivel | Sí |
| GET | `/challenges/{id}` | Ver un challenge específico | Sí |
| POST | `/challenges` | Crear nuevo challenge | Sí |
| DELETE | `/challenges/{id}` | Eliminar challenge (solo creador) | Sí |

**Lenguajes disponibles:** `PYTHON`, `JAVA`, `KOTLIN`, `HTML_CSS_JS`  
**Niveles disponibles:** `BEGINNER`, `INTERMEDIATE`

---

### 📊 **Progreso**

| Método | Endpoint | Descripción | Auth |
|--------|----------|-------------|------|
| GET | `/progress` | Ver mi progreso | Sí |
| POST | `/progress` | Marcar challenge como completado | Sí |

---

## Datos de Prueba

La aplicación incluye **seeders automáticos** con datos de ejemplo:

### **Usuarios de prueba:**

| Rol | Email | Password |
|-----|-------|----------|
| Admin | `admin@deepcode.com` | `admin123` |
| User | `alex@test.com` | `test123` |
| User | `maria@test.com` | `test123` |
| User | `carlos@test.com` | `test123` |

---

### **Challenges incluidos:**

| Lenguaje | Beginner | Intermediate | Total |
|----------|----------|--------------|-------|
| 🐍 Python | 3 | 3 | 6 |
| ☕ Java | 2 | 2 | 4 |
| 🔷 Kotlin | 2 | 2 | 4 |
| 🌐 HTML/CSS/JS | 2 | 3 | 5 |
| **📊 TOTAL** | **9** | **10** | **19** |

---

## Estructura del Proyecto
```
deepcode_mvp_backend/
│
├── postman/                            # Colección Postman
│   ├── DeepCode-API.postman_collection.json
│   ├── DeepCode-Local.postman_environment.json
│   └── README.md
│
├── src/main/java/com/deepcode/deepcode_backend/
│   ├── config/
│   │   ├── CorsConfig.java
│   │   ├── DataSeeder.java             # Seeders automáticos
│   │   └── SecurityConfig.java
│   │
│   ├── controller/
│   │   ├── AuthController.java
│   │   ├── ChallengeController.java
│   │   └── ProgressController.java
│   │
│   ├── dto/
│   │   ├── auth/
│   │   ├── challenge/
│   │   └── progress/
│   │
│   ├── entity/
│   │   ├── UserModel.java
│   │   ├── ChallengesModel.java
│   │   ├── UserChallengesModel.java
│   │   ├── LanguageChallenge.java
│   │   └── LevelChallenge.java
│   │
│   ├── repository/
│   ├── security/
│   └── service/
│
├── src/main/resources/
│   ├── application.properties
│   └── application-docker.properties
│
├── Dockerfile                          # Imagen Docker
├── docker-compose.yml                  # Orquestación
├── .dockerignore
├── pom.xml
├── LICENSE
└── README.md
```

---

## 🗺️ Roadmap

### **Completado (Sprint 1 - Octubre/Noviembre 2025)**
- [x] Sistema de autenticación JWT
- [x] CRUD de usuarios
- [x] CRUD de challenges
- [x] Sistema de progreso
- [x] Validaciones de negocio
- [x] Dockerización completa
- [x] Seeders automáticos (19 challenges + 4 usuarios)
- [x] Colección Postman con token automático

### **En desarrollo (Sprint 2 - Diciembre 2025)**
- [ ] Sistema de badges/logros
- [ ] Ranking de usuarios
- [ ] Comentarios en challenges
- [ ] Likes en challenges

### **Planificado (Futuro)**
- [ ] Frontend Android (Jetpack Compose)
- [ ] Sistema de notificaciones
- [ ] CI/CD con GitHub Actions
- [ ] Deploy en Railway/Render
- [ ] Documentación con Swagger/OpenAPI

---

## Contribuciones

Este es un proyecto académico personal, pero si encuentras algún bug o tienes sugerencias:

1. Abre un **Issue** describiendo el problema
2. Haz un **Fork** del proyecto
3. Crea una **rama** para tu feature (`git checkout -b feature/amazing-feature`)
4. Haz **commit** de tus cambios (`git commit -m 'Add amazing feature'`)
5. Haz **push** a la rama (`git push origin feature/amazing-feature`)
6. Abre un **Pull Request**

---

## Licencia

Este proyecto está bajo la licencia **MIT**. Ver archivo [LICENSE](LICENSE) para más detalles.

---

## Contacto

**Alex Izquierdo Rottier** - Desarrollador Backend buscando prácticas en Spring Boot

- 📧 Email: alexdeepcodeai@gmail.com
- 💼 LinkedIn: [linkedin.com](https://www.linkedin.com/in/alex-izquierdo-rottier-1b4225350/)
- 🐙 GitHub: [@AlexIzquierdo21](https://github.com/AlexIzquierdo21)
- 🌐 Portfolio: [deepcodeia.com](https://deepcodeia.com)

---

## Agradecimientos

- **Lorenzo Poderoso Dalmau** - Mentoría durante el desarrollo
- **Spring Boot Community** - Documentación y recursos
- **Stack Overflow** - Resolución de problemas técnicos
- **La Tecnología Avanza** - Canal de YouTube (Curso de Spring Boot)

---

## 📊 Estadísticas del Proyecto

![GitHub last commit](https://img.shields.io/github/last-commit/AlexIzquierdo21/deepcode_mvp_backend)
![GitHub repo size](https://img.shields.io/github/repo-size/AlexIzquierdo21/deepcode_mvp_backend)
![GitHub stars](https://img.shields.io/github/stars/AlexIzquierdo21/deepcode_mvp_backend?style=social)

---

<div align="center">

### **Si este proyecto te resulta útil, dale una estrella en GitHub**

*Proyecto académico desarrollado en 6 semanas (Octubre - Noviembre 2025)*

</div>