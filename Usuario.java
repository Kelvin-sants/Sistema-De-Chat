import java.util.Scanner;
import java.net.*;
import java.io.IOException;
import java.io.PrintStream;

public class Usuario{

    //atributos:
    private String nome;
    private boolean ehAdm;
    private Sala salaAtual;

    public Usuario(String nome){
        this.nome = nome;
        ehAdm = false;
        salaAtual = null;
    }

    //métodos:

    //setters:
    public void setNome(String nome){
        this.nome = nome;
    } //define o nome do usuário

    public void setStatusAdm(boolean setStatusAdm){
        this.ehAdm = setStatusAdm;
    } // define o status do usuário 

    public void setSala(Sala sala){
        this.salaAtual = sala;
    } // define a sala em que o usuario vai estar

    //getters: 
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