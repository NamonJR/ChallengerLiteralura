package br.com.alura.Desafio.repository;

import br.com.alura.Desafio.modelo.Book;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import java.util.List;

public interface BookRepository extends JpaRepository<Book, Long> {
    Book getByTitulo(String titulo);

    List<Book> findAll();

    @Query("SELECT COUNT(b) FROM Book b WHERE b.idioma LIKE %:idioma%")
    long contaLivrosEmIdioma(String idioma);

    List<Book> findBookByIdioma(String idioma);
}