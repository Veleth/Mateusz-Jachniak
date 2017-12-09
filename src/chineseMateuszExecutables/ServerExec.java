package chineseMateuszExecutables;

import chineseMateusz.Server;

import static java.lang.System.exit;

public class ServerExec {

    public static void main(String[] args) {
        try {
            Server server = new Server();
            server.listenSocket();
            server.clientServerCommunication();
            server.closeStreams();
            exit(0);
        } catch(Exception e) {
            e.printStackTrace();
        }
    }
}
