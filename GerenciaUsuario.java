/*Importando bibliotecas */
import java.util.*;
import java.util.concurrent.*;
import java.net.*;
import java.io.*;


/*A classe GerenciaUsuario é responsável por gerenciar todos os clientes que se conectam ao sistema */

public class GerenciaUsuario extends Usuario {

    //atributos:
   private Map<String, Usuario> usuariosOnline;


   //construtor: para usuarios comuns
   public GerenciaUsuario(String nome, boolean ehAdm) 
   {
        super(nome);
        this.setStatusAdm(ehAdm);
        this.usuariosOnline = new ConcurrentHashMap<>();
   }
   //construtor: para usuarios administradores
   public GerenciaUsuario(String nome)
   {
     this(nome, true);
   }

 
   /*-------------------------Métodos-------------------------*/

   //Para adicionar o usuario
   public synchronized boolean adicionarUsuario(Usuario u)
   {
    if(u != null) {
        if(u.getNome() != null) {

            //verifica se o nome escolhido já está em uso
            if(usuariosOnline.containsKey(u.getNome())) {
                return false;
            }

            //avisa a conexão do usuario
            usuariosOnline.put(u.getNome(), u);
            System.out.println("->" + this.getNome() +  u.getNome() + "conectado(a) ao servidor com sucesso.") ;

            //avisa ao adm em sala sobre a conexão
            if(this.getSalaAtual() != null) {
                this.getSalaAtual().broadcast(u.getNome() + "está conectado(a) ao servidor ", this);
            }

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
                Usuario removido = usuariosOnline.remove(u.getNome());
                
                if(removido != null) {

                    //remove o usuario da sala se ele estiver em uma
                    if(u.getSalaAtual() != null) {
                        u.getSalaAtual().removerUsuario(u);
                        u.setSala(null);
                    }

                    //avisa geralmente sobre a remoção
                    System.out.println("Usuario " + u.getNome() + " foi removido(a) do servidor");

                    //avisa em sala
                    if (this.getSalaAtual() != null) {
                        this.getSalaAtual().broadcast(u.getNome() + "foi removido(a) do servidor", this);
                    }
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
            if(!nome.trim().isEmpty()) {
                return usuariosOnline.get(nome);
            }
        }
        return null;
   }

   /*_________________________________________________________________________________________ */

   //desconecta um usuario pelo nome
   public synchronized boolean desconectarUsuarioNome(String nome) 
   {
        //avisa que não é possivel desconectar um usuario se não for adm
        if(!this.getStatusAdm()) {
            System.out.println(this.getNome() + " sem permissao para desconectar outros usuarios");
            return false;
        }

        Usuario u = getUsuarioPorNome(nome);
        if(u!= null) {
            try {
                System.out.println("Usuario desconectado: " + nome);

                return removerUsuario(u);
            } catch(Exception e) {
                System.err.println("Erro ao desconectar "+ nome + "(" + e.getMessage() + ")"); 
                return false;
            }
        }
        return false;
   }

   /*________________________________________________________________________________________ */

   //Move um usuário para uma sala
   public synchronized boolean moverUsuarioParaSala(Usuario u, Sala novaSala)
   {
        if(u!=null) {
            Sala salaAtual = u.getSalaAtual();
            if(salaAtual != null) {
                salaAtual.removerUsuario(u);
                //avisa que o usuario saiu da sala
                salaAtual.broadcast(u.getNome() + " saiu da sala.", null);
            }

            if(novaSala != null) {
                novaSala.adicionarUsuario(u);
                u.setSala(novaSala);
                novaSala.broadcast(u.getNome() + " entrou na sala", null);
                System.out.println(u.getNome() + "movido para a sala " + novaSala.getNome());
            } else {
                u.setSala(null);
            }

            return true;
        }
        return false;
   }
}