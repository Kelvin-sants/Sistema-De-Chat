import java.io.BufferedReader;
import java.io.IOException;
import java.io.PrintStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Scanner;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class ClientHandler implements Runnable{
    
    private Usuario usuario;
    private GerenciaSalas gerenciaSalas;
    private GerenciaUsuario gerenciaUsuarios;
    private ControladorDeComandos controladorDeComandos;
    


    public ClientHandler(Usuario usuario, GerenciaUsuario gerenciaUsuarios, GerenciaSalas gerenciaSalas){             //metodo construtor

        this.usuario = usuario;
        this.gerenciaSalas = gerenciaSalas;
        this.gerenciaUsuarios = gerenciaUsuarios;
        this.controladorDeComandos = new ControladorDeComandos(usuario, gerenciaSalas, gerenciaUsuarios);
    }
    
    Scanner entrada = null;
    PrintStream saida = null;
    String comando;

    @Override
    public void run(){                          //parte que vai ser rodada em paralelo quando chamar thread


        try{
            saida = usuario.getOut();
            entrada = usuario.getIn();

            while(entrada.hasNextLine()){                               //lendo os comandos enviados pelo cliente

                comando = entrada.nextLine();
                controladorDeComandos.interpretaComando(comando, gerenciaUsuarios, gerenciaSalas);        //interpretando o comando

                if(comando.equals("/sair")){                        //se o usuario enviou "/sair" para de ler
                    break;
                }
            }

       }catch(Exception e){                                                               //tratando erros
            System.out.println("Erro na comunicacao com o cliente: " + e.getMessage());

       }finally{                                                                           //executa apos parar de ler ou depois da exceção 
        
            try{
                if(entrada != null){
                    entrada.close();
                }
                if(saida != null){
                    saida.close();
                }
    
                if (usuario.getSocket() != null && !usuario.getSocket().isClosed()) {
                    usuario.getSocket().close();                                                // Fecha o socket explicitamente
                }
            }catch(IOException e){
                System.out.println("Erro ao fechar entrada e/ou socketCliente");
            }
       }

    }       //fim metodo run

}           //fim classe ClienteHandler
