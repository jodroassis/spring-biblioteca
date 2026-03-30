package sptech.school.biblioteca.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import sptech.school.biblioteca.model.Livro;

public interface LivroRepository extends JpaRepository<Livro, String> {
}
