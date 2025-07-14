package servidor;

//classe responsavel por tratar individualmente cada cliente conectado ao servidor
//recebe um comando enviado pelo cliente e usa a classe ControladorDeComandos para executar a ação necessária

import java.io.BufferedReader;
import java.io.IOException;
import java.io.PrintWriter;

public class ClientHandler implements Runnable{
    
    private Usuario usuario;                                            //atributos da classe ClienteHandler
    private GerenciaSalas gerenciaSalas;
    private GerenciaUsuario gerenciaUsuarios;
    private ControladorDeComandos controladorDeComandos;
    


    public ClientHandler(Usuario usuario, GerenciaUsuario gerenciaUsuarios, GerenciaSalas gerenciaSalas){             //metodo construtor

        this.usuario = usuario;
        this.gerenciaSalas = gerenciaSalas;
        this.gerenciaUsuarios = gerenciaUsuarios;
        this.controladorDeComandos = new ControladorDeComandos(usuario);
    }
    
    BufferedReader entrada = null;                                          //declarando variaveis
    PrintWriter saida = null;
    String comando;

    @Override
    public void run(){                                                          //parte que vai ser executada em paralelo quando chamar thread

        try{                                                                    //tenta executar esse bloco de código
            saida = usuario.getOut();                                           //saida será o canal out do socket
            entrada = usuario.getIn();                                          //entrada será o canal in do socket

            while(true){         
                comando = entrada.readLine();                                   //lendo os comandos enviados pelo cliente
                if(comando != null){                                                                          //se tiver algum comando diferente de null
                    controladorDeComandos.interpretaComando(comando, gerenciaUsuarios, gerenciaSalas);        //interpreta o comando
                    if(comando.equals("/sairServidor")){                   //se o usuario enviou "/sairServidor" encerra o while
                        break;
                    }
                }
            }

       }catch(Exception e){                                                               //em caso de exceção
            System.out.println("Erro na comunicacao com o cliente: " +usuario.getNome() +" ERRO: " + e.getMessage());       //informa o erro no terminal do servidor

       }finally{                                                                           //executa após parar de ler ou depois da exceção 
        
            try{
                if(entrada != null){                                                    //se a entrada não tiver fechada
                    entrada.close();                                                    //fecha a entrada
                }
                if(saida != null){                                                      //se a saida não tiver fechada
                    saida.close();                                                      //fecha a saida
                }
                if (usuario.getSocket() != null && !usuario.getSocket().isClosed()) {       //se o socket não estiver fechado
                    usuario.getSocket().close();                                            // Fecha o socket explicitamente
                }

            }catch(IOException e){                                                          //em caso de exceção ao tentar encerrar o socket
                System.out.println("Erro ao fechar entrada e/ou socketCliente");        //informa o erro no terminal do servidor
            }
       }

    }

}   
