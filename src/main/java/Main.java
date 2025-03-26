import GUI.LoginScreen;

import javax.swing.*;

public class Main {
    public static void main(String[] args) {
        // Start the GUI.LoginScreen application
        SwingUtilities.invokeLater(() -> {
            new LoginScreen();
        });
    }
}