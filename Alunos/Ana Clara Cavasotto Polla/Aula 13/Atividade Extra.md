# Atividade Extra – Conceitos de Programação

**Nome:** Ana Clara Cavasotto Polla

---

## Conceito 1: Elvis Operator

**Timestamp do vídeo:** 0:53

### O que é?

O Elvis Operator é um operador da linguagem Kotlin utilizado para tratar valores nulos de forma simples e elegante. Seu nome vem da semelhança visual do símbolo `?:` com o penteado do cantor Elvis Presley. Esse operador ajuda a reduzir a quantidade de código necessária para verificar se uma variável possui ou não um valor.

### Para que serve?

Ele serve para definir um valor padrão quando uma variável é nula, evitando erros de execução e tornando o código mais legível. Dessa forma, não é necessário utilizar estruturas condicionais extensas para verificar a existência de um valor antes de utilizá-lo.

### Como é normalmente utilizado?

O operador `?:` verifica se uma variável contém um valor válido. Caso contenha, esse valor é retornado. Caso a variável seja nula, será retornado o valor definido após o operador. Esse recurso é amplamente utilizado em Kotlin para simplificar o tratamento de valores opcionais.

### Exemplo de código

```kotlin
val nome: String? = null

val resultado = nome ?: "Usuário não informado"

println(resultado)
```

### Explicação do exemplo

No exemplo acima, a variável `nome` possui valor nulo. Ao utilizar o Elvis Operator (`?:`), o programa verifica essa condição e retorna o texto `"Usuário não informado"` como valor padrão. Dessa forma, evita-se que o programa tente utilizar um valor inexistente.

---

## Conceito 2: Parallel Programming (Programação Paralela)

**Timestamp do vídeo:** 3:20

### O que é?

Programação Paralela é uma técnica de desenvolvimento de software que permite a execução simultânea de múltiplas tarefas. Em vez de realizar uma operação por vez, o sistema divide o trabalho em diferentes partes que podem ser executadas ao mesmo tempo, tornando o processamento mais rápido e eficiente.

### Para que serve?

Essa abordagem é utilizada para melhorar o desempenho das aplicações, reduzindo o tempo necessário para executar tarefas complexas. É muito comum em sistemas que processam grandes volumes de dados, aplicações web, jogos, inteligência artificial e softwares que precisam lidar com várias atividades simultaneamente.

### Como é normalmente utilizada?

Na linguagem Kotlin, a programação paralela e concorrente pode ser implementada por meio de coroutines, um recurso que simplifica a execução assíncrona de tarefas. As coroutines permitem que diferentes operações cooperem entre si de forma eficiente, sem a necessidade de criar e gerenciar várias threads pesadas manualmente.

### Exemplo de código

```kotlin
import kotlinx.coroutines.*

fun main() = runBlocking {

    launch {
        delay(1000)
        println("Primeira tarefa concluída")
    }

    launch {
        delay(500)
        println("Segunda tarefa concluída")
    }
}
```

### Explicação do exemplo

No código acima, duas tarefas são executadas utilizando coroutines. Embora ambas sejam iniciadas quase ao mesmo tempo, a segunda tarefa termina primeiro porque possui um tempo de espera menor. Isso demonstra como diferentes atividades podem ser executadas de forma concorrente e assíncrona, otimizando o tempo de resposta da aplicação sem travar o fluxo principal.