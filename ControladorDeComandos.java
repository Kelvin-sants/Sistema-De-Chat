import java.io.IOException;
import java.util.ArrayList;

public class ControladorDeComandos {
    // Atributo que guarda a referência ao objeto Usuario associado a este controlador
    private Usuario usuario;  

    // Construtor da classe
    public ControladorDeComandos(Usuario usuario) {         
        this.usuario = usuario;
    }

    // Método principal que interpreta e executa os comandos enviados pelo cliente
    public void interpretaComando(String comando, GerenciaUsuario gerenciaUsuarios, GerenciaSalas gerenciaSalas){
        
        String nomeSala = null; // Variável local para armazenar nome de sala
        Sala sala = null;       // Variável local para armazenar objeto Sala

        // Tratamento do comando login
        if(comando.startsWith("/login")){                   
            String nome = comando.substring(7); // Nome do usuário
            if(!nome.isEmpty()){
                this.usuario.setNome(nome);
                // Tenta adicionar o usuário à lista de usuários online do servidor
                if(gerenciaUsuarios.adicionarUsuario(usuario)){
                    usuario.getOut().println("SERVIDOR: Voce se conectou ao servidor");
                }else{
                    usuario.getOut().println("SERVIDOR: Nao foi possivel se conectar ao servidor, tente outro nome");
                }
            }else{
                usuario.getOut().println("SERVIDOR: Nome inválido");
            }
        }

        // Tratamento do comando /msg 
        // Permite ao usuário enviar uma mensagem para a sala em que está
        if(comando.startsWith("/msg")){                         
            String mensagem = comando.substring(5);  // Extrai mensagem
            if(!mensagem.isEmpty()){
                if(this.usuario != null){                        // Verifica se o usuario é valido
                    sala = this.usuario.getSalaAtual();          // Extrai sala do usuario
                    if(sala != null){                            // Verifica se a sala é valida
                        sala.broadcast(mensagem, usuario);       // A própria sala se encarrega do roteamento
                        //usuario.getOut().println("SERVIDOR: voce mandou na sala: " + mensagem);         //APAGAR ISSO - apenas para testes
                    }else{
                        usuario.getOut().println("SERVIDOR: Voce nao esta em uma sala");
                    }
                }
            }else{
                usuario.getOut().println("SERVIDOR: Mensagem inválida");
            }
        }

        // Tratamento do comando /criar
        if(comando.startsWith("/criar")){                    
            String nomeSalaCriar = comando.substring(7); // Extrai o nome da nova sala
            if(!nomeSalaCriar.isEmpty()){
                if(this.usuario != null){                           // Verifica se usuario é válido
                    if(this.usuario.getStatusAdm()){                                // Verifica se usuario é adm
                        // Tenta criar a sala usando o gerenciador de salas
                        if(gerenciaSalas.criarSala(nomeSalaCriar, this.usuario)){  
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

        // Tratamento do comando /salas
        // Lista todas as salas disponíveis no servidor
        if (comando.equals("/salas")){                  
            boolean flagListou = false; // Flag para verificar se alguma sala foi listada
            int i = 0; // Contador para enumerar as salas
            for(String nomesala: gerenciaSalas.listarSalas()){
                flagListou = true;
                usuario.getOut().println("SERVIDOR: " + i + " - " + nomesala);
                i++;
            }
            if(flagListou == false){ // Se nenhuma sala foi encontrada
                usuario.getOut().println("SERVIDOR: Nao ha salas disponiveis");
            }
        }

        // Tratamento do comando /entrar
        // Permite ao usuário entrar em uma sala existente
        if(comando.startsWith("/entrar")){                  
            nomeSala = comando.substring(8); // Extrai o nome da sala a ser acessada
            if(!nomeSala.isEmpty()){
                sala = gerenciaSalas.getSala(nomeSala); // Obtém a referência da sala pelo nome
                if(sala != null){                         
                    // Tenta adicionar o usuário à sala              
                    if(sala.adicionarUsuario(usuario)){             
                        usuario.getOut().println("SERVIDOR: voce entrou na sala " + nomeSala);
                        System.out.println(usuario.getNome() + "entrou na sala " + sala.getNome()); // Log no servidor
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
        
        // Tratamento do comando /sair
        // Permite ao usuário sair da sala atual
        if(comando.startsWith("/sair")){                          
            if(!comando.startsWith("/sairServidor")){
                sala = usuario.getSalaAtual(); // Obtém a sala atual
                if(sala != null && usuario != null){   // Verifica se a sala é válida
                    if(sala.removerUsuario(usuario)){    // Remove o usuario da sala
                        usuario.setSala(null);        //Remove a referencia de usuario a sala
                        usuario.getOut().println("SERVIDOR: Voce saiu da sala");
                    }else{
                        usuario.getOut().println("SERVIDOR: Nao foi possivel sair da sala");
                    }
                }else{
                    usuario.getOut().println("SERVIDOR: Sala nao encontrada");
                }
            }
        }

        // Tratamento do comando /expulsar 
        // Permite a administradores expulsar um usuário de uma sala
        if (comando.startsWith("/expulsar")) {                               
            if (usuario.getStatusAdm()) { // Verifica se o usuário que executou o comando é um administrador 
                String nomeExpulsar = comando.substring(9).trim(); // Extrai o nome do usuário a ser expulso 
                if (nomeExpulsar.isEmpty()) {
                    usuario.getOut().println("SERVIDOR: Nome do usuário inválido.");
                } else {
                    Usuario usuarioExpulsar = gerenciaUsuarios.getUsuarioPorNome(nomeExpulsar); // Busca o usuário pelo nome
                    if (usuarioExpulsar == null) {
                        usuario.getOut().println("SERVIDOR: Usuário '" + nomeExpulsar + "' não encontrado.");
                    } else {
                        Sala salaAtual = usuarioExpulsar.getSalaAtual(); // Obtém a sala do usuário a ser expulso
                        if (salaAtual == null) {
                            usuario.getOut().println("SERVIDOR: Usuário '" + nomeExpulsar + "' não está em nenhuma sala.");
                        } else {
                            boolean removido = salaAtual.removerUsuario(usuarioExpulsar); // Tenta remover o usuário da sala
                            if (removido) {
                                usuarioExpulsar.setSala(null); // Limpa a referência de sala no usuário expulso
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
        // Tratamento do comando /encerrar
        // Permite a administradores encerrar uma sala e desconectar todos os usuários dela
        if (comando.startsWith("/encerrar")) {                        
            nomeSala = comando.substring(10).trim(); // Extrai o nome da sala a ser encerrada
            if(!nomeSala.isEmpty()){
                if (usuario.getStatusAdm()) {                   // Verifica se o usuário é um administrador
                    sala = gerenciaSalas.getSala(nomeSala);     // Busca sala
                    if (sala != null) {
                        ArrayList<Usuario> usuariosNaSala = new ArrayList<>(sala.getUsuarios());  // Faz cópia uma copia do arrayList dos usuarios
                        for (Usuario u : usuariosNaSala) {
                            if (u.getOut() != null) {
                                u.getOut().println("SERVIDOR: A sala que voce estava foi encerrada por um ADM");
                            }
                            sala.removerUsuario(u);                       // Remove da sala
                            u.setSala(null);                              // Atualiza referência
                        }
                        gerenciaSalas.removerSala(nomeSala);              // Remove sala do gerenciador
                        usuario.getOut().println("SERVIDOR: Sala '" + nomeSala + "' foi encerrada com sucesso.");       // Manda para o usuario que chamou a funcao encerrar
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

        // Tratamento do comando /sairServidor 
        // Permite ao usuário desconectar-se completamente do servidor
        if(comando.startsWith("/sairServidor")){                

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

        // Tratamento do comando /virarADM 
        // Permite ao usuário se tornar administrador com uma senha secreta
        if(comando.startsWith("/virarADM")){
            String senha = comando.substring(10); // Extrai a senha
            if(!senha.isEmpty()){
                if(usuario.getStatusAdm()){ // Verifica se já é admin
                    usuario.getOut().println("SERVIDOR: Voce ja e um ADM");
                }else{
                    if(senha.equals("JAVA123")){ // Senha secreta para se tornar ADM
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


