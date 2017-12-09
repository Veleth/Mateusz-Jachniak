package chineseMateuszExecutables;

import chineseMateusz.Client;

import static java.lang.System.exit;

public class ClientExec {
    public static void main(String args[]) {

        try {
            Client window = new Client();
            window.communication();
            window.setGUI();
            window.play();
            window.closeStreams();
            exit(0);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
