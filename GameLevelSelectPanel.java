package hangman;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Image;
import java.awt.Insets;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JOptionPane;
import javax.swing.JPanel;

public class GameLevelSelectPanel extends JPanel {
	private Controller controller;
	private UserManager userManager;
    private JButton easyModeButton, normalModeButton, hardModeButton, gameExplainButton;
    private Image backgroundImage;

    public GameLevelSelectPanel(Controller controller) {
        this.controller = controller;
        userManager = UserManager.getInstance();
        backgroundImage = new ImageIcon(getClass().getResource("hangmanback.jpeg")).getImage();
        setLayout(new GridBagLayout());

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.gridwidth = GridBagConstraints.REMAINDER;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.insets = new Insets(10, 50, 10, 50);

        easyModeButton = createCustomButton("이지 모드");
        easyModeButton.setForeground(new Color(0, 255, 0));
        easyModeButton.addActionListener(e -> startEasyMode());
        gbc.gridy = 0;
        add(easyModeButton, gbc);

        normalModeButton = createCustomButton("노멀 모드");
        normalModeButton.setForeground(new Color(255, 127, 0));
        normalModeButton.addActionListener(e -> startNormalMode());
        gbc.gridy = 1;
        add(normalModeButton, gbc);

        hardModeButton = createCustomButton("하드 모드");
        hardModeButton.setForeground(new Color(255, 0, 0));
        hardModeButton.addActionListener(e -> startHardMode());
        gbc.gridy = 2;
        add(hardModeButton, gbc);
        
        gameExplainButton = createCustomButton("게임 설명");
        gameExplainButton.setForeground(Color.white);
        gameExplainButton.addActionListener(e -> showGameModeExplanation());
        gbc.gridy = 3; // 새로운 버튼은 4번째에 추가
        add(gameExplainButton, gbc);
    }

    private JButton createCustomButton(String text) {
        JButton button = new JButton(text);
        button.setFont(new Font("Serif", Font.BOLD, 25));
        button.setPreferredSize(new Dimension(255, 85));
        button.setBackground(Color.black);
        button.setOpaque(true); // 버튼 배경색 적용
        button.setBorderPainted(false); // 테두리 없애기
        return button;
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        g.drawImage(backgroundImage, 0, 0, getWidth(), getHeight(), this); // 배경 이미지 그리기
    }
    
    private void startEasyMode() {
    	HangmanGame.gameLevel = 1;
    	userManager.updateCurrentGameLevel(1);
    	controller.startGame();
    }
    private void startNormalMode() {
    	HangmanGame.gameLevel = 2;
    	userManager.updateCurrentGameLevel(2);
    	controller.startGame();
    }
    private void startHardMode() {
    	HangmanGame.gameLevel = 3;
    	userManager.updateCurrentGameLevel(3);
    	controller.startGame();
    }
    
    // 게임 모드 설명을 팝업 창으로 띄우는 메서드
    private void showGameModeExplanation() {
        String explanation = "게임 모드 설명\n\n"
                + "이지 모드\n"
                + "- 정답 알파벳 하나 제공\n"
                + "- 힌트 사용: 단어 뜻 제공\n"
                + ""
                + "- 기본 점수 낮음\n\n"
                + "노멀 모드\n"
                + "- 힌트 사용: 정답 알파벳 하나 제공\n"
                + "- 중급자 용\n\n"
                + "하드 모드\n"
                + "- 제한시간: 1분 30초\n"
                + "- 힌트 사용: 정답 알파벳 하나 제공\n"
                + "- 힌트 사용 또는 오답시 큰 감점 점수\n"
                + "- 높은 기본 점수 및 추가 점수\n"
                + "- 숙련자 용 (하이리스크 하이리턴)";

        JOptionPane.showMessageDialog(this, explanation, "게임 모드 설명", JOptionPane.INFORMATION_MESSAGE);
    }
}
