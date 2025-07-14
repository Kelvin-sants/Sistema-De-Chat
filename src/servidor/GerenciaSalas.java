package servidor;

import java.util.HashMap;
import java.util.List;
import java.util.ArrayList;
import java.util.Map;

public class GerenciaSalas {
    private Map<String, Sala> salasAtivas;          // mapa que armazena todas as salas ativas no servidor. A chave é o nome da sala.

    // Construtor
    public GerenciaSalas() {
        this.salasAtivas = new HashMap<>();
    }

    // Método para criar uma nova sala
    public boolean criarSala(String nomeSala, Usuario usuario) {
        if (salasAtivas.containsKey(nomeSala)) {
            return false;                           // sala já existe
        } else {
            Sala novaSala = new Sala(nomeSala);     // cria nova instância
            salasAtivas.put(nomeSala, novaSala);    // adiciona ao mapa
            return true;                            
        }
    }

    // Método para remover uma sala existente
    public boolean removerSala(String nomeSala) {
        if (!salasAtivas.containsKey(nomeSala)) {
            return false;                           // sala não existe
        } else {
            salasAtivas.remove(nomeSala);           // remove a sala do mapa
            return true;                            
        }
    }

    // Método para listar todas as salas disponíveis
    public List<String> listarSalas() {
        return new ArrayList<>(salasAtivas.keySet()); // cria uma nova lista contendo os elementos do Set retornado por keySet()
    }

    // Método para obter uma sala pelo nome
    public Sala getSala(String nomeSala) {
        return salasAtivas.get(nomeSala);              // Retorna a referência da sala ou null caso ela não exista
    }

    
    // Métodos auxiliares

    // Verifica se há usuários
    public boolean estaVazia(String nomeSala) {
        Sala sala = salasAtivas.get(nomeSala);
        return sala != null && sala.getUsuarios().isEmpty();
    }

    // Remove o usuário fornecido de todas as salas em que ele possa estar
    public void removerUsuarioDeTodasAsSalas(Usuario u) {
        for (Sala sala : salasAtivas.values()) {
            sala.removerUsuario(u); // método seguro com verificação interna
        }
    }

    // Retorna a lista de usuários de uma sala específica
    public List<Usuario> listarUsuariosDaSala(String nomeSala) {
        Sala sala = salasAtivas.get(nomeSala);
        if (sala != null) {
            return sala.getUsuarios();
        }
        return new ArrayList<>();
    }



}
