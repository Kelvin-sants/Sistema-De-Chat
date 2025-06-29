import java.util.ArrayList;
import java.util.List;


public class Sala {

    private String nome; //variável do nome da sala
    private List<Usuario> usuarios; // lista de usuários na sala

    //construtor
   public Sala(String nome) {
        this.nome = nome;
        this.usuarios = new ArrayList<>(); //lista vazia

    }  
    // getter e setter (para retornar o nome da sala e permitir a mudança do nome da sala caso necessário)
       
        public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }
    // Retorna a lista de usuários da sala

    public List<Usuario> getUsuarios() {
        return usuarios;
    }

    //métodos:

        //adicionar Usuário

   // synchronized: garante que só uma pessoa por vez possa entrar na sala ao mesmo tempo (evita bagunça no código)     
   public synchronized boolean adicionarUsuario(Usuario u) {
    //ele verifica se o usuário é nulo e se está ou nao na sala 
        if (u != null && !usuarios.contains(u)) {
            usuarios.add(u); //caso ele ele nao seja nulo e nao esteja na sala ele é adicionado 
            u.setSala(this);   // notifica ao usuário sua entrada 
            broadcast(u.getNome() + " entrou na sala.", null); // avisa a todos da sala que o usuário x entrou na sala
            return true; 
        }
        return false; 
    }

        //remover Usuário
     public synchronized boolean removerUsuario(Usuario u) {
        if (u != null && usuarios.contains(u)) {
            usuarios.remove(u); // remove o usuário da lista de usuários
            broadcast(u.getNome() + " saiu da sala.", null); // notifica a saida para o grupo
            return true;
        }
        return false;
    }

// Envia uma mensagem para todos os usuários da sala, exceto o remetente
    public synchronized void broadcast(String msg, Usuario remetente) {
        for (Usuario u : usuarios) {
            // Se não foi o próprio usuário que mandou a mensagem, ele recebe
            if (remetente == null || !u.getNome().equals(remetente.getNome())) {
                System.out.println("[Sala " + nome + "] " + msg);
                // Aqui você pode fazer o envio via socket se desejar
            }
        }
    }
    

    // Lista os nomes dos usuários na sala
    public synchronized void listarUsuarios() {
        System.out.println("Usuários na sala '" + nome + "':");
        for (Usuario u : usuarios) {
            System.out.println("- " + u.getNome()); // mostra o nome de cada usuário
        }
    }
}

