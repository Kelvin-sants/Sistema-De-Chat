import java.io.PrintStream;
import java.util.ArrayList;
import java.util.List;

public class ControladorDeComandos {
    private Usuario usuario;
    private  GerenciaSalas gerenciaSalas;
    private GerenciaUsuario gerenciaUsuarios;
    

    public ControladorDeComandos(Usuario usuario, GerenciaSalas gerenciaSalas, GerenciaUsuario gerenciaUsuarios) {         //construtor
        this.usuario = usuario;
        this.gerenciaSalas = gerenciaSalas;
        this.gerenciaUsuarios = gerenciaUsuarios;
    }


    public void interpretaComando(String comando, GerenciaUsuario gerenciaUsuarios, GerenciaSalas gerenciaSalas){
        
        String nomeSala = null;                 //declarando variavel local
        Sala sala = null;


        if(comando.startsWith("/login")){                   //login
            String nome = comando.substring(7);
            if(this.usuario.getNome() == null){
                this.usuario.setNome(nome);
                usuario.getOut().println("SERVER: Voce logou no servidor");
            }else{
                this.usuario.setNome(nome);
                usuario.getOut().println("SERVER: Voce trocou seu nome no servidor");
            }
        }

        if(comando.startsWith("/msg")){                         //msg
            String mensagem = comando.substring(5);
            if(this.usuario != null){
                sala = this.usuario.getSalaAtual();
                sala.broadcast(mensagem, usuario);
            }else{
                usuario.getOut().println("ERRO: Voce precisa fazer login primeiro\nPara fazer login use: /login nome");
            }
        }

        if(comando.startsWith("/criar")){                           //criar
            String nomeSalaCriar = comando.substring(7);
            if(this.usuario != null){                                           //se o usuario é válido
                if(this.usuario.getStatusAdm()){                                //se o usuario for adm
                    if(gerenciaSalas.criarSala(nomeSalaCriar, this.usuario)){       //se conseguiu criar a sala com sucesso
                        usuario.getOut().println("Sala Criada com sucesso");
                    }else{
                        usuario.getOut().println("Nao foi possivel criar a sala");         //em caso de erro ao criar a sala
                    }
                }else{
                        usuario.getOut().println("Somente ADMs podem criar salas");            //caso o usuario nao seja adm
                }
            }
        }

        if (comando.equals("/salas")){                      //salas
            for(String nomesala: gerenciaSalas.listarSalas()){
                usuario.getOut().println("-" + nomesala);
            }
        }

        if(comando.startsWith("/entrar")){                  //entrar
            nomeSala = comando.substring(8);            //pega o nome da sala
            sala = gerenciaSalas.getSala(nomeSala);
            if(sala != null){                                       //se a sala é valida
                sala.adicionarUsuario(usuario);                     //adiciona usuario
                usuario.setSala(sala);
            }else{
                usuario.getOut().println("Sala nao encontrada");
            }

        if(comando.startsWith("/sair")){                     //sair
            nomeSala = comando.substring(6);             //pega o nome da sala
            sala = gerenciaSalas.getSala(nomeSala);                 //procura a sala
            if(sala != null){                                       //se a sala for válida
                sala.removerUsuario(usuario);
                usuario.setSala(null);
            }else{
                usuario.getOut().println("Sala nao encontrada");
            }
        }

        if(comando.startsWith("/expulsar")){                 //expulsar
            if(usuario.getStatusAdm()){                             //se o usuario for adm
                String nomeExpulsar = comando.substring(10);        //pega o nome do usuario a ser expulso
                gerenciaUsuarios.desconectarUsuarioNome(usuario, nomeExpulsar);
            }
        }


        if(comando.startsWith("/encerrar")){                        //encerrar
            nomeSala = comando.substring(10);
            if(usuario.getStatusAdm()){                                   //se o usuario for um adm
                sala = gerenciaSalas.getSala(nomeSala);                   //procura a sala
                if(sala != null){                                         //se a sala existir
                    List<Usuario> usuarios = sala.getUsuarios();        //pega todos os usuarios daquela sala
                    for(Usuario u : usuarios){                          //percorre todos os ususarios daquela sala
                        u.setSala(null);                            //para cada usuario coloca null no campo sala
                        usuario.getOut().println("SERVIDOR: A sala que voce estava foi encerrada por um ADM");
                        sala.removerUsuario(u);
                    }
                    gerenciaSalas.removerSala(nomeSala);                    //remove a sala
                }else{
                    usuario.getOut().println("Sala nao encontrada");
                }
            }
        }


        if(comando.startsWith("/sair")){                        //sair
            String nomeUsuario = usuario.getNome();                    //pega o nome do usuario
            nomeSala = usuario.getSalaAtual().getNome();               //pega o nome da sala do usuario
            if(sala != null){                                          //se ele estiver em uma sala
                sala = gerenciaSalas.getSala(nomeSala);                //pega a sala que ele esta
                sala.removerUsuario(usuario);                          //remove ele da sala
                usuario.setSala(null);
                usuario.getOut().println("Voce saiu da sala");

            }else{
                usuario.getOut().println("voce nao esta em uma sala");
            }
        }







        }









    }

}
