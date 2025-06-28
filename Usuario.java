import java.util.Scanner;
import java.net.*;
import java.io.IOException;
import java.io.PrintStream;

public class Usuario{

    private String nome;
    private boolean ehAdm;
    private Sala salaAtual;

    public Usuario(String nome){
        this.nome = nome;
        ehAdm = false;
        salaAtual = null;
    }

    public void setNome(String nome){
        this.nome = nome;
    }

    public void setStatusAdm(boolean setStatusAdm){
        this.ehAdm = setStatusAdm;
    }

    public void setSala(Sala sala){
        this.salaAtual = sala;
    }

    public String getNome(){
        return this.nome;
    }

    public boolean getStatusAdm(){
        return this.ehAdm;
    }

    public Sala getSalaAtual(){
        return this.salaAtual;
    }
}