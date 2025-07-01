import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Scanner;


public class ServidorChat {

    private static final int PORTA = 12345;             //porta do servidor

    public static void main(String[] args){

        GerenciaUsuarios GerenciaUsuarios = new GerenciaUsuarios();              //Instancia Gerencia usuarios
        GerenciaSalas GerenciaSalas = new GerenciaSalas();                      //Instacia Gerencia Salas
        Socket socketCliente;
        Scanner entrada = null;
        
        try(ServerSocket servidor = new ServerSocket(PORTA)){                        //tenta abrir o servidor na porta especifica
            
            System.out.println("Servidor Iniciado com sucesso na porta:" + PORTA);
            
            while(true){
                
                socketCliente = servidor.accept();                                                          //espera o usuario conectar no servidor
                System.out.println("Novo cliente conectado, ID: " + socketCliente.getInetAddress());            //informa no servidor que um novo cliente se conectou
                Usuario usuario = new Usuario(socketCliente);
                ClientHandler handler = new ClientHandler(usuario, GerenciaUsuarios, GerenciaSalas);      //cria uma nova ClientHandler para aquele cliente
                Thread  threadHandler = new Thread(handler);                        //colocando handler em uma thread
                threadHandler.start();                                            // Inicia a thread (vai executar o run() do handler)
            }

        }catch(IOException e){

            System.err.println("Erro no servidor: " + e.getMessage());

        }finally{

            if(entrada != null){
                entrada.close();
            }
            
        }

    }
}
