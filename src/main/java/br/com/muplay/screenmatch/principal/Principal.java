package br.com.muplay.screenmatch.principal;

import br.com.muplay.screenmatch.model.DadosEpisodio;
import br.com.muplay.screenmatch.model.DadosSerie;
import br.com.muplay.screenmatch.model.DadosTemporada;
import br.com.muplay.screenmatch.model.Episodio;
import br.com.muplay.screenmatch.services.ConsumoAPI;
import br.com.muplay.screenmatch.services.ConverteDados;
import org.springframework.cglib.core.Local;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Scanner;
import java.util.stream.Collectors;

public class Principal {
    private Scanner scanner = new Scanner(System.in);
    private ConsumoAPI consumoAPI = new ConsumoAPI();
    private ConverteDados conversor = new ConverteDados();
    private final String ENDERECO = "https://www.omdbapi.com/?t=";
    private final String API_KEY = "c89273bd";

    public void exibeMenu(){
        System.out.println("Digite o nome da série para busca: ");
        String nomeSerie = scanner.nextLine();
        String busca = ENDERECO + nomeSerie.replace(" ", "+")+"&apikey="+API_KEY;


        var json = consumoAPI.obterDados(busca);


        DadosSerie dadosSerie = conversor.obterDados(json, DadosSerie.class);
        System.out.println(dadosSerie);

        DadosEpisodio dadosEpisodio = conversor.obterDados(json, DadosEpisodio.class);
        System.out.println(dadosEpisodio);

		List<DadosTemporada> temporadas = new ArrayList<>();

		for(int i = 1; i<=dadosSerie.totalTemporadas(); i++) {
			json = consumoAPI.obterDados(ENDERECO + nomeSerie.replace(" ", "+")+"&season="+i+"&apikey="+API_KEY);
			DadosTemporada dadosTemporada = conversor.obterDados(json, DadosTemporada.class);
			temporadas.add(dadosTemporada);
		}
		temporadas.forEach(System.out::println);

//        for(int i = 0; i < dadosSerie.totalTemporadas(); i++){
//            List<DadosEpisodio> episodiosTemporada = temporadas.get(i).episodios();
//            for(int j = 0; j < episodiosTemporada.size(); i++){
//                System.out.println(episodiosTemporada.get(i).titulo());
//            }
//        }

        temporadas.forEach(t -> t.episodios().forEach(e -> System.out.println(e.titulo())));

        List<DadosEpisodio> episodiosList = temporadas.stream()
                .flatMap(t -> t.episodios().stream())
                .collect(Collectors.toList());

        System.out.println("\nTop5 Episódios");

        episodiosList.stream()
                .sorted(Comparator.comparing(DadosEpisodio::avaliacao).reversed())
                .filter(e -> !e.avaliacao().equalsIgnoreCase("N/A"))
                .limit(5)
                .forEach(System.out::println);

        List<Episodio> episodios = temporadas.stream()
                .flatMap(t -> t.episodios().stream()
                        .map(d -> new Episodio(t.num(), d))
                )//.filter(e -> !e.getTitulo().equalsIgnoreCase("N/A"))
                .collect(Collectors.toList());

        episodios.forEach(System.out::println);

        System.out.println("A partir de que ano você deseja ver os episódios?");
        var ano = scanner.nextInt();
        scanner.nextLine();

        DateTimeFormatter formatador = DateTimeFormatter.ofPattern("dd/MM/yyyy");
        LocalDate dataBusca = LocalDate.of(ano, 1,1);

        episodios.stream()
                .filter(e -> e.getDataLancamento() != null && e.getDataLancamento().isAfter(dataBusca))
                .forEach(e-> System.out.println(
                        "Temporada: " + e.getTemporada()+
                                "Episódio: " + e.getTitulo()+
                                "Data Lançamento: " + e.getDataLancamento().format(formatador)
                ));

    }
}
