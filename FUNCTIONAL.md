# DeepCode App Challenges Tracker

## 1. Descripción general

DeepCode Challenges Tracker es una aplicación educativa multiplataforma orientada a estudiantes y entusiastas de la programación. Su objetivo es ofrecer un espacio donde los usuarios puedan explorar, crear y completar retos de programación en distintos lenguajes, además de acceder a contenido educativo en vídeo del canal DeepCodeIA. El sistema se compone de un frontend en Kotlin (Jetpack Compose), un backend en Spring Boot y una base de datos PostgreSQL, creando un ecosistema completo con autenticación, gestión de retos y seguimiento de progreso.

## 2. Público objetivo

La aplicación está dirigida a:

- Estudiantes de programación que buscan practicar con ejercicios reales
- Seguidores del canal DeepCodeIA que desean reforzar lo aprendido
- Autodidactas que desean medir su progreso en distintos lenguajes

## 3. Objetivo del MVP

El MVP busca proporcionar una experiencia educativa completa sin depender de servicios externos, centrándose en:

1. Autenticación segura
2. Gestión básica de retos (ver, crear y marcar completados)
3. Acceso a contenido educativo (videos de YouTube)
4. Seguimiento del progreso del usuario

## 4. Flujo general de la aplicación

```
Login/Register → HomeScreen → (Sección seleccionada)
```

HomeScreen incluye cuatro tiles:

1. **Videos**: lleva a VideosScreen, donde se muestran listas de reproducción de YouTube
2. **Lista de Retos**: acceso a retos filtrables por lenguaje y dificultad
3. **Crear Reto**: formulario para crear nuevos retos
4. **Perfil**: muestra información y progreso del usuario

## 5. Sección de Retos

Los retos están organizados por lenguaje y nivel de dificultad:

- **Lenguajes**: Python, HTML/CSS/JS, Java y Kotlin
- **Niveles**: Principiante e Intermedio (futuro: Difícil)

El usuario puede explorar los retos, ver descripciones, marcarlos como completados y consultar su progreso global desde la sección Perfil.

## 6. Sección de Videos

En VideosScreen el usuario encuentra las diferentes listas de reproducción del canal DeepCodeIA, organizadas por temática o lenguaje. Al hacer clic en una lista, se abre directamente en YouTube.

### Estructura de ejemplo:

- **Python**: Aprendiendo Python desde cero, Retos Python, Proyectos en Python
- **HTML/CSS/JS**: Curso básico y mini proyectos
- **Java/Kotlin**: Fundamentos y Jetpack Compose
- **DeepCodeIA Talks**: Hablando DEEP, Deep Sundays

## 7. Arquitectura técnica

| Componente | Tecnologías |
|------------|-------------|
| **Frontend** | Kotlin + Jetpack Compose (MVVM, Navigation, Retrofit, DataStore) |
| **Backend** | Spring Boot (Spring Web, Security con JWT, Data JPA, Validación) |
| **Base de datos** | PostgreSQL (tablas: users, challenges, user_challenges) |

Todo el desarrollo y ejecución se realiza en local, sin costes externos.

## 8. Funcionalidades principales del MVP

- Registro/Login de usuario
- Visualización de retos por lenguaje y nivel
- Creación de nuevos retos por parte del usuario
- Marcar retos como completados
- Acceso a listas de reproducción de YouTube
- Visualización de progreso en el perfil

## 9. Futuras mejoras

- Integración con IA para generar retos dinámicos
- Sistema de puntos y ranking
- Validación automática de código
- Comunidad o chat interno entre usuarios

## 10. Identidad visual

- **Paleta de colores**: negro, gris oscuro y verde neón (marca DeepCodeIA)
- **Estilo**: moderno, minimalista y tecnológico
- **Tipografía**: sans-serif con títulos en verde brillante
- **Diseño**: basado en tarjetas (tiles) con iconografía clara y legible

## 11. Justificación del proyecto

DeepCode Challenges Tracker representa una propuesta educativa que combina teoría, práctica y seguimiento del progreso. Su desarrollo demuestra competencias clave del ciclo DAM:

- Diseño de apps móviles modernas
- Creación de APIs REST seguras
- Modelado de bases de datos relacionales
- Comunicación cliente-servidor completa

## 12. Esquema base de datos (ERD)

### Tabla: users

| Campo | Tipo | Descripción |
|-------|------|-------------|
| `id` | PK | Identificador único |
| `username` | string (unique) | Nombre de usuario |
| `email` | string (unique) | Correo electrónico |
| `password_hash` | string | Contraseña hasheada |
| `created_at` | timestamp | Fecha de creación |

**Notas**: índice único en `username` y `email`.

### Tabla: challenges

| Campo | Tipo | Descripción |
|-------|------|-------------|
| `id` | PK | Identificador único |
| `title` | string | Título del reto |
| `description` | text | Descripción detallada |
| `language` | enum | PYTHON, HTML_CSS_JS, JAVA, KOTLIN |
| `level` | enum | BEGINNER, INTERMEDIATE (futuro: HARD) |
| `created_by` | FK → users.id | Autor del reto (nullable) |
| `created_at` | timestamp | Fecha de creación |

**Búsquedas típicas**: por `language`, `level`, `created_by`, y texto en `title`.

### Tabla: user_challenges

| Campo | Tipo | Descripción |
|-------|------|-------------|
| `id` | PK | Identificador único |
| `user_id` | FK → users.id | Usuario |
| `challenge_id` | FK → challenges.id | Reto |
| `status` | enum | PENDING, COMPLETED |
| `notes` | text (optional) | Notas del usuario |
| `completed_at` | timestamp (optional) | Fecha de completado |

