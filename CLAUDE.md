# CLAUDE.md

This file provides guidance to Claude Code (claude.ai/code) when working with code in this repository.

## Build & Run Commands

```bash
./mvnw clean install       # Build and install
./mvnw spring-boot:run     # Run the application
./mvnw test                # Run all tests
./mvnw clean package       # Package without running
```

On Windows, use `mvnw.cmd` instead of `./mvnw`.

## Database Setup

The app connects to a local MySQL instance. Required before running:
- Host: `localhost:3306`
- Database: `biblioteca`
- User: `root` / Password: `sptech`

Hibernate DDL is set to `update`, so schema is auto-managed.

## Architecture

Standard Spring Boot layered architecture for a library management REST API:

```
Controller → Service → Repository → DB
```

- **Model**: `Livro.java` — JPA entity with ISBN (String PK), titulo, autor, ano, status
- **Enum**: `StatusLivro` — `DISPONIVEL` | `EMPRESTADO`
- **Exception flow**: `LivroException` is caught by `GlobalExceptionHandler`, which returns HTTP 400

## API

Base path: `/livros`

| Method | Path | Description |
|--------|------|-------------|
| POST | `/livros` | Register a book (201) |
| GET | `/livros` | List all books |
| GET | `/livros/{isbn}` | Get book by ISBN |
| PATCH | `/livros/{isbn}/emprestar` | Borrow book |
| PATCH | `/livros/{isbn}/devolver` | Return book |

Business rules (borrow/return state transitions) live in `LivroService`.
