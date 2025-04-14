package ProjetoRPG;

import java.io.*;
import java.util.*;
import ProjetoRPG.classe.Cavaleiro;
import ProjetoRPG.classe.Mago;
import ProjetoRPG.classe.Ninja;

public class Salvamento {
    private static final String SAVE_FILE = "savegame.txt";

    public static void salvarJogo(Personagem personagem, int cenaAtual) {
        try (PrintWriter writer = new PrintWriter(SAVE_FILE)) {
            writer.println("nome=" + personagem.nome);
            writer.println("classe=" + personagem.getClass().getSimpleName());
            writer.println("habilidade=" + personagem.habilidade);
            writer.println("energia=" + personagem.energia);
            writer.println("sorte=" + personagem.sorte);
            writer.println("arma=" + personagem.arma);
            writer.println("moedas=" + personagem.moedas);
            writer.println("provisoes=" + personagem.provisoes);
            writer.println("cenaAtual=" + cenaAtual);

            List<Item> itens = personagem.getInventario();
            String itensSalvos = "";
            for (Item item : itens) {
                itensSalvos += item.getNome() + ";" + item.getTipo() + ";" + (item.isUsoCombate() ? 1 : 0) + ";" + item.getBonusFA() + ";" + item.getBonusDano() + "|";
            }
            if (!itensSalvos.isEmpty()) {
                writer.println("itens=" + itensSalvos.substring(0, itensSalvos.length() - 1)); // remove último '|'
            } else {
                writer.println("itens=");
            }

            System.out.println("Jogo salvo com sucesso!");
        } catch (IOException e) {
            System.out.println("Erro ao salvar o jogo: " + e.getMessage());
        }
    }

    public static Personagem carregarJogo() {
        Properties prop = new Properties();
        try (BufferedReader reader = new BufferedReader(new FileReader(SAVE_FILE))) {
            prop.load(new StringReader(reader.lines().reduce("", (a, b) -> a + "\n" + b)));

            String nome = prop.getProperty("nome");
            String classe = prop.getProperty("classe");
            int habilidade = Integer.parseInt(prop.getProperty("habilidade"));
            int energia = Integer.parseInt(prop.getProperty("energia"));
            int sorte = Integer.parseInt(prop.getProperty("sorte"));
            String arma = prop.getProperty("arma");

            Personagem personagem = switch (classe) {
                case "Cavaleiro" -> new Cavaleiro(nome, habilidade, energia, sorte, arma);
                case "Mago" -> new Mago(nome, habilidade, energia, sorte, arma);
                case "Ninja" -> new Ninja(nome, habilidade, energia, sorte, arma);
                default -> throw new IllegalArgumentException("Classe desconhecida: " + classe);
            };

            personagem.moedas = Integer.parseInt(prop.getProperty("moedas"));
            personagem.provisoes = Integer.parseInt(prop.getProperty("provisoes"));

            // Restaurar inventário
            String[] itensStr = prop.getProperty("itens", "").split("\\|");
            for (String itemStr : itensStr) {
                if (itemStr.isBlank()) continue;
                String[] partes = itemStr.split(";");
                String nomeItem = partes[0];
                char tipo = partes[1].charAt(0);
                boolean usoCombate = partes[2].equals("1");
                int fa = Integer.parseInt(partes[3]);
                int dano = Integer.parseInt(partes[4]);
                personagem.adicionarItem(new Item(nomeItem, tipo, usoCombate, fa, dano));
            }

            System.out.println("Jogo carregado com sucesso!");
            personagem.exibirStatus();
            return personagem;

        } catch (IOException e) {
            System.out.println("Erro ao carregar o jogo: " + e.getMessage());
            return null;
        }
    }

    public static int getCenaAtual() {
        try (BufferedReader reader = new BufferedReader(new FileReader(SAVE_FILE))) {
            return reader.lines()
                    .filter(l -> l.startsWith("cenaAtual="))
                    .map(l -> l.split("=")[1])
                    .mapToInt(Integer::parseInt)
                    .findFirst()
                    .orElse(1);
        } catch (IOException e) {
            return 1;
        }
    }
}