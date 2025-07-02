import java.io.IOException;
import java.util.ArrayList;

public class ControladorDeComandos {
    private Usuario usuario;  

    public ControladorDeComandos(Usuario usuario) {         //construtor
        this.usuario = usuario;
    }

    public void interpretaComando(String comando, GerenciaUsuario gerenciaUsuarios, GerenciaSalas gerenciaSalas){
        
        String nomeSala = null;                 //declarando variavel local
        Sala sala = null;


        if(comando.startsWith("/login")){                   //login
            String nome = comando.substring(7);
            if(!nome.isEmpty()){
                this.usuario.setNome(nome);
                if(gerenciaUsuarios.adicionarUsuario(usuario)){
                    usuario.getOut().println("SERVIDOR: Voce se conectou ao servidor");
                }else{
                    usuario.getOut().println("SERVIDOR: Nao foi possivel se conectar ao servidor, tente outro nome");
                }
            }else{
                usuario.getOut().println("SERVIDOR: Nome inválido");
            }
        }

        if(comando.startsWith("/msg")){                         //msg
            String mensagem = comando.substring(5);         //pega a mensagem
            if(!mensagem.isEmpty()){
                if(this.usuario != null){                                  //verifica se o usuario é valido
                    sala = this.usuario.getSalaAtual();                    //pega a sala que o usuario esta
                    if(sala != null){                                      //verifica se a sala for valida
                        sala.broadcast(mensagem, usuario);
                        //usuario.getOut().println("SERVIDOR: voce mandou na sala: " + mensagem);         //APAGAR ISSO - apenas para testes
                    }else{
                        usuario.getOut().println("SERVIDOR: Voce nao esta em uma sala");
                    }
                }
            }else{
                usuario.getOut().println("SERVIDOR: Mensagem inválida");
            }
        }

        if(comando.startsWith("/criar")){                           //criar
            String nomeSalaCriar = comando.substring(7);
            if(!nomeSalaCriar.isEmpty()){
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
                }
            }else{
                usuario.getOut().println("SERVIDOR: Somente administradores podem criar salas");
            }
        }

        if (comando.equals("/salas")){                  //salas
            boolean flagListou = false;
            int i = 0; 
            for(String nomesala: gerenciaSalas.listarSalas()){
                flagListou = true;
                usuario.getOut().println("SERVIDOR: " + i + " - " + nomesala);
                i++;
            }
            if(flagListou == false){
                usuario.getOut().println("SERVIDOR: Nao ha salas disponiveis");
            }
        }

        if(comando.startsWith("/entrar")){                  //entrar
            nomeSala = comando.substring(8);            //pega o nome da sala
            if(!nomeSala.isEmpty()){
                sala = gerenciaSalas.getSala(nomeSala);
                if(sala != null){                                       //se a sala é valida
                    if(sala.adicionarUsuario(usuario)){                  //adiciona usuario
                        usuario.getOut().println("SERVIDOR: voce entrou na sala " + nomeSala);
                        System.out.println(usuario.getNome() + "entrou na sala " + sala.getNome());
                    }else{
                        usuario.getOut().println("SERVIDOR: nao foi possivel entrar na sala");
                    }
                }else{
                    usuario.getOut().println("SERVIDOR: Sala nao encontrada");
                }
            }else{
                usuario.getOut().println("SERVIDOR: Nome de sala inválido");
            }
        }
        
        if(comando.startsWith("/sair")){                          //sair
            if(!comando.startsWith("/sairServidor")){
                sala = usuario.getSalaAtual();                              //pega a sala atual
                if(sala != null && usuario != null){                        //verifica se a sala é válida
                    if(sala.removerUsuario(usuario)){                       //remove o usuario da sala
                        usuario.setSala(null);                          //remove a referencia de usuario a sala
                        usuario.getOut().println("SERVIDOR: Voce saiu da sala");
                    }else{
                        usuario.getOut().println("SERVIDOR: Nao foi possivel sair da sala");
                    }
                }else{
                    usuario.getOut().println("SERVIDOR: Sala nao encontrada");
                }
            }
        }

