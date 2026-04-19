package sptech.school.biblioteca.dto;

import sptech.school.biblioteca.enums.Role;

public record CadastroRequest(String email, String senha, Role role) {
}
