package sptech.school.biblioteca.service;

import org.springframework.stereotype.Service;
import sptech.school.biblioteca.enums.StatusLivro;
import sptech.school.biblioteca.exception.LivroException;
import sptech.school.biblioteca.model.Livro;
import sptech.school.biblioteca.repository.LivroRepository;

import java.util.List;
import java.util.Optional;

@Service
public class LivroService {
    private final LivroRepository livroRepository;

    public LivroService(LivroRepository livroRepository) {
        this.livroRepository = livroRepository;
    }

    public Livro cadastrarLivro(Livro livro){
        if (livroRepository.existsById(livro.getIsbn())){
            throw new LivroException("ISBN" + livro.getIsbn() + " já cadastrado");
        }
        return livroRepository.save(livro);
    }

    public Livro buscarPorIsbn(String isbn) {
        return livroRepository.findById(isbn).orElseThrow(
                () -> new LivroException("Livro não encontrado")
        );
    }

    public List<Livro> buscarTodos() {
        return livroRepository.findAll();
    }

    public Livro emprestar(String isbn) {
        Livro livro = buscarPorIsbn(isbn);
        if (livro.getStatus().equals(StatusLivro.EMPRESTADO)) {
            throw new LivroException("Livro já está emprestado");
        }
        livro.setStatus(StatusLivro.EMPRESTADO);
        return livroRepository.save(livro);
    }

    public Livro devolver(String isbn) {
        Livro livro = buscarPorIsbn(isbn);
        if (livro.getStatus().equals(StatusLivro.DISPONIVEL)) {
            throw new LivroException("Livro já estava disponível");
        }
        livro.setStatus(StatusLivro.DISPONIVEL);
        return livroRepository.save(livro);
    }
}
