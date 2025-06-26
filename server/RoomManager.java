// RoomManager.java
package server;

import java.util.*;

public class RoomManager {
    private static Map<String, List<User>> salas = new HashMap<>();

    public static synchronized boolean criarSala(String nomeSala) {
        if (!salas.containsKey(nomeSala)) {
            salas.put(nomeSala, new ArrayList<>());
            return true;
        }
        return false;
    }

    public static synchronized void removerSala(String nomeSala) {
        salas.remove(nomeSala);
    }

    public static synchronized List<String> listarSalas() {
        return new ArrayList<>(salas.keySet());
    }

    public static synchronized void entrarSala(String nomeSala, User user) {
        salas.get(nomeSala).add(user);
        user.setSalaAtual(nomeSala);
    }

    public static synchronized void sairSala(User user) {
        String sala = user.getSalaAtual();
        if (sala != null && salas.containsKey(sala)) {
            salas.get(sala).remove(user);
        }
        user.setSalaAtual(null);
    }

    public static synchronized void enviarMensagem(User sender, String mensagem) {
        String sala = sender.getSalaAtual();
        if (sala == null) return;
        for (User u : salas.get(sala)) {
            if (!u.equals(sender)) {
                u.sendMessage("[" + sender.getNome() + "]: " + mensagem);
            }
        }
    }
}

// O que já está pronto?

// Uma lista de salas (Map<String, List<User>>).

// Métodos para:

// Criar sala

// Apagar sala

// Listar salas

// Entrar/sair de uma sala

// Enviar mensagem para todos de uma sala

// A lógica central de salas está pronta.
// Falta: amarrar isso com os comandos que o cliente envia (isso é feito no ClientHandler).