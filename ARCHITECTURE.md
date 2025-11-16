# DOCUMENTACIÓN DE ESTRUCTURA DEL PROYECTO - DeepCode Backend

## Arquitectura General

**DeepCode Backend** es una API REST desarrollada con **Spring Boot** siguiendo el patrón de **arquitectura en capas** (Layered Architecture). La estructura está organizada para separar responsabilidades: configuración, controladores, DTOs, entidades, repositorios, seguridad y servicios.

---

## Estructura de Carpetas Principal
```
com.deepcode.deepcode_backend/
├── config/            → Configuración de Spring (Seguridad)
├── controller/        → Endpoints REST (API)
├── dto/               → Data Transfer Objects (requests/responses)
├── entity/            → Entidades JPA (modelos de BD)
├── repository/        → Interfaces JPA para acceso a BD
├── security/          → Autenticación JWT y filtros
├── service/           → Lógica de negocio
└── DeepCodeBackendApplication.java → Clase principal
```

---

## CAPA CONFIG (`config/`)

**Responsabilidad:** Configuración de Spring Security y otros componentes del framework.

| Archivo | Descripción |
|---------|-------------|
| `SecurityConfig.java` | Configuración de Spring Security (rutas públicas/protegidas, filtros JWT, CORS, CSRF) |

**Funcionalidades:**
- Define rutas públicas: `/auth/**`
- Define rutas protegidas: todo lo demás (requiere JWT)
- Configura `BCryptPasswordEncoder` para encriptar contraseñas
- Añade `JwtAuthenticationFilter` a la cadena de filtros
- Desactiva CSRF (API REST stateless)

---

## CAPA CONTROLLER (`controller/`)

**Responsabilidad:** Expone endpoints REST y maneja peticiones HTTP.

| Archivo | Descripción |
|---------|-------------|
| `AuthController.java` | Endpoints de autenticación (POST /auth/register, POST /auth/login) |
| `ChallengeController.java` | Endpoints de gestión de retos (GET, POST, DELETE /challenges) |
| `ProgressController.java` | Endpoints de progreso del usuario (POST /progress, GET /progress/me) |
| `TestController.java` | Endpoints de prueba (opcional, para testing) |
| `UserController.java` | Endpoints de información del usuario (GET /users/me) |

### **Endpoints implementados:**

#### **AuthController** (`/auth`)
- `POST /auth/register` - Registrar nuevo usuario
- `POST /auth/login` - Iniciar sesión (devuelve JWT)

#### **UserController** (`/users`)
- `GET /users/me` - Obtener perfil del usuario autenticado

#### **ChallengeController** (`/challenges`)
- `POST /challenges` - Crear nuevo reto (requiere JWT)
- `GET /challenges` - Listar todos los retos con filtros opcionales (`?language=PYTHON&level=BEGINNER`)
- `GET /challenges/my-challenges` - Obtener retos creados por el usuario autenticado
- `GET /challenges/{id}` - Obtener reto específico por ID
- `DELETE /challenges/{id}` - Eliminar reto (solo el creador puede eliminarlo)

#### **ProgressController** (`/progress`)
- `POST /progress` - Marcar reto como completado (requiere JWT)
- `GET /progress/me` - Obtener progreso completo del usuario autenticado

---

## CAPA DTO (`dto/`)

**Responsabilidad:** Objetos de transferencia de datos entre frontend y backend (no se persisten en BD).

### dto/auth/
DTOs relacionados con autenticación.

| Archivo | Descripción |
|---------|-------------|
| `AuthResponse.java` | Respuesta de login/register (contiene: token JWT, username, email) |
| `LoginRequest.java` | Request de login (contiene: email, password) |
| `RegisterRequest.java` | Request de registro (contiene: username, email, password) |

---

### dto/challenge/
DTOs relacionados con retos.

| Archivo | Descripción |
|---------|-------------|
| `CreateChallengeRequest.java` | Request para crear reto (contiene: title, description, language, level) |

---

### dto/progress/
DTOs relacionados con progreso del usuario.

| Archivo | Descripción |
|---------|-------------|
| `MarkChallengeRequest.java` | Request para marcar reto como completado (contiene: challengeId, notes opcionales) |

---

##  CAPA ENTITY (`entity/`)

**Responsabilidad:** Entidades JPA que representan las tablas de la base de datos MySQL.