**Restricciones**:
- Única `(user_id, challenge_id)` → cada usuario tiene como mucho un registro por reto
- Índices en `user_id`, `challenge_id` para rapidez

### Diagrama de relaciones

```
users
 ├─ id (PK)
 ├─ username (unique)
 ├─ email (unique)
 ├─ password_hash
 └─ created_at

challenges
 ├─ id (PK)
 ├─ title
 ├─ description
 ├─ language (ENUM)
 ├─ level (ENUM)
 ├─ created_by (FK → users.id)
 └─ created_at

user_challenges
 ├─ id (PK)
 ├─ user_id (FK → users.id)
 ├─ challenge_id (FK → challenges.id)
 ├─ status (PENDING|COMPLETED)
 ├─ notes
 └─ completed_at
UNIQUE(user_id, challenge_id)
```

## 13. Endpoints REST y estructuras JSON

### Convenciones generales

- **Base URL**: `/api`
- **Auth**: JWT en cabecera `Authorization: Bearer <token>`
- **Formato errores**:

```json
{
  "timestamp": "2025-10-14T18:00:00Z",
  "status": 400,
  "error": "Bad Request",
  "message": "Email ya registrado",
  "path": "/auth/register"
}
```

### A. Autenticación

#### POST /auth/register

**Request**:
```json
{
  "username": "alex",
  "email": "alex@example.com",
  "password": "********"
}
```

**Response** (201 Created):
```json
{
  "message": "User created"
}
```

#### POST /auth/login

**Request**:
```json
{
  "email": "alex@example.com",
  "password": "********"
}
```

**Response** (200 OK):
```json
{
  "token": "JWT_BASE64...",
  "user": {
    "id": 7,
    "username": "alex"
  }
}
```

### B. Usuario

#### GET /users/me

**Requiere**: JWT

**Response** (200 OK):
```json
{
  "id": 7,
  "username": "alex",
  "email": "alex@example.com",
  "stats": {
    "completed": 8,
    "total": 20,
    "byLanguage": {
      "PYTHON": {"completed": 3, "total": 5},
      "JAVA": {"completed": 2, "total": 6}
    },
    "byLevel": {
      "BEGINNER": {"completed": 6, "total": 10},
      "INTERMEDIATE": {"completed": 2, "total": 10}
    }
  }
}
```

### C. Retos

#### GET /challenges

**Query params** (opcionales):
- `language`: PYTHON | HTML_CSS_JS | JAVA | KOTLIN
- `level`: BEGINNER | INTERMEDIATE
- `page`, `size`: (si se implementa paginación)

**Response** (200 OK):
```json
[
  {
    "id": 12,
    "title": "Suma de dígitos",
    "description": "Dado un número, suma sus dígitos",
    "language": "PYTHON",
    "level": "BEGINNER",
    "createdBy": 7,
    "createdAt": "2025-10-14T12:30:00Z"
  }
]
```

#### GET /challenges/{id}

**Response** (200 OK):
```json
{
  "id": 12,
  "title": "Suma de dígitos",
  "description": "Dado un número, suma sus dígitos",
  "language": "PYTHON",
  "level": "BEGINNER",
  "createdBy": 7,
  "createdAt": "2025-10-14T12:30:00Z"
}
```

**Response** (404 Not Found): si no existe.

#### POST /challenges

**Requiere**: JWT

**Request**:
```json
{
  "title": "Palindrome Checker",
  "description": "Comprobar si una palabra es palíndromo",
  "language": "JAVA",
  "level": "INTERMEDIATE"
}
```

**Response** (201 Created):
```json
{
  "id": 31,
  "title": "Palindrome Checker",
  "description": "Comprobar si una palabra es palíndromo",
  "language": "JAVA",
  "level": "INTERMEDIATE",
  "createdBy": 7,
  "createdAt": "2025-10-14T12:35:00Z"
}
```

**Errores**:
- 400: validaciones
- 401: sin token

#### PUT/DELETE /challenges/{id}

**Opcionales en MVP**

- **PUT**: para editar (solo autor/admin)
- **DELETE**: para borrar (solo autor/admin)
- **403 Forbidden**: si no eres el creador

### D. Progreso (marcar como completado)

#### GET /progress/me

**Requiere**: JWT

**Response** (200 OK):
```json
[
  {
    "challengeId": 12,
    "status": "COMPLETED",
    "notes": "Resuelto con recursión",
    "completedAt": "2025-10-14T15:10:00Z"
  }
]
```

#### POST /progress

**Requiere**: JWT

**Request**:
```json
{
  "challengeId": 12,
  "status": "COMPLETED",
  "notes": "Resuelto con recursión"
}
```

**Response** (200 OK - upsert):
```json
{
  "challengeId": 12,
  "status": "COMPLETED",
  "notes": "Resuelto con recursión",
  "completedAt": "2025-10-14T15:10:00Z"
}
```

**Errores**:
- 400: si `challengeId` no existe
- 409: si hay conflicto

## 14. Validaciones y reglas

### users
- `email` y `username` únicos
- `password` mínima longitud (ej. 8 caracteres)

### challenges
- `title` obligatorio, longitud razonable
- `language` y `level` solo valores permitidos

### user_challenges
- `status` ∈ {PENDING, COMPLETED}
- Única `(user_id, challenge_id)`
