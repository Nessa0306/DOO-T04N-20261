package Objetos;
import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;

public class Main {

    public static void main(String[] args) {

        // 1
        System.out.println(" ATIVIDADE 1 ");

        List<Integer> numeros = Arrays.asList(10, 15, 22, 33, 40, 51, 68, 79);

        List<Integer> pares = numeros.stream()
                .filter(n -> n % 2 == 0)
                .collect(Collectors.toList());

        System.out.println("Números pares: " + pares);

        //  2
        System.out.println("\n ATIVIDADE 2 ");

        List<String> nomes = Arrays.asList("roberto", "josé", "caio", "vinicius");

        List<String> nomesMaiusculos = nomes.stream()
                .map(String::toUpperCase)
                .collect(Collectors.toList());

        System.out.println("Nomes em maiúsculo: " + nomesMaiusculos);

        // 3
        System.out.println("\n ATIVIDADE 3 ");

        List<String> palavras = Arrays.asList(
                "se", "talvez", "hoje", "sábado",
                "se", "quarta", "sábado"
        );

        Map<String, Long> contagem = palavras.stream()
                .collect(Collectors.groupingBy(
                        Function.identity(),
                        Collectors.counting()
                ));

        contagem.forEach((palavra, qtd) ->
                System.out.println(palavra + ": " + qtd));

        //  4
        System.out.println("\n ATIVIDADE 4 ");

        List<Produto> produtos = Arrays.asList(
                new Produto("Mouse", 80.00),
                new Produto("Teclado", 150.00),
                new Produto("Monitor", 900.00),
                new Produto("Webcam", 120.00)
        );

        List<Produto> produtosMaior100 = produtos.stream()
                .filter(p -> p.getPreco() > 100)
                .collect(Collectors.toList());

        produtosMaior100.forEach(System.out::println);

        // 5
        System.out.println("\n ATIVIDADE 5");

        double soma = produtos.stream()
                .mapToDouble(Produto::getPreco)
                .sum();

        System.out.println("Soma total dos produtos: R$ " + soma);

        // 6
        System.out.println("\n ATIVIDADE 6 ");

        List<String> linguagens = Arrays.asList(
                "Java", "Python", "C", "JavaScript", "Ruby"
        );

        List<String> ordenadas = linguagens.stream()
                .sorted(Comparator.comparingInt(String::length))
                .collect(Collectors.toList());

        System.out.println("Linguagens ordenadas: " + ordenadas);
    }
}