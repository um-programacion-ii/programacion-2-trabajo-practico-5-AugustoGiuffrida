# Prompts de desarrollo


## Prompt 1: Se buscaba entender la configuracion y funcionamiento de las relaciones entre entidades

### Prompt Utilizado: 

Estoy Desarrollando un sistema de gestión de empleados utilizando Spring Boot con JPA, 
implementando diferentes profiles para trabajar con múltiples bases de datos (H2, MySQL y PostgreSQL). 
El sistema deberá manejar empleados, departamentos y proyectos, 
utilizando Docker para containerizar las bases de datos y aplicando las mejores prácticas de JPA.

Mi dudas actual son con la configuracion y funcionamiento de la relaciones entre entidades
Usa este ejemplo para la explicacion

```java
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "departamento_id")
    private Departamento departamento;

 @OneToMany(mappedBy = "departamento", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<Empleado> empleados = new ArrayList<>();

    @ManyToMany
    @JoinTable(name = "empleado_proyecto",
        joinColumns = @JoinColumn(name = "empleado_id"),
        inverseJoinColumns = @JoinColumn(name = "proyecto_id")
    )
    private Set<Proyecto> proyectos = new HashSet<>();
```

- Para que se usa fetch = FetchType.LAZY y cuando deberia usarlo?
- Que hace cascade = CascadeType.ALL ?
- Por ejemplo, si en la tabla empleado defino la columna departamento_id la cual es una clave foranea que contiene las claves primarias de la tabla de departamento , que mecanismo se encarga de asociar la clave foranea de la tabla de empleado con la clave primaria de la tabla departamento y como funciona?
- Porque en las relaciones @ManyToMany se utilizan Set en lugar de List


### Respuesta Recibida:
Perfecto, vamos a desglosar cada parte de tu duda paso a paso, usando el ejemplo que proporcionaste. Este tipo de relaciones son el núcleo del manejo de entidades en JPA y es importante comprender bien su funcionamiento. Vamos punto por punto:

---

### 🔹 1. ¿Para qué se usa `fetch = FetchType.LAZY` y cuándo debería usarse?

**`FetchType.LAZY`** (carga perezosa) le indica a JPA que **no cargue la entidad relacionada de inmediato**, sino **solo cuando sea necesaria** (cuando se accede explícitamente).
Esto es útil para **optimizar el rendimiento**, sobre todo cuando la relación puede contener muchos elementos o no siempre se necesita acceder a ella.

#### Ejemplo:

```java
Empleado e = empleadoRepository.findById(1L).get();
// NO se carga el departamento automáticamente
Departamento d = e.getDepartamento(); // recién aquí se hace la consulta a la base de datos
```

#### ¿Cuándo deberías usarlo?

* Cuando la entidad relacionada no siempre se necesita (evitas sobrecarga).
* En relaciones 1\:N o N\:N que podrían traer muchas filas (como listas de empleados o proyectos).
* Siempre que quieras tener **control total sobre cuándo se consulta** la relación.

#### ¿Cuándo usar `EAGER`?

* Cuando **siempre necesitas la relación** al cargar la entidad principal (por ejemplo, un usuario y su rol).

---

### 🔹 2. ¿Qué hace `cascade = CascadeType.ALL`?

El atributo `cascade` le dice a JPA **qué operaciones deben “propagarse”** de una entidad principal a las relacionadas.

**`CascadeType.ALL`** incluye todas las operaciones:

* `PERSIST` (guardar)
* `MERGE` (actualizar)
* `REMOVE` (eliminar)
* `REFRESH` (refrescar desde la base)
* `DETACH` (desvincular del contexto de persistencia)

#### Ejemplo:

```java
Departamento d = new Departamento();
Empleado e = new Empleado();
e.setDepartamento(d);

empleadoRepository.save(e); // también guarda automáticamente el departamento gracias a cascade
```

#### Precaución:

Usar `CascadeType.ALL` **no siempre es buena idea**, especialmente en relaciones `@ManyToOne`, ya que podrías terminar modificando/eliminando entidades compartidas.

💡 **Recomendado**: usar `cascade` principalmente en relaciones `@OneToMany` y `@OneToOne`, cuando las entidades relacionadas son **exclusivas** de la principal.

---

### 🔹 3. ¿Cómo se asocia la clave foránea (`departamento_id`) con la clave primaria de `departamento`?

Este proceso se maneja automáticamente por JPA y Hibernate usando la anotación:

```java
@ManyToOne(fetch = FetchType.LAZY)
@JoinColumn(name = "departamento_id")
private Departamento departamento;
```

#### ¿Qué hace internamente?

