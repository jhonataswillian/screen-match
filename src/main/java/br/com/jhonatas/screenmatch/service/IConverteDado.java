package br.com.jhonatas.screenmatch.service;

public interface IConverteDado {
    <T> T obterDado(String json, Class<T> classe);
}
