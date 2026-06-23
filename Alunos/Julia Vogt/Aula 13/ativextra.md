# Característica 1

**Nome:** Null Safety (Segurança contra valores nulos)

**Conceito escolhido:** Nullable Types (`?`) e prevenção de `NullPointerException`

**Timestamp do vídeo que menciona o conceito:** aproximadamente **00:48**.

## O que é?

O Kotlin possui um sistema de segurança contra valores nulos chamado **Null Safety**. Diferentemente de outras linguagens, ele diferencia variáveis que obrigatoriamente devem possuir um valor daquelas que podem armazenar `null`. Essa verificação acontece durante a compilação, reduzindo a possibilidade de erros em tempo de execução.

## Para que serve?

Seu principal objetivo é evitar o famoso erro `NullPointerException` (NPE), um dos problemas mais comuns em aplicações Java. Com isso, o código se torna mais seguro, confiável e fácil de manter.

## Como é normalmente utilizado?

Ao declarar uma variável que pode ser nula, utiliza-se o caractere `?` após o tipo. Além disso, operadores como `?.` (*safe call*) e `?:` (*Elvis operator*) permitem acessar valores de forma segura e definir valores padrão quando necessário.

## Exemplo de código

```kotlin
var nome: String? = null

println(nome?.length)
println(nome ?: "Visitante")
```

Nesse exemplo, o programa não gera exceção caso `nome` seja `null`, exibindo `"Visitante"` como valor padrão.

---

# Característica 2

**Nome:** Coroutines

**Conceito escolhido:** Programação assíncrona e concorrente com Coroutines

**Timestamp do vídeo que menciona o conceito:** aproximadamente **03:21**.

## O que é?

Coroutines são um recurso do Kotlin que permite executar tarefas concorrentes e assíncronas de maneira simples e eficiente. Elas possibilitam escrever código que parece sequencial, mas executa operações sem bloquear a aplicação.

## Para que serve?

São utilizadas para realizar atividades demoradas, como chamadas para APIs, acesso a bancos de dados ou processamento em segundo plano, mantendo o programa responsivo e utilizando menos recursos do sistema do que o gerenciamento manual de múltiplas threads.

## Como é normalmente utilizado?

No desenvolvimento Android e em aplicações de servidor, é comum utilizar `launch`, `async` e funções `suspend` para executar tarefas paralelas ou de longa duração sem interromper a execução principal da aplicação.

## Exemplo de código

```otlin
import kotlinx.coroutines.*

fun main() = runBlocking {
    launch {
        println("Executando uma coroutine")
    }

    println("Programa em execução")
}
```

Nesse exemplo, uma coroutine é iniciada utilizando `launch`, permitindo executar uma tarefa de forma concorrente dentro do bloco principal.

---

# Conclusão

A música **"Kotlin"**, da banda **Nanowar Of Steel**, utiliza humor para apresentar diversas características reais da linguagem. Entre elas, destacam-se o sistema de **Null Safety**, que reduz erros relacionados a valores nulos, e o uso de **Coroutines**, que simplifica a programação assíncrona e concorrente. Ambos os recursos são amplamente utilizados no desenvolvimento moderno e contribuem para tornar o Kotlin uma linguagem segura, produtiva e eficiente.