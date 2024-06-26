package br.com.alura.Desafio.repository;

import br.com.alura.Desafio.modelo.Author;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import java.util.List;

public interface AuthorRepository extends JpaRepository<Author, Long> {
    Author findByNomeAndAnoDeNascimentoAndAnoDeFalecimento(String nome, Integer anoDeNascimento, Integer anoDeFalecimento);

    @Query("SELECT a FROM Author a WHERE a.anoDeFalecimento >= :anoInformado")
    List<Author> autorVivoNoAno(Integer anoInformado);
}
