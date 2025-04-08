package ProjetoRPG;

import java.util.Scanner;
import ProjetoRPG.Classe.Cavaleiro;
import ProjetoRPG.Classe.Mago;
import ProjetoRPG.Classe.Ninja;

public class Main {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        int escolhaClasse = 0;
        
        System.out.println("Bem-vindo ao RPG!");
        System.out.print("Digite o nome do seu personagem: ");
        String nome = scanner.nextLine();

        System.out.println("\nEscolha sua classe:");
        System.out.println("1 - Cavaleiro");
        System.out.println("2 - Mago");
        System.out.println("3 - Ninja");
        while (true) {
            escolhaClasse = scanner.nextInt();
            scanner.nextLine();
            if (escolhaClasse == 1 | escolhaClasse == 2 | escolhaClasse == 3) { // Tratamento de erro
                break;
            }
            else {
                System.out.println("Escolha inválida! Tente novamente.");
                continue;
            }
        }
        
        // Distribuição de pontos
        int habilidade, energia, sorte, pontosTotais;
 
        while (true) {
            pontosTotais = 12;
            habilidade = 6;
            energia = 12;
            sorte = 6;

            System.out.print("\nHabilidade (mín 0, máx 6): ");
            int habilidadeSoma = scanner.nextInt();
            if ( habilidade + habilidadeSoma > pontosTotais) { // Tratamento de erro
                System.out.println("Extrapolou o limite! Resdistribua seus atributos");
                continue;
            } else { habilidade = habilidade + habilidadeSoma; }

            System.out.print("\nEnergia (mín 0, máx 12): ");
            int energiaSoma = scanner.nextInt();
            if (energia + energiaSoma > pontosTotais + 12 | energiaSoma + habilidadeSoma > pontosTotais) { // Tratamento de erro
                System.out.println("Extrapolou o limite! Resdistribua seus atributos");
                continue;
            } else { energia = energia + energiaSoma; }

            System.out.print("\nSorte (mín 0, máx 6): ");
            int sorteSoma = scanner.nextInt();
            if (sorte + sorteSoma > pontosTotais | energiaSoma + habilidadeSoma + sorteSoma > pontosTotais) { // Tratamento de erro
                System.out.println("Extrapolou o limite! Resdistribua seus atributos");
                continue;
            } else { sorte = sorte + sorteSoma; }

            System.out.println("\n\n--Seus atributos--\nHabilidade: " + habilidade + "\nEnergia: " + energia + "\n Sorte: " + sorte);
            System.out.println("Deseja redistribuir seus atributos?\n1 - Sim\n2- Não");
            int redistribuirAtributos = scanner.nextInt();
            if (redistribuirAtributos == 1) {
                continue;
            }
            break;
        }

        // Escolha de arma
        String arma = "";
        if (escolhaClasse == 1) {
            System.out.println("\nEscolha sua arma: 1 - Espada | 2 - Lança");
            int escolhaArma = scanner.nextInt();
            arma = (escolhaArma == 1) ? "Espada" : "Lança";
        }
        else if (escolhaClasse == 2) {
            System.out.println("\nEscolha sua arma: 1 - Cajado | 2 - Cetro");
            int escolhaArma = scanner.nextInt();
            arma = (escolhaArma == 1) ? "Cajado" : "Cetro";
        }
        else if (escolhaClasse == 3) {
            System.out.println("\nEscolha sua arma: 1 - Katana | 2 - Cimitarra");
            int escolhaArma = scanner.nextInt();
            arma = (escolhaArma == 1) ? "Katana" : "Cimitarra";
        }

        // Criando personagem
        Personagem personagem;
        switch (escolhaClasse) {
            case 1: personagem = new Cavaleiro(nome, habilidade, energia, sorte, arma); break;
            case 2: personagem = new Mago(nome, habilidade, energia, sorte, arma); break;
            case 3: personagem = new Ninja(nome, habilidade, energia, sorte, arma); break;
            default: System.out.println("Problema"); // Tratamento de erro
            scanner.close();
            return;
        }

        personagem.exibirStatus();
        // Criando um monstro para testar o combate
        Monstro monstro = new Monstro("Orc", 8, 16);
        
        System.out.println("\nDeseja lutar contra um monstro? (1 - Sim, 2 - Não)");
        int opcao = scanner.nextInt();
        if (opcao == 1) {
            personagem.lutarContra(monstro);
        } else {
            System.out.println("Você escolheu não lutar.");
        }
        scanner.close();
    }
}
