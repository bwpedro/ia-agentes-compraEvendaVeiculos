/*
 * Código desenvolvido por Othon Alberto.
 * github: othonalberto
 * email: othon@alunos.utfpr.edu.br
 */
package compraEvenda;

/**
 *
 * @author othonalberto
 */
public class Veiculo {
    
    private int ano;
    private int potencia;
    private String cor;
    private String nome;
    private boolean cambio; // automatico = 1; manual = 0
    private double preco;

    
    public Veiculo(int ano, int potencia, String cor, String nome, boolean cambio, double preco) {
        this.ano = ano;
        this.potencia = potencia;
        this.cor = cor;
        this.nome = nome;
        this.cambio = cambio;
        this.preco = preco;
    }

    public Veiculo(int ano) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    
    public int getAno() {
        return ano;
    }

    public void setAno(int ano) {
        this.ano = ano;
    }

    public int getPotencia() {
        return potencia;
    }

    public void setPotencia(int potencia) {
        this.potencia = potencia;
    }

    public String getCor() {
        return cor;
    }

    public void setCor(String cor) {
        this.cor = cor;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public boolean isCambio() {
        return cambio;
    }

    public void setCambio(boolean cambio) {
        this.cambio = cambio;
    }

    public double getPreco() {
        return preco;
    }

    public void setPreco(double preco) {
        this.preco = preco;
    }
    
    
    
}
