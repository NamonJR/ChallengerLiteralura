package br.com.alura.Desafio.servico;

import br.com.alura.Desafio.modelo.Author;
import br.com.alura.Desafio.modelo.Book;
import br.com.alura.Desafio.repository.AuthorRepository;
import br.com.alura.Desafio.repository.BookRepository;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
public class BookService {
    @Autowired
    private BookRepository bookRepository;

    @Autowired
    private AuthorRepository authorRepository;

    @Transactional
    public void salvarLivro(Book book) {
        Book livroExistente = bookRepository.getByTitulo(book.getTitulo());

        if(livroExistente == null) {
            Author autor = book.getAuthor();
            Author autorExistente = authorRepository.findByNomeAndAnoDeNascimentoAndAnoDeFalecimento(
                    autor.getNome(), autor.getAnoDeNascimento(), autor.getAnoDeFalecimento());

            if (autorExistente != null) {
                book.setAuthor(autorExistente);
                autorExistente.addBooks(book);
            } else {
                Author autorSalvo = authorRepository.save(autor);
                book.setAuthor(autorSalvo);
                autorSalvo.addBooks(book);
            }

            bookRepository.save(book);
            System.out.println("\n******* Livro cadastrado com sucesso *******");
        } else {
            System.out.println("\n********* ATENÇÃO - LIVRO JÁ CADASTRADO *********");
        }
    }

    public List<Book> findAll() {
        return bookRepository.findAll();
    }

    public List<Author> findAllAuthor() {
        return authorRepository.findAll();
    }

    public List<Author> autorVivoNoAno(Integer anoInformado) {
        return authorRepository.autorVivoNoAno(anoInformado);
    }

    public Integer contaLivrosEmIdioma(String idiomaBuscado) {
        return (int) bookRepository.contaLivrosEmIdioma(idiomaBuscado);
    }

    public List<Book> listaDeLivrosPorIdioma(String idioma) {
        return bookRepository.findBookByIdioma(idioma);
    }
}