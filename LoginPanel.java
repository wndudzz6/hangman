package hangman;
import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

public class LoginPanel extends JPanel {
    private Controller controller;
    private JTextField userField;
    private JPasswordField passField;
    private JButton loginButton;

    public LoginPanel(Controller controller) {
        this.controller = controller;
        setLayout(new BorderLayout(10, 10)); // Layout spacing

        // Title at the top
        JLabel titleLabel = new JLabel("Hangman Game", SwingConstants.CENTER);
        titleLabel.setFont(new Font("Arial", Font.BOLD, 30)); // Large font size
        add(titleLabel, BorderLayout.NORTH);

        // Hangman drawing panel, precisely positioned and proportioned
        JPanel hangmanPanel = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                int panelWidth = getWidth();
                int xOffset = panelWidth / 2 - 75; // Centering adjustment

                // Constants for the hangman figure
                int gallowsTopY = 100;
                int headDiameter = 40;
                int bodyLength = 100;
                int legHeight = 40;
                int armWidth = 30;
                int ropeLength = 20;
                int gallowsHeight = bodyLength + legHeight + headDiameter + ropeLength + 30;
                int horizontalBarLength = 75;

                // Gallows
                g.drawLine(xOffset, gallowsTopY, xOffset, gallowsTopY + gallowsHeight); // Vertical line
                g.drawLine(xOffset, gallowsTopY, xOffset + horizontalBarLength, gallowsTopY); // Horizontal top line
                g.drawLine(xOffset + horizontalBarLength, gallowsTopY, xOffset + horizontalBarLength, gallowsTopY + ropeLength); // Rope

                // Head and Body
                g.drawOval(xOffset + horizontalBarLength - 10, gallowsTopY + ropeLength, headDiameter, headDiameter); // Head
                g.drawLine(xOffset + horizontalBarLength, gallowsTopY + ropeLength + headDiameter, xOffset + horizontalBarLength, gallowsTopY + ropeLength + headDiameter + bodyLength); // Body
                g.drawLine(xOffset + horizontalBarLength, gallowsTopY + ropeLength + headDiameter + bodyLength, xOffset + horizontalBarLength - 40, gallowsTopY + ropeLength + headDiameter + bodyLength + legHeight); // Left leg
                g.drawLine(xOffset + horizontalBarLength, gallowsTopY + ropeLength + headDiameter + bodyLength, xOffset + horizontalBarLength + 40, gallowsTopY + ropeLength + headDiameter + bodyLength + legHeight); // Right leg
                int armStartY = gallowsTopY + ropeLength + headDiameter + 2 * bodyLength / 5; // 2/5 of the body length for arms
                g.drawLine(xOffset + horizontalBarLength, armStartY, xOffset + horizontalBarLength - 40, armStartY + 30); // Left arm
                g.drawLine(xOffset + horizontalBarLength, armStartY, xOffset + horizontalBarLength + 40, armStartY + 30); // Right arm

                // Eyes X_X correctly positioned
                g.drawLine(xOffset + horizontalBarLength - 5, gallowsTopY + ropeLength + 10, xOffset + horizontalBarLength + 5, gallowsTopY + ropeLength + 20); // Left eye slash
                g.drawLine(xOffset + horizontalBarLength + 5, gallowsTopY + ropeLength + 10, xOffset + horizontalBarLength - 5, gallowsTopY + ropeLength + 20); // Left eye backslash
                g.drawLine(xOffset + horizontalBarLength + 15, gallowsTopY + ropeLength + 10, xOffset + horizontalBarLength + 25, gallowsTopY + ropeLength + 20); // Right eye slash
                g.drawLine(xOffset + horizontalBarLength + 25, gallowsTopY + ropeLength + 10, xOffset + horizontalBarLength + 15, gallowsTopY + ropeLength + 20); // Right eye backslash
            }
        };
        hangmanPanel.setPreferredSize(new Dimension(800, 600)); // Using the full size for custom drawing
        add(hangmanPanel, BorderLayout.CENTER);

        // Panel for login form at the bottom using GridBagLayout for better control
        JPanel formPanel = new JPanel(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(2, 4, 2, 4); // Padding

        gbc.gridx = 0;
        gbc.gridy = 0;
        formPanel.add(new JLabel("Username:"), gbc);

        gbc.gridx = 1;
        gbc.gridy = 0;
        userField = new JTextField(15);
        formPanel.add(userField, gbc);

        gbc.gridx = 0;
        gbc.gridy = 1;
        formPanel.add(new JLabel("Password:"), gbc);

        gbc.gridx = 1;
        gbc.gridy = 1;
        passField = new JPasswordField(15);
        formPanel.add(passField, gbc);

        gbc.gridx = 0;
        gbc.gridy = 2;
        gbc.gridwidth = 2;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        loginButton = new JButton("Login");
        loginButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
            	login();
            }
        });
        formPanel.add(loginButton, gbc);

        add(formPanel, BorderLayout.SOUTH);
    }
    
    private void login() {
    	String id = userField.getText();
    	String password = new String(passField.getPassword());
    	
    	if(controller.userManager.login(id, password)) {
    		controller.showMainMenu();
    	}
    	else {
    		JOptionPane.showMessageDialog(this, "아이디 또는 비밀번호가 잘못 되었습니다. 아이디와 비밀번호를 정확히 입력해 주세요.", "Warning", JOptionPane.WARNING_MESSAGE);
    	}
    }
}
