package br.com.alura.Desafio.principal;

import br.com.alura.Desafio.modelo.Author;
import br.com.alura.Desafio.modelo.Book;
import br.com.alura.Desafio.modelo.DescricaoLivro;
import br.com.alura.Desafio.modelo.Descricao;
import br.com.alura.Desafio.servico.BookService;
import br.com.alura.Desafio.servico.SolicitacaoAPI;
import br.com.alura.Desafio.servico.ConverteDados;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Scanner;
import java.util.stream.Collectors;

public class Principal {
    private final String ENDERECO = "https://gutendex.com/books/?search=";
    private SolicitacaoAPI consumoApi = new SolicitacaoAPI();
    private ConverteDados conversor = new ConverteDados();
    private Scanner leitura = new Scanner(System.in);
    private BookService bookService = new BookService();
    private Book book = new Book();
    private List<Book> livros = new ArrayList<>();
    private List<Author> autores = new ArrayList<>();

    public Principal(BookService bookService) {
        this.bookService = bookService;
    }

    public void exibeMenu() {
        var opcao = -1;
        while(opcao != 6) {
            var menu = """                    
                    \n********** Escolha uma das opções **********
                    1 - Buscar livro pelo título
                    2 - Livros registrados
                    3 - Autores registrados
                    4 - Autores vivos em determinado ano
                    5 - Livros em um determinado idioma
                    6 - Sair do aplicativo
                    """;

            System.out.println(menu);
            opcao = leitura.nextInt();
            leitura.nextLine();

            switch (opcao) {
                case 1:
                    buscarLivroPeloTitulo();
                    break;
                case 2:
                    listarLivrosRegistrados();
                    break;
                case 3:
                    listarAutoresRegistrados();
                    break;
                case 4:
                    listarAutoresVivosData();
                    break;
                case 5:
                    contarLivrosDoIdioma();
                    break;
                case 6:
                    System.out.println("Saindo...");
                    break;
                default:
                    System.out.println("* * * * * OPÇÃO INVÁLIDA * * * * *");
            }
        }
    }

    private void buscarLivroPeloTitulo() {
        System.out.println("***** Digite o nome do livro *****");
        var tituloLivro = leitura.nextLine();
        var json = consumoApi.solicitacao(ENDERECO + tituloLivro.replace(" ", "+"));
        Descricao dados = conversor.obterDados(json, Descricao.class);
        List<DescricaoLivro> bookData = new ArrayList<>();
        bookData = dados.livros();
        try {
            book = new Book(bookData.get(0));
            System.out.println(book);
            List<Author> autores = bookData.stream()
                    .limit(1)
                    .flatMap(b -> b.autor().stream()
                            .map(d -> new Author(b.titulo(), d))
                    ).collect(Collectors.toList());
            autores.forEach(System.out::println);
            Author author = autores.get(0);
            book.setAuthor(author);
            bookService.salvarLivro(book);
        } catch (Exception e) {
            System.out.println("\n* * * * * LIVRO NÃO ENCONTRADO * * * * *");
        }
    }

    private void listarLivrosRegistrados() {
        livros = bookService.findAll();
        livros.stream()
                .sorted(Comparator.comparing(Book::getNomeAutor))
                .forEach(System.out::println);
    }

    private void listarAutoresRegistrados() {
        autores = bookService.findAllAuthor();
        autores.stream()
                .sorted(Comparator.comparing(Author::getNome))
                .forEach(System.out::println);
    }

    private void listarAutoresVivosData() {
        System.out.println("****** Informe o ano *****");
        var anoInformado = leitura.nextInt();
        autores = bookService.autorVivoNoAno(anoInformado);
        autores.stream()
                .sorted(Comparator.comparing(Author::getNome))
                .forEach(System.out::println);
    }

    private void contarLivrosDoIdioma() {
        System.out.println("*********** Escolha o idioma ***********" +
                "\nPortuguês: pt" +
                "\nInglês: en" +
                "\nEspanhol: es" +
                "\nItaliano: it");
        var idiomaBuscado = leitura.nextLine();
        Integer ocorrencias = bookService.contaLivrosEmIdioma(idiomaBuscado);
        var traduzido = traduzIdioma(idiomaBuscado);
        if(ocorrencias == 0) {
            System.out.println("* * * Nenhum livro encontrado no idioma " + traduzido + " * * *");
        } if(ocorrencias == 1) {
            System.out.println("\n* * * Encontrado 1 livro em " + traduzido + " * * *\n");
        } else {
            System.out.println("\n* * * Encontrado " + ocorrencias + " livros em " + traduzido + " * * *\n");
        }
        listarLivrosEmIdioma(idiomaBuscado);
    }

    private void listarLivrosEmIdioma(String idioma) {
        livros  = bookService.listaDeLivrosPorIdioma(idioma);
        livros.forEach(System.out::println);
    }

    private String traduzIdioma(String idioma) {
        var traduz = "";
        switch (idioma) {
            case "pt": traduz = "Português";
                break;
            case "en": traduz = "Inglês";
                break;
            case "es": traduz = "Espanhol";
                break;
            case "it": traduz = "Italiano";
                break;
        }
        return traduz;
    }

}