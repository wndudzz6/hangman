package hangman;

import javax.swing.*;
import java.awt.*;

public class Controller extends JFrame {
    private CardLayout cardLayout;
    private JPanel cardPanel;
    private LoginPanel loginPanel;
    private MainMenuPanel mainMenuPanel;
    private GameLevelSelectPanel gameLevelSelectPanel;
    private HangmanGame hangmanGame;
    private RankingPanel rankingPanel;
    private MyPagePanel myPagePanel;

    public Controller() {
        setTitle("Hangman Game Controller");
        setSize(800, 600);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        
        cardLayout = new CardLayout();
        cardPanel = new JPanel(cardLayout);

        loginPanel = new LoginPanel(this);
        mainMenuPanel = new MainMenuPanel(this);
        gameLevelSelectPanel = new GameLevelSelectPanel(this);
        hangmanGame = new HangmanGame(this);
        rankingPanel = new RankingPanel(this);
        myPagePanel = new MyPagePanel(this);

        cardPanel.add(loginPanel, "Login");
        cardPanel.add(mainMenuPanel, "Main Menu");
        cardPanel.add(gameLevelSelectPanel, "Game Level Select");
        cardPanel.add(hangmanGame, "Game");
        cardPanel.add(rankingPanel, "Rankings");
        cardPanel.add(myPagePanel, "MyPage");

        add(cardPanel);
        setVisible(true);
    }

    public void showMainMenu() {
        cardLayout.show(cardPanel, "Main Menu");
    }
    
    // 게임 레벨 선택
    public void gameLevelSelect() {
    	cardLayout.show(cardPanel, "Game Level Select");
    }
    
    // 게임 시작 화면을 보여주는 메서드
    public void startGame() {
    	hangmanGame.loadGame();
        cardLayout.show(cardPanel, "Game");
    }

    // 랭킹 화면을 보여주는 메서드
    public void viewRankings() {
    	rankingPanel.loadScores();
        cardLayout.show(cardPanel, "Rankings");
    }

    public void viewMyPage() {
        myPagePanel.loadMyPage(); // MyPage 데이터를 로드
        cardLayout.show(cardPanel, "MyPage");
    }

    public static void main(String[] args) {
        new Controller();
    }
}
