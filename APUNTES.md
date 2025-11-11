# **APUNTES CURSO SPRING BOOT APIS REST**
(https://www.youtube.com/watch?v=Ajfz5Zztsv8)

## ¿QUE ES UNA API?

Api (App programming interface) conjunto de reglas que permite que
dos sistemas de software se comuniquen entre sí. Expone funcionalidades o datos
para que otros sistemas puedan utilizarlos, sin necesidad de conocer su implementación
interna.

## ¿QUE ES REST?
REST es un estilo arquitectónico que define cómo interactuar con recursos
a través de peticiones HTTP estandarizadas.

### UNA API PERMITE LA COMUNICACIÓN ENTRE SISTEMAS, Y REST ES UNA FORMA DE DISEÑAR APIS USANDO REGLAS BASADAS EN HTTP.

## JSON (JavaScript Object Notation) 
Es un formato ligero de intercambio de datos,
fácil de leer y escribir para humanos, y fácil de interpretar y generar por máquina.
Se basa en pares clave: valor.

{
    "id": 1,
    "nombre": "Alex"
    "correo": "alex@example.com"
}

## MÉTODOS HTTP
Los métodos HTTP son acciones estándar como [GET, POST, PUT, DELETE] que
indican al servidor qué operación realizar sobre un recurso(por ejemplo, un usuario o
un producto)
Un recurso es cualquier entidad o info accesible en la web, identificada por una URL,
como una página, un usuario o un archivo.

### HTTP(HyperText TransferProtocol) protocola de comunicación usado por la web para enviar y recibir datos.

GET: Obtiene Información                                    GET /usuarios
POST: Crear un nuevo recurso                                POST /usuarios
PUT: Actualizar un recurso completo                         PUT /usuarios/1
PATCH: Actualizar parcialmente un recurso (solo un campo)   PATCH /usuarios/1
DELETE: Eliminar un recurso                                 DELETE /usuarios/1

## MÉTODO IDEMPOTENTE
Es aquel método que, al ejecutarse una o varias veces con los mismos datos, siempre produce el mismo resultado en el servidor
PUT /usuario/123: si envias los mismos datos varias veces, el estado final del recurso será siempre igual
POST /usuario: cada vez que lo envias, crea un nuevo usuario -> resultado distinto cada vez.

## CÓDIGOS DE ESTADO HTTP

![tabla_estadosHTTP.png](tabla_estadosHTTP.png)

## URI, URL y URN

URI: (Uniform Resources Identificator) Cadena de texto que identifica de manera única un recurso en la web, como una dirección web o un nombre. (Concepto más general).

URL: (Uniform Resources Locator) Es un tipo de URI que especifica la dirección exacta para acceder a un recurso en la web, incluyendo el protocola, dominio y ruta.

URN: (Uniforme Resources Name) Es un tipo de URI que identifica un recurso por su nombre, de forma única y persistente, sin indicar su ubicación o cómo acceder a él.

## SEPARACIÓN DE RESPONSABILIDADES

La separación de repsonsabilidades es un principio de diseño de software que dice: "Cada módulo o capa del sistema debe encargarse de una única responsabilidad"

Permite que el código sea más organizado y mantenible.

Cada parte del sistema sea más reutilizable.

Facilita el testeo y trabajo en equipo.

## MODEL - VIEW - CONTROLLER (MVC)

Model: Representa los datos (entidades y lógica de negocio).

View: Interfaz de usuario (lo que ve el usuario).

Controller: Recibe la entrada del usuario y coordina acciones.

## ARQUITECTURA EN 3 CAPAS (USADO EN SPRING BOOT)

Es una forma de estructurar apps dividiendo el código en 3 capas (similar a MVC)

Controller: Recibe las solicitudes HTTP (como /usuarios) y delega a la capa del servicio

Service: Contiene la lógica del negocio(Procesa, transforma, válida datos, etc..)

Repository: Se comunica directamente con la base de datos (JPA, Hibernate...) 

### MVC separa lógica (Model), Interfaz (View), control (Controller), mientras que la arquitectura en capas divide el sistema en niveles jerárquicos (Presentación, negocio, datos)

En proyectos Spring Boot con APIs REST, no usamos directamente View, asi que no aplicamos MVC.

Sin embargo, la arquitectura en capas nace del mismo principio de separación que MVC.

En lugar de View, tenemos repsuestas en formato JSON.

Por lo tanto, usamos una variación más adecuada: --> Controller - Service - Repository (Modelo de 3 capas + DTOs)

# Estructura de paquetes en Spring Boot

---

## Controller — Interfaz HTTP (entrada/salida)
**Responsabilidad:** recibir peticiones HTTP, validar entrada básica, convertir DTO y llamar al Service. Debe devolver un `ResponseEntity` o DTO con código HTTP apropiado. No debe contener lógica de negocio.

**Qué poner en el archivo:**
- Clase anotada `@RestController` o `@Controller`.
- Endpoints con `@RequestMapping`, `@GetMapping`, `@PostMapping`, etc.
- Uso de `@RequestBody`, `@PathVariable`, `@RequestParam`.
- Validación con `@Valid` y `@Validated`.
- Transformaciones mínimas entre DTO y llamadas al Service.

---

## Service — Lógica de negocio y orquestación
**Responsabilidad:** contener la lógica de negocio, las reglas y transacciones. Debe orquestar los repositorios y otras integraciones, aplicando `@Transactional` cuando sea necesario.

**Qué poner en el archivo:**
- Interfaz `Service` y su implementación `ServiceImpl` anotada con `@Service`.
- Métodos que representen casos de uso (no simples CRUDs).
- Manejo de excepciones de negocio.
- Conversión entre Entity y DTO (directamente o mediante un Mapper).

---

## Repository — Acceso a datos (DAO)
**Responsabilidad:** manejar la comunicación con la base de datos. Proporciona métodos para consultas y operaciones de persistencia, sin lógica de negocio.

**Qué poner en el archivo:**
- Interfaz que extienda `JpaRepository` o `CrudRepository`.
- Consultas derivadas (`findByEmail`, `existsBy...`) o personalizadas con `@Query`.
- No incluir lógica condicional ni decisiones de negocio.

---

## Entity — Modelo persistente (JPA)
**Responsabilidad:** representar las tablas y relaciones en la base de datos. Solo debe contener datos y lógica mínima asociada al modelo.

**Qué poner en el archivo:**
- Clase anotada con `@Entity` y `@Table`.
- Campos con `@Id`, `@GeneratedValue`, y mapeos (`@ManyToOne`, `@OneToMany`, etc.).
- Restricciones e índices (`@Column(nullable = false)`).
- Evitar reglas de negocio complejas.

---

## DTO (Data Transfer Object) — Contrato API / capa externa
**Responsabilidad:** definir los objetos que se utilizan para entrada y salida en las APIs. Aíslan el modelo interno (Entity) del externo (API).

**Qué poner en el archivo:**
- Clases para creación, actualización y salida (`CreateDto`, `UpdateDto`, `Dto`).
- Anotaciones de validación (`@NotBlank`, `@Email`, `@Size`).
- Sin lógica compleja ni dependencias con entidades o repositorios.

---

## Exception — Manejo de errores y respuestas coherentes
**Responsabilidad:** definir y manejar las excepciones personalizadas. Centraliza el manejo de errores y genera respuestas HTTP coherentes.

**Qué poner en el archivo:**
- Excepciones personalizadas (`ResourceNotFoundException`, `BusinessException`, etc.).
- Clase `@ControllerAdvice` con métodos `@ExceptionHandler` para mapear errores a códigos HTTP.
- DTO de error (`ErrorDto`) con campos como `message`, `timestamp`, `code`, `details`.

---

## Reglas de oro y buenas prácticas
1. **No mezcles capas.** Si hay lógica de negocio en el controller, refactoriza.
2. **Usa DTOs en toda entrada/salida.** No expongas entities directamente.
3. **Service = transacciones.** Marca métodos con `@Transactional` donde haya cambios.
4. **Repository = consultas.** Evita lógica condicional o validaciones.
5. **Mapper.** Usa MapStruct o ModelMapper para evitar conversiones manuales.
6. **Validación.** Coloca las anotaciones en DTOs y usa `@Valid` en controllers.
7. **Excepciones unchecked.** Usa excepciones que extiendan `RuntimeException`.
8. **Tests.** Unit tests para Service; Integration tests para Repository y Controller.




![Estructura_básica.png](Estructura_b%C3%A1sica.png)

# ANOTACIONES EN SPRING BOOT

---

## Capa de Entidad (Modelo / Base de Datos)
Estas anotaciones definen cómo se mapean tus clases y campos a tablas en la base de datos.

- **@Entity:** Indica que la clase es una entidad JPA, es decir, representa una tabla en la base de datos. Cada objeto será una fila (registro).
- **@Id:** Marca el campo que será la clave primaria (Primary Key).
- **@GeneratedValue:** Indica que el valor del ID se genera automáticamente (auto-incremental).
- **@Column:** Configura cómo se mapea un campo de la clase con una columna específica de la base de datos.
- **@Enumerated:** Se usa cuando una variable es un `enum` y quieres guardar su valor en la base de datos.
- **@ManyToOne:** se usa en JPA (Hibernate) para indicar una relación entre entidades donde muchos registros de una tabla están asociados a uno solo de otra tabla.
- **@Data:** (Lombok) Genera automáticamente getters, setters, toString, equals, hashCode, etc.
- **@AllArgsConstructor:** (Lombok) Genera un constructor con todos los argumentos (todas las variables como parámetros).
- **@NoArgsConstructor:** (Lombok) Genera un constructor vacío (sin parámetros).
- **@JoinColumn:** se usa junto con anotaciones de relación como @ManyToOne, @OneToOne, etc., para indicar el nombre de la columna que actúa como clave foránea (foreign key) en la base de datos.

---

## Capa de Repositorio (Acceso a Datos)
Aquí se manejan las operaciones CRUD sobre la base de datos.

- **@Repository:** Marca una clase o interfaz como componente de acceso a datos (DAO). Indica a Spring que esta clase se encarga de interactuar con la base de datos.

---

## Capa de Servicio (Lógica de Negocio)
Aquí se implementa la lógica de la aplicación y se usan los repositorios.

- **@Autowired:** Inyecta automáticamente dependencias (por ejemplo, un repositorio dentro de un servicio o controlador).

---

## Capa de Controlador (API / Peticiones HTTP)
Estas anotaciones gestionan las rutas y los datos que llegan o salen por la API.

- **@RestController:** Indica que la clase es un controlador REST, que maneja peticiones HTTP y devuelve datos (JSON).
- **@RequestMapping:** Define la ruta base o general del controlador (por ejemplo, `/api/usuarios`).
- **@GetMapping:** Maneja peticiones HTTP GET (leer datos).
- **@PostMapping:** Maneja peticiones HTTP POST (crear recursos).
- **@DeleteMapping:** Maneja peticiones HTTP DELETE (eliminar recursos).
- **@RequestBody:** Recibe datos JSON en el cuerpo de la petición y los convierte a un objeto Java.
- **@PathVariable:** Captura valores directamente desde la URL y los pasa como parámetros al método.
- **@PutMapping:** Sirve para editar o reemplazar un registro ya guardado en la base de datos. Maneja peticiones HTTP PUT.
- **@RequestParam:** Se usa en los controladores para recibir parámetros desde la URL, concretamente desde la parte del query String, es decir, después del signo "?".


## Otras anotaciones útiles (Lombok / Utilitarias)

- **@SneakyThrows:** (Lombok) Permite lanzar excepciones verificadas sin declararlas ni capturarlas con `try/catch`.  
  Se puede usar en cualquier capa, aunque se recomienda solo en casos puntuales (tests, ejemplos o lógica controlada).






### PREGUNTAS CLASE

- EN EL ENUM CLASS TENGO DOS ESTADOS; ¿Podría utilizar un boolean? En caso afirmativo
- ¿por que en la bbdd no me aparece la tabla en orden?
- Diference en eliminarProducto y eliminarCateogira Responseentity<?> y ResponseEntity<Void>
- @RequestBody y @RequestParam creo que lo entiendo... es decir en la URL lo que va después de {idCategoria}?nombre=Laptop

Acceder producto usuario tiene mas sentido cuando no actualizas muchos datos. JSON + protección.