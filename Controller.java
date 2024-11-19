package hangman;

import javax.swing.*;
import java.awt.*;

public class Controller extends JFrame {
	public UserManager userManager; //HangmanGame, LoginPanel, RankingPanel에서 사용  
    private CardLayout cardLayout;
    private JPanel cardPanel;
    private LoginPanel loginPanel;
    private MainMenuPanel mainMenuPanel;
    private HangmanGame hangmanGame;
    private RankingPanel rankingPanel;

    public Controller() {
        setTitle("Hangman Game Controller");
        setSize(800, 600);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        
        userManager = new UserManager();
        
        cardLayout = new CardLayout();
        cardPanel = new JPanel(cardLayout);

        loginPanel = new LoginPanel(this);
        mainMenuPanel = new MainMenuPanel(this);
        hangmanGame = new HangmanGame(this);
        rankingPanel = new RankingPanel(this);

        cardPanel.add(loginPanel, "Login");
        cardPanel.add(mainMenuPanel, "Main Menu");
        cardPanel.add(hangmanGame, "Game");
        cardPanel.add(rankingPanel, "Rankings");

        add(cardPanel);
        setVisible(true);
    }

    public void showMainMenu() {
        cardLayout.show(cardPanel, "Main Menu");
    }

    // 게임 시작 화면을 보여주는 메서드
    public void startGame() {
    	hangmanGame.startGame();
        cardLayout.show(cardPanel, "Game");
    }

    // 랭킹 화면을 보여주는 메서드
    public void viewRankings() {
    	rankingPanel.loadScores();
        cardLayout.show(cardPanel, "Rankings");
    }

    public static void main(String[] args) {
        new Controller();
    }
}
