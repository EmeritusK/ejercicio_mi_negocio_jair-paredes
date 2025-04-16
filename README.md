# 🧾 Proyecto de Gestión de Clientes - API REST

Este proyecto es una API REST construida con **Java + Spring Boot** y una base de datos **PostgreSQL**, diseñada para gestionar clientes y sus direcciones.

## 📌 Características principales

- Crear, editar y eliminar clientes.
- Asignar direcciones adicionales a clientes.
- Listar todas las direcciones de un cliente.
- Búsqueda por nombre o número de identificación con soporte a múltiples palabras (orden libre).
- Paginación en los resultados de búsqueda.
- Manejo detallado de errores con respuestas específicas según el tipo de fallo.
- Pruebas unitarias para los servicios y controladores.

---

## 🧩 Requisitos de base de datos

Para ejecutar este proyecto necesitas tener una instancia de PostgreSQL local corriendo en el puerto `5432`.

### 📘 Configuración por defecto:

- Base de datos: `mi_negocio`
- Usuario: `postgres`
- Contraseña: `tu_contraseña` _(puedes cambiarla en `application.properties`)_

> ⚠️ Si la base de datos `mi_negocio` no existe, créala manualmente con el siguiente comando:

```sql
CREATE DATABASE mi_negocio;
```

Spring Boot se encargará automáticamente de crear las tablas necesarias al ejecutar la aplicación.

---

## ✅ Nuevas funcionalidades implementadas

- 📄 **Paginación** al obtener clientes, ideal para integración con frontend de tipo lazy-loading.
- 🚫 **Manejo de errores detallado**:
  - `409 Conflict`: cliente ya existe o dirección matriz duplicada.
  - `404 Not Found`: cliente no encontrado.
  - `400 Bad Request`: errores de validación o campos faltantes.
  - `500 Internal Server Error`: error inesperado.
- 🧪 **Pruebas unitarias** con Mockito y Spring MVC Test para cubrir los principales endpoints.
- 📁 **Arquitectura limpia** basada en principios de Clean Code y SOLID: separación clara entre controladores, servicios, DTOs, entidades, excepciones y mapeadores.

---

## 🧪 Enfoque de pruebas (TDD)

El desarrollo inicial de los casos principales (`crearCliente`, `editarCliente`, validaciones de negocio) se realizó aplicando **Test Driven Development (TDD)**: primero se escribieron los tests, luego se implementó la lógica mínima para hacerlos pasar.

Por cuestiones de tiempo, el resto de funcionalidades (búsquedas, pruebas del controlador y mejoras de errores) se completaron **por programación directa**, manteniendo pruebas asociadas cuando fue posible.

---

## 🚀 Endpoints principales

| Método | Ruta                                    | Descripción                        |
| ------ | --------------------------------------- | ---------------------------------- |
| POST   | `/api/clientes`                         | Crear cliente con dirección matriz |
| PUT    | `/api/clientes/{id}`                    | Editar cliente                     |
| DELETE | `/api/clientes/{id}`                    | Eliminar cliente                   |
| POST   | `/api/clientes/direcciones/{clienteId}` | Registrar dirección adicional      |
| GET    | `/api/clientes/direcciones/{clienteId}` | Listar direcciones del cliente     |
| GET    | `/api/clientes?filtro=&page=0&size=10`  | Buscar clientes paginados          |

---

## 📬 Colecciones Postman

Este proyecto incluye dos colecciones Postman para facilitar el testeo manual:

### 🧑‍💼 Clientes

Ruta: `/api/clientes`

- Crear cliente
- Editar cliente
- Eliminar cliente
- Buscar clientes con paginación

### 🏠 Direcciones

Ruta: `/api/clientes/direcciones`

- Registrar nueva dirección
- Listar direcciones de un cliente

📥 **Cómo importarlas:**

1. Abre Postman.
2. Ve a **File > Import** o haz clic en el botón `Import`.
3. Selecciona los archivos `.json` ubicados en la carpeta `/postman`.

> Asegúrate de que la API esté corriendo en `http://localhost:8081`

## 📬 Colecciones Postman (en línea)

Puedes probar los endpoints directamente desde estas colecciones públicas:

- [Colección Clientes](https://www.postman.com/python-6832/workspace/minegocioejercicio/collection/30541379-172d1277-239a-47cd-901e-1082855b691f?action=share&creator=30541379)
- [Colección Direcciones](https://www.postman.com/python-6832/workspace/minegocioejercicio/collection/30541379-37642e1c-a727-4b39-8fd1-a70ac194e315?action=share&creator=30541379)

> Requiere tener Postman instalado o usar la app web en [https://www.postman.com](https://www.postman.com).

## 📦 Tecnologías usadas

- Java 17
- Spring Boot 3.x
- PostgreSQL
- Spring Data JPA
- JUnit 5 + Mockito
- Maven
- ObjectMapper / JSON