| Archivo | Descripción | Tabla BD |
|---------|-------------|----------|
| `UserModel.java` | Usuario de la plataforma | `users` |
| `ChallengesModel.java` | Reto de programación | `challenges` |
| `UserChallenge.java` | Relación usuario-reto (progreso) | `user_challenges` |
| `LanguageChallenge.java` | Enum de lenguajes (PYTHON, JAVA, KOTLIN, HTML_CSS_JS) | - |
| `LevelChallenge.java` | Enum de niveles (BEGINNER, INTERMEDIATE) | - |
| `StatusChallenge.java` | Enum de estados (PENDING, COMPLETED) | - |

### **Relaciones entre entidades:**

**UserModel (1) ↔ (N) ChallengesModel**
- Un usuario puede crear muchos retos
- Relación `@OneToMany` en UserModel
- Relación `@ManyToOne` en ChallengesModel (campo `createdBy`)

**UserModel (1) ↔ (N) UserChallenge**
- Un usuario puede tener progreso en muchos retos
- Relación `@OneToMany` en UserModel

**ChallengesModel (1) ↔ (N) UserChallenge**
- Un reto puede ser intentado por muchos usuarios
- Relación `@OneToMany` en ChallengesModel

**UserChallenge** es la tabla intermedia que relaciona `users` con `challenges` y almacena:
- `status` (PENDING, COMPLETED)
- `notes` (notas del usuario sobre el reto)
- `completedAt` (fecha de completado)

---

## CAPA REPOSITORY (`repository/`)

**Responsabilidad:** Interfaces JPA que Spring Data implementa automáticamente para acceso a BD.

| Archivo | Descripción |
|---------|-------------|
| `UserRepository.java` | Acceso a datos de usuarios (métodos: findByEmail, existsByEmail) |
| `ChallengesRepository.java` | Acceso a datos de retos (métodos: findByLanguage, findByLevel, findByCreatedBy) |
| `UserChallengeRepository.java` | Acceso a datos de progreso (métodos: findByUserId, findByUserIdAndChallengeId) |

**Métodos personalizados implementados:**
- `Optional<UserModel> findByEmail(String email)`
- `boolean existsByEmail(String email)`
- `List<ChallengesModel> findByLanguage(LanguageChallenge language)`
- `List<ChallengesModel> findByLevel(LevelChallenge level)`
- `List<ChallengesModel> findByCreatedBy(UserModel createdBy)`
- `List<UserChallenge> findByUserId(UserModel userId)`
- `Optional<UserChallenge> findByUserIdAndChallengeId(UserModel userId, ChallengesModel challengeId)`

---

## CAPA SECURITY (`security/`)

**Responsabilidad:** Maneja la autenticación JWT y seguridad de la aplicación.

| Archivo | Descripción |
|---------|-------------|
| `JwtAuthenticationFilter.java` | Filtro que intercepta peticiones HTTP, valida JWT y autentica usuarios |
| `JwtUtil.java` | Utilidades para generar, validar y extraer información de tokens JWT |

### **JwtAuthenticationFilter.java**
**Flujo de ejecución:**
1. Intercepta todas las peticiones HTTP
2. Extrae el header `Authorization`
3. Si no hay header o no empieza con "Bearer " → continúa sin autenticar
4. Extrae el token JWT (quita "Bearer ")
5. Valida el token con `JwtUtil`
6. Busca el usuario en BD por email
7. Si todo es válido → crea `UsernamePasswordAuthenticationToken`
8. Setea el usuario autenticado en `SecurityContextHolder`
9. Continúa con la cadena de filtros

### **JwtUtil.java**
**Métodos principales:**
- `generateToken(String email)` - Genera JWT firmado con secret key (válido 10 horas)
- `extractEmail(String token)` - Extrae el email (subject) del token
- `validateToken(String token, String email)` - Verifica firma y expiración

**Configuración:**
- Secret key: definida en `application.properties` (`jwt.secret`)
- Tiempo de expiración: 10 horas (`jwt.expiration`)

---

## CAPA SERVICE (`service/`)

**Responsabilidad:** Contiene la lógica de negocio de la aplicación.

| Archivo | Descripción |
|---------|-------------|
| `AuthService.java` | Lógica de autenticación (registro, login, generación de JWT) |
| `UserService.java` | Lógica de gestión de usuarios (buscar por email, obtener perfil) |
| `ChallengeService.java` | Lógica de gestión de retos (CRUD, filtros, validaciones) |
| `UserChallengeService.java` | Lógica de progreso del usuario (marcar completado, obtener progreso) |

