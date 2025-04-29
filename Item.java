package ProjetoRPG;

public class Item {
    private String nome;
    private char tipo; // c: comum, r: armadura, w: arma
    private boolean usoCombate;
    private int bonusFA;
    private int bonusDano;

    public Item(String nome, char tipo, boolean usoCombate, int bonusFA, int bonusDano) {
        this.nome = nome;
        this.tipo = tipo;
        this.usoCombate = usoCombate;
        this.bonusFA = bonusFA;
        this.bonusDano = bonusDano;
    }

    public String getNome() {
        return nome;
    }

    public char getTipo() {
        return tipo;
    }

    public boolean isUsoCombate() {
        return usoCombate;
    }

    public int getBonusFA() {
        return bonusFA;
    }

    public int getBonusDano() {
        return bonusDano;
    }

    @Override
    public String toString() {
        return nome + " [" + tipo + "] (FA: " + bonusFA + ", Dano: " + bonusDano + ")";
    }
}