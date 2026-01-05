package br.com.jhonatas.screenmatch.model;

import com.fasterxml.jackson.annotation.JsonAlias;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import java.util.List;

@JsonIgnoreProperties(ignoreUnknown = true)
public record DadoTemporada(
        @JsonAlias("Season") Integer numeroTemporada,
        @JsonAlias("Episodes") List<DadoEpisodio> episodios
) {
}
