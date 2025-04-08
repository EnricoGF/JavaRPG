package ProjetoRPG;

import java.util.Random;
import java.util.Scanner;
public abstract class Personagem {
    protected String nome;
    protected int habilidade;
    protected int energia;
    protected int sorte;
    protected String arma;
    protected Random random = new Random();

    public Personagem(String nome, int habilidade, int energia, int sorte, String arma) {
        this.nome = nome;
        this.habilidade = habilidade;
        this.energia = energia;
        this.sorte = sorte;
        this.arma = arma;
    }

    public void exibirStatus() {
        System.out.println("\nPersonagem: " + nome);
        System.out.println("Classe: " + this.getClass().getSimpleName());
        System.out.println("Habilidade: " + habilidade);
        System.out.println("Energia: " + energia);
        System.out.println("Sorte: " + sorte);
        System.out.println("Arma: " + arma);
    }

    public boolean testarSorte() {
        if (sorte <= 0) {
            System.out.println("Você está sem sorte disponível!");
            return false;
        }
        
        int sorteio = random.nextInt(12) + 1; // Número aleatório entre 1 e 12
        boolean sucesso = sorteio <= sorte;
        sorte--; // Cada uso de sorte reduz 1 ponto
        
        System.out.println("Teste de sorte: " + (sucesso ? "SUCESSO!" : "FALHA!"));
        return sucesso;
    }

    public void lutarContra(Monstro monstro) {
        Scanner scanner = new Scanner(System.in);
        Random random = new Random();
    
        System.out.println("\nBatalha contra: " + monstro.getNome());
        System.out.println("Energia do monstro: " + monstro.getEnergia());
        System.out.println("Habilidade do monstro: " + monstro.getHabilidade());
        System.out.println("Sua energia: " + this.energia);
    
        while (this.energia > 0 && monstro.estaVivo()) {
            System.out.println("\n--Escolha sua ação--");
            System.out.println("1 - Atacar");
            System.out.println("2 - Testar Sorte (pode aumentar ou reduzir seu dano)");
            System.out.println("3 - Tentar fugir");

            System.out.println("\nEnergia do monstro: " + monstro.getEnergia());
            System.out.println("Sua energia: " + this.energia);
    
            int escolha = scanner.nextInt();
            int modificadorDano = 0;  // Modificador que pode ser +2 ou -1
    
            if (escolha == 2) {  // Se escolher testar a sorte
                if (testarSorte()) {
                    modificadorDano = 2;  // +2 de dano extra
                    System.out.println("Sorte bem-sucedida! Você causará +2 de dano neste turno!");
                } else {
                    modificadorDano = -1; // -1 de dano (o ataque será mais fraco)
                    System.out.println("Você falhou no teste de sorte... Seu ataque será mais fraco neste turno.");
                }
                this.sorte--;  // A cada teste de sorte, reduz 1 ponto de sorte
            } else if (escolha == 3) {  // Se tentar fugir, faz o teste de sorte
                int sorteJogador = random.nextInt(11);
                int sorteMonstro = random.nextInt(11);
    
                System.out.println("\nSorte do jogador: " + sorteJogador);
                System.out.println("Sorte do monstro: " + sorteMonstro);
    
                if (sorteJogador > sorteMonstro) {
                    System.out.println("Você conseguiu fugir da batalha!");
                    scanner.close();
                    return;  // Sai do método, encerrando a luta
                } else {
                    System.out.println("Você falhou ao tentar fugir!");
                }
            }
    
            // **Teste de ataque**
            int testeSorteJogador = random.nextInt(10) + 1 + this.habilidade;
            int testeSorteMonstro = random.nextInt(10) + 1 + monstro.getHabilidade();
    
            System.out.println("\nTeste de combate:");
            System.out.println("Seu valor: " + testeSorteJogador);
            System.out.println("Valor do monstro: " + testeSorteMonstro);
    
            if (testeSorteJogador > testeSorteMonstro) {
                int dano = 2 + modificadorDano; // Aplica o modificador de dano do teste de sorte
                monstro.reduzirEnergia(dano);
                System.out.println("Você acertou o ataque! Causou " + dano + " de dano.");
            } else {
                this.energia -= 2;
                System.out.println("O monstro venceu o teste de sorte! Você perdeu 2 de energia.");
            }
        }
    
        if (this.energia <= 0) {
            System.out.println("Você foi derrotado...");
        } else {
            System.out.println("Você derrotou o " + monstro.getNome() + "!");
        }
        scanner.close();
    }
}
