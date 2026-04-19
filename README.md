# Biblioteca API

REST API para gerenciamento de acervo de uma biblioteca, desenvolvida com Spring Boot.

## Tecnologias

- Java 17+
- Spring Boot
- Spring Data JPA
- MySQL
- Bean Validation (Jakarta)
- JUnit 5 + Mockito (testes unitários)

## Configuração

### Banco de dados

Antes de executar, garanta que há uma instância MySQL local rodando com:

- Host: `localhost:3306`
- Database: `biblioteca`
- User: `root`
- Password: `sptech`

O Hibernate está configurado com `ddl-auto: update`, então o schema é gerenciado automaticamente.

## Comandos

```bash
./mvnw clean install       # Build e instalação
./mvnw spring-boot:run     # Executar a aplicação
./mvnw test                # Rodar todos os testes
./mvnw clean package       # Empacotar sem executar
```

> No Windows, use `mvnw.cmd` no lugar de `./mvnw`.

## Arquitetura

```
Controller → Service → Repository → DB
```

| Camada | Responsabilidade |
|--------|-----------------|
| `LivroController` | Recebe requisições HTTP, converte DTOs |
| `LivroService` | Regras de negócio e validações de estado |
| `LivroRepository` | Acesso ao banco via Spring Data JPA |

### DTOs

- **`LivroRequest`** — entrada para cadastro (`isbn`, `titulo`, `autor`, `ano`)
- **`LivroResponse`** — saída com todos os campos incluindo `status`

### Tratamento de erros

- **`LivroException`** — exceção de negócio lançada pelo service
- **`GlobalExceptionHandler`** — captura exceções e retorna respostas estruturadas:
  - `LivroException` → HTTP 400 com `ErroResponse` (`status`, `mensagem`, `timestamp`)
  - `MethodArgumentNotValidException` → HTTP 400 com mapa de campos e mensagens de validação

## API

Base path: `/livros`

### Endpoints

| Método | Caminho | Descrição | Status |
|--------|---------|-----------|--------|
| `POST` | `/livros` | Cadastrar livro | 201 |
| `GET` | `/livros` | Listar todos os livros | 200 |
| `GET` | `/livros?titulo={titulo}` | Buscar livros por título (parcial, case-insensitive) | 200 |
| `GET` | `/livros?autor={autor}` | Buscar livros por autor (parcial, case-insensitive) | 200 |
| `GET` | `/livros/{isbn}` | Buscar livro por ISBN | 200 |
| `PATCH` | `/livros/{isbn}/emprestar` | Emprestar livro | 200 |
| `PATCH` | `/livros/{isbn}/devolver` | Devolver livro | 200 |
| `DELETE` | `/livros/{isbn}` | Remover livro | 204 |

### Exemplo de cadastro

**Request**
```json
POST /livros
{
  "isbn": "978-3-16-148410-0",
  "titulo": "Clean Code",
  "autor": "Robert C. Martin",
  "ano": 2008
}
```

**Response** `201 Created`
```json
{
  "isbn": "978-3-16-148410-0",
  "titulo": "Clean Code",
  "autor": "Robert C. Martin",
  "ano": 2008,
  "status": "DISPONIVEL"
}
```

### Exemplo de erro

```json
{
  "status": 400,
  "mensagem": "Livro já está emprestado",
  "timestamp": "2026-03-31T10:00:00"
}
```

## Regras de negócio

- ISBN é a chave primária — duplicatas retornam erro 400
- Livro só pode ser emprestado se estiver `DISPONIVEL`
- Livro só pode ser devolvido se estiver `EMPRESTADO`
- Livro com status `EMPRESTADO` não pode ser removido
