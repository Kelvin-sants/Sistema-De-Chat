//classe responsavel por receber os comandos de ClientHandler e executar cada um
//classe auxiliar para aliviar a classe ClientHandler

import java.io.IOException;
import java.util.ArrayList;

public class ControladorDeComandos {
    private Usuario usuario;                            //atributo - TENTAR DEIXAR ELE SEM ATRIBUTOS

    public ControladorDeComandos(Usuario usuario) {         //construtor        
        this.usuario = usuario;
    }

    public void interpretaComando(String comando, GerenciaUsuario gerenciaUsuarios, GerenciaSalas gerenciaSalas){           //funcao principal da classe
        
        String nomeSala = null;                 //declarando variaveis locais
        Sala sala = null;


        if(comando.startsWith("/login")){                               //comando /login
            if(comando.length() > 7){                                         //verifica se o nome é válido
                String nome = comando.substring(7).trim();         //separa a substring com o nome após "/login"
                if(!nome.isEmpty()){                                            //verifica se o nome não é vazio
                    this.usuario.setNome(nome);                                 //coloca o nome infomado no campo nome do usuario
                    if(gerenciaUsuarios.adicionarUsuario(usuario)){             //tenta adicionar o usuario ao servidor
                        usuario.getOut().println("SERVIDOR: Voce se conectou ao servidor");         //se conseguir, informa o usuario
                    }else{
                        usuario.getOut().println("SERVIDOR: Nao foi possivel se conectar ao servidor, tente outro nome");       //se não conseguir, infoma o usuário
                    }
                }else{
                    usuario.getOut().println("SERVIDOR: Nome inválido");
                }
            }else{
                    usuario.getOut().println("SERVIDOR: Nome inválido");
            }
        }
            
        else if(comando.startsWith("/msg")){                                 //comando "/msg"
            if(comando.length() > 5){                                          //verifica se a mensagem é válida
                String mensagem = comando.substring(5).trim();      //pega a mensagem após "/msg"
                if(!mensagem.isEmpty()){                                       //verifica se a mensagem não é vazia
                    if(this.usuario != null){                                  //verifica se o usuario é valido
                        sala = this.usuario.getSalaAtual();                    //pega a sala que o usuario esta
                        if(sala != null){                                      //verifica se a sala for valida
                            sala.broadcast(mensagem, usuario);                 //manda a mensagem para todos os usuarios da sala, exceto o usuario remetente
                        }else{
                            usuario.getOut().println("SERVIDOR: Voce nao esta em uma sala");         //caso o usuario nao esteja em nenhuma sala, informa o usuario
                        }
                    }else{
                        usuario.getOut().println("SERVIDOR: Usuario inválido");        //caso de erro no usuário
                    }
                    return;
                }
            }   
            usuario.getOut().println("SERVIDOR: Mensagem inválida");            //se a mensagem for invalida
            return;
        }

        else if(comando.startsWith("/criar")){                                     //comando "/criar"
            if(comando.length() > 7){                                                    //verifica se o nome da sala é válido
                String nomeSalaCriar = comando.substring(7).trim();           //pega o nome da sala a ser criada
                if(!nomeSalaCriar.isEmpty()){                                           //verifica se o nome da sala não é vazio
                    if(this.usuario != null){                                           //se o usuario é válido
                        if(this.usuario.getStatusAdm()){                                //se o usuario for adm
                            if(gerenciaSalas.criarSala(nomeSalaCriar, this.usuario)){       //se conseguiu criar a sala com sucesso
                                usuario.getOut().println("SERVIDOR: Sala Criada com sucesso");
                            }else{
                                usuario.getOut().println("SERVIDOR: Nao foi possivel criar a sala");         //em caso de erro ao criar a sala
                            }
                        }else{
                                usuario.getOut().println("SERVIDOR: Somente administradores podem criar salas");            //caso o usuario nao seja adm
                        }
                    }else{
                        usuario.getOut().println("SERVIDOR: Usuário inválido");                 //caso de erro no usuario
                    }
                    return;
                }
            }
            usuario.getOut().println("SERVIDOR: nome inválido");                //se o nome for inválido
            return;          
        }

        else if (comando.equals("/salas")){                     //comando "/salas"
            boolean flagListou = false;                             //flag para saber se listou pelo menos uma sala (para questoes esteticas)
            int numSala = 1;                                        //contador para lista enumerada das salas (questoes esteticas)
            for(String nomesala: gerenciaSalas.listarSalas()){      //se tiver salas no servidor, para cada sala
                flagListou = true;                                  //levanta a flag se lista pelo menos uma sala
                usuario.getOut().println("SERVIDOR: " + numSala + " - " + nomesala);      //lista  sala
                numSala++;                                                //incrementa o contador
            }
            if(flagListou == false){                                        //se não lista ao menos uma sala
                usuario.getOut().println("SERVIDOR: Nao ha salas disponiveis");     //informa o usuario que não há salas disponíveis
            }
            return;
        }

        else if(comando.startsWith("/entrar")){                                  //comando "/entrar"
            if(comando.length() > 8){                                                   //verifica se o nome da sala é válido
                nomeSala = comando.substring(8).trim();                     //pega o nome da sala
                if(!nomeSala.isEmpty()){                                                //verifica se o nome não é vazio
                    sala = gerenciaSalas.getSala(nomeSala);                             //busca a instacia da sala informada
                    if(sala != null){                                                   //se a sala é valida
                        if(sala.adicionarUsuario(usuario)){                             //tenta adiciona usuario a sala, se conseguir:
                            usuario.getOut().println("SERVIDOR: voce entrou na sala " + nomeSala);      //informa o usuario que ele entrou na sala
                            System.out.println(usuario.getNome() + "entrou na sala " + sala.getNome());     //informa no terminal do servidor que o usuario entrou na sala
                        }else{                                                                              //se não conseguir adicionar o usuario a sala
                            usuario.getOut().println("SERVIDOR: nao foi possivel entrar na sala");          //informa que não foi possivel entrar na sala
                        }
                    }else{                                                                    //se a sala não for válida
                        usuario.getOut().println("SERVIDOR: Sala nao encontrada");          //informa o usuário
                    }
                    return;
                }
            }
            usuario.getOut().println("SERVIDOR: Nome de sala inválido");            //caso o usuario informe um nome inválido
            return;
        }
        
        else if(comando.equals("/sair")){                                          //comando "/sair"
            sala = usuario.getSalaAtual();                                                  //pega a sala atual
            if(sala != null && usuario != null){                                            //verifica se a sala é válida
                if(sala.removerUsuario(usuario)){                                           //tenta remover o usuario da sala
                    usuario.setSala(null);                                             //remove a referencia de usuario a sala
                    usuario.getOut().println("SERVIDOR: Voce saiu da sala");               //informa o usuário a sua saída da sala
                    System.out.println(usuario.getNome() + "saiu da sala" + sala.getNome()); //imprime no terminal do servidor a saída do usuário da sala
                }else{                                                                       //se não conseguir remover o usuário da sala
                    usuario.getOut().println("SERVIDOR: Nao foi possivel sair da sala");   //informa o usuário que não foi possivel remover
                }
            }else{                                                                            //se a sala for null
                usuario.getOut().println("SERVIDOR: Você não está em uma sala");            //informa o usuário que ele não está em uma sala
            }
            return;
        }
        

        else if (comando.startsWith("/expulsar")) {                                             // comando "/expulsar"
            if(comando.length() > 9){                                                                  //verifica se o nome é válido
                String nomeExpulsar = comando.substring(9).trim();  
                if (!nomeExpulsar.isEmpty()) {                                                          //se o nome for válido

                    if (usuario.getStatusAdm()) {                                                       // verifica se é administrador
                        Usuario usuarioExpulsar = gerenciaUsuarios.getUsuarioPorNome(nomeExpulsar);     //pega o usuarioExpulsar
                        if (usuarioExpulsar == null) {                                                  //se o usuarioExpulsar não for válido
                            usuario.getOut().println("SERVIDOR: Usuário '" + nomeExpulsar + "' não encontrado.");       //informa o usuário que não foi encontrado
                        } else {                                                                        //se o usuarioExpulsar for válido  
                            Sala salaAtual = usuarioExpulsar.getSalaAtual();                            //pega a sala atual do usuarioExpulsar
                            if (salaAtual == null) {                                                    //se ele não estiver em nenhuma sala
                                usuario.getOut().println("SERVIDOR: Usuário '" + nomeExpulsar + "' não está em nenhuma sala.");         //informa o usuario que o usuarioExpulsar não está em nenhuma sala
                            } else {                                                                      //se ele estiver em alguma sala
                                boolean removido = salaAtual.removerUsuario(usuarioExpulsar);             //tenta remover ele da sala
                                if (removido) {                                                           //se conseguiu remover
                                    usuarioExpulsar.setSala(null);
                                    usuario.getOut().println("SERVIDOR: Você expulsou " + nomeExpulsar + " da sala.");      //informa o usuario
                                    usuarioExpulsar.getOut().println("SERVIDOR: Você foi expulso da sala por um administrador.");       //informa o usuarioExpulsar que ele foi removido da sala por um adm
                                    salaAtual.broadcast(nomeExpulsar + " foi expulso da sala pelo administrador.", usuarioExpulsar);      //informa todos os usuários da sala que usuarioExpulsar foi removido da sala
                                    System.out.println(nomeExpulsar + " foi expulso da sala " + salaAtual.getNome());
                                } else {
                                    usuario.getOut().println("SERVIDOR: Não foi possível expulsar o usuário da sala.");             //se não conseguiu expulsar avisa usuário
                                }
                            }
                        }
                    }else{                                                                              //se o usuario não for administrador
                        usuario.getOut().println("SERVIDOR: Somente ADMs podem expulsar outros usuários.");         //avisa que apenas adm podem expulsar da sala
                    }
                    return;
                }
            }
            usuario.getOut().println("SERVIDOR: Nome do usuário inválido.");                //se o nome do usuário for invalido
            return;
        }

        else if (comando.startsWith("/encerrar")) {                         //comando "/encerrar"
            if(comando.length() > 10){                                        //verifica nome da sala é válido
                nomeSala = comando.substring(10).trim();           //pega o nome da sala
                if(!nomeSala.isEmpty()){                                      //verifica se o nome da sala é válido
                    if (usuario.getStatusAdm()){                             //verifica se o usuário é adm
                        sala = gerenciaSalas.getSala(nomeSala);               // busca sala
                        if (sala != null) {
                            ArrayList<Usuario> usuariosNaSala = new ArrayList<>(sala.getUsuarios());  // faz cópia uma copia do arrayList dos usuarios
                            for (Usuario u : usuariosNaSala) {                 //para cada usuário
                                if (u.getOut() != null) {                       //verifica se a saída é válida
                                    u.getOut().println("SERVIDOR: A sala que voce estava foi encerrada por um ADM");        //informa que a sala foi encerrada
                                }
                                sala.removerUsuario(u);                          //remove o usuário da sala
                                u.setSala(null);                            //atualiza a referência a sala
                            }
                            gerenciaSalas.removerSala(nomeSala);                 // remove sala do gerenciador
                            usuario.getOut().println("SERVIDOR: Sala '" + nomeSala + "' foi encerrada com sucesso.");       //manda para o usuario que chamou a funcao encerrar
                        }else{                                                                          
                            usuario.getOut().println("SERVIDOR: Sala '" + nomeSala + "' não encontrada.");              //caso não encontre a sala informada
                        }
                    }else{                                                        //caso o usuário não seja administrador
                        usuario.getOut().println("SERVIDOR: Comando reservado para administradores.");          //informa que ele não pode realizar a operação
                    }
                    return;
                }
            }
            usuario.getOut().println("SERVIDOR: Nome de sala inválido");
            return;
        }

        else if(comando.startsWith("/sairServidor")){                //sairServidor

            sala = usuario.getSalaAtual();                             //pega a sala do usuario
            if(sala != null){                                          //se ele estiver em uma sala
                sala.removerUsuario(usuario);                          //remove ele da sala
                usuario.setSala(null);                            //coloca a referencia dele a sala como null
            }
            try{                                                       //tenta fechar as conexões
                usuario.getOut().println("SERVIDOR: Você escolheu sair do servidor. Até mais");     //informa o usuário que ele saiu do servidor
                
                if(usuario.getIn() != null){                                //se a entrada do usuário ainda estiver aberta
                    usuario.getIn().close();                                //encerra a entrada do usuario
                }                                    
                    
                if(usuario.getOut() != null){                               //se a saida do usuário ainda estiver aberta
                    usuario.getOut().close();                               //encerra a saida do usuario
                }
                if(usuario.getSocket() != null){                            //se a conexão com o usuário ainda estiver aberta
                    usuario.getSocket().close();                            //encerra a conexão com o usuario
                }                              
                return;

            }catch(IOException e){                                     //caso ocorra alguma exceção
                System.out.println("ERRO: um usuario nao conseguiu sair de servidor; " + e.getMessage());
            }
        }

        else if(comando.startsWith("/virarADM")){                       //comando "/virarADM"
            if(comando.length() > 10){                                         //verifica se a entrada pode ser válida
                String senha = comando.substring(10).trim();        //pega a senha
                if(!senha.isEmpty()){                                          //verifica se a entrada da senha é válida
                    if(usuario.getStatusAdm()){                                //se o usuário for administrador
                        usuario.getOut().println("SERVIDOR: Voce ja e um ADM");     //informa que ele já é administrador
                    }else{                                                      //se o usuário não for administrador
                        if(senha.equals("JAVA123")){                   //se a senha estiver correta
                            usuario.setStatusAdm(true);               //transforma ele em administrador
                            usuario.getOut().println("SERVIDO: agora voce e um administrador");      //informa o usuario que agora ele é um administrador
                            System.out.println(usuario.getNome() + "virou administrador");          //informa no terminal do servidor que o usuário virou administrador
                        }else{                                                  //caso a senha esteja incorreta
                            usuario.getOut().println("SERVIDOR: senha incorreta");         //informa o usuário que a senha está incorreta
                        }
                    }
                    return;
                }
            }
            usuario.getOut().println("SERVIDOR: entrada inválida");          //se caso a entrada for incorreta
            return;
        }

        else{
            usuario.getOut().println("SERVIDOR: Entrada incorreta");
            return;
        }




    }           //fecaha interpretaComando
}               //fecha a classe


