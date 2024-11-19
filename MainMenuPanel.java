package hangman;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

public class MainMenuPanel extends JPanel {
    private Controller controller;
    private JButton startGameButton, viewRankingsButton;

    public MainMenuPanel(Controller controller) {
        this.controller = controller;
        setLayout(new GridBagLayout()); // Using GridBagLayout for flexible positioning

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.gridwidth = GridBagConstraints.REMAINDER; // Each component in its own row
        gbc.fill = GridBagConstraints.HORIZONTAL; // Expand horizontally
        gbc.insets = new Insets(10, 50, 10, 50); // Top, left, bottom, right padding

        // Creating the "Start Game" button
        startGameButton = new JButton("Start Game");
        startGameButton.setFont(new Font("Arial", Font.BOLD, 20)); // Increased font size
        startGameButton.setPreferredSize(new Dimension(300, 100)); // Set preferred size
        startGameButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                controller.startGame(); // 게임 화면으로 전환
            }
        });
        gbc.gridy = 0; // Position on the first row
        add(startGameButton, gbc); // Add to panel with constraints

        // Creating the "View Rankings" button
        viewRankingsButton = new JButton("View Rankings");
        viewRankingsButton.setFont(new Font("Arial", Font.BOLD, 20)); // Increased font size
        viewRankingsButton.setPreferredSize(new Dimension(300, 100)); // Set preferred size
        viewRankingsButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                controller.viewRankings(); // 랭킹 화면으로 전환
            }
        });
        gbc.gridy = 1; // Position on the second row
        add(viewRankingsButton, gbc); // Add to panel with constraints
    }
}
