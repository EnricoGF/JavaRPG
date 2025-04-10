package ProjetoRPG;

import java.io.IOException;
import java.nio.file.*;
import java.util.*;
import java.util.stream.*;

public class LeitorCena {

    private Personagem personagem;
    private Scanner scanner = new Scanner(System.in);

    public LeitorCena(Personagem personagem) {
        this.personagem = personagem;
    }

    public void carregarCena(int numeroCena) {
        Path caminho = Paths.get("cenas", numeroCena + ".txt");

        try (Stream<String> linhas = Files.lines(caminho)) {
            List<String> conteudo = linhas.collect(Collectors.toList());

            if (conteudo.isEmpty()) {
                System.out.println("Arquivo de cena vazio.");
                return;
            }

            if (conteudo.stream().anyMatch(l -> l.startsWith("M:"))) {
                processarMonstro(conteudo);
            } else {
                processarCenaNarrativa(conteudo);
            }

        } catch (IOException e) {
            System.out.println("Erro ao ler a cena: " + e.getMessage());
        }
    }

    private void processarCenaNarrativa(List<String> linhas) {
        System.out.println("\n Cena:");
        linhas.forEach(linha -> {
            if (!linha.startsWith("#")) {
                System.out.println(linha);
            }
        });

        System.out.println("\nEscolhas:");
        Map<Integer, Integer> opcoes = new HashMap<>();

        linhas.stream()
                .filter(linha -> linha.startsWith("#"))
                .forEach(linha -> {
                    String[] partes = linha.substring(1).split(":");
                    int destino = Integer.parseInt(partes[0].trim());
                    String descricao = partes[1].trim();
                    System.out.println(destino + " - " + descricao);
                    opcoes.put(destino, destino);
                });

        if (!opcoes.isEmpty()) {
            int escolha = -1;
            do {
                System.out.print("Escolha: ");
                try {
                    escolha = scanner.nextInt();
                } catch (InputMismatchException e) {
                    scanner.nextLine(); // Limpa o buffer
                    System.out.println("Entrada inválida! Tente novamente.");
                    continue;
                }

                if (!opcoes.containsKey(escolha)) {
                    System.out.println("Opção inválida. Escolha novamente.");
                    escolha = -1;
                }
            } while (escolha == -1);

            carregarCena(escolha); // Continua o jogo com a próxima cena
        }
    }


    private void processarMonstro(List<String> linhas) {
        String nome = "";
        int habilidade = 0, energia = 0, sorte = 0;
        String item = null;
        int tesouro = 0, provisoes = 0;
        int proximaVitoria = -1, proximaDerrota = -1;

        for (String linha : linhas) {
            if (linha.startsWith("N:")) nome = linha.substring(2).trim();
            if (linha.startsWith("H:")) habilidade = Integer.parseInt(linha.substring(2).trim());
            if (linha.startsWith("E:")) energia = Integer.parseInt(linha.substring(2).trim());
            if (linha.startsWith("S:")) sorte = Integer.parseInt(linha.substring(2).trim());
            if (linha.startsWith("T:")) tesouro = Integer.parseInt(linha.substring(2).trim());
            if (linha.startsWith("P:")) provisoes = Integer.parseInt(linha.substring(2).trim());
            if (linha.startsWith("I:")) item = linha.substring(2).trim();
            if (!linha.contains(":") && linha.contains(";")) {
                String[] cenas = linha.trim().split(";");
                proximaVitoria = Integer.parseInt(cenas[0]);
                proximaDerrota = Integer.parseInt(cenas[1]);
            }
        }

        Monstro monstro = new Monstro(nome, habilidade, energia);

        System.out.println("\n Monstro encontrado: " + nome);
        System.out.println("Habilidade: " + habilidade + " | Energia: " + energia + " | Sorte: " + sorte);
        if (item != null) {
            System.out.println("Item: " + item.replace(";", ", "));
        }

        //  COMBATE
        String resultado = personagem.lutarContra(monstro);

        // VERIFICA RESULTADO
        switch (resultado) {
            case "vitoria":
                System.out.println("🎉 Você venceu o combate!");
                if (proximaVitoria > 0) carregarCena(proximaVitoria);
                break;
            case "derrota":
                System.out.println("💀 Você foi derrotado em combate...");
                if (proximaDerrota > 0) carregarCena(proximaDerrota);
                break;
            case "fuga":
                System.out.println("🏃‍♂️ Você fugiu do combate. O perigo ainda ronda...");
                if (proximaDerrota > 0) carregarCena(proximaDerrota);
                break;
            default:
                System.out.println("⚠️ Resultado inesperado do combate.");
        }
    }

}