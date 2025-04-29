package ProjetoRPG;

public class Monstro {
    private String nome;
    private int habilidade;
    private int energia;

    public Monstro(String nome, int habilidade, int energia) {
        this.nome = nome;
        this.habilidade = habilidade;
        this.energia = energia;
    }

    public String getNome() {
        return nome;
    }

    public int getHabilidade() {
        return habilidade;
    }

    public int getEnergia() {
        return energia;
    }

    public void reduzirEnergia(int dano) {
        this.energia -= dano;
    }

    public boolean estaVivo() {
        return this.energia > 0;
    }
}