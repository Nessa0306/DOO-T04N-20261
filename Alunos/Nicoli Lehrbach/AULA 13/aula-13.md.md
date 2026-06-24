Conceito 1 — Null Safety
Timestamp do vídeo que menciona o conceito: 00:58
O que é? É um recurso do Kotlin que evita o erro clássico chamado NullPointerException, ele obriga o programador a declarar se uma variável pode ou não receber valores nulos.
Pra que serve? Serve para aumentar a segurança do código, prevenindo falhas inesperadas quando tentamos acessar algo que não existe.
Como é normalmente utilizado? Usa-se operadores como ?.  (safe call) e ?: (elvis operator) para lidar com valores nulos de forma controlada.
Exemplo de código em Kotlin:
var nome: String? = null  
// Safe call evita erro 
println(nome?.length)  
// Elvis operator fornece valor padrão 
println(nome?.length ?: 0)
Conceito 2 — Extension Functions
Timestamp do vídeo que menciona o conceito: 02:15 
O que é? São funções que permitem adicionar novos comportamentos a classes já existentes sem precisar alterar o código original delas.
Pra que serve? Serve para deixar o código mais limpo e reutilizável, criando funções que parecem ser parte da própria classe.
Como é normalmente utilizado? Cria-se uma função com a palavra-chave fun seguida do tipo que será estendido. Depois, essa função pode ser chamada como se fosse nativa da classe.
Exemplo de código em Kotlin: 
//Criando
fun String.reverseWords(): String {     
    return this.split(" ").reversed().joinToString(" ") 
}  
fun main() {     
    val frase = "Kotlin é incrível"     
    println(frase.reverseWords())  // saida
}