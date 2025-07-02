package cliente;

//classe auxilar para receber mensagens enviadas pelo servidor e imprimir no terminal
//É uma classe Runnable para ser usada juntamente com um thread; o objetivo é executar em paralelo ao envio de mensagens

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.Socket;

public class RecebimentoUsuario implements Runnable {           
    
    BufferedReader entrada;                             //atributos
    Socket cliente;

    public RecebimentoUsuario(Socket cliente){          //metodo construtor
        this.entrada = null;
        this.cliente = cliente;
    }

    @Override
    public void run(){                                              //parte que vai ser executada em paralelo quando chamar thread
        try{                                                        //tenta executar esse bloco de código

            entrada = new BufferedReader(new InputStreamReader(cliente.getInputStream()));          //instancia um BufferedReader responsável por receber as mensagens
            String linha;
            System.out.println("SERVIDOR: Conexão de entrada de dados efetivada");                //informa o usuário que a conexão de entrada ocorreu com sucesso    
            while (true) {                                                                          //emquanto verdadeiro
                linha = entrada.readLine();                                                         //lê a linha que chegou do servidor
                if(linha != null){                                                                  //se a linha for válida
                    System.out.println(linha);                                                      //imprime no terminal do usuário
                }
            }

        }catch(IOException e){                                                                      //em caso de exceção
            System.out.println("ERRO: nao foi possivel ler o servidor; ERRO: " + e.getMessage());       //avisa o usuário

        }finally{                                                                                   //por ultimo
            try{                                                                                    //tenta executar esse bloco de código
                if(entrada != null){                                                                //caso a entrada ainda esteja aberta
                    entrada.close();                                                                //fecha a entrada
                }
                System.out.println("SERVIDOR: VocÊ parou de escutar o servidor");                 //avisa o usuário que ele parou de escutar o servidor

            }catch(IOException e){                                                                  //em caso de exceção
                System.out.println("SERVIDOR: Erro ao desconectar a entrada de dados; ERRO: " + e.getMessage());     //informa o usuário que não conseguiu desconectar a entrada
            }
        }
    }
}
