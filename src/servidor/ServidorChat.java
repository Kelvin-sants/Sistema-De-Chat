package servidor;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;


public class ServidorChat {

    private static final int PORTA = 12345;             //porta do servidor

    public static void main(String[] args){

        GerenciaUsuario GerenciaUsuarios = new GerenciaUsuario();              //Instancia Gerencia usuarios - 1 por servidor
        GerenciaSalas GerenciaSalas = new GerenciaSalas();                      //Instacia Gerencia Salas - 1 por servidor
        Socket socketCliente = null;
        ServerSocket servidor = null;
        
        try{                                                                    //tenta executar esse bloco de código
            
            servidor = new ServerSocket(PORTA);                                                     //tenta abrir o servidor na porta especifica
            System.out.println("Servidor Iniciado com sucesso na porta:" + PORTA);                  //avisa no terminal do servidor que foi possivel criar o servidor
            
            while(true){
                
                socketCliente = servidor.accept();                                                          //espera o usuario conectar no servidor
                System.out.println("Novo cliente conectado, ID: " + socketCliente.getInetAddress());        //informa no servidor que um novo cliente se conectou
                Usuario usuario = new Usuario(socketCliente);                                               //cria uma instancia do usuario no servidor
                ClientHandler handler = new ClientHandler(usuario, GerenciaUsuarios, GerenciaSalas);        //cria uma nova ClientHandler para aquele cliente
                Thread  threadHandler = new Thread(handler);                                                //colocando handler em uma thread
                threadHandler.start();                                                                      // Inicia a thread (vai executar o run() do ClienteHandler)
            }

        }catch(IOException e){                                                                              //em caso de exceção
            System.err.println("Erro no servidor: " + e.getMessage());                                      //avisa no terminal do servidor

        }finally{                                                                                           //por ultimo executa esse bloco de código
            try{
                if(socketCliente != null){                                                                  //se o socketCliente ainda estiver aberto
                    socketCliente.close();                                                                  //fecha o socketCliente
                }   
                if(servidor != null){                                                                       //se o servidor ainda estiver aberto
                    servidor.close();;                                                                      //fecha o servidor
                }
                        
                System.out.println("SERVIDOR FOI DESLIGADO");                                                  //informa no terminal do servidor que ele foi desligado

            }catch(IOException e){                                                                    //em caso de exceção
                System.out.println("Houve um erro ao tentar encerrar o servidor");                  //informa no terminal do  servidor que não foi possivel encerrar
            }
        }

    }
}
