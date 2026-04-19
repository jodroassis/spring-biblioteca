package sptech.school.biblioteca.service;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import sptech.school.biblioteca.enums.StatusLivro;
import sptech.school.biblioteca.exception.LivroException;
import sptech.school.biblioteca.model.Livro;
import sptech.school.biblioteca.repository.LivroRepository;

import java.util.AbstractSet;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class LivroServiceTest {
    @Mock
    LivroRepository livroRepository;

    @InjectMocks
    LivroService livroService;

    @Test
    void deveLancarExcecaoQuandoIsbnJaCadastrado() {
        // prepara o cenário
        Livro livro = new Livro("1", "Clean Code", "Martin", 2008);
        when(livroRepository.existsById("1")).thenReturn(true);

        // executa e verifica
        assertThrows(LivroException.class, () -> livroService.cadastrarLivro(livro));
    }

    @Test
    void deveCadastrarLivroComSucesso() {
        // arrange
        Livro livro = new Livro("1", "Clean Code", "Martin", 2008);
        when(livroRepository.existsById("1")).thenReturn(false);
        when(livroRepository.save(livro)).thenReturn(livro);

        // act
        Livro resultado = livroService.cadastrarLivro(livro);

        // assert
        assertNotNull(resultado);
        assertEquals("1", resultado.getIsbn());
        verify(livroRepository, times(1)).save(livro);
    }

    @Test
    void deveLancarExcecaoAoEmprestarLivroJaEmprestado() {
        // arrange
        Livro livro = new Livro("1", "Clean Code", "Martin", 2008);
        livro.setStatus(StatusLivro.EMPRESTADO);
        when(livroRepository.findById("1")).thenReturn(Optional.of(livro));

        // act + assert
        assertThrows(LivroException.class, () -> livroService.emprestar("1"));
    }

    @Test
    void deveEmprestarLivroComSucesso() {
        // arrange
        Livro livro = new Livro("1", "Clean Code", "Martin", 2008);
        when(livroRepository.findById("1")).thenReturn(Optional.of(livro));
        when(livroRepository.save(livro)).thenReturn(livro);

        // act
        Livro resultado = livroService.emprestar("1");

        // assert
        assertEquals(StatusLivro.EMPRESTADO, resultado.getStatus());
        verify(livroRepository, times(1)).save(livro);
    }

    @Test
    void deveLancarExcecaoAoBuscarIsbnInexistente() {
        Livro livro = new Livro("1", "Clean Code", "Martin", 2008);
        when(livroRepository.findById("1")).thenReturn(Optional.empty());

        assertThrows(LivroException.class, () -> livroService.buscarPorIsbn("1"));
    }

    @Test
    void deveDevolverLivroComSucesso() {
        Livro livro = new Livro("1", "Clean Code", "Martin", 2008);
        livro.setStatus(StatusLivro.EMPRESTADO);
        when(livroRepository.save(livro)).thenReturn(livro);
        when(livroRepository.findById("1")).thenReturn(Optional.of(livro));

        Livro resultado = livroService.devolver("1");

        assertEquals(StatusLivro.DISPONIVEL, resultado.getStatus());
    }

    @Test
    void deveLancarExcecaoAoDevolverLivroDisponivel() {
        Livro livro = new Livro("1", "Clean Code", "Martin", 2008);
        when(livroRepository.findById("1")).thenReturn(Optional.of(livro));

        assertThrows(LivroException.class, () -> livroService.devolver("1"));
    }

    @Test
    void deveLancarExcecaoAoDeletarLivroEmprestado() {
        Livro livro = new Livro("1", "Clean Code", "Martin", 2008);
        livro.setStatus(StatusLivro.EMPRESTADO);
        when(livroRepository.findById("1")).thenReturn(Optional.of(livro));

        assertThrows(LivroException.class, () -> livroService.deletar("1"));
    }
}