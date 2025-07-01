import java.io.*;
import java.net.Socket;
import java.util.Scanner;

public class Usuario {

    private String nome;
    private boolean ehAdm;
    private Sala salaAtual;

    private Socket socket;
    private PrintStream out;
    private Scanner in;

    public Usuario(Socket socket) throws IOException {                          //metodo construtor
        this.socket = socket;
        this.out = new PrintStream(socket.getOutputStream(), true);
        this.in = new Scanner(new InputStreamReader(socket.getInputStream()));
        this.ehAdm = false;
        this.salaAtual = null;
    }

    // --- Métodos de comunicação ---
    public void enviarMensagem(String msg) {
        out.println(msg);
    }

    public String lerMensagem() throws IOException {
        return in.nextLine();
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

    public PrintStream getOut() {
        return this.out;
    }

    public Scanner getIn() {
        return this.in;
    }
}
