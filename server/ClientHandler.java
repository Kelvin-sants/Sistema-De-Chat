// ClientHandler.java
package server;

import java.io.*;
import java.net.Socket;

public class ClientHandler implements Runnable {
    private Socket socket;
    private User user;

    public ClientHandler(Socket socket) {
        this.socket = socket;
    }

    @Override
    public void run() {
        // Aqui irá o loop de leitura dos comandos do cliente
        // e a execução das ações (login, entrar em sala, enviar mensagem, etc.)
    }
}

// O que já está pronto?

// Cria uma estrutura para lidar com cada cliente.

// Recebe o socket (canal de comunicação).

// Deixa espaço para colocar o código que vai processar comandos como /login, /entrar, /msg, etc.

// Isso já permite que cada usuário tenha sua “thread” (filho do servidor).
// Falta: interpretar comandos e interagir com as salas e o usuário.