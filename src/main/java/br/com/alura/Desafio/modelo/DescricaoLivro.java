package br.com.alura.Desafio.modelo;

import com.fasterxml.jackson.annotation.JsonAlias;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import java.util.List;

@JsonIgnoreProperties(ignoreUnknown = true)
public record DescricaoLivro(@JsonAlias("title") String titulo,
                             @JsonAlias("authors") List<DescricaoAutor> autor,
                             @JsonAlias("download_count") Integer quantidadeDeDownloads,
                             @JsonAlias("languages") List<String> idiomas) {

}