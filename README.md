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
- [Stack Tecnológico](#-stack-tecnológico)
- [Arquitectura](#-arquitectura)
- [Instalación Rápida con Docker](#-instalación-rápida-con-docker)
- [Instalación Manual](#-instalación-manual)
- [Endpoints de la API](#-endpoints-de-la-api)
- [Datos de Prueba](#-datos-de-prueba)
- [Estructura del Proyecto](#-estructura-del-proyecto)
- [Testing](#-testing)
- [Roadmap](#-roadmap)
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
- Búsqueda de retos
- Sistema de autoría (cada reto tiene un creador)

### **Sistema de Progreso**
- Los usuarios pueden marcar retos como completados
- Historial de progreso por usuario
- Notas personales en cada reto completado
- Estadísticas de progreso

### **Validaciones de Negocio**
- No se permiten retos duplicados (mismo título + lenguaje + nivel)
- Solo el creador puede eliminar sus propios retos
- Validación de email único en registro

---

## Stack Tecnológico

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

## Arquitectura
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
bash
# 1. Clonar el repositorio
git clone https://github.com/AlexIzquierdo21/deepcode_mvp_backend
cd deepcode-backend

# 2. Construir y ejecutar con Docker Compose
docker-compose up --build -d

# 3. Esperar 30-60 segundos a que se inicialice MySQL

# 4. Ver logs para verificar que arrancó correctamente
docker-compose logs -f app

# Deberías ver:
# Seeders completados
# DATOS CREADOS: 4 usuarios, 19 challenges
# API lista en: http://localhost:8080
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

## Instalación Manual

### **Requisitos:**
- [Java 17+](https://www.oracle.com/java/technologies/downloads/)
- [Maven 3.9+](https://maven.apache.org/download.cgi)
- [MySQL 8.0+](https://dev.mysql.com/downloads/mysql/)

### **Pasos:**

#### 1️**Clonar repositorio**
```bash
git clone https://github.com/tu-usuario/deepcode-backend.git
cd deepcode-backend
```

#### 2️**Crear base de datos**
```sql
-- Conectarse a MySQL
mysql -u root -p

-- Crear base de datos
CREATE DATABASE deepcode_db;
```

#### 3️**Configurar application.properties**

Editar `src/main/resources/application.properties`:
```properties
spring.datasource.url=jdbc:mysql://localhost:3306/deepcode_db
spring.datasource.username=root
spring.datasource.password=TU_PASSWORD_MYSQL
spring.jpa.hibernate.ddl-auto=update
spring.profiles.active=dev
```

#### 4️**Compilar y ejecutar**
```bash
# Compilar proyecto
./mvnw clean install

# Ejecutar aplicación
./mvnw spring-boot:run
```

#### 5️**Verificar**

Abrir navegador en: http://localhost:8080

**Deberías ver mensaje:** `Whitelabel Error Page` (esperado, porque no hay ruta raíz definida)

---

## Endpoints de la API

### **Base URL:** `http://localhost:8080`

---

### **Autenticación**

#### **Registrar usuario**
```http
POST /auth/register
Content-Type: application/json

{
  "username": "Juan Pérez",
  "email": "juan@example.com",
  "password": "password123"
}
```

**Respuesta exitosa (200 OK):**
```json
{
  "token": "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9...",
  "email": "juan@example.com",
  "username": "Juan Pérez"
}
```

---

#### **Login**
```http
POST /auth/login
Content-Type: application/json

{
  "email": "juan@example.com",
  "password": "password123"
}
```

**Respuesta exitosa (200 OK):**
```json
{
  "token": "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9...",
  "email": "juan@example.com",
  "username": "Juan Pérez"
}
```

---

#### **Obtener perfil del usuario autenticado**
```http
GET /auth/me
Authorization: Bearer <tu-token>
```

**Respuesta exitosa (200 OK):**
```json
{
  "id": 1,
  "username": "Juan Pérez",
  "email": "juan@example.com",
  "createdAt": "2025-01-15T10:30:00"
}
```

---

### **Challenges (Retos)**

#### **Listar todos los retos**
```http
GET /challenges
Authorization: Bearer <tu-token>
```

**Respuesta exitosa (200 OK):**
```json
[
  {
    "id": 1,
    "title": "Hello World en Python",
    "description": "Crea un programa que imprima 'Hello World'",
    "language": "PYTHON",
    "level": "BEGINNER",
    "createdAt": "2025-01-15T10:00:00",
    "createdBy": {
      "id": 1,
      "username": "Admin DeepCode"
    }
  }
]
```

---

#### **Filtrar retos por lenguaje**
```http
GET /challenges?language=PYTHON
Authorization: Bearer <tu-token>
```

**Lenguajes disponibles:** `PYTHON`, `JAVA`, `KOTLIN`, `HTML_CSS_JS`

---

#### **Filtrar retos por nivel**
```http
GET /challenges?level=BEGINNER
Authorization: Bearer <tu-token>
```

**Niveles disponibles:** `BEGINNER`, `INTERMEDIATE`

---

#### **Filtrar por lenguaje Y nivel**
```http
GET /challenges?language=PYTHON&level=INTERMEDIATE
Authorization: Bearer <tu-token>
```

---

#### **Buscar retos por ID**
```http
GET /challenges/{id}
Authorization: Bearer <tu-token>
```

---

#### **Crear nuevo reto**
```http
POST /challenges
Authorization: Bearer <tu-token>
Content-Type: application/json

{
  "title": "Calculadora básica",
  "description": "Crea una calculadora con suma, resta, multiplicación y división",
  "language": "PYTHON",
  "level": "BEGINNER"
}
```

**Validaciones:**
- Título mínimo 3 caracteres
- No puede existir reto con mismo título + lenguaje + nivel
- Language y Level son obligatorios

---

#### **Eliminar reto**
```http
DELETE /challenges/{id}
Authorization: Bearer <tu-token>
```

**Nota:** Solo el creador del reto puede eliminarlo.

---

### **Progreso de Usuario**

#### **Ver mi progreso**
```http
GET /progress
Authorization: Bearer <tu-token>
```

**Respuesta exitosa (200 OK):**
```json
[
  {
    "id": 1,
    "challenge": {
      "id": 1,
      "title": "Hello World en Python",
      "language": "PYTHON",
      "level": "BEGINNER"
    },
    "completedAt": "2025-01-15T14:30:00",
    "notes": "Muy fácil, completado en 5 minutos"
  }
]
```

---

#### **Marcar reto como completado**
```http
POST /progress
Authorization: Bearer <tu-token>
Content-Type: application/json

{
  "challengeId": 1,
  "notes": "Completado sin problemas"
}
```

**Validaciones:**
- No se puede marcar el mismo reto como completado dos veces

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

### **Challenges incluidos:**

| Lenguaje | Beginner | Intermediate | Total |
|----------|----------|--------------|-------|
| Python | 3 | 3 | 6 |
| Java | 2 | 2 | 4 |
| Kotlin | 2 | 2 | 4 |
| HTML/CSS/JS | 2 | 3 | 5 |
| **TOTAL** | **9** | **10** | **19** |

---

## Testing con Postman

### **1️-Importar colección Postman**

Descargar archivo: [`DeepCode-API.postman_collection.json`](./postman/DeepCode-API.postman_collection.json)

**Importar en Postman:**
1. Abrir Postman
2. Click en "Import"
3. Seleccionar el archivo `.json`
4. Listo

---

### **2️-Flujo de prueba recomendado:**
```
1. POST /auth/login (con alex@test.com / test123)
   → Copiar el token de la respuesta

2. GET /challenges
   → Ver todos los retos

3. GET /challenges?language=PYTHON
   → Filtrar por lenguaje

4. POST /challenges
   → Crear un nuevo reto

5. POST /progress
   → Marcar reto como completado

6. GET /progress
   → Ver tu progreso

7. DELETE /challenges/{id}
   → Eliminar tu reto creado
```

---

## Estructura del Proyecto
```
deepcode-backend/
│
├── src/main/java/com/deepcode/deepcode_backend/
│   ├── config/
│   │   ├── CorsConfig.java                 # Configuración CORS
│   │   ├── DataSeeder.java                 # Seeders de datos de prueba
│   │   └── SecurityConfig.java             # Configuración Spring Security
│   │
│   ├── controller/
│   │   ├── AuthController.java             # Endpoints de autenticación
│   │   ├── ChallengeController.java        # Endpoints de retos
│   │   └── ProgressController.java         # Endpoints de progreso
│   │
│   ├── dto/
│   │   ├── auth/
│   │   │   ├── LoginRequest.java
│   │   │   ├── RegisterRequest.java
│   │   │   └── AuthResponse.java
│   │   ├── challenge/
│   │   │   └── CreateChallengeRequest.java
│   │   └── progress/
│   │       └── CreateProgressRequest.java
│   │
│   ├── entity/
│   │   ├── UserModel.java                  # Entidad Usuario
│   │   ├── ChallengesModel.java            # Entidad Reto
│   │   ├── UserChallengesModel.java        # Entidad Progreso
│   │   ├── LanguageChallenge.java          # Enum Lenguajes
│   │   └── LevelChallenge.java             # Enum Niveles
│   │
│   ├── repository/
│   │   ├── UserRepository.java
│   │   ├── ChallengesRepository.java
│   │   └── UserChallengesRepository.java
│   │
│   ├── security/
│   │   ├── JwtUtil.java                    # Utilidad JWT
│   │   └── JwtAuthenticationFilter.java    # Filtro JWT
│   │
│   ├── service/
│   │   ├── UserService.java
│   │   ├── ChallengeService.java
│   │   └── ProgressService.java
│   │
│   └── DeepcodeBackendApplication.java     # Clase principal
│
├── src/main/resources/
│   ├── application.properties              # Configuración local
│   └── application-docker.properties       # Configuración Docker
│
├── Dockerfile                              # Imagen Docker
├── docker-compose.yml                      # Orquestación Docker
├── pom.xml                                 # Dependencias Maven
└── README.md                               # Este archivo
```

---

## Testing

### **Ejecutar tests unitarios:**
```bash
./mvnw test
```

### **Tests implementados:**
- Unit tests: Services
- Integration tests: Controllers
- Security tests: JWT validation

**Cobertura actual:** ~70%

---

## 🗺Roadmap

### **Completado (Sprint 1 - Octubre/Noviembre 2025)**
- [x] Sistema de autenticación JWT
- [x] CRUD de usuarios
- [x] CRUD de challenges
- [x] Sistema de progreso
- [x] Validaciones de negocio
- [x] Dockerización
- [x] Seeders automáticos

### **En desarrollo (Sprint 2 - Diciembre 2025)**
- [ ] Sistema de badges/logros
- [ ] Ranking de usuarios
- [ ] Comentarios en challenges
- [ ] Likes en challenges

### **Planificado (Futuro)**
- [ ] API para frontend Android (Jetpack Compose)
- [ ] Sistema de notificaciones
- [ ] Tests automáticos con GitHub Actions
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

## 👨‍Contacto

**Alex Izquierdo Rottier** - Desarrollador Backend Junior buscando prácticas en Spring Boot

- 📧 Email: alexdeepcodeai@gmail.com
- 💼 LinkedIn: [linkedin.com/in/tu-perfil](https://linkedin.com/in/tu-perfil)
- 🐙 GitHub: [@AlexIzquierdo21](https://github.com/AlexIzquierdo21)
- 🌐 Portfolio: [deepcodeia.com](https://tu-portfolio.com)

---

## Agradecimientos

- **Lorenzo Poderoso Dalmau** - Mentoría durante el desarrollo
- **Spring Boot Community** - Documentación y recursos
- **Stack Overflow** - Resolución de problemas técnicos
- **La Tecnología Avanza** - Canal de YouTube (Curso de Spring Boot)

---

## Estadísticas del Proyecto

![GitHub last commit](https://img.shields.io/github/last-commit/tu-usuario/deepcode-backend)
![GitHub issues](https://img.shields.io/github/issues/tu-usuario/deepcode-backend)
![GitHub pull requests](https://img.shields.io/github/issues-pr/tu-usuario/deepcode-backend)

---

<div align="center">

**⭐ Si este proyecto te resulta útil, dale una estrella en GitHub ⭐**

Hecho con ❤️ por [Alex](https://github.com/AlexIzquierdo21)

</div>
```

---

## **Archivos adicionales a crear:**

### **1. LICENSE (MIT)**

**Ubicación:** `LICENSE`
```
MIT License

Copyright (c) 2025 DeepCodeIA

Permission is hereby granted, free of charge, to any person obtaining a copy
of this software and associated documentation files (the "Software"), to deal
in the Software without restriction, including without limitation the rights
to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
copies of the Software, and to permit persons to whom the Software is
furnished to do so, subject to the following conditions:

The above copyright notice and this permission notice shall be included in all
copies or substantial portions of the Software.

THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
SOFTWARE.
```

---

### **2. .gitignore**

Ya deberías tenerlo, pero asegúrate de incluir:
```
target/
!.mvn/wrapper/maven-wrapper.jar
.mvn/
mvnw
mvnw.cmd

### STS ###
.apt_generated
.classpath
.factorypath
.project
.settings
.springBeans
.sts4-cache

### IntelliJ IDEA ###
.idea
*.iws
*.iml
*.ipr

### NetBeans ###
/nbproject/private/
/nbbuild/
/dist/
/nbdist/
/.nb-gradle/
build/
!**/src/main/**/build/
!**/src/test/**/build/

### VS Code ###
.vscode/

### Environment ###
.env
application-local.properties