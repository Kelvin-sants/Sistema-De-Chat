package servidor;

import java.util.ArrayList;

// Classe responsável por encapsular o estado de uma sala individual de chat
public class Sala {

    private String nome; // Variável do nome da sala
    private ArrayList<Usuario> usuarios; // Lista de usuários na sala

    // Construtor da Classe
   public Sala(String nome) {
        this.nome = nome;
        this.usuarios = new ArrayList<>(); // Lista vazia

    }  
    // Getter e Setter (para retornar o nome da sala e permitir a mudança do nome da sala caso necessário)
       
    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }
    
    // Retorna a lista de usuários atualmente na sala
    public ArrayList<Usuario> getUsuarios() {
        return usuarios;
    }

   // O 'synchronized' garante que apenas uma operação de adição/remoção possa ocorrer por vez, evitando inconsistências na lista de usuários
   public synchronized boolean adicionarUsuario(Usuario u) {
    // Verifica se o objeto usuário não é nulo
        if (u != null) {
            if(!usuarios.contains(u)){
                // Verifica se o usuário está na sala
                usuarios.add(u); //  Adiciona o usuário à lista de usuários
                u.setSala(this);   // Notifica ao usuário sua entrada 
                broadcastSemRemetente("SERVIDOR: " + u.getNome() + " entrou na sala.", u); // Informa a todos da sala que o usuário x entrou na sala
                //System.out.println("Usuario " + u.getNome() + " entrou na sala " + this.nome);      //imprime no terminal do servidor
                return true; // Sucesso
            }
        }
        return false; // Falha
    }

    public synchronized boolean removerUsuario(Usuario u) {
        if (u != null){    // Caso o usuario seja válido
            if(usuarios.contains(u)){ // Se o usuario estiver na sala
                usuarios.remove(u);   // Remove o usuário da lista de usuários
                broadcastSemRemetente("SERVIDOR: " + u.getNome() + " saiu da sala.", u);              // Notifica a saida para o grupo
                return true;
            }
        }
        return false;
    }

// Envia uma mensagem para todos os usuários da sala, exceto o remetente
    public synchronized void broadcast(String msg, Usuario remetente) {
        // Cria uma cópia da lista de usuários 
        ArrayList<Usuario> copia = new ArrayList<>(usuarios);
        for (Usuario u : copia) {
            // Se houver um remetente especificado (não nulo)
            if (remetente != null){
                // E o usuário atual no loop não for o remetente
                if(!u.getNome().equals(remetente.getNome())) {
                    // Envia a mensagem formatada
                    u.getOut().println(remetente.getNome() + ": " + msg);
                }
            }else{      //remetente == null
                u.getOut().println(msg);
            }
        }
        return;
    }
    
    // Lista os nomes dos usuários na sala
    public synchronized void listarUsuarios() {
        System.out.println("Usuários na sala '" + nome + "':");
        for (Usuario u : usuarios) {
            System.out.println("- " + u.getNome()); // Imprime o nome de cada usuário
        }
        return;
    }

    public synchronized void broadcastSemRemetente(String msg, Usuario remetente) {
        // Cria uma cópia da lista de usuários 
        ArrayList<Usuario> copia = new ArrayList<>(usuarios);
        for (Usuario u : copia) {
            // Se houver um remetente especificado (não nulo)
            if (remetente != null){
                // E o usuário atual no loop não for o remetente
                if(!u.getNome().equals(remetente.getNome())) {
                    // Envia a mensagem formatada
                    u.getOut().println(msg);
                }
            }else{      //remetente == null
                u.getOut().println(msg);
            }
        }
        return;
    }
}

