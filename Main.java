package ProjetoRPG;

import java.util.Scanner;
import ProjetoRPG.classe.Cavaleiro;
import ProjetoRPG.classe.Mago;
import ProjetoRPG.classe.Ninja;

public class Main {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);

        System.out.println("Bem-vindo ao RPG!");
        System.out.println("1 - Novo Jogo");
        System.out.println("2 - Carregar Jogo");
        System.out.println("3 - Créditos");
        System.out.println("4 - Sair");
        int escolhaInicial = scanner.nextInt();
        scanner.nextLine();

        Personagem personagem;


        int cenaInicial = 1;

        if(escolhaInicial == 3) {
            System.out.println("Criadores: Enrico Forte e Gabriel Soldi");
        }
        if (escolhaInicial == 4) {
            System.out.println("Saindo...");
        }

        if (escolhaInicial == 2) {
            personagem = Salvamento.carregarJogo();
            cenaInicial = Salvamento.getCenaAtual();
            LeitorCena leitor = new LeitorCena(personagem);
            leitor.carregarCena(cenaInicial);
            if (personagem == null) {
                System.out.println("Não foi possível carregar. Iniciando novo jogo...");
                escolhaInicial = 1;
            }
        }

        if (escolhaInicial == 1) {
            System.out.print("Digite o nome do seu personagem: ");
            String nome = scanner.nextLine();

            int escolhaClasse = 0;
            System.out.println("\nEscolha sua classe:");
            System.out.println("1 - Cavaleiro");
            System.out.println("2 - Mago");
            System.out.println("3 - Ninja");
            while (true) {
                escolhaClasse = scanner.nextInt();
                scanner.nextLine();
                if (escolhaClasse == 1 || escolhaClasse == 2 || escolhaClasse == 3) {
                    break;
                } else {
                    System.out.println("Escolha inválida! Tente novamente.");
                }
            }

            // Distribuição de pontos
            int habilidade, energia, sorte, pontosTotais;

            while (true) {
                pontosTotais = 12;
                habilidade = 6;
                energia = 12;
                sorte = 6;
                System.out.println("\nVocê tem 12 pontos para distribuir");
                System.out.print("Habilidade (mín 0, máx 6): ");
                int habilidadeSoma = scanner.nextInt();
                if (habilidade + habilidadeSoma > pontosTotais) {
                    System.out.println("Extrapolou o limite! Redistribua seus atributos");
                    continue;
                } else {
                    habilidade += habilidadeSoma;
                }

                System.out.print("\nEnergia (mín 0, máx 12): ");
                int energiaSoma = scanner.nextInt();
                if (energia + energiaSoma > pontosTotais + 12 || energiaSoma + habilidadeSoma > pontosTotais) {
                    System.out.println("Extrapolou o limite! Redistribua seus atributos");
                    continue;
                } else {
                    energia += energiaSoma;
                }

                System.out.print("\nSorte (mín 0, máx 6): ");
                int sorteSoma = scanner.nextInt();
                if (sorte + sorteSoma > pontosTotais || energiaSoma + habilidadeSoma + sorteSoma > pontosTotais) {
                    System.out.println("Extrapolou o limite! Redistribua seus atributos");
                    continue;
                } else {
                    sorte += sorteSoma;
                }

                if (sorteSoma + habilidadeSoma + energiaSoma < pontosTotais) {
                    System.out.println("\n\nVocê ainda tem " + (pontosTotais - (sorteSoma + habilidadeSoma + energiaSoma)) + " ponto(s) para distribuir.");
                }

                System.out.println("\n--Seus atributos--\nHabilidade: " + habilidade + "\nEnergia: " + energia + "\nSorte: " + sorte);
                System.out.println("Deseja redistribuir seus atributos?\n1 - Sim\n2 - Não");
                int redistribuir = scanner.nextInt();
                if (redistribuir == 1) continue;
                break;
            }

            // Escolha de arma
            String arma = "";
            if (escolhaClasse == 1) {
                System.out.println("\nEscolha sua arma:\n 1 - Espada \n 2 - Lança");
                int escolhaArma = scanner.nextInt();
                arma = (escolhaArma == 1) ? "Espada" : "Lança";
            } else if (escolhaClasse == 2) {
                System.out.println("\nEscolha sua arma\n 1 - Cajado \n 2 - Cetro");
                int escolhaArma = scanner.nextInt();
                arma = (escolhaArma == 1) ? "Cajado" : "Cetro";
            } else if (escolhaClasse == 3) {
                System.out.println("\nEscolha sua arma\n 1 - Katana \n 2 - Shuriken");
                int escolhaArma = scanner.nextInt();
                arma = (escolhaArma == 1) ? "Katana" : "Shuriken";
            }

            switch (escolhaClasse) {
                case 1:
                    personagem = new Cavaleiro(nome, habilidade, energia, sorte, arma);
                    break;
                case 2:
                    personagem = new Mago(nome, habilidade, energia, sorte, arma);
                    break;
                case 3:
                    personagem = new Ninja(nome, habilidade, energia, sorte, arma);
                    break;
                default:
                    System.out.println("Erro ao criar personagem.");
                    scanner.close();
                    return;
            }

            personagem.exibirStatus();
            //inicia leitor de cenas
            LeitorCena leitor = new LeitorCena(personagem);
            leitor.carregarCena(cenaInicial); // Inicia na cena 1

            scanner.close();
        }
    }
}
