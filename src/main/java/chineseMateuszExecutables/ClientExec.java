package main.java.chineseMateuszExecutables;

import main.java.chineseMateusz.Client;

import javax.swing.*;

import static java.lang.System.exit;

public class ClientExec {
    public static void main(String args[]) {
        try {
            Client window = new Client(21372);
            window.play();
            window.closeStreams();
            exit(0);

        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, "Could not establish connection with server!", "ERROR", JOptionPane.ERROR_MESSAGE);
            exit(1);
        }
    }
}
