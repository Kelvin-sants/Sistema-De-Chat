import java.io.IOException;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.Scanner;
import java.util.*;


public class AppUsuario {
    
    private static final int PORTA = 12345;             //porta do servidor

    public static void main(String[] args){
    
        Socket cliente = null;
        PrintWriter saida = null;
        String comando;
        Scanner teclado = null;
        RecebimentoUsuario entrada;
        Thread SistemaEntrada = null;

        try{    
            cliente = new Socket("localhost",PORTA);                                //conecta ao servidor
            System.out.println("Você se conectou aos servidor de chat\nBem Vindo(a)!");

            teclado = new Scanner(System.in);

            saida = new PrintWriter(cliente.getOutputStream(), true);                             //Cria um objeto PrintStream que envia mensagens do cliente para o servidor pela conexão do Socket
            
            entrada = new RecebimentoUsuario(cliente);                      //cria uma thread para ficar recebendo as mensagens em paralelo
            SistemaEntrada = new Thread(entrada);
            SistemaEntrada.start();

            comando = null;
        

            while(true){                        
                comando = teclado.nextLine();                   //espera algo ser escrito
                saida.println(comando);                         //manda o que foi escrito para o servidor

                if(comando != null){
                    if(comando.equals("/sairServidor")){                       //se o usuario digitou "/sair"
                        System.out.println("SERVIDOR: Você escolheu sair do servidor");
                        break;                                                  //sai do laço
                    }
                } 
            }

        }catch(IOException e){
            System.err.println("SERVIDOR: Não foi possível se conectar ao servidor; ERRO: " + e.getMessage());

        }finally{
            try{
                if(teclado != null){
                    teclado.close();
                }
                if(saida != null){
                    saida.close();
                }
                if(SistemaEntrada != null){
                    SistemaEntrada.interrupt();
                }
                if (cliente != null){
                    cliente.close(); // Fecha o socket explicitamente
                }
            }catch(IOException e){
                System.out.println("Erro ao fechar entrada e/ou socketCliente");
            }

            System.out.println("CONEXÃO ENCERRADA");
        }
    }
}
