package br.com.jhonatas.screenmatch;

import br.com.jhonatas.screenmatch.model.DadoSerie;
import br.com.jhonatas.screenmatch.service.ConsumoApi;
import br.com.jhonatas.screenmatch.service.ConverteDado;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class ScreenmatchApplication implements CommandLineRunner {

	public static void main(String[] args) {
		SpringApplication.run(ScreenmatchApplication.class, args);
	}

	@Override
	public void run(String... args) throws Exception {
		var consumoApi = new ConsumoApi();
		var json = consumoApi.obterDados("https://www.omdbapi.com/?t=gilmore+girls&apikey=4f12b0bc");
		System.out.println(json);

		ConverteDado conversor = new ConverteDado();
		DadoSerie dados = conversor.obterDado(json, DadoSerie.class);
		System.out.println(dados);

//		json = consumoApi.obterDados("https://coffee.alexflipnote.dev/random.json");
//		System.out.println(json);
	}
}
