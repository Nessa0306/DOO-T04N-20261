# Aula 13 - Kotlin

## Nullable data types

### Timestamp do vídeo que menciona o conceito:

1:00

### O que é? Pra que serve? Como é normalmente utilizado?

Os **Nullable Data Types** são tipos de dados que podem armazenar o valor `null`. Em Kotlin, essa funcionalidade foi criada para aumentar a segurança do código e evitar erros comuns, como o famoso `NullPointerException`.

Para indicar que uma variável pode ser nula, utiliza-se o símbolo `?` após o tipo da variável. O compilador obriga o programador a verificar se o valor é nulo antes de utilizá-lo, tornando o código mais seguro.

### Exemplo de código em Kotlin

```kotlin
fun main() {
    var nome: String? = "João"

    println(nome)

    nome = null

    println(nome)
}
```

---

## Suspending Functions

### Timestamp do vídeo que menciona o conceito:

3:29

### O que é? Pra que serve? Como é normalmente utilizado?

As **Suspending Functions** são funções especiais utilizadas em programação assíncrona com Coroutines. Elas podem pausar sua execução sem bloquear a thread principal e continuar posteriormente do ponto onde pararam.

São declaradas com a palavra-chave `suspend` e são muito utilizadas para operações demoradas, como requisições de rede, acesso a banco de dados e leitura de arquivos.

### Exemplo de código em Kotlin

```kotlin
import kotlinx.coroutines.*

suspend fun buscarDados() {
    delay(1000)
    println("Dados carregados!")
}

fun main() = runBlocking {
    buscarDados()
}
```
