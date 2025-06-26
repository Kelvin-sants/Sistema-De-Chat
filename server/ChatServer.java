// ChatServer.java
package server;

import java.net.ServerSocket;
import java.net.Socket;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class ChatServer {
    private static final int PORT = 12345;
    private static ExecutorService pool = Executors.newFixedThreadPool(50);

    public static void main(String[] args) throws Exception {
        ServerSocket serverSocket = new ServerSocket(PORT);
        System.out.println("Servidor iniciado na porta " + PORT);

        while (true) {
            Socket clientSocket = serverSocket.accept();
            System.out.println("Novo cliente conectado");
            pool.execute(new ClientHandler(clientSocket));
        }
    }
}
// O que já está pronto?

// Cria um servidor que escuta na porta 12345.

// Aceita novas conexões de usuários.

// Para cada novo usuário, inicia uma nova thread ClientHandler.

// Isso já permite que várias pessoas se conectem ao servidor ao mesmo tempo.
// Falta: tratar o que esses usuários vão fazer (login, enviar mensagem etc.).