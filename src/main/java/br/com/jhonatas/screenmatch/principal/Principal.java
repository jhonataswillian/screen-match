package br.com.jhonatas.screenmatch.principal;

import br.com.jhonatas.screenmatch.service.ConsumoApi;

import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.Scanner;

public class Principal {

    private Scanner leitura = new Scanner(System.in);
    private ConsumoApi consumo = new ConsumoApi();

    private final String ENDERECO = "https://www.omdbapi.com/?t=";
    private final String API_KEY = "&apikey=4f12b0bc";

    public void exibeMenu() {
        System.out.println("Digite o nome da série para busca");
        var nomeSerie = leitura.nextLine();
        var nomeSerieEncoder = URLEncoder.encode(nomeSerie, StandardCharsets.UTF_8);

        var json = consumo.obterDados(ENDERECO + nomeSerieEncoder + API_KEY);
    }
}
