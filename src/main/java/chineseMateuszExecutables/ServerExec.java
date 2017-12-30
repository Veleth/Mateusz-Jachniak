package main.java.chineseMateuszExecutables;

import main.java.chineseMateusz.Server;

import static java.lang.System.exit;

public class ServerExec {

    public static void main(String[] args) {
        try {
            Server server = new Server(21372);
            server.gameHandling();
            server.closeStreams();
            exit(0);
        } catch(Exception e) {
            e.printStackTrace();
        }
    }
}
