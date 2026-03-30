package sptech.school.biblioteca.controller;

import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import sptech.school.biblioteca.model.Livro;
import sptech.school.biblioteca.service.LivroService;

import java.util.List;

@RestController
@RequestMapping("/livros")
public class LivroController {

    private final LivroService livroService;

    public LivroController(LivroService livroService) {
        this.livroService = livroService;
    }

    @PostMapping
    public ResponseEntity<Livro> cadastrarLivro(@Valid @RequestBody Livro livro) {
        Livro cadastrado = livroService.cadastrarLivro(livro);
        return ResponseEntity.status(201).body(cadastrado);
    }

    @GetMapping("/{isbn}")
    public ResponseEntity<Livro> buscarPorIsbn(@PathVariable String isbn) {
        Livro buscado = livroService.buscarPorIsbn(isbn);
        return ResponseEntity.status(200).body(buscado);
    }

    @GetMapping
    public ResponseEntity<List<Livro>> buscaTodos() {
        List<Livro> livros = livroService.buscarTodos();
        return ResponseEntity.status(200).body(livros);
    }

    @PatchMapping("/{isbn}/emprestar")
    public ResponseEntity<Livro> emprestar(@PathVariable String isbn) {
        Livro emprestado = livroService.emprestar(isbn);
        return ResponseEntity.status(200).body(emprestado);
    }

    @PatchMapping("/{isbn}/devolver")
    public ResponseEntity<Livro> devolver(@PathVariable String isbn) {
        Livro devolvido = livroService.devolver(isbn);
        return ResponseEntity.status(200).body(devolvido);
    }
}
