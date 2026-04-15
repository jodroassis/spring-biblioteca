package sptech.school.biblioteca.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import sptech.school.biblioteca.model.Livro;

import java.util.List;

public interface LivroRepository extends JpaRepository<Livro, String> {
    List<Livro> findByTituloContainingIgnoreCase(String titulo);
    List<Livro> findByAutorContainingIgnoreCase(String autor);
}
