import java.util.ArrayList;


public class Sala {

    private String nome; //variável do nome da sala
    private ArrayList<Usuario> usuarios; // lista de usuários na sala

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

    public ArrayList<Usuario> getUsuarios() {
        return usuarios;
    }

   // synchronized: garante que só uma pessoa por vez possa entrar na sala ao mesmo tempo (evita bagunça no código) 
      
   public synchronized boolean adicionarUsuario(Usuario u) {
    //ele verifica se o usuário é nulo e se está ou nao na sala 
        if (u != null) {
            if(!usuarios.contains(u)){
                usuarios.add(u); //caso ele ele nao seja nulo e nao esteja na sala ele é adicionado 
                u.setSala(this);   // notifica ao usuário sua entrada 
                broadcast("SERVIDOR: " + u.getNome() + " entrou na sala.", u); // avisa a todos da sala que o usuário x entrou na sala
                System.out.println("Usuario " + u.getNome() + " entrou na sala " + this.nome);
                return true; 
            }
        }
        return false; 
    }

    //remover Usuário
    public synchronized boolean removerUsuario(Usuario u) {
        if (u != null){                                                         //se o usuario for valido
            if(usuarios.contains(u)){                                           //se o usuario estiver na sala
                usuarios.remove(u);                                             // remove o usuário da lista de usuários
                broadcast("SERVIDOR: " + u.getNome() + " saiu da sala.", u);              // notifica a saida para o grupo
                System.out.println("Usuario " + u.getNome() + "saiu da sala " + this.nome);
                return true;
            }
        }
        return false;
    }

// Envia uma mensagem para todos os usuários da sala, exceto o remetente
    public synchronized void broadcast(String msg, Usuario remetente) {

        ArrayList<Usuario> copia = new ArrayList<>(usuarios);

        for (Usuario u : copia) {

            if (remetente != null){
                if(!u.getNome().equals(remetente.getNome())) {            //se o remetente for null ou a mensagem nao for para o remetente
                u.getOut().println(remetente.getNome() + ": " + msg);
                }
            }else{      //remetente == null
                u.getOut().println(msg);
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

