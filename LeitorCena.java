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
        System.out.println("\n---------------------------------------------------------\nCena:");
        linhas.forEach(linha -> {
            if (!linha.startsWith("#") && !linha.startsWith("I:")) {
                System.out.println(linha);
            }
        });

        // Verifica se há um item direto na cena
        linhas.stream()
                .filter(linha -> linha.startsWith("I:"))
                .findFirst()
                .ifPresent(itemStr -> {
                    String[] partes = itemStr.substring(2).trim().split(";");
                    String nome = partes[0];
                    char tipo = partes[1].charAt(0);
                    boolean combate = partes[2].equals("1");
                    int fa = Integer.parseInt(partes[3]);
                    int dano = Integer.parseInt(partes[4]);
                    Item item = new Item(nome, tipo, combate, fa, dano);
                    personagem.adicionarItem(item);
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
                    scanner.nextLine();
                    System.out.println("Entrada inválida! Tente novamente.");
                    continue;
                }

                if (!opcoes.containsKey(escolha)) {
                    System.out.println("Opção inválida. Escolha novamente.");
                    escolha = -1;
                }
            } while (escolha == -1);

            Salvamento.salvarJogo(personagem, escolha); // salva antes de carregar nova cena
            carregarCena(escolha);
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

        System.out.println("\n---------------------------------------------------------\nMonstro encontrado: " + nome);
        System.out.println("Habilidade: " + habilidade + " | Energia: " + energia + " | Sorte: " + sorte);
        if (item != null) {
            System.out.println("Item: " + item.replace(";", ", "));
        }

        String resultado = personagem.lutarContra(monstro);

        switch (resultado) {
            case "vitoria":
                System.out.println(" Você venceu o combate!");
                if (tesouro > 0) personagem.ganharTesouro(tesouro);
                if (provisoes > 0) personagem.ganharProvisoes(provisoes);
                if (item != null) {
                    String[] partes = item.split(";");
                    String nomeItem = partes[0];
                    char tipo = partes[1].charAt(0);
                    boolean usoCombate = partes[2].equals("1");
                    int fa = Integer.parseInt(partes[3]);
                    int dano = Integer.parseInt(partes[4]);
                    personagem.adicionarItem(new Item(nomeItem, tipo, usoCombate, fa, dano));
                }
                if (proximaVitoria > 0) {
                    Salvamento.salvarJogo(personagem, proximaVitoria);
                    carregarCena(proximaVitoria);
                }
                break;

            case "derrota":
                System.out.println(" Você foi derrotado em combate...");
                if (proximaDerrota > 0) {
                    Salvamento.salvarJogo(personagem, proximaDerrota);
                    carregarCena(proximaDerrota);
                }
                break;

            case "fuga":
                System.out.println(" Você fugiu do combate. O perigo ainda ronda...");
                if (proximaDerrota > 0) {
                    Salvamento.salvarJogo(personagem, proximaDerrota);
                    carregarCena(proximaDerrota);
                }
                break;

            default:
                System.out.println(" Resultado inesperado do combate.");
        }
    }


}