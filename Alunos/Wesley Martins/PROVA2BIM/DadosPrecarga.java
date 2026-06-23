
import java.time.LocalDate;
import java.util.*;

/**
 * Classe que fornece dados pré-carregados para agilizar a utilização do sistema
 */
public class DadosPrecarga {

    /**
     * Retorna uma lista de séries pré-carregadas para demonstração
     */
    public static List<Serie> obterSeriesDemonstacao() {
        List<Serie> series = new ArrayList<>();

        // Breaking Bad
        series.add(new Serie(
                1396,
                "Breaking Bad",
                "English",
                new String[]{"Drama", "Crime", "Thriller"},
                9.5,
                "Ended",
                LocalDate.of(2008, 1, 20),
                LocalDate.of(2013, 9, 29),
                "AMC",
                "When an unassuming high school chemistry teacher discovers he is dying of lung cancer, he decides to turn to a life of crime to assure his family's financial security.",
                "https://static.tvmaze.com/uploads/images/original_untouched/0/1.jpg"
        ));

        // The Office
        series.add(new Serie(
                6091,
                "The Office",
                "English",
                new String[]{"Comedy"},
                9.0,
                "Ended",
                LocalDate.of(2005, 3, 24),
                LocalDate.of(2013, 5, 16),
                "NBC",
                "The everyday happenings of the workers at the Wernham Hogg paper company.",
                "https://static.tvmaze.com/uploads/images/original_untouched/81/202627.jpg"
        ));

        // Game of Thrones
        series.add(new Serie(
                1399,
                "Game of Thrones",
                "English",
                new String[]{"Drama", "Adventure", "Fantasy"},
                9.3,
                "Ended",
                LocalDate.of(2011, 4, 17),
                LocalDate.of(2019, 5, 19),
                "HBO",
                "Nine noble families fight for control over the lands of Westeros, while an ancient enemy emerges from the far North.",
                "https://static.tvmaze.com/uploads/images/original_untouched/3/8711.jpg"
        ));

        // Stranger Things
        series.add(new Serie(
                42009,
                "Stranger Things",
                "English",
                new String[]{"Drama", "Fantasy", "Horror"},
                8.7,
                "Running",
                LocalDate.of(2016, 7, 15),
                null,
                "Netflix",
                "When a young boy disappears, his friends, family and local police go on a search to find him, only to discover a secret government project and a strange new world.",
                "https://static.tvmaze.com/uploads/images/original_untouched/0/145.jpg"
        ));

        // The Crown
        series.add(new Serie(
                44180,
                "The Crown",
                "English",
                new String[]{"Drama", "History"},
                8.6,
                "Ended",
                LocalDate.of(2016, 11, 4),
                LocalDate.of(2023, 12, 14),
                "Netflix",
                "Follows the political rivalries and romance of Queen Elizabeth II's reign and the events that shaped Britain for decades to come.",
                "https://static.tvmaze.com/uploads/images/original_untouched/10/26156.jpg"
        ));

        // The Mandalorian
        series.add(new Serie(
                45317,
                "The Mandalorian",
                "English",
                new String[]{"Action", "Adventure", "Drama"},
                8.7,
                "Running",
                LocalDate.of(2019, 11, 12),
                null,
                "Disney+",
                "After the stories of Jango and Boba Fett, another warrior emerges in the Star Wars universe. The Mandalorian is set after the fall of the Empire and before the emergence of the First Order.",
                "https://static.tvmaze.com/uploads/images/original_untouched/6/16706.jpg"
        ));

        // Friends
        series.add(new Serie(
                4952,
                "Friends",
                "English",
                new String[]{"Comedy", "Romance"},
                8.9,
                "Ended",
                LocalDate.of(1994, 9, 22),
                LocalDate.of(2004, 5, 6),
                "NBC",
                "Follows the personal and professional lives of six New York City friends.",
                "https://static.tvmaze.com/uploads/images/original_untouched/39/99489.jpg"
        ));

        // The Witcher
        series.add(new Serie(
                40008,
                "The Witcher",
                "English",
                new String[]{"Action", "Adventure", "Drama", "Fantasy"},
                8.0,
                "Running",
                LocalDate.of(2019, 12, 20),
                null,
                "Netflix",
                "Geralt of Rivia, a mutated monster-hunter for hire, journeys toward his destiny in a turbulent world where people often prove more wicked than beasts.",
                "https://static.tvmaze.com/uploads/images/original_untouched/4/10687.jpg"
        ));

        // Sherlock
        series.add(new Serie(
                19885,
                "Sherlock",
                "English",
                new String[]{"Crime", "Drama", "Mystery"},
                9.1,
                "Ended",
                LocalDate.of(2010, 7, 25),
                LocalDate.of(2017, 1, 15),
                "BBC",
                "A modern adaptation of Sherlock Holmes and Dr. Watson's relationship.",
                "https://static.tvmaze.com/uploads/images/original_untouched/3/8771.jpg"
        ));

        // The Good Place
        series.add(new Serie(
                47833,
                "The Good Place",
                "English",
                new String[]{"Comedy", "Fantasy"},
                8.8,
                "Ended",
                LocalDate.of(2016, 9, 19),
                LocalDate.of(2020, 1, 30),
                "NBC",
                "Four people and their newly deceased on a forked road.",
                "https://static.tvmaze.com/uploads/images/original_untouched/3/8778.jpg"
        ));

        return series;
    }

    /**
     * Carrega as séries de demonstração no sistema
     */
    public static void carregarSeriesDemonstracao(ListasSeries listas) {
        List<Serie> series = obterSeriesDemonstacao();

        // Adiciona algumas séries aos favoritos
        if (series.size() > 0) {
            listas.adicionarFavorito(series.get(0)); // Breaking Bad

                }if (series.size() > 1) {
            listas.adicionarFavorito(series.get(1)); // The Office

                }if (series.size() > 2) {
            listas.adicionarFavorito(series.get(2)); // Game of Thrones
        }
        // Adiciona algumas às já assistidas
        if (series.size() > 3) {
            listas.adicionarJaAssistida(series.get(3)); // Stranger Things

                }if (series.size() > 4) {
            listas.adicionarJaAssistida(series.get(4)); // The Crown

                }if (series.size() > 5) {
            listas.adicionarJaAssistida(series.get(5)); // The Mandalorian
        }
        // Adiciona algumas ao desejo
        if (series.size() > 6) {
            listas.adicionarDesejamAssistir(series.get(6)); // Friends

                }if (series.size() > 7) {
            listas.adicionarDesejamAssistir(series.get(7)); // The Witcher

                }if (series.size() > 8) {
            listas.adicionarDesejamAssistir(series.get(8)); // Sherlock

                }if (series.size() > 9) {
            listas.adicionarDesejamAssistir(series.get(9)); // The Good Place

            }}
}
