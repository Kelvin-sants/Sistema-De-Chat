import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.Socket;
import java.util.*;


public class RecebimentoUsuario implements Runnable {
    
    BufferedReader entrada;
    Socket cliente;

    public RecebimentoUsuario(Socket cliente){
        this.entrada = null;
        this.cliente = cliente;
    }

    @Override
    public void run(){
        try{

            entrada = new BufferedReader(new InputStreamReader(cliente.getInputStream()));
            String linha;
            System.out.println("SERVIDOR: ESTA RECEBENDO DO SERVIDOR");             //APAGAR ISSO - APENAS PARA TESTES
            while (true) {
                linha = entrada.readLine();
                if(linha != null){
                    System.out.println(linha);
                }
            }

        }catch(IOException e){
            System.out.println("ERRO: nao foi possivel ler o servidor; ERRO: " + e.getMessage());

        }finally{
                        //-------------------------FALTA COLOCAR AQUI------------------------//
        }

    }
}
