import java.util.Scanner;

public class Main {
    static class Musica {
        final String titulo, artista, album;
        final int duracao;
        Musica(String titulo, String artista, String album, int duracao) {
            if (titulo.trim().isEmpty() || artista.trim().isEmpty() || album.trim().isEmpty() || duracao <= 0)
                throw new IllegalArgumentException("Campos obrigatorios; duracao deve ser positiva.");
            this.titulo = titulo.trim(); this.artista = artista.trim(); this.album = album.trim(); this.duracao = duracao;
        }
        public String toString() {
            return titulo + " | " + artista + " | " + album + " | " + duracao / 60 + ":" + String.format("%02d", duracao % 60);
        }
    }

    static class No {
        final Musica musica;
        No anterior, proximo;
        No(Musica musica) { this.musica = musica; }
    }

    static class Playlist {
        private No inicio, fim, atual;
        private int tamanho;
        int tamanho() { return tamanho; }
        Musica atual() { return atual == null ? null : atual.musica; }
        private No noNaPosicao(int posicao) {
            if (posicao < 1 || posicao > tamanho) throw new IllegalArgumentException("Posicao invalida.");
            No no = inicio;
            for (int i = 1; i < posicao; i++) no = no.proximo;
            return no;
        }
        void adicionar(Musica musica, int posicao) {
            if (posicao < 1 || posicao > tamanho + 1) throw new IllegalArgumentException("Posicao invalida.");
            No novo = new No(musica);
            No seguinte = posicao == tamanho + 1 ? null : noNaPosicao(posicao);
            No anterior = seguinte == null ? fim : seguinte.anterior;
            novo.anterior = anterior;
            novo.proximo = seguinte;
            if (anterior == null) inicio = novo; else anterior.proximo = novo;
            if (seguinte == null) fim = novo; else seguinte.anterior = novo;
            if (atual == null) atual = novo;
            tamanho++;
        }
        Musica remover(int posicao) {
            No no = noNaPosicao(posicao);
            if (no.anterior == null) inicio = no.proximo; else no.anterior.proximo = no.proximo;
            if (no.proximo == null) fim = no.anterior; else no.proximo.anterior = no.anterior;
            if (atual == no) atual = no.proximo != null ? no.proximo : no.anterior;
            no.anterior = null;
            no.proximo = null;
            tamanho--;
            return no.musica;
        }
        Musica removerTitulo(String titulo) {
            int posicao = 1;
            for (No no = inicio; no != null; no = no.proximo, posicao++)
                if (no.musica.titulo.equalsIgnoreCase(titulo.trim())) return remover(posicao);
            throw new IllegalArgumentException("Titulo nao encontrado.");
        }
        boolean proximo() {
            if (atual == null || atual.proximo == null) return false;
            atual = atual.proximo; return true;
        }
        boolean anterior() {
            if (atual == null || atual.anterior == null) return false;
            atual = atual.anterior; return true;
        }
        // Insertion sort: reposiciona os nos, preservando a identidade da musica atual.
        void ordenar() {
            No cursor = inicio == null ? null : inicio.proximo;
            while (cursor != null) {
                No seguinte = cursor.proximo;
                No destino = cursor.anterior;
                while (destino != null && destino.musica.titulo.compareToIgnoreCase(cursor.musica.titulo) > 0)
                    destino = destino.anterior;
                if (destino != cursor.anterior) {
                    cursor.anterior.proximo = cursor.proximo;
                    if (cursor.proximo != null) cursor.proximo.anterior = cursor.anterior;
                    else fim = cursor.anterior;
                    cursor.anterior = destino;
                    cursor.proximo = destino == null ? inicio : destino.proximo;
                    cursor.proximo.anterior = cursor;
                    if (destino == null) inicio = cursor; else destino.proximo = cursor;
                }
                cursor = seguinte;
            }
        }
        void listar() {
            if (inicio == null) System.out.println("Playlist vazia.");
            int posicao = 1;
            for (No no = inicio; no != null; no = no.proximo)
                System.out.println(posicao++ + ". " + no.musica + (no == atual ? " [ATUAL]" : ""));
        }
        void buscar(String termo) {
            boolean encontrou = false;
            for (No no = inicio; no != null; no = no.proximo) {
                Musica m = no.musica;
                if (contem(m.titulo, termo) || contem(m.artista, termo) || contem(m.album, termo)) {
                    System.out.println(m); encontrou = true;
                }
            }
            if (!encontrou) System.out.println("Nenhuma musica encontrada.");
        }
        private boolean contem(String texto, String termo) {
            for (int i = 0; i <= texto.length() - termo.length(); i++)
                if (texto.regionMatches(true, i, termo, 0, termo.length())) return true;
            return false;
        }
    }

    static String ler(Scanner entrada, String pergunta) {
        System.out.println(pergunta);
        if (!entrada.hasNextLine()) throw new IllegalStateException("Entrada encerrada.");
        return entrada.nextLine().trim();
    }
    static int lerInteiro(Scanner entrada, String pergunta) {
        try { return Integer.parseInt(ler(entrada, pergunta)); }
        catch (NumberFormatException erro) { throw new IllegalArgumentException("Digite um numero inteiro valido."); }
    }
    public static void main(String[] args) {
        Playlist playlist = new Playlist();
        try (Scanner entrada = new Scanner(System.in)) {
            while (true) {
                System.out.println("\nPLAYLIST\n1 Adicionar no inicio\n2 Adicionar no fim\n3 Adicionar na posicao\n4 Remover por posicao\n5 Remover por titulo\n6 Listar\n7 Proxima\n8 Anterior\n9 Reproduzir atual\n10 Ordenar por titulo\n11 Buscar\n0 Sair");
                if (!entrada.hasNextLine()) return;
                String opcao = entrada.nextLine().trim();
                try {
                    switch (opcao) {
                        case "1": case "2": case "3":
                            int posicao = opcao.equals("1") ? 1 : opcao.equals("2") ? playlist.tamanho() + 1 : lerInteiro(entrada, "Posicao (a partir de 1):");
                            String titulo = ler(entrada, "Titulo:");
                            String artista = ler(entrada, "Artista:");
                            String album = ler(entrada, "Album:");
                            int duracao = lerInteiro(entrada, "Duracao em segundos:");
                            Musica musica = new Musica(titulo, artista, album, duracao);
                            playlist.adicionar(musica, posicao);
                            System.out.println("Adicionada: " + musica); break;
                        case "4": System.out.println("Removida: " + playlist.remover(lerInteiro(entrada, "Posicao:"))); break;
                        case "5": System.out.println("Removida: " + playlist.removerTitulo(ler(entrada, "Titulo:"))); break;
                        case "6": playlist.listar(); break;
                        case "7":
                            System.out.println(playlist.proximo() ? "Atual: " + playlist.atual() : "Nao ha proxima musica."); break;
                        case "8":
                            System.out.println(playlist.anterior() ? "Atual: " + playlist.atual() : "Nao ha musica anterior."); break;
                        case "9":
                            System.out.println(playlist.atual() == null ? "Playlist vazia." : "Reproduzindo (simulacao): " + playlist.atual()); break;
                        case "10": playlist.ordenar(); System.out.println("Playlist ordenada."); break;
                        case "11": playlist.buscar(ler(entrada, "Buscar por titulo, artista ou album:")); break;
                        case "0": return;
                        default: System.out.println("Opcao invalida.");
                    }
                } catch (IllegalArgumentException erro) { System.out.println(erro.getMessage()); }
                catch (IllegalStateException erro) { System.out.println(erro.getMessage()); return; }
            }
        }
    }
}