1. `@ManyToOne` indica que muchos empleados pueden estar en un departamento.
2. `@JoinColumn(name = "departamento_id")` le dice a JPA que en la **tabla de empleados** hay una columna `departamento_id` que **referencia a la PK** de la tabla `departamento`.
3. Cuando guardás un empleado con `empleado.setDepartamento(d)`, JPA extrae `d.getId()` y guarda ese valor en la columna `departamento_id`.

#### Mecanismo de asociación:

* El **mapeo** lo define el `@JoinColumn`.
* La **asociación lógica** se da por la propiedad `departamento` dentro de `Empleado`.
* La **persistencia** es responsabilidad del **EntityManager/Hibernate**, que traduce eso a SQL.

---

### 🔹 4. ¿Por qué se usa `Set<Proyecto>` en relaciones `@ManyToMany` en lugar de `List<Proyecto>`?

En relaciones `@ManyToMany`, se usa generalmente `Set` porque:

✅ **Evita duplicados**: Un empleado **no debería estar dos veces en el mismo proyecto**.

✅ **Representa una colección sin orden garantizado**, como suele ocurrir en relaciones N\:N (la tabla intermedia no tiene un campo de orden natural).

✅ Es más eficiente en operaciones de búsqueda (`contains`, `remove`) porque internamente utiliza `HashMap`.

#### ¿Se puede usar `List`?

Sí, pero perderías la garantía de unicidad y podrías tener entradas repetidas en la tabla intermedia `empleado_proyecto`.

---

### 📌 Recapitulación final

| Relación                | Anotación                    | ¿Quién tiene la FK?  | ¿Por qué usar LAZY?                                                   | ¿Cuándo usar Cascade.ALL?                                         |
| ----------------------- | ---------------------------- | -------------------- | --------------------------------------------------------------------- | ----------------------------------------------------------------- |
| Empleado - Departamento | `@ManyToOne` + `@JoinColumn` | Empleado             | Para no cargar el departamento salvo que se acceda                    | En general **NO** usarlo aquí (departamento puede ser compartido) |
| Departamento - Empleado | `@OneToMany(mappedBy = ...)` | FK sigue en Empleado | Si la lista es grande, conviene usar LAZY                             | Útil si los empleados dependen completamente del departamento     |
| Empleado - Proyecto     | `@ManyToMany` + `@JoinTable` | Tabla intermedia     | Usar LAZY para evitar traer todos los proyectos al cargar un empleado | Depende del diseño: cuidado al borrar                             |

---

## Prompt 2: Se buscar entender como se configura correctamente los controladores 


### Prompt utilizado

Estoy desarrollando un sistema de gestión de empleados con **Spring Boot + JPA**, perfiles para **H2/MySQL/PostgreSQL** y Docker.

Entidades: **Empleado**, **Departamento**, **Proyecto**.

Relaciones:

* `Empleado` — `@ManyToOne` → `Departamento`
* `Departamento` — `@OneToMany` → `Empleado`
* `Empleado` — `@ManyToMany` → `Proyecto` (tabla `empleado_proyecto`)

Necesito una guía  específicamente **cuándo y cómo usar `@PathVariable`, `@RequestBody` y `@RequestParam`**.

**Objetivo**

> Explicá **para qué se usan** `@PathVariable` `@RequestBody` y `@RequestParam`, **cuándo aplicarlos**, **cómo combinarlos**, y dame **ejemplos completos** . Incluí **antipatrónes** y **buenas prácticas**.

### Respuesta recibida:

¡Perfecto! Vamos a desglosar el uso de `@PathVariable`, `@RequestParam` y `@RequestBody` en **Spring Boot**, enfocándonos en tu sistema de gestión de empleados. La idea es que quede claro **para qué sirve cada uno, cuándo usarlo, cómo combinarlos y qué evitar**.

---

## 1️⃣ `@PathVariable`

**Qué hace:**
Se usa para **extraer valores de la URL** y pasarlos como parámetros a tu método del controlador.

**Cuándo usarlo:**

* Cuando el valor es **parte de la ruta del recurso**.
* Generalmente se usa para **identificar un recurso específico** (ej. un empleado por su ID).

**Ejemplo: Obtener un empleado por ID**

```java
@RestController
@RequestMapping("/api/empleados")
public class EmpleadoController {

    @GetMapping("/{id}")
    public ResponseEntity<Empleado> getEmpleado(@PathVariable Long id) {
        Empleado e = empleadoService.findById(id);
        return ResponseEntity.ok(e);
    }
}
```

**Buen patrón:**

