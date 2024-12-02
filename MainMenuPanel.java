package hangman;

import javax.swing.*;
import java.awt.*;

public class MainMenuPanel extends JPanel {
    private Controller controller;
    private UserManager userManger;
    private JButton startGameButton, viewRankingsButton, myPageButton, exitButton;
    private Image backgroundImage;

    public MainMenuPanel(Controller controller) {
        this.controller = controller;
        userManger = UserManager.getInstance();
        backgroundImage = new ImageIcon(getClass().getResource("hangmanback.jpeg")).getImage();
        setLayout(new GridBagLayout()); // Using GridBagLayout for flexible positioning

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.gridwidth = GridBagConstraints.REMAINDER; // Each component in its own row
        gbc.fill = GridBagConstraints.HORIZONTAL; // Expand horizontally
        gbc.insets = new Insets(10, 50, 10, 50); // Top, left, bottom, right padding

        // Creating "Start Game" button
        startGameButton = createCustomButton("게임시작하기");
        startGameButton.addActionListener(e -> startGame());
        gbc.gridy = 0;
        add(startGameButton, gbc);

        // Creating "View Rankings" button
        viewRankingsButton = createCustomButton("랭킹 확인하기");
        viewRankingsButton.addActionListener(e -> controller.viewRankings());
        gbc.gridy = 1;
        add(viewRankingsButton, gbc);

        // Creating "My Page" button
        myPageButton = createCustomButton("마이 페이지");
        myPageButton.addActionListener(e -> controller.viewMyPage());
        gbc.gridy = 2;
        add(myPageButton, gbc);
        
        exitButton = createCustomButton("게임 종료");
        exitButton.addActionListener(e -> exitGame());
        gbc.gridy = 3;
        add(exitButton, gbc);
    }

    private JButton createCustomButton(String text) {
        JButton button = new JButton(text);
        button.setFont(new Font("Serif", Font.BOLD, 30));
        button.setPreferredSize(new Dimension(300, 85));
        button.setForeground(Color.WHITE); // 흰색 글씨
        button.setBackground(Color.BLACK); // 검정색 배경
        button.setOpaque(true); // 버튼 배경색 적용
        button.setBorderPainted(false); // 테두리 없애기
        return button;
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        g.drawImage(backgroundImage, 0, 0, getWidth(), getHeight(), this); // 배경 이미지 그리기
    }
    
    // 유저의 게임 레벨에 맞게 시작 (현재 선택된 레벨이 없을 경우 GameLevelSelectPanel로 이동해서 선택)
    private void startGame() {
    	int gameLevel = userManger.getCurrentGameLevel();
    	if(gameLevel == 0) {
    		controller.gameLevelSelect();
    	} else {
    		HangmanGame.gameLevel = gameLevel;
    		controller.startGame();
    	}
    }
    
    // 게임 종료
    private void exitGame() {
        int response = JOptionPane.showConfirmDialog(this, "게임을 종료하시겠습니까?", "게임 종료", JOptionPane.YES_NO_OPTION);
        if (response == JOptionPane.YES_OPTION) {
            System.exit(0); // 프로그램 종료
        }
    }
}
