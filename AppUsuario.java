import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Scanner;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class AppUsuario {
    
    private static final int PORTA = 12345;             //porta do servidor

    public static void main(String[] args){
    
        Socket cliente = null;
        Scanner teclado = null;
        PrintStream saida = null;
        String comando;
        BufferedReader entrada = null;

        try{    
            cliente = new Socket("localhost",PORTA);                                //conecta ao servidor
            System.out.println("Voce se conectou aos servidor de chat\nBem Vindo(a)!");
            teclado = new Scanner(System.in);
            saida = new PrintStream(cliente.getOutputStream());                             //Cria um objeto PrintStream que envia mensagens do cliente para o servidor pela conexão do Socket
            entrada = new BufferedReader(new InputStreamReader(cliente.getInputStream()));

            while(teclado.hasNextLine()){                                   //verifica se algo foi digitado

                comando = teclado.nextLine();
                saida.println(comando);                                    //envia o que foi digitado para o servidor

                if(entrada.ready()){                                        //verifica se chegou alguma mensagem para o cliente mas sem bloquear a execução
                    System.out.print(entrada.readLine());
                }

                if(comando.equals("/sair")){                       //se o usuario digitou "/sair"
                    break;                                                  //sai do laço
                }
                
            }

        }catch(IOException e){
            System.err.println("Nao foi possivel se conectar ao servidor; ERRO: " + e.getMessage());

        }finally{

            try{

                if(teclado != null){
                    teclado.close();
                }
                if(saida != null){
                    saida.close();
                }
        
                if(entrada != null){
                     entrada.close();
                }

                if (cliente != null){
                    cliente.close(); // Fecha o socket explicitamente
                }

            }catch(IOException e){
                System.out.println("Erro ao fechar entrada e/ou socketCliente");
            }
        }
    }
}
