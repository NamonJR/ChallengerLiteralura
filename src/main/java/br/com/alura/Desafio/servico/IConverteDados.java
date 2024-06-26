package br.com.alura.Desafio.servico;

public interface IConverteDados {
    <T> T obterDados(String json, Class<T> classe);
}
