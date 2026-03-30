package sptech.school.biblioteca.exception;

import java.time.LocalDateTime;

public record ErroResponse(int status, String mensagem, LocalDateTime timestamp) {
}
