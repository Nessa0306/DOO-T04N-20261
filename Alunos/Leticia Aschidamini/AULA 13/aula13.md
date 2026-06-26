# Aula 13 - Kotlin 

## Portabilidade (Write Once, Run Anywhere)

### Timestamp do vídeo que menciona o conceito:

01:18

### O que é? Pra que serve? Como é normalmente utilizado?

A portabilidade é uma das principais características do Kotlin quando utilizado na JVM. Ela permite que um programa seja escrito uma única vez e executado em diferentes sistemas operacionais sem precisar ser reescrito. Isso acontece porque o código é compilado para bytecode, que pode ser executado por qualquer ambiente compatível com a JVM.

Essa característica é muito utilizada no desenvolvimento de aplicações Android, sistemas desktop, serviços web e aplicações corporativas, permitindo que o mesmo código funcione em diferentes ambientes.

### Exemplo de código em Kotlin

```kotlin
fun main() {
    println("Olá, mundo!")
}
```

Ao compilar esse código, é gerado um bytecode que pode ser executado em diferentes sistemas operacionais compatíveis com a JVM, contribuindo para a portabilidade da aplicação.

---

## Tipagem Forte (Strong Typing)

### Timestamp do vídeo que menciona o conceito:
02:26

### O que é? Pra que serve? Como é normalmente utilizado?

A tipagem forte é uma característica do Kotlin que exige que cada variável possua um tipo bem definido. Isso permite que o compilador verifique se os dados estão sendo utilizados de forma correta, reduzindo a ocorrência de erros e comportamentos inesperados durante a execução do programa.

Essa característica aumenta a segurança, a confiabilidade e a legibilidade do código, além de facilitar a manutenção e a identificação de problemas ainda durante o desenvolvimento.

### Exemplo de código em Kotlin

```kotlin
fun main() {
    val nome: String = "Letícia"
    val idade: Int = 20

    println("$nome tem $idade anos")
}
```

Nesse exemplo, `nome` foi definido como `String` e `idade` como `Int`. O Kotlin não permite atribuir valores incompatíveis a essas variáveis, garantindo maior segurança no código.
