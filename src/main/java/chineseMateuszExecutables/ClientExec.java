package main.java.chineseMateuszExecutables;

import main.java.chineseMateusz.Client;

import static java.lang.System.exit;

public class ClientExec {
    public static void main(String args[]) {

        try {
            Client window = new Client(21372);
            window.play();
            window.closeStreams();
            exit(0);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