* Usar `@PathVariable` solo para **identificadores** o recursos jerárquicos.
* Nombrar la variable igual que en la ruta: `/{id}` → `@PathVariable Long id`.

**Antipatrón:**

```java
// ❌ Evitar pasar parámetros opcionales que no son IDs por PathVariable
@GetMapping("/empleados/{nombre}")
public Empleado findByNombre(@PathVariable String nombre) { ... }
```

* Esto rompe la RESTful convention: nombres o filtros deberían ir en query params (`@RequestParam`), no en la ruta.

---

## 2️⃣ `@RequestParam`

**Qué hace:**
Se usa para **extraer valores de parámetros de consulta** (query parameters) de la URL.

**Cuándo usarlo:**

* Para **filtrar, paginar o buscar** recursos.
* Valores opcionales o múltiples parámetros de búsqueda.

**Ejemplo: Buscar empleados por departamento y paginar**

```java
@GetMapping
public List<Empleado> buscarEmpleados(
        @RequestParam(required = false) Long departamentoId,
        @RequestParam(defaultValue = "0") int pagina,
        @RequestParam(defaultValue = "10") int size) {

    return empleadoService.buscar(departamentoId, pagina, size);
}
```

**URL de ejemplo:**

```
GET /api/empleados?departamentoId=2&pagina=0&size=10
```

**Buen patrón:**

* Usar `required=false` para parámetros opcionales.
* Usar `defaultValue` para paginación o valores por defecto.
* Evitar sobrecargar la URL con demasiados parámetros, si es compleja, usar POST con `@RequestBody`.

**Antipatrón:**

* Usar `@RequestParam` para **entidades completas** o **payloads grandes**.

```java
// ❌ No recomendable
@PostMapping("/empleados")
public Empleado crearEmpleado(
        @RequestParam String nombre,
        @RequestParam String email,
        @RequestParam Long departamentoId) { ... }
```

* Esto hace la API poco mantenible y no soporta estructuras complejas (como listas de proyectos).

---

## 3️⃣ `@RequestBody`

**Qué hace:**
Se usa para **mapear automáticamente el body de la solicitud HTTP** a un objeto Java.

**Cuándo usarlo:**

* Para **crear o actualizar recursos completos**.
* Cuando el payload es un **JSON complejo**, como un `Empleado` con relaciones a `Departamento` y `Proyectos`.

**Ejemplo: Crear un empleado**

```java
@PostMapping
public ResponseEntity<Empleado> crearEmpleado(@RequestBody Empleado empleado) {
    Empleado guardado = empleadoService.save(empleado);
    return ResponseEntity.status(HttpStatus.CREATED).body(guardado);
}
```

**JSON de ejemplo para el body:**

```json
{
  "nombre": "Juan Pérez",
  "email": "juan.perez@empresa.com",
  "departamento": { "id": 1 },
  "proyectos": [{ "id": 3 }, { "id": 5 }]
}
```

**Buen patrón:**

* Usar `@RequestBody` para **objetos completos**, nunca para datos triviales.
* Combinar validación con `@Valid`.

**Antipatrón:**

```java
// ❌ Evitar recibir solo IDs en @RequestBody si es trivial
@PostMapping("/empleados")
public Empleado crearEmpleado(@RequestBody Long departamentoId) { ... }
```

* Mejor usar `@RequestParam` o un DTO con estructura clara.

---

## 4️⃣ Cómo combinarlos

Es común combinar los tres para casos específicos:

**Ejemplo: Actualizar proyecto de un empleado**

```java
@PutMapping("/{id}/proyectos")
public ResponseEntity<Empleado> agregarProyectos(
        @PathVariable Long id,
        @RequestBody Set<Proyecto> proyectos) {

    Empleado e = empleadoService.findById(id);
    e.getProyectos().addAll(proyectos);
    return ResponseEntity.ok(empleadoService.save(e));
}
```

* `@PathVariable` → identifica el empleado a modificar
* `@RequestBody` → recibe los proyectos a agregar
* `@RequestParam` → podrías usarlo para opciones extra (ej. `notify=true`)

---

## 5️⃣ Resumen rápido de uso

| Anotación       | Uso principal                         | Buen patrón                   | Antipatrón                      |
| --------------- | ------------------------------------- | ----------------------------- | ------------------------------- |
| `@PathVariable` | Identificadores en la URL             | `/empleados/{id}`             | Filtrar por nombre u opcionales |
| `@RequestParam` | Filtros, paginación, flags            | `/empleados?departamentoId=2` | Recibir objetos complejos       |
| `@RequestBody`  | Crear o actualizar recursos completos | JSON con entidad              | Recibir un solo valor trivial   |

---
