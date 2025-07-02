/*Importando bibliotecas */
import java.util.*;
import java.util.concurrent.*;
//import java.net.*;
//import java.io.*;


/*A classe GerenciaUsuario é responsável por gerenciar todos os clientes que se conectam ao sistema */

public class GerenciaUsuario {

    //atributos:
   private Map<String, Usuario> usuariosOnline;


   //construtor: para usuarios comuns
   public GerenciaUsuario() 
   {
        this.usuariosOnline = new ConcurrentHashMap<>();
    }
 
   /*-------------------------Métodos-------------------------*/

   //Para adicionar o usuario
   public synchronized boolean adicionarUsuario(Usuario u)
   {
    if(u != null) {
        if(u.getNome() != null) { //verifica se o usuário existe e se ele tem nome

            //verifica se o nome escolhido já está em uso
            if(usuariosOnline.containsKey(u.getNome())) {
                return false;
            }

            //avisa a conexão do usuario
            usuariosOnline.put(u.getNome(), u);
            System.out.println("->" + u.getNome() + "conectado(a) ao servidor com sucesso.") ;
            return true;
        }
        return false;
    }
    return false;
   }

   /*________________________________________________________________________________________*/

   //Para remover um usuário
   public synchronized boolean removerUsuario(Usuario u)
   {
        if(u!= null) {
            if(u.getNome() != null) {
                Usuario removido = usuariosOnline.remove(u.getNome()); //se o usuário existir e tiver um nome, ele será removido
                
                if(removido != null) { 

                    //remove o usuario da sala se ele estiver em uma
                    if(u.getSalaAtual() != null) {
                        u.getSalaAtual().removerUsuario(u);
                        u.setSala(null);
                    }

                    //avisa geralmente sobre a remoção
                    System.out.println("Usuario " + u.getNome() + " foi removido(a) do servidor");
                    return true;
                }
                return true;
            }
            return false;
        }
        return false;
   }

   /*_________________________________________________________________________________________ */

   //buscar um usuário pelo seu nome:
   public Usuario getUsuarioPorNome(String nome)
    {
        if(nome != null) {
            if(!nome.trim().isEmpty()) { //se a string não for vazia
                return usuariosOnline.get(nome);  //retorna seu nome
            }
        }
        return null;
   }

   /*_________________________________________________________________________________________ */

   //desconecta um usuario pelo nome
  public synchronized boolean desconectarUsuarioNome(Usuario solicitante, String nomeUsuario) {
        // Verifica se é um adm válido
        if(solicitante == null || !solicitante.getStatusAdm()) {
            solicitante.getOut().println((solicitante != null ? solicitante.getNome() : "Null") +  " não tem permissão para desconectar");
            return false;
        }
    
        // Verifica usuário alvo
        if(nomeUsuario == null || nomeUsuario.trim().isEmpty()) {
            solicitante.getOut().println("SERVIDOR: Nome de usuario invalido");
            return false;
        }
    
        Usuario usuarioAlvo = getUsuarioPorNome(nomeUsuario);
        if(usuarioAlvo == null) {
            solicitante.getOut().println("SERVIDOR: Usuario " + nomeUsuario + " nao encontrado");
            return false;
        }
    
        // Executa desconexão
        try {
            if(usuarioAlvo.getSocket() != null && !usuarioAlvo.getSocket().isClosed()) {
                usuarioAlvo.getSocket().close();
            }
            boolean removido = removerUsuario(usuarioAlvo);
            if(removido) {
                System.out.println(solicitante.getNome() + " desconectou " + nomeUsuario);
            }
            return removido;
        } catch(Exception e) {
            System.err.println("Erro ao desconectar " + nomeUsuario + ": " + e.getMessage());
            solicitante.getOut().println("Erro ao desconectar " + nomeUsuario + ": " + e.getMessage());
            return false;
        }
    }
   /*________________________________________________________________________________________ */

   //Move um usuário para uma sala
   public synchronized boolean moverUsuarioParaSala(Usuario u, Sala novaSala)
   {
        if(u!=null) {
            Sala salaAtual = u.getSalaAtual(); //captura  a sala que o usuario esta antes dele mudar
            if(salaAtual != null) {
                salaAtual.removerUsuario(u); // remove o usuario da sala
                //avisa que o usuario saiu da sala
                salaAtual.broadcast(u.getNome() + " saiu da sala.", null);
            }

            if(novaSala != null) {
                novaSala.adicionarUsuario(u); //agora adiciona o usuario para a nova sala
                u.setSala(novaSala); //faz um set de sala para usuario
                //avisos:
                novaSala.broadcast(u.getNome() + " entrou na sala", null); 
                System.out.println(u.getNome() + "movido para a sala " + novaSala.getNome());
            } else {
                u.setSala(null); //se der errado, ele fica sem sala (sala = null)
            }

            return true;
        }
        return false;
   }
}