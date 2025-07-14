package servidor;

// Importações 
import java.io.*;
import java.net.Socket;

// Classe que representa um usuário conectado ao chat
public class Usuario {

    private String nome;
    private boolean ehAdm; // Define se o usuário tem privilégios de administrador
    private Sala salaAtual; // Sala atual do usuário

    private Socket socket;  // Conexão com o cliente
    private PrintWriter out;    // Canal de saída (envio de mensagens para o cliente)
    private BufferedReader in;  // Canal de entrada (recebimento de mensagens do cliente)

    // Método Construtor
    public Usuario(Socket socket) throws IOException {                          //metodo construtor
        this.socket = socket;   // Inicializa o socket do usuário com o socket da conexão
        this.out = new PrintWriter(socket.getOutputStream(), true); // Auto-flush ativado
        this.in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
        this.ehAdm = false; // Por padão, usuário não é admin
        this.salaAtual = null;  // Por padrão, usuário não está em nenhuma sala
    }

    // --- Métodos de comunicação ---
    public void enviarMensagem(String msg) {
        out.println(msg);
    }

    public String lerMensagem() throws IOException {
        return in.readLine();
    }

    // --- Getters e Setters ---
    public void setNome(String nome) {
        this.nome = nome;
    }

    public void setStatusAdm(boolean statusAdm) {
        this.ehAdm = statusAdm;
    }

    public void setSala(Sala sala) {
        this.salaAtual = sala;
    }

    public String getNome() {
        return this.nome;
    }

    public boolean getStatusAdm() {
        return this.ehAdm;
    }

    public Sala getSalaAtual() {
        return this.salaAtual;
    }

    public Socket getSocket() {
        return this.socket;
    }

    public PrintWriter getOut() {
        return this.out;
    }

    public BufferedReader getIn() {
        return this.in;
    }
}