### **AuthService.java**
**Métodos:**
- `register(RegisterRequest)` → Registra nuevo usuario, encripta password, genera JWT
- `login(LoginRequest)` → Valida credenciales, genera JWT

**Validaciones:**
- Email único (no puede haber duplicados)
- Contraseña encriptada con BCrypt
- Generación de token JWT al registrar/login

---

### **UserService.java**
**Métodos:**
- `findByEmail(String email)` → Busca usuario por email (usado por JWT filter)
- `getCurrentUser(String email)` → Obtiene información del usuario autenticado

---

### **ChallengeService.java**
**Métodos:**
- `createChallenge(CreateChallengeRequest, String email)` → Crea reto asociado al usuario autenticado
- `getAllChallenges()` → Lista todos los retos
- `getChallengesByFilters(language, level)` → Filtra retos por lenguaje y/o nivel
- `getMyCreatedChallenges(String email)` → Obtiene retos creados por el usuario autenticado
- `getChallengeById(Long id)` → Obtiene reto específico
- `deleteChallenge(Long id, String email)` → Elimina reto (valida que el usuario sea el creador)

**Validaciones:**
- Solo el creador puede eliminar su reto
- Campos obligatorios: title, description, language, level
- Relación automática con usuario autenticado (createdBy)

---

### **UserChallengeService.java**
**Métodos:**
- `markAsCompleted(Long challengeId, String email, String notes)` → Marca reto como completado
- `getUserProgress(String email)` → Obtiene todo el progreso del usuario

**Lógica de markAsCompleted:**
1. Busca usuario por email (del JWT)
2. Busca reto por ID
3. Verifica si ya existe relación UserChallenge:
    - **Si ya está COMPLETED** → Lanza excepción "Ya completaste este reto"
    - **Si está PENDING** → Actualiza a COMPLETED, setea `completedAt` y `notes`
    - **Si no existe** → Crea nueva relación con status COMPLETED
4. Guarda en BD y devuelve el UserChallenge actualizado

---

## CLASE PRINCIPAL

| Archivo | Descripción |
|---------|-------------|
| `DeepCodeBackendApplication.java` | Clase main que arranca la aplicación Spring Boot |

---

## 🔗 FLUJO COMPLETO DE UNA PETICIÓN (Ejemplo: Marcar reto como completado)

1. Cliente Android envía:
   POST /progress
   Headers: Authorization: Bearer <JWT>
   Body: { "challengeId": 5, "notes": "Completado" }
   
   ↓

2. JwtAuthenticationFilter intercepta la petición:
   - Extrae token del header
   - Valida con JwtUtil
   - Busca usuario en BD
   - Setea autenticación en SecurityContext
   
   ↓

3. Spring Security verifica autorización:
   - /progress requiere autenticación ✓
   - Usuario autenticado ✓
   - Permite continuar
   
   ↓

4. ProgressController.markAsCompleted() recibe petición:
   - Obtiene email del SecurityContext
   - Valida body con @Valid (MarkChallengeRequest)
   - Llama al service
   
   ↓

5. UserChallengeService.markAsCompleted():
   - Busca usuario por email
   - Busca reto por ID
   - Verifica si ya existe UserChallenge
   - Decide: crear nuevo o actualizar existente
   - Guarda en BD
   
   ↓

6. UserChallengeRepository.save():
   - JPA ejecuta INSERT o UPDATE en user_challenges
   
   ↓

7. Response 200 OK:
   {
     "id": 10,
     "userId": {...},
     "challengeId": {...},
     "status": "COMPLETED",
     "notes": "Completado",
     "completedAt": "2025-11-15T14:30:00"
   }
   
   ↓

8. Cliente Android recibe respuesta exitosa


---

## BASE DE DATOS (MySQL)

### **Tablas:**

#### **users**

CREATE TABLE users (
    id BIGINT PRIMARY KEY AUTO_INCREMENT,
    username VARCHAR(100) NOT NULL,
    email VARCHAR(100) UNIQUE NOT NULL,
    password VARCHAR(255) NOT NULL,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);


#### **challenges**

CREATE TABLE challenges (
    id BIGINT PRIMARY KEY AUTO_INCREMENT,
    title VARCHAR(200) NOT NULL,
    description TEXT NOT NULL,
    language VARCHAR(50) NOT NULL, -- PYTHON, JAVA, KOTLIN, HTML_CSS_JS
    level VARCHAR(50) NOT NULL, -- BEGINNER, INTERMEDIATE
    created_by_id BIGINT NOT NULL,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (created_by_id) REFERENCES users(id)
);


