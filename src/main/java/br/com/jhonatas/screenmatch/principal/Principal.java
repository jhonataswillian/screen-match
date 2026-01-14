package br.com.jhonatas.screenmatch.principal;

import br.com.jhonatas.screenmatch.model.DadoEpisodio;
import br.com.jhonatas.screenmatch.model.DadoSerie;
import br.com.jhonatas.screenmatch.model.DadoTemporada;
import br.com.jhonatas.screenmatch.model.Episodio;
import br.com.jhonatas.screenmatch.service.ConsumoApi;
import br.com.jhonatas.screenmatch.service.ConverteDado;

import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.stream.Collectors;

public class Principal {

    private Scanner leitura = new Scanner(System.in);
    private ConsumoApi consumo = new ConsumoApi();
    private ConverteDado conversor = new ConverteDado();

    private final String ENDERECO = "https://www.omdbapi.com/?t=";
    private final String API_KEY = "&apikey=4f12b0bc";

    public void exibeMenu() {
        System.out.println("Digite o nome da série para busca");
        var nomeSerie = leitura.nextLine();
        var nomeSerieEncoder = URLEncoder.encode(nomeSerie, StandardCharsets.UTF_8);

        var json = consumo.obterDados(ENDERECO + nomeSerieEncoder + API_KEY);

        DadoSerie dadoSerie = conversor.obterDado(json, DadoSerie.class);
//        System.out.println(dadoSerie);

        List<DadoTemporada> temporadas = new ArrayList<>();
        for (int i = 1; i <= dadoSerie.totalTemporadas(); i++) {
            json = consumo.obterDados(
                    ENDERECO + nomeSerieEncoder + "&Season=" + i + API_KEY
            );
            DadoTemporada dadoTemporada = conversor.obterDado(json, DadoTemporada.class);
            temporadas.add(dadoTemporada);
        }

//        for (int i = 0; i < dadoSerie.totalTemporadas(); i++) {
//            List<DadoEpisodio> episodioTemporada = temporadas.get(i).episodios();
//            for (DadoEpisodio dadoEpisodio : episodioTemporada) {
//                System.out.println(dadoEpisodio.titulo());
//            }
//        }

//        temporadas.forEach(
//                t -> t.episodios().forEach(
//                        e -> System.out.println(e.titulo())));

        List<DadoEpisodio> dadosEpisodios = temporadas.stream()
                .flatMap(t -> t.episodios().stream())
                .collect(Collectors.toList());

        dadosEpisodios.add(new DadoEpisodio("teste", 3, "8", "2020-01-01"));

//        System.out.println("\n===== Top 10 episódios =====\n");
//        dadosEpisodios.stream()
//                .filter(e -> !e.avaliacao().equalsIgnoreCase("N/A"))
//                .peek(e -> System.out.println("Primeiro filtro(N/A) " + e))
//                .sorted(Comparator.comparing(DadoEpisodio::avaliacao).reversed())
//                .peek(e -> System.out.println("Ordenação " + e))
//                .limit(10)
//                .peek(e -> System.out.println("Limit " + e))
//                .map(e -> e.titulo().toUpperCase())
//                .peek(e -> System.out.println("Mapeamento " + e))
//                .forEach(System.out::println);

        List<Episodio> episodios =  temporadas.stream()
                .flatMap(t -> t.episodios().stream()
                        .map(d -> new Episodio(t.numeroTemporada(), d))
                ).collect(Collectors.toList());

        episodios.forEach(System.out::println);

//        System.out.println("Digite um trechodo titulo do episódio");
//        var trechoTitulo = leitura.nextLine();
//        Optional< Episodio> episodioBuscado = episodios.stream()
//                .filter(e -> e.getTitulo().toLowerCase().contains(trechoTitulo.toLowerCase()))
//                .findFirst();
//
//        if (episodioBuscado.isPresent()) {
//            System.out.println("Episódio encontrado!");
//            System.out.println("Temporada: " + episodioBuscado.get().getTemporada());
//            System.out.println("Episódio: " + episodioBuscado.get().getTitulo());
//        } else {
//            System.out.println("Episódio não encontrado!");
//        }
//
//
//        System.out.println("A partir de que ano você deseja ver os episódios?");
//        var ano = leitura.nextInt();
//        leitura.nextLine();
//
//        LocalDate dataBusca = LocalDate.of(ano, 1, 1);
//
//        DateTimeFormatter formatador = DateTimeFormatter.ofPattern("dd/MM/yyyy");
//        episodios.stream()
//                .filter(e -> e.getDataLancamento() != null &&  e.getDataLancamento().isAfter(dataBusca))
//                .forEach(e -> System.out.printf("""
//                        Temporada: %d
//                        Episódio: %s
//                        Data de Lançamento: %s
//                        """,
//                        e.getTemporada(),
//                        e.getTitulo(),
//                        e.getDataLancamento().format(formatador))
//                );

        Map<Integer, Double> avaliacoesPorTemporada = episodios.stream()
                .filter(e -> e.getAvaliacao() != null && e.getAvaliacao() > 0.0)
                .collect(Collectors.groupingBy(Episodio::getTemporada,
                        Collectors.averagingDouble(Episodio::getAvaliacao)));

        System.out.println(avaliacoesPorTemporada);

        DoubleSummaryStatistics est = episodios.stream()
                .filter(e -> e.getAvaliacao() != null && e.getAvaliacao() > 0.0)
                .collect(Collectors.summarizingDouble(Episodio::getAvaliacao));

        System.out.println("Média: " + est.getAverage() +
                "\nMelhor episódio: " + est.getMax() +
                "\nPior episódio: " + est.getMin() +
                "\nQuantidade: " + est.getCount());
    }
}
