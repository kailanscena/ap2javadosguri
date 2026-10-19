# Trabalho de Estruturas de Dados — Java

Universidade Luterana do Brasil — ULBRA, Campus Torres  
Curso: ADS — Disciplina: Estrutura de Dados  
Professor: Juliano Ramos Matos  
Acadêmicos: **Regis Bopsin da Luz; Kailan de Scena Pacheco; Guilherme Birlem Machado**  
Turma: **terça-feira à noite** — Data: **[PREENCHER]**

## 1. Introdução

Implementar estruturas de dados manualmente permite compreender como os elementos são organizados na memória e como suas conexões determinam o custo das operações. Em Java, usamos referências a objetos no lugar de ponteiros manipulados diretamente. Cada inserção cria um nó, enquanto a remoção desfaz suas conexões; objetos sem referências acessíveis podem ser recuperados pelo coletor de lixo. A implementação explicita essas relações e ajuda a entender filas, pilhas e listas sem depender de coleções prontas.

## 2. Organização e execução

- [Cafeteria](cafeteria/Main.java): fila de pendentes e pilha de cancelados.
- [Playlist](playlist/Main.java): lista duplamente ligada e player simulado.
- [Evidências](evidencias/): entradas reproduzíveis e saídas reais das execuções.

Os programas são independentes e possuem suas próprias classes `Main` e `Testes`. Compile cada um em seu diretório de saída; não compile os dois juntos. Requer JDK 8 ou superior, com `java` e `javac` no PATH. Validado com JDK 21. Não há dependências externas. `Scanner` é utilizado somente para entrada de teclado; nenhuma coleção pronta implementa as estruturas.

Abra o terminal nesta pasta e execute:

```powershell
javac -encoding UTF-8 -d cafeteria/out cafeteria/Main.java cafeteria/Testes.java
java -cp cafeteria/out Main
```

Para o exercício 2:

```powershell
javac -encoding UTF-8 -d playlist/out playlist/Main.java playlist/Testes.java
java -cp playlist/out Main
```

Cada menu permanece aberto até a opção `0` ou o encerramento da entrada. Os dados ficam somente na memória e são perdidos ao sair. O player exibe informações, sem tocar arquivos de áudio.

## 3. Implementação

### 3.1 Cafeteria: lista simplesmente ligada

`Pedido` armazena ID e descrição; `No` contém um pedido e a referência `proximo`. Os IDs são sequenciais na sessão e preservados ao cancelar e restaurar pedidos.

A fila mantém `inicio` e `fim`. A inserção liga o último nó ao novo; a remoção retira o primeiro. Isso implementa FIFO: o primeiro pedido recebido é o primeiro atendido. A pilha mantém `topo`: inserir e remover acontecem nessa extremidade, implementando LIFO: o último cancelado é o primeiro restaurado.

Cancelar transfere o pedido mais antigo da fila para o topo da pilha. Restaurar remove o topo e coloca o pedido no **fim** da fila. A impressão dos pendentes segue do início ao fim; a dos cancelados segue do topo para a base.

### 3.2 Playlist: lista duplamente ligada

`Musica` contém título, artista, álbum e duração em segundos. `No` armazena uma música e as referências `anterior` e `proximo`. A playlist mantém `inicio`, `fim`, `atual` e `tamanho`. A listagem exibe a duração em minutos e segundos e identifica a música atual.

As posições começam em **1**. Inserir aceita posições de 1 até tamanho + 1; remover aceita de 1 até tamanho. Na inserção, os dois vizinhos passam a apontar para o novo nó. Na remoção, os vizinhos são ligados entre si e as referências do nó removido são limpas.

A primeira música adicionada torna-se a atual. Inserções posteriores preservam a seleção. Próximo e anterior navegam sem dar a volta nas extremidades. Se a música atual for removida, seleciona-se a seguinte; se não houver, a anterior. Ao remover a última música, a seleção fica vazia.

A ordenação usa **insertion sort manual**, reposicionando os próprios nós. A comparação por título ignora maiúsculas/minúsculas e segue a comparação de strings do Java, sem regras linguísticas especiais para acentos. Títulos equivalentes mantêm sua ordem relativa. Como a referência `atual` aponta para o mesmo nó, a seleção é preservada mesmo quando sua posição muda.

A remoção por título usa correspondência completa sem diferenciar maiúsculas/minúsculas e remove a primeira ocorrência. A busca adicional encontra trechos em título, artista ou álbum, também sem diferenciar maiúsculas/minúsculas. Uma busca vazia lista todas as músicas. Múltiplas playlists, repetição e aleatoriedade são sugestões opcionais do enunciado e não foram implementadas.

### 3.3 Casos críticos

| Situação | Tratamento |
| --- | --- |
| Fila ou pilha vazia | Remoção retorna `null`; o menu informa que está vazia. |
| Último pedido removido | A fila limpa também `fim`, permitindo novas inserções. |
| Playlist vazia ou com um elemento | Referências das extremidades e da seleção são atualizadas corretamente. |
| Posição fora dos limites | Operação rejeitada antes de modificar os elos. |
| Remoção no início, meio ou fim | Atualização dos dois vizinhos e das extremidades afetadas. |
| Navegação além dos limites | Seleção preservada e mensagem explicativa. |
| Título não encontrado | Mensagem de erro, sem remover músicas. |
| Texto obrigatório vazio ou duração não positiva | Cadastro rejeitado. |
| Número inválido ou fora do intervalo de `int` | Mensagem explicativa e retorno ao menu. |
| Fim da entrada | Encerramento sem rastreamento de exceção. |

### 3.4 Complexidade

| Operação | Tempo | Espaço adicional |
| --- | --- | --- |
| Adicionar/retirar da fila ou pilha | O(1) | O(1) por operação |
| Listar pedidos ou músicas; buscar | O(n) | O(1) |
| Inserir música no início/fim | O(1) | O(1) por nó |
| Inserir/remover por posição | O(n) no pior caso para localizar | O(1) |
| Remover por título | O(n) | O(1) |
| Próximo/anterior | O(1) | O(1) |
| Ordenar músicas | O(n²) no pior caso; O(n) se ordenadas | O(1) |

As estruturas armazenam O(n) nós. As estimativas tratam a comparação de títulos como operação unitária; textos maiores acrescentam o custo de comparar caracteres.

## 4. Evidências de execução

Os arquivos abaixo contêm a saída real dos programas, incluindo os menus. São transcrições de terminal, **não capturas de tela**.

- [Entrada da cafeteria](evidencias/cafeteria-entrada.txt) e [saída da cafeteria](evidencias/cafeteria-saida.txt).
- [Entrada da playlist](evidencias/playlist-entrada.txt) e [saída da playlist](evidencias/playlist-saida.txt).

Para reproduzir no PowerShell, após compilar:

```powershell
Get-Content evidencias/cafeteria-entrada.txt | java -cp cafeteria/out Main
Get-Content evidencias/playlist-entrada.txt | java -cp playlist/out Main
```

Na cafeteria, são inseridos Café (ID 1), Chá (ID 2) e Bolo (ID 3). Os dois primeiros são cancelados. A pilha apresenta 2 antes de 1. Restaurar traz o ID 2, mas a fila atende primeiro o ID 3, demonstrando que a restauração ocorre no fim. Ao concluir, ambas as estruturas estão vazias.

Na playlist, são inseridas Zulu, Alfa e Meio, incluindo uma inserção por posição. A ordem antes de ordenar é Zulu, Meio, Alfa; depois é Alfa, Meio, Zulu. A execução demonstra navegação, reprodução simulada, busca pelo álbum, remoção por posição e título e lista vazia ao final.

### 4.1 Testes automatizados

```powershell
java -cp cafeteria/out Testes
java -cp playlist/out Testes
```

Resultado obtido:

```text
Cafeteria: 11 verificacoes aprovadas (FIFO, LIFO, restauracao e reuso).
Playlist: 1382 verificacoes aprovadas (elos, ordenacao, limites e musica atual).
```

Os testes usam verificações próprias que lançam `AssertionError`, sem bibliotecas de teste e sem exigir `-ea`. Incluem estruturas vazias, reutilização após esvaziamento, posições inválidas, títulos repetidos, preservação da seleção, remoção da música atual e 20 sequências distintas de inserção, verificando navegação nos dois sentidos após ordenar.

### 4.2 Capturas solicitadas pelo professor — pendentes

Antes de entregar, execute os comandos acima em um terminal visível e capture as telas com `Win + Shift + S`. As capturas não foram produzidas automaticamente nesta entrega. Inclua imagens mostrando:

1. Cafeteria: inserções, cancelamentos e restauração; depois a fila e a pilha vazias.
2. Playlist: listagem antes e depois da ordenação, reprodução e remoções.
3. Resultado dos testes automatizados.

Salve as imagens nesta pasta de evidências e insira os links Markdown nesta seção. Preencha também a data no início do relatório.

## 5. Conclusão

Os pontos de maior cuidado na implementação são manter as extremidades corretas após remover o último elemento e preservar os dois sentidos da playlist durante a ordenação. A solução concentra as atualizações de referências nos métodos de inserção, remoção e ordenação; os testes percorrem a lista para frente e para trás para detectar conexões incorretas. O exercício demonstra a diferença entre FIFO e LIFO, o papel das referências e o impacto da busca por posição no custo de uma lista ligada.

**Complemento do aluno:** antes de entregar, relate aqui suas dificuldades reais e o que aprendeu ao executar e estudar os programas. O texto técnico acima descreve a solução e não substitui esse relato pessoal.

