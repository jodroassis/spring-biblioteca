package sptech.school.biblioteca.dto;

import sptech.school.biblioteca.model.Livro;

public record LivroRequest(String isbn, String titulo, String autor, int ano) {

    public Livro toEntity() {
        return new Livro(isbn, titulo, autor, ano);
    }
}