        if (comando.startsWith("/expulsar")) {                               // comando: /expulsar nomeUsuario
            if (usuario.getStatusAdm()) {                                    // verifica se é ADM
                String nomeExpulsar = comando.substring(9).trim();
                if (nomeExpulsar.isEmpty()) {
                    usuario.getOut().println("SERVIDOR: Nome do usuário inválido.");
                } else {
                    Usuario usuarioExpulsar = gerenciaUsuarios.getUsuarioPorNome(nomeExpulsar);
                    if (usuarioExpulsar == null) {
                        usuario.getOut().println("SERVIDOR: Usuário '" + nomeExpulsar + "' não encontrado.");
                    } else {
                        Sala salaAtual = usuarioExpulsar.getSalaAtual();
                        if (salaAtual == null) {
                            usuario.getOut().println("SERVIDOR: Usuário '" + nomeExpulsar + "' não está em nenhuma sala.");
                        } else {
                            boolean removido = salaAtual.removerUsuario(usuarioExpulsar);
                            if (removido) {
                                usuarioExpulsar.setSala(null);
                                usuario.getOut().println("SERVIDOR: Você expulsou " + nomeExpulsar + " da sala.");
                                usuarioExpulsar.getOut().println("SERVIDOR: Você foi expulso da sala por um administrador.");
                                salaAtual.broadcast(nomeExpulsar + " foi expulso da sala pelo administrador.", usuarioExpulsar);
                            } else {
                                usuario.getOut().println("SERVIDOR: Não foi possível expulsar o usuário da sala.");
                            }
                        }
                    }
                }
            } else {
                usuario.getOut().println("SERVIDOR: Somente ADMs podem expulsar outros usuários.");
            }
        }

        if (comando.startsWith("/encerrar")) {                        // /encerrar
            nomeSala = comando.substring(10).trim();          // extrai nome da sala
            if(!nomeSala.isEmpty()){
                if (usuario.getStatusAdm()) {                             // se for ADM
                    sala = gerenciaSalas.getSala(nomeSala);               // busca sala
                    if (sala != null) {
                        ArrayList<Usuario> usuariosNaSala = new ArrayList<>(sala.getUsuarios());  // faz cópia uma copia do arrayList dos usuarios
                        for (Usuario u : usuariosNaSala) {
                            if (u.getOut() != null) {
                                u.getOut().println("SERVIDOR: A sala que voce estava foi encerrada por um ADM");
                            }
                            sala.removerUsuario(u);                       // remove da sala
                            u.setSala(null);                              // atualiza referência
                        }
                        gerenciaSalas.removerSala(nomeSala);              // remove sala do gerenciador
                        usuario.getOut().println("SERVIDOR: Sala '" + nomeSala + "' foi encerrada com sucesso.");       //manda para o usuario que chamou a funcao encerrar
                    } else {
                        usuario.getOut().println("SERVIDOR: Sala '" + nomeSala + "' nao encontrada.");
                    }
                } else {
                    usuario.getOut().println("SERVIDOR: Comando reservado para administradores.");
                }
            }else{
                usuario.getOut().println("SERVIDOR: Nome de sala inválido");
            }
        }


        if(comando.startsWith("/sairServidor")){                //sairServidor

            nomeSala = usuario.getSalaAtual().getNome();               //pega o nome da sala do usuario
            if(sala != null){                                          //se ele estiver em uma sala
                sala = gerenciaSalas.getSala(nomeSala);                //pega a sala que ele esta
                sala.removerUsuario(usuario);                          //remove ele da sala
                usuario.setSala(null);
            }
            try{
                usuario.getOut().println("SERVIDOR: Voce escolheu sair do servidor. Ate mais");
                usuario.getIn().close();                                    //encerra a entrada do usuario
                usuario.getOut().close();                                   //encerra a saida do usuario
                usuario.getSocket().close();                                //encerra a conexão com o usuario

            }catch(IOException e){
                System.out.println("ERRO: um usuario nao conseguiu sair de servidor; " + e.getMessage());
            }
        }

        if(comando.startsWith("/virarADM")){
            String senha = comando.substring(10);
            if(!senha.isEmpty()){
                if(usuario.getStatusAdm()){
                    usuario.getOut().println("SERVIDOR: Voce ja e um ADM");
                }else{
                    if(senha.equals("JAVA123")){
                        usuario.setStatusAdm(true);
                        usuario.getOut().println("SERVIDO: agora voce e um ADM");
                    }else{
                        usuario.getOut().println("SERVIDOR: senha incorreta");
                    }
                }
            }else{
                usuario.getOut().println("SERVIDOR: Senha inválida");
            }
        }
    }
}


