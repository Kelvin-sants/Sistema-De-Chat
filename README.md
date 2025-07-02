# Sistema de Chat em Java

Este projeto é um **sistema de chat em Java**, desenvolvido com foco em comunicação cliente-servidor via sockets. Ele permite a troca de mensagens entre múltiplos clientes conectados simultaneamente a um servidor central.

##  Descrição

O sistema foi construído como um exercício acadêmico para praticar conceitos de:
- Programação em Java
- Comunicação via TCP/IP
- Threads para gerenciamento de múltiplos clientes
- Programação orientada a objetos (POO)

## Visão Geral do Código

O projeto é dividido em dois principais componentes:

- **Servidor**: Responsável por receber conexões de clientes, gerenciar os usuários ativos e distribuir mensagens.
- **Cliente**: Aplicação que permite a um usuário se conectar ao servidor, enviar mensagens e receber mensagens de outros usuários.

### Principais funcionalidades:

- Envio e recebimento de mensagens em tempo real
- Múltiplos clientes conectados simultaneamente
- Interface de linha de comando simples

#### Comandos: 
```/login nome```: entra no servidor com o nome do cliente

```\salas```: mostra as salas disponíveis

```\entrar sala1``` : entra na sala1

```\sair salaAtual``` : sai da sala atual

```\msg Olá``` : manda mensagem na sala atual

```\criar sala1``` : cria a sala1(somente ADM)

```\expulsar nome``` : expulsa o usuário (somente ADM)

```\encerrar sala1```: encerra a sala (somente ADM)

```\sairServidor```: fecha o servidor

```\virarADM senha``` : vira ADM se a senha (JAVA123) estiver correta


## Estrutura do Projeto
ServidorChat.java: O ponto de entrada central para todos os clientes. Ele inicia o servidor, ouve em uma porta específica, aceita as conexões de entrada dos clientes e delega a comunicação subsequente a instâncias de ClientHandler.

```ClientHandler.java```: Criado para cada cliente conectado, é responsável por gerenciar a comunicação individual com um cliente específico, processando suas mensagens e enviando respostas 

```AppUsuario.java```: Representa a aplicação do lado do cliente que o usuário final utiliza para se conectar ao servidor e interagir com o sistema de chat 

```GerenciaSalas.java```: Um módulo dedicado ao gerenciamento das salas de chat, incluindo a criação, adição e remoção de usuários de salas específicas

```Sala.java```: Representa uma sala de chat individual, contendo informações sobre os usuários presentes nela

```GerenciaUsuario.java```: Responsável pela gestão dos usuários conectados, incluindo a diferenciação por tipo (comum e administrador) e suas permissões 

```Usuario.java```: Um objeto que representa um usuário individual, contendo seus dados e seu tipo (comum ou administrador)

```ControladorDeComandos.java```:  Componente para interpretar e executar comandos enviados pelos clientes, facilitando a interação e controle dentro do chat 

```RecebimentoUsuario.java```: Componente do servidor responsável por lidar com a etapa inicial de conexão e autenticação de novos usuários 


##  Requisitos

- Java JDK 8 ou superior
- Terminal / Prompt de Comando

##  Compilação

Você pode compilar os arquivos do cliente e do servidor usando o compilador `javac`. Certifique-se de estar na raiz do projeto:


```bash
javac server/ChatServer.java
javac client/ChatClient.java
```

## Execução
Execute o servidor com:

```bash
java server.ChatServer
```

O servidor ficará escutando na porta padrão 12345.

-> Abra outros terminais para iniciar os clientes:

```bash
java client.ChatClient
```

O cliente solicitará um nome de usuário para identificação no chat.

##  Observações

  -   O sistema utiliza comunicação TCP/IP via sockets.

  -   Cada cliente roda em uma thread separada no servidor.

   -  O projeto é executado em ambiente de terminal/linha de comando.

--- 
Universidade Federal do Maranhão.

Este projeto é de uso acadêmico e educacional. 
Feito por Ana Luíza Ribeiro, Brendda Rodrigues, Camilly Campos, Kelvin Santos