#### **user_challenges**

CREATE TABLE user_challenges (
    id BIGINT PRIMARY KEY AUTO_INCREMENT,
    user_id_id BIGINT NOT NULL,
    challenge_id_id BIGINT NOT NULL,
    status VARCHAR(50) NOT NULL, -- PENDING, COMPLETED
    notes TEXT,
    completed_at TIMESTAMP,
    FOREIGN KEY (user_id_id) REFERENCES users(id),
    FOREIGN KEY (challenge_id_id) REFERENCES challenges(id)
);


---

## SEGURIDAD IMPLEMENTADA

### **Autenticación:**
- JWT (JSON Web Tokens) con firma HMAC-SHA256
- Tokens válidos por 10 horas
- Secret key configurable en `application.properties`

### **Autorización:**
- Rutas públicas: `/auth/**` (login, register)
- Rutas protegidas: Todo lo demás (requiere JWT válido)
- Validación por creador: Solo el creador puede eliminar sus retos

### **Encriptación:**
- Contraseñas encriptadas con BCrypt (salt automático)
- Nunca se almacenan contraseñas en texto plano

### **Validaciones:**
- DTOs con `@Valid` y anotaciones Jakarta Validation
- `@NotNull`, `@NotBlank`, `@Email`, etc.
- Validaciones de negocio en services

---

## TECNOLOGÍAS UTILIZADAS

### **Framework:**
- Spring Boot 3.x
- Spring Web (REST)
- Spring Data JPA
- Spring Security

### **Base de datos:**
- MySQL 8.x
- Hibernate (ORM)

### **Seguridad:**
- JWT (jjwt library)
- BCrypt

### **Herramientas:**
- Lombok (reduce boilerplate)
- Jakarta Validation
- Maven (gestor de dependencias)

---

## ESTADÍSTICAS DEL PROYECTO

- **Total de endpoints:** 12
- **Total de entidades:** 3 (+ 3 enums)
- **Total de repositorios:** 3
- **Total de services:** 4
- **Total de controllers:** 5
- **Total de DTOs:** 5
- **Líneas de código:** ~2500+

---

## 📡 ENDPOINTS COMPLETOS

| Método | Endpoint | Auth | Descripción |
|--------|----------|------|-------------|
| POST | `/auth/register` | No | Registrar usuario |
| POST | `/auth/login` | No | Iniciar sesión |
| GET | `/users/me` | Sí | Perfil usuario autenticado |
| POST | `/challenges` | Sí | Crear reto |
| GET | `/challenges` | Sí | Listar retos (con filtros) |
| GET | `/challenges/my-challenges` | Sí | Mis retos creados |
| GET | `/challenges/{id}` | Sí | Obtener reto por ID |
| DELETE | `/challenges/{id}` | Sí | Eliminar reto (solo creador) |
| POST | `/progress` | Sí | Marcar reto completado |
| GET | `/progress/me` | Sí | Ver mi progreso |

---

## CARACTERÍSTICAS DESTACADAS

### **Arquitectura:**
- Arquitectura en capas bien definida
- Separación de responsabilidades (Controller → Service → Repository)
- DTOs para contratos API limpios
- Entidades JPA con relaciones complejas

### **Seguridad:**
- JWT authentication stateless
- Filtro personalizado para validar tokens
- Contraseñas encriptadas con BCrypt
- Autorización basada en roles (creador de reto)

### **API REST:**
- Endpoints RESTful bien diseñados
- Códigos HTTP apropiados (200, 204, 400, 401, 403, 404)
- Validaciones robustas con Jakarta Validation
- Manejo de errores con excepciones personalizadas

### **Base de datos:**
-  Relaciones complejas (OneToMany, ManyToOne)
-  Spring Data JPA para acceso simplificado
-  Métodos de consulta personalizados
-  Enums para valores constantes

---

## CONCLUSIÓN

**DeepCode Backend** es una API REST profesional que demuestra:
- Conocimientos sólidos de Spring Boot y arquitectura backend
- Implementación correcta de autenticación JWT
- Diseño de API RESTful siguiendo mejores prácticas
- Manejo robusto de seguridad y validaciones
- Código limpio y bien organizado en capas

---

**Desarrollado por:** Alex Izquierdo Rottier
**Fecha:** Marzo 2026  
**Tecnología:** Spring Boot 3.x + MySQL  
**Arquitectura:** Layered Architecture (MVC + Services)