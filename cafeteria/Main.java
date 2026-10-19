import java.util.Scanner;

public class Main {
    static class Pedido {
        final int id;
        final String descricao;
        Pedido(int id, String descricao) { this.id = id; this.descricao = descricao; }
        public String toString() { return "ID " + id + " - " + descricao; }
    }

    // Cada no possui apenas uma referencia para o proximo elemento.
    static class No {
        final Pedido pedido;
        No proximo;
        No(Pedido pedido) { this.pedido = pedido; }
    }

    static class Fila {
        private No inicio, fim;
        void adicionar(Pedido pedido) {
            No novo = new No(pedido);
            if (fim == null) inicio = novo;
            else fim.proximo = novo;
            fim = novo;
        }
        Pedido remover() {
            if (inicio == null) return null;
            Pedido pedido = inicio.pedido;
            inicio = inicio.proximo;
            if (inicio == null) fim = null;
            return pedido;
        }
        void imprimir() {
            if (inicio == null) System.out.println("Nenhum pedido pendente.");
            for (No no = inicio; no != null; no = no.proximo) System.out.println(no.pedido);
        }
    }

    static class Pilha {
        private No topo;
        void empilhar(Pedido pedido) {
            No novo = new No(pedido);
            novo.proximo = topo;
            topo = novo;
        }
        Pedido desempilhar() {
            if (topo == null) return null;
            Pedido pedido = topo.pedido;
            topo = topo.proximo;
            return pedido;
        }
        void imprimir() {
            if (topo == null) System.out.println("Nenhum pedido cancelado.");
            for (No no = topo; no != null; no = no.proximo) System.out.println(no.pedido);
        }
    }

    public static void main(String[] args) {
        Fila pendentes = new Fila();
        Pilha cancelados = new Pilha();
        int proximoId = 1;
        try (Scanner entrada = new Scanner(System.in)) {
            while (true) {
                System.out.println("\nCAFETERIA\n1 Adicionar\n2 Atender\n3 Cancelar\n4 Restaurar\n5 Pendentes\n6 Cancelados\n0 Sair");
                if (!entrada.hasNextLine()) return;
                String opcao = entrada.nextLine().trim();
                Pedido pedido;
                switch (opcao) {
                    case "1":
                        System.out.println("Descricao:");
                        if (!entrada.hasNextLine()) return;
                        String descricao = entrada.nextLine().trim();
                        if (descricao.isEmpty()) { System.out.println("Descricao obrigatoria."); break; }
                        pedido = new Pedido(proximoId++, descricao);
                        pendentes.adicionar(pedido);
                        System.out.println("Adicionado: " + pedido);
                        break;
                    case "2":
                        pedido = pendentes.remover();
                        System.out.println(pedido == null ? "Fila vazia." : "Atendido: " + pedido);
                        break;
                    case "3":
                        pedido = pendentes.remover();
                        if (pedido == null) System.out.println("Fila vazia.");
                        else { cancelados.empilhar(pedido); System.out.println("Cancelado: " + pedido); }
                        break;
                    case "4":
                        pedido = cancelados.desempilhar();
                        if (pedido == null) System.out.println("Pilha vazia.");
                        else { pendentes.adicionar(pedido); System.out.println("Restaurado: " + pedido); }
                        break;
                    case "5": pendentes.imprimir(); break;
                    case "6": cancelados.imprimir(); break;
                    case "0": return;
                    default: System.out.println("Opcao invalida.");
                }
            }
        }
    }
}
