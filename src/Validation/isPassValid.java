package Validation;

import javax.swing.JFrame;
import javax.swing.JOptionPane;

public class isPassValid extends JFrame {

    public boolean isValidPass(String password) {
        // Check if the password is at least 8 characters long
        if (password.length() < 8) {
            JOptionPane.showMessageDialog(this, "Password must be at least 8 characters long.");
            return false;
        }

        // Check if the password contains at least one uppercase letter
        if (!password.matches(".*[A-Z].*")) {
            JOptionPane.showMessageDialog(this, "Password must contain at least one uppercase letter.");
            return false;
        }

        // Check if the password contains at least one lowercase letter
        if (!password.matches(".*[a-z].*")) {
            JOptionPane.showMessageDialog(this, "Password must contain at least one lowercase letter.");
            return false;
        }

        // Check if the password contains at least one digit
        if (!password.matches(".*\\d.*")) {
            JOptionPane.showMessageDialog(this, "Password must contain at least one digit.");
            return false;
        }

        // Check if the password contains at least one special character (e.g., !, @, #, $, etc.)
        if (!password.matches(".*[!@#$%^&*()].*")) {
            JOptionPane.showMessageDialog(this, "Password must contain at least one special character.");
            return false;
        }

        // If all checks pass, the password is valid
        return true;
    }

}
