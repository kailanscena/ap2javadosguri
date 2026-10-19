public class Testes {
    private static int verificacoes;
    static void conferir(boolean condicao) {
        verificacoes++;
        if (!condicao) throw new AssertionError("Falha na verificacao " + verificacoes);
    }
    public static void main(String[] args) {
        Main.Fila fila = new Main.Fila();
        Main.Pilha pilha = new Main.Pilha();
        Main.Pedido cafe = new Main.Pedido(1, "Cafe");
        Main.Pedido cha = new Main.Pedido(2, "Cha");
        Main.Pedido bolo = new Main.Pedido(3, "Bolo");
        conferir(fila.remover() == null);
        conferir(pilha.desempilhar() == null);
        fila.adicionar(cafe); fila.adicionar(cha); fila.adicionar(bolo);
        pilha.empilhar(fila.remover()); pilha.empilhar(fila.remover());
        Main.Pedido restaurado = pilha.desempilhar();
        conferir(restaurado == cha);
        fila.adicionar(restaurado);
        conferir(fila.remover() == bolo);
        conferir(fila.remover() == cha);
        conferir(fila.remover() == null);
        conferir(pilha.desempilhar() == cafe);
        conferir(pilha.desempilhar() == null);
        fila.adicionar(cafe);
        conferir(fila.remover() == cafe);
        conferir(fila.remover() == null);
        pilha.empilhar(bolo);
        conferir(pilha.desempilhar() == bolo);
        System.out.println("Cafeteria: " + verificacoes + " verificacoes aprovadas (FIFO, LIFO, restauracao e reuso).");
    }
}
