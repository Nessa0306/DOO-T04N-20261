# Nome:

Guilherme Dietrich

# Conceito escolhido: Null Safety

**Timestamp do vídeo que menciona o conceito: 00:39** 

## O que é?

Null Safety é um conjunto de mecanismos que ajudam a evitar erros causados por referências nulas. Em muitas linguagens de programação, tentar acessar um método ou atributo de um objeto nulo gera falhas em tempo de execução. Com Null Safety, o compilador verifica previamente se uma variável pode ou não conter valores nulos.

## Para que serve?

Serve para aumentar a segurança do código, reduzir erros comuns e tornar os programas mais confiáveis. Além disso, facilita a manutenção do sistema ao deixar explícito quando uma variável pode receber um valor nulo.

## Como é normalmente utilizado?

Linguagens modernas como Kotlin, Dart e Swift possuem suporte nativo a Null Safety. O programador precisa indicar quando uma variável pode ser nula e utilizar operadores específicos para tratar esses casos.

## Exemplo de código

```kotlin
fun main() {
    var nome: String? = null

    println(nome?.length)
}
```

Nesse exemplo, o operador `?.` verifica se a variável `nome` é nula antes de acessar a propriedade `length`. Caso seja nula, o programa não gera erro.

---

# Conceito escolhido: Native String Templating

**Timestamp do vídeo que menciona o conceito: 1:42** 

## O que é?

Native String Templating é um recurso que permite inserir variáveis e expressões diretamente dentro de uma string, sem a necessidade de concatenar textos utilizando operadores como `+`.

## Para que serve?

Serve para tornar o código mais legível, organizado e fácil de manter. Esse recurso é muito utilizado na construção de mensagens, relatórios, logs e saídas formatadas para o usuário.

## Como é normalmente utilizado?

Diversas linguagens modernas possuem suporte nativo a String Templates, como Kotlin, JavaScript, Python e C#. O valor das variáveis é inserido automaticamente no texto durante a execução do programa.

## Exemplo de código

```kotlin
fun main() {
    val nome = "Guilherme"
    val idade = 19

    println("Meu nome é $nome e tenho $idade anos.")
}
```

Saída:

```text
Meu nome é Guilherme e tenho 19 anos.
```

Nesse exemplo, as variáveis `nome` e `idade` são inseridas diretamente na string utilizando a sintaxe de template, sem necessidade de concatenação.
