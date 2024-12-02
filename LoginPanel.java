package hangman;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

public class LoginPanel extends JPanel {
    private Controller controller;
    private UserManager userManager;
    private JTextField userField;
    private JPasswordField passField;
    private JButton loginButton;
    private Image backgroundImage;

    public LoginPanel(Controller controller) {
        this.controller = controller;
        userManager = UserManager.getInstance();
        // 배경 이미지 로드
        backgroundImage = new ImageIcon(getClass().getResource("hangmanimage.jpeg")).getImage();

        setLayout(new BorderLayout(10, 10)); // Layout spacing

        // Transparent panel to draw the background image
        JPanel backgroundPanel = new JPanel(new BorderLayout()) {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                g.drawImage(backgroundImage, 0, 0, getWidth(), getHeight(), this); // 배경 이미지 그리기
            }
        };
        backgroundPanel.setOpaque(false); // 패널 투명하게 설정
        add(backgroundPanel, BorderLayout.CENTER);

        // Panel for login form using GridBagLayout for better control
        JPanel formPanel = new JPanel(new GridBagLayout());
        formPanel.setOpaque(false); // 패널 투명하게 설정
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 10, 5, 10); // Padding

        // Style for labels and text fields to enhance readability
        Color backgroundColor = new Color(255, 255, 255, 200); // Semi-transparent white
        Font labelFont = new Font("Serif", Font.BOLD, 14);

        gbc.gridx = 0;
        gbc.gridy = 0;
        JLabel idLabel = new JLabel("아이디:");
        idLabel.setFont(labelFont);
        idLabel.setOpaque(true);
        idLabel.setBackground(backgroundColor);
        formPanel.add(idLabel, gbc);

        gbc.gridx = 1;
        userField = new JTextField(15);
        userField.setOpaque(true);
        userField.setBackground(backgroundColor);
        formPanel.add(userField, gbc);

        gbc.gridx = 0;
        gbc.gridy = 1;
        JLabel pwLabel = new JLabel("비밀번호:");
        pwLabel.setFont(labelFont);
        pwLabel.setOpaque(true);
        pwLabel.setBackground(backgroundColor);
        formPanel.add(pwLabel, gbc);

        gbc.gridx = 1;
        passField = new JPasswordField(15);
        passField.setOpaque(true);
        passField.setBackground(backgroundColor);
        formPanel.add(passField, gbc);

        gbc.gridx = 0;
        gbc.gridy = 2;
        gbc.gridwidth = 2;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        loginButton = new JButton("로그인");
        loginButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                login();
            }
        });
        formPanel.add(loginButton, gbc);

        // Add some space at the top of the form panel
        JPanel spacer = new JPanel();
        spacer.setOpaque(false);
        spacer.setPreferredSize(new Dimension(100, 200)); // Adjust this value to move the form panel down
        backgroundPanel.add(spacer, BorderLayout.NORTH);

        // Add form panel to the center
        backgroundPanel.add(formPanel, BorderLayout.SOUTH);
    }

    private void login() {
        String id = userField.getText();
        String password = new String(passField.getPassword());

        if(userManager.login(id, password)) {
            controller.showMainMenu();
        }
        else {
            JOptionPane.showMessageDialog(this, "아이디 또는 비밀번호가 잘못 되었습니다. 아이디와 비밀번호를 정확히 입력해 주세요.", "Warning", JOptionPane.WARNING_MESSAGE);
        }
    }
}
