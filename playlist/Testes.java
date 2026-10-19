public class Testes {
    private static int verificacoes;
    static void conferir(boolean condicao) {
        verificacoes++;
        if (!condicao) throw new AssertionError("Falha na verificacao " + verificacoes);
    }
    static void rejeitar(Runnable operacao) {
        boolean rejeitada = false;
        try { operacao.run(); } catch (IllegalArgumentException esperado) { rejeitada = true; }
        conferir(rejeitada);
    }
    static Main.Musica musica(String titulo) { return new Main.Musica(titulo, "Artista", "Album", 125); }
    public static void main(String[] args) {
        Main.Playlist lista = new Main.Playlist();
        conferir(lista.atual() == null && !lista.proximo() && !lista.anterior());
        lista.ordenar();
        rejeitar(() -> lista.remover(1));
        rejeitar(() -> lista.adicionar(musica("X"), 0));
        rejeitar(() -> lista.adicionar(musica("X"), 2));
        rejeitar(() -> new Main.Musica(" ", "A", "B", 2));
        rejeitar(() -> new Main.Musica("X", "A", "B", 0));
        Main.Musica z = musica("Zulu"), a = musica("Alfa"), m = musica("Meio"), a2 = musica("alfa");
        lista.adicionar(z, 1); lista.adicionar(a, 1); lista.adicionar(m, 2); lista.adicionar(a2, 4);
        conferir(lista.tamanho() == 4 && lista.atual() == z);
        lista.ordenar();
        conferir(lista.atual() == z && !lista.proximo());
        conferir(lista.anterior() && lista.atual() == m);
        conferir(lista.anterior() && lista.atual() == a2);
        conferir(lista.anterior() && lista.atual() == a && !lista.anterior());
        conferir(lista.proximo() && lista.atual() == a2);
        conferir(lista.proximo() && lista.atual() == m);
        conferir(lista.proximo() && lista.atual() == z);
        lista.ordenar();
        conferir(lista.remover(4) == z && lista.atual() == m);
        conferir(lista.removerTitulo("ALFA") == a);
        conferir(lista.remover(2) == m && lista.atual() == a2);
        conferir(lista.remover(1) == a2 && lista.atual() == null && lista.tamanho() == 0);
        rejeitar(() -> lista.removerTitulo("Ausente"));
        lista.adicionar(a, 1); lista.adicionar(m, 2); lista.adicionar(z, 3);
        conferir(lista.remover(1) == a && lista.atual() == m);
        conferir(lista.remover(2) == z);
        lista.ordenar();
        conferir(lista.remover(1) == m);
        // Exercita diversas ordens, verificando todos os elos nas duas direcoes.
        for (int rodada = 0; rodada < 20; rodada++) {
            for (int i = 0; i < 17; i++) lista.adicionar(musica(String.format("%02d", (i * 7 + rodada) % 17)), lista.tamanho() + 1);
            lista.ordenar();
            while (lista.anterior()) { /* voltar ao inicio */ }
            for (int i = 0; i < 17; i++) {
                conferir(lista.atual().titulo.equals(String.format("%02d", i)));
                conferir(lista.proximo() == (i < 16));
            }
            for (int i = 16; i >= 0; i--) {
                conferir(lista.atual().titulo.equals(String.format("%02d", i)));
                conferir(lista.anterior() == (i > 0));
            }
            while (lista.tamanho() > 0) lista.remover(1);
        }
        System.out.println("Playlist: " + verificacoes + " verificacoes aprovadas (elos, ordenacao, limites e musica atual).");
    }
}
