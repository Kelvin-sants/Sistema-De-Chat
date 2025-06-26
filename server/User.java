package server;

import java.io.PrintWriter;

public class User {
    private String nome;
    private boolean isAdmin;
    private PrintWriter out;
    private String salaAtual;

    public User(String nome, boolean isAdmin, PrintWriter out) {
        this.nome = nome;
        this.isAdmin = isAdmin;
        this.out = out;
    }

    public String getNome() {
        return nome;
    }

    public boolean isAdmin() {
        return isAdmin;
    }

    public String getSalaAtual() {
        return salaAtual;
    }

    public void setSalaAtual(String salaAtual) {
        this.salaAtual = salaAtual;
    }

    public void sendMessage(String msg) {
        out.println(msg);
    }
}

// O que já está pronto?

// Campos: nome, tipo (admin ou não), sala atual, saída (PrintWriter).

// Métodos:

// Getters e setters

// Enviar mensagem para o usuário

// Já permite representar qualquer pessoa conectada, com nome e permissões.
// Falta: lógica para expulsar usuários e para criar ou excluir salas, via comandos (feito no ClientHandler).