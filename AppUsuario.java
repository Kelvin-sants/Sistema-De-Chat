//classe responsavel por executar o executar o App do cliente
//envia os comandos do usuário para ClienteHandler
//recebe as entradas enviadas do servidor pela classe auxiliar RecebimentoUsuario

import java.io.IOException;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.Scanner;

public class AppUsuario {
    
    private static final int PORTA = 12345;             //porta do servidor

    public static void main(String[] args){             //main
    
        Socket cliente = null;                          //declarando variáveis 
        PrintWriter saida = null;
        String comando;
        Scanner teclado = null;
        RecebimentoUsuario entrada;
        Thread SistemaEntrada = null;

        try{    
            cliente = new Socket("localhost",PORTA);                                //conecta ao servidor
            System.out.println("Você se conectou aos servidor de chat\nBem Vindo(a)!");         

            teclado = new Scanner(System.in);                                            //instancia um scanner para ler o teclado

            saida = new PrintWriter(cliente.getOutputStream(), true);         //Cria um objeto PrintStream que envia mensagens do cliente para o servidor pela conexão do Socket
            
            entrada = new RecebimentoUsuario(cliente);                                  //instancia um objeto RecebimentoUsuario para assumir a funcao de entrada de dados
            SistemaEntrada = new Thread(entrada);                                       //cria uma thread para ficar receber as mensagens em paralelo
            SistemaEntrada.start();                                                     //inicia a thread responsavel pela entrada de dados

            comando = null;

            while(true){                        
                comando = teclado.nextLine();                                           //espera algo ser escrito pelo teclado
                saida.println(comando);                                                 //manda o que foi escrito para o servidor

                if(comando != null){
                    if(comando.startsWith("/sairServidor")){                       //se o usuario digitou "/sairServidor"
                        System.out.println("SERVIDOR: Você escolheu sair do servidor");
                        break;                                                          //sai do laço
                    }
                } 
            }

        }catch(IOException e){                                                         //em caso de exceção executa esse bloco de código 
            System.err.println("SERVIDOR: Não foi possível se conectar ao servidor; ERRO: " + e.getMessage());

        }finally{                                                                      //após a execução do try ou de catch, executa esse bloco de código
            try{                                                                       //tenta executar o finally
                if(teclado != null){                                                   //se o teclado não foi fechado
                    teclado.close();                                                   //fecha o teclado
                }       
                if(saida != null){                                                     //se a saida não foi fechada
                    saida.close();                                                     //fecha a saida
                }
                if(SistemaEntrada != null){
                    SistemaEntrada.interrupt();
                }
                if (cliente != null){
                    cliente.close(); // Fecha o socket explicitamente
                }
            }catch(IOException e){                                                    //caso o finally lance alguma exceção
                System.out.println("Erro ao fechar entrada e/ou socketCliente");
            }

            System.out.println("CONEXÃO ENCERRADA");                                //informa o usuário que a conexão foi encerrada
        }
    }
}
