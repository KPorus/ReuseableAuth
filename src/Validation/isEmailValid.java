package Validation;

import javax.swing.JOptionPane;

public class isEmailValid {

    public boolean isValidEmail(String email) {
        // Basic regex pattern for email validation
        String emailPattern = "^[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\\.[a-zA-Z]{2,}$";

        // Check for null or empty email
        if (email == null || email.trim().isEmpty()) {
            JOptionPane.showMessageDialog(null, "Email is not valid. The email address cannot be null or empty.");
            return false;
        }

        // Check for leading or trailing spaces
        if (!email.equals(email.trim())) {
            JOptionPane.showMessageDialog(null, "Email is not valid. The email address cannot have leading or trailing spaces.");
            return false;
        }

        // Check for the presence of the "@" symbol
        int atIndex = email.indexOf("@");
        if (atIndex == -1) {
            JOptionPane.showMessageDialog(null, "Email is not valid. The email address must contain an '@' symbol.");
            return false;
        }

        // Check for a valid domain structure
        String domainPart = email.substring(atIndex + 1);
        if (domainPart.indexOf(".") == -1) {
            JOptionPane.showMessageDialog(null, "Email is not valid. The domain part must contain at least one '.' symbol.");
            return false;
        }

        // Check if the email matches the regex pattern
        if (!email.matches(emailPattern)) {
            JOptionPane.showMessageDialog(null, "Email is not valid. Please enter a valid email address.");
            return false;
        }

        //JOptionPane.showMessageDialog(null, "Email is valid.");
        return true;
    }
}
