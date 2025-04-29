package ProjetoRPG;

import java.util.Random;
import java.util.Scanner;
import java.util.ArrayList;
import java.util.List;

public abstract class Personagem {
    protected String nome;
    protected int habilidade;
    protected int energia;
    protected int sorte;
    protected String arma;
    protected List<Item> inventario = new ArrayList<>();
    protected int moedas = 0;
    protected int provisoes = 0;
    protected Random random = new Random();

    public Personagem(String nome, int habilidade, int energia, int sorte, String arma) {
        this.nome = nome;
        this.habilidade = habilidade;
        this.energia = energia;
        this.sorte = sorte;
        this.arma = arma;
    }

    public List<Item> getInventario() {
        return inventario;
    }

    public void exibirStatus() {
        System.out.println("\n---------------------------------------------------------\nPersonagem: " + nome);
        System.out.println("Classe: " + this.getClass().getSimpleName());
        System.out.println("Habilidade: " + habilidade);
        System.out.println("Energia: " + energia);
        System.out.println("Sorte: " + sorte);
        System.out.println("Arma: " + arma);
    }

    public boolean testarSorte() {
        if (this.sorte <= 0) {
            System.out.println("Você está sem sorte disponível!");
            return false;
        }

        int sorteio = random.nextInt(12) + 1; // Número aleatório entre 1 e 12
        boolean sucesso = sorteio <= sorte;
        sorte--; // Cada uso de sorte reduz 1 ponto

        System.out.println("Teste de sorte: " + (sucesso ? "SUCESSO!" : "FALHA!"));
        return sucesso;
    }

    public String lutarContra(Monstro monstro) {
        Scanner scanner = new Scanner(System.in);
        Random random = new Random();

        while (this.energia > 0 && monstro.estaVivo()) {
            System.out.println("\n--Escolha sua ação--");
            System.out.println("1 - Atacar");
            System.out.println("2 - Testar Sorte (pode aumentar ou reduzir seu dano)");
            System.out.println("3 - Tentar fugir");
            System.out.println("4 - Abrir inventário");

            System.out.println("\nEnergia do monstro: " + monstro.getEnergia() + "\nSua energia: " + this.energia);

            int escolha = scanner.nextInt();
            int modificadorDano = 0;

            if (escolha == 2) {
                if (testarSorte()) {
                    modificadorDano = 2;
                    System.out.println("Sorte bem-sucedida! Você causará +2 de dano neste turno!");
                } else {
                    modificadorDano = -1;
                    System.out.println("Você falhou no teste de sorte... Seu ataque será mais fraco neste turno.");
                }
                this.sorte--;
            }
            else if (escolha == 3) {
                int sorteJogador = random.nextInt(11);
                int sorteMonstro = random.nextInt(11);

                System.out.println("\nSorte do jogador: " + sorteJogador);
                System.out.println("Sorte do monstro: " + sorteMonstro);

                if (sorteJogador > sorteMonstro) {
                    System.out.println("Você conseguiu fugir da batalha!");
                    return "fuga";
                } else {
                    System.out.println("Você falhou ao tentar fugir!");
                }
            }

            else if (escolha == 4) {
                abrirMenuInventario();
                continue;
            }


            int testeSorteJogador = random.nextInt(10) + 1 + this.habilidade;
            int testeSorteMonstro = random.nextInt(10) + 1 + monstro.getHabilidade();

            System.out.println("\nTeste de combate:");
            System.out.println("Seu valor: " + testeSorteJogador);
            System.out.println("Valor do monstro: " + testeSorteMonstro);

            if (testeSorteJogador > testeSorteMonstro) {
                int dano = 2 + modificadorDano;
                monstro.reduzirEnergia(dano);
                System.out.println("Você acertou o ataque! Causou " + dano + " de dano.");
            } else {
                this.energia -= 2;
                System.out.println("O monstro venceu o teste de sorte! Você perdeu 2 de energia.");
            }
        }

        if (!monstro.estaVivo()) {
            return "vitoria";
        } else if (this.energia <= 0) {
            return "derrota";
        } else {
            return "fuga";
        }
    }

    public void adicionarItem(Item item) {
        inventario.add(item);
        System.out.println("Item adicionado ao inventário: " + item);
    }

    public void ganharTesouro(int valor) {
        moedas += valor;
        System.out.println("Você recebeu " + valor + " moedas. Total: " + moedas);
    }

    public void ganharProvisoes(int quantidade) {
        provisoes += quantidade;
        System.out.println("Você recebeu " + quantidade + " provisões. Total: " + provisoes);
    }

    public void exibirInventario() {
        System.out.println("\n Inventário:");
        if (inventario.isEmpty()) {
            System.out.println("Seu inventário está vazio.");
        } else {
            for (Item item : inventario) {
                System.out.println("- " + item);
            }
        }
        System.out.println(" Moedas: " + moedas);
        System.out.println(" Provisões: " + provisoes);
    }

    public void abrirMenuInventario() {
        Scanner scanner = new Scanner(System.in);

        while (true) {
            System.out.println("\n===== INVENTÁRIO =====");
            exibirInventario();
            System.out.println("\n1 - Usar provisão (+4 de energia)");
            System.out.println("2 - Voltar");

            int escolha = scanner.nextInt();

            if (escolha == 1) {
                if (provisoes > 0) {
                    provisoes--;
                    energia += 4;
                    System.out.println("Você usou uma provisão e recuperou 4 de energia!");
                    System.out.println("Energia atual: " + energia);
                } else {
                    System.out.println("Você não tem provisões para usar.");
                }
            } else if (escolha == 2) {
                break;
            } else {
                System.out.println("Opção inválida. Tente novamente.");
            }
        }
    }


}