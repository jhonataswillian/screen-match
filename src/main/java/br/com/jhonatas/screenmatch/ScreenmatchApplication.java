package br.com.jhonatas.screenmatch;

import br.com.jhonatas.screenmatch.model.DadoEpisodio;
import br.com.jhonatas.screenmatch.model.DadoSerie;
import br.com.jhonatas.screenmatch.model.DadoTemporada;
import br.com.jhonatas.screenmatch.service.ConsumoApi;
import br.com.jhonatas.screenmatch.service.ConverteDado;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import java.util.ArrayList;
import java.util.List;

@SpringBootApplication
public class ScreenmatchApplication implements CommandLineRunner {

	public static void main(String[] args) {
		SpringApplication.run(ScreenmatchApplication.class, args);
	}

	@Override
	public void run(String... args) throws Exception {
		var consumoApi = new ConsumoApi();
		var json = consumoApi.obterDados(
				"https://www.omdbapi.com/?t=gilmore+girls&apikey=4f12b0bc"
		);
		System.out.println(json);

		ConverteDado conversor = new ConverteDado();

		DadoSerie dadosSerie = conversor.obterDado(json, DadoSerie.class);
		System.out.println(dadosSerie);

		json = consumoApi.obterDados(
				"https://www.omdbapi.com/?t=gilmore+girls&season=1&episode=2&apikey=4f12b0bc"
		);
		DadoEpisodio dadosEpisodio = conversor.obterDado(json, DadoEpisodio.class);
		System.out.println(dadosEpisodio);

		List<DadoTemporada> temporadas = new ArrayList<>();
		for (int i = 1; i <= dadosSerie.totalTemporadas(); i++) {
			json = consumoApi.obterDados(
					"https://www.omdbapi.com/?t=gilmore+girls&season=" + i + "&apikey=4f12b0bc"
			);
			DadoTemporada dadoTemporada = conversor.obterDado(json, DadoTemporada.class);
			temporadas.add(dadoTemporada);
		}
		temporadas.forEach(System.out::println);
	}
}
