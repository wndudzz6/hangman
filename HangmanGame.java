package hangman;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;

public class HangmanGame extends JPanel {
	public static int gameLevel; // 1 = 이지모드, 2 = 노멀모드, 3 = 하드모드 / 0 = 미선택
    private Controller controller;
    private UserManager userManager;
    private WordManager wordManager;
    private GameTimer gameTimer;
    private String currentWord;
    private StringBuilder currentGuess;
    private int wrongAttempts;
    private final int maxAttempts = 6;
    private int scoreSum;
    private int score;

    private JLabel stageLabel;
    private JLabel categoryLabel;
    private JLabel displayWordLabel;
    private JLabel attemptsLabel;
    private JLabel scoreLabel;
    private JLabel timeLabel;
    private JButton hintButton;
    private HangmanPanel hangmanPanel;
    private JPanel keyboardPanel;
    private JButton[] letterButtons;
    private Image backgroundImage;

    public HangmanGame(Controller controller) {
    	this.controller = controller;
        userManager = UserManager.getInstance();
        wordManager = new WordManager();
        backgroundImage = new ImageIcon(getClass().getResource("hangmanback.jpeg")).getImage();

        // letterButtons 배열을 사용하기 전에 먼저 초기화
        letterButtons = new JButton[26];  // 배열의 크기를 지정하여 초기화

        initializeUI();  // UI 초기화 메서드 호출

        gameTimer = new GameTimer(controller, timeLabel);  // GameTimer 객체 생성

        resetGame();  // 게임 상태 초기화를 GameTimer 생성 후에 호출
    }


    private void initializeUI() {
        setLayout(new BorderLayout());
        Font labelFont = new Font(Font.SANS_SERIF, Font.PLAIN, 20);
        Font wordFont = new Font(Font.SANS_SERIF, Font.PLAIN, 48);
        Font buttonFont = new Font(Font.SANS_SERIF, Font.PLAIN, 30);  // 버튼 폰트 크기 증가

        // 레이블 생성
        stageLabel = createLabel("", labelFont);
        categoryLabel = createLabel("카테고리: ", labelFont);
        scoreLabel = createLabel("점수: " + score, labelFont);
        attemptsLabel = createLabel("시도 횟수: " + (maxAttempts - wrongAttempts), labelFont);
        timeLabel = createLabel("타이머: 0초", labelFont);
        displayWordLabel = createLabel("", wordFont);

        // 힌트 버튼 생성
        hintButton = createButton("힌트", labelFont);
        hintButton.addActionListener(e -> getHint());
        
        hintButton.setPreferredSize(new Dimension(125, 25)); // 원하는 크기 설정
        hintButton.setMaximumSize(new Dimension(125, 25));  // 크기 고정
        hintButton.setMinimumSize(new Dimension(125, 25));
        
        // Left Top Panel
        JPanel leftTopPanel = new JPanel();
        leftTopPanel.setOpaque(false);
        leftTopPanel.setLayout(new BoxLayout(leftTopPanel, BoxLayout.Y_AXIS));
        leftTopPanel.add(stageLabel);
        leftTopPanel.add(categoryLabel);
        leftTopPanel.add(displayWordLabel);  // 단어 레이블 추가

        add(leftTopPanel, BorderLayout.NORTH);

        // Center Top Panel for Score
        JPanel centerTopPanel = new JPanel();
        centerTopPanel.setOpaque(false);
        centerTopPanel.add(scoreLabel);

        // Right Top Panel
        JPanel rightTopPanel = new JPanel();
        rightTopPanel.setOpaque(false);
        rightTopPanel.setLayout(new BoxLayout(rightTopPanel, BoxLayout.Y_AXIS));
        rightTopPanel.add(timeLabel);
        rightTopPanel.add(hintButton);
        rightTopPanel.add(attemptsLabel);


        // Top Panel
        JPanel topPanel = new JPanel(new BorderLayout());
        topPanel.setOpaque(false);
        topPanel.add(leftTopPanel, BorderLayout.WEST);
        topPanel.add(centerTopPanel, BorderLayout.CENTER);
        topPanel.add(rightTopPanel, BorderLayout.EAST);

        // Hangman Panel
        hangmanPanel = new HangmanPanel();
        hangmanPanel.setOpaque(false);
        hangmanPanel.setPreferredSize(new Dimension(150, 360));

        // Keyboard Panel
        keyboardPanel = new JPanel(new GridLayout(3, 9, 10, 10));
        keyboardPanel.setOpaque(false);
        for (int i = 0; i < 26; i++) {
            char letter = (char) ('A' + i);
            JButton button = createButton(String.valueOf(letter), buttonFont);  // 버튼 폰트 적용
            button.setPreferredSize(new Dimension(60, 60));  // 버튼 크기 조정
            button.addActionListener(e -> processGuess(e.getActionCommand()));
            keyboardPanel.add(button);
            letterButtons[i] = button;
        }

        // Bottom Panel
        JPanel bottomPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        bottomPanel.setOpaque(false);
        bottomPanel.add(keyboardPanel);

        // Add panels to the main layout
        add(topPanel, BorderLayout.NORTH);
        add(hangmanPanel, BorderLayout.CENTER);
        add(bottomPanel, BorderLayout.SOUTH);
    }

    private JButton createButton(String text, Font font) {
        JButton button = new JButton(text);
        button.setFont(font);
        button.setForeground(Color.BLACK);
        button.setBorder(BorderFactory.createLineBorder(Color.WHITE));
        button.setFocusPainted(false);
        return button;
    }

    private JLabel createLabel(String text, Font font) {
        JLabel label = new JLabel(text, SwingConstants.CENTER);
        label.setFont(font);
        label.setForeground(Color.WHITE);
        return label;
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        g.drawImage(backgroundImage, 0, 0, getWidth(), getHeight(), this);
        g.setColor(new Color(0, 0, 0, 123));
        g.fillRect(0, 0, getWidth(), getHeight());
    }



    public void loadGame() {
    	scoreSum = userManager.getCurrentScore();
    	wordManager.setStageLevel(userManager.getUserCurrentProgress());
    	resetGame();
    }
    public static void gameOver(Controller controller) { // 제한 시간 초과 (하드모드)
    	JOptionPane.showMessageDialog(null, "제한 시간 초과!", "게임 오버", JOptionPane.WARNING_MESSAGE);

        // 필요한 데이터 저장 및 종료 처리
        UserManager.getInstance().updateGameFinish(); // 게임 종료 상태 업데이트
        UserManager.getInstance().writeUserData();    // 사용자 데이터 저장

        // 메인 메뉴로 이동
        controller.showMainMenu();
    }

    private void resetGame() {
    	timeLabel.setText("경과시간: 0초");
    	gameTimer.start();
        currentWord = wordManager.getWord();
        currentGuess = new StringBuilder("_".repeat(currentWord.length()));
        wrongAttempts = 0;
        resetScore(); //현재 단계에 맞는 점수 설정
        resetHint();
        resetKeyboard();
        setDeleteChars();
        
        if(gameLevel == 1) // 이지 모드
        	processGuess(String.valueOf(wordManager.getAnswerChar(currentGuess)));
        updateDisplay();
    }

    private void processGuess(String letter) {
        char guess = letter.charAt(0);
        updateKeyboardButton(guess);

        if (currentWord.indexOf(guess) >= 0) {
            updateCorrectGuess(guess);
        } else {
            updateWrongGuess(guess);
        }
    }
    private void updateCorrectGuess(char guess) {
        for (int i = 0; i < currentWord.length(); i++) {
            if (currentWord.charAt(i) == guess) {
                currentGuess.setCharAt(i, guess);
            }
        }
        updateDisplay();

        // 단어를 완전히 맞춘 경우
        if (currentGuess.toString().equals(currentWord)) {
        	gameTimer.stop();
        	scoreSum += getFinalScore();
        	userManager.updateClearedWordsCount();
        	userManager.updateScore(scoreSum); // 현재 점수, 최고 점수 갱신
        	userManager.updateTopProgress(wordManager.getCurrentStage(), wordManager.getCurrentLevel()); //최고 기록갱신
        	
            JOptionPane.showMessageDialog(this, "클리어! 완성한 단어: " + currentWord +
                    "\n현재 단계: " + wordManager.getCurrentStage() + "-" + wordManager.getCurrentLevel() +
                    "\n시간 보너스 점수: " + timeBonusScore() +
                    "\n현재 단계 점수: " + getFinalScore() +
                    "\n총 점수: " + scoreSum);
            // 다음 단계로 진행할 것인지 묻기
            if (!wordManager.isGameFinished()) {
            	wordManager.nextLevel(); // 다음 레벨로 진행
                userManager.updateCurrentProgress(wordManager.getCurrentStage(), wordManager.getCurrentLevel()); // 현재 스테이지 갱신
                
                int response = JOptionPane.showConfirmDialog(this, "다음 단계로 가시겠습니까?", "다음 단계", JOptionPane.YES_NO_OPTION);
                if (response == JOptionPane.YES_OPTION) {
                    resetGame();
                    return;
                }
            } else {
            	userManager.updateGameFinish();
                JOptionPane.showMessageDialog(this, "모든 레벨을 클리어했습니다!");
            }
            // 메인메뉴로 나갈 때 or 게임을 완전히 클리어 했을 때
            userManager.writeUserData();
            controller.showMainMenu();
        }
    }
    private void updateWrongGuess(char guess) {
        wrongAttempts++;
        
        switch(gameLevel) { //게임레벨에 따른 단어 틀릴 경우 감점
    	case 1:
    		score -= 2;
			break;
		case 2:
			score -= 3;
			break;
		case 3:
			score -= 5;
			break;
		default:
			break;
    	}
        score = Math.max(0, score); // 설계상 0점이하로 내려갈 일은 없으나 안전성과 유지보수를 위해서
        if (wrongAttempts >= maxAttempts) {
            score = 0;
            JOptionPane.showMessageDialog(this, "게임오버! 단어: " + currentWord + ". 최종 점수: " + scoreSum);
            userManager.updateGameFinish();
            userManager.writeUserData();
            controller.showMainMenu();
        } else {
            updateDisplay();
        }
    }

    private void updateDisplay() {
    	stageLabel.setText(wordManager.getCurrentStage()+"-"+wordManager.getCurrentLevel());
    	categoryLabel.setText("카테고리: " + wordManager.getCategory());
        displayWordLabel.setText(String.join(" ", currentGuess.toString().split("")));
        attemptsLabel.setText("시도횟수: " + (maxAttempts - wrongAttempts)+"     ");
        scoreLabel.setText("점수: " + score);
        hangmanPanel.setWrongAttempts(wrongAttempts);
        hangmanPanel.repaint();
    }

    private void resetScore() { //단계별 점수 설정
    	int stage = wordManager.getCurrentStage();
    	switch(stage) { // 게임레벨에 따른 기본 점수 설계
    	case 1:
    		if(gameLevel == 1) score = 20;
    		else if(gameLevel == 2) score = 30;
    		else if(gameLevel == 3) score = 40;
    		break;
    	case 2:
    		if(gameLevel == 1) score = 35;
    		else if(gameLevel == 2) score = 55;
    		else if(gameLevel == 3) score = 80;
    		break;
    	case 3:
    		if(gameLevel == 1) score = 40;
    		else if(gameLevel == 2) score = 65;
    		else if(gameLevel == 3) score = 100;
    		break;
    	default:
    		score = 0;
    		break;
    	}
    }
    private int timeBonusScore() { //경과시간에 따른 추가 점수 반환
    	int bonusScore = 0;
    	int elapsedTime = gameTimer.getElapsedSeconds();

    	if(elapsedTime <= 30) { //게임레벨에 따른 추가첨수
    		switch(gameLevel) {
    		case 1:
    			bonusScore += 5;
    			break;
    		case 2:
    			bonusScore += 10;
    			break;
    		case 3:
    			bonusScore += 15;
    			break;
    		default:
    			bonusScore += 0;
    			break;
    		}
    	} else if(elapsedTime <= 60) {
    		switch(gameLevel) { //게임레벨에 따른 추가첨수
    		case 1:
    			bonusScore += 3;
    			break;
    		case 2:
    			bonusScore += 5;
    			break;
    		case 3:
    			bonusScore += 10;
    			break;
    		default:
    			bonusScore += 0;
    			break;
    		}
    	}
    	return bonusScore;
    }
    private int getFinalScore() { //최종 점수 반환
    	int bonusScore = timeBonusScore();
    	int finalScore = score + bonusScore;
    	return finalScore;
    }

    private void getHint() { //hint 제공(점수 -5점)
    	switch(gameLevel) { //게임레벨에 따른 힌트감점
    	case 1:
    		score -= 5;
			break;
		case 2:
			score -= 7;
			break;
		case 3:
			score -= 10;
			break;
		default:
			break;
    	}
    	
    	score = Math.max(0, score); // 설계상 0점이하로 내려갈 일은 없으나 안전성과 유지보수를 위해서
    	if(gameLevel == 1) {
    		hintButton.setText(wordManager.getHint());
    		JOptionPane.showMessageDialog(this, "단어의 뜻: " + wordManager.getHint());
    	} else {
    		processGuess(String.valueOf(wordManager.getAnswerChar(currentGuess)));
    	}
    		
    	hintButton.setEnabled(false);
    	hintButton.setBackground(Color.GRAY);
    	updateDisplay();
    }
    private void resetHint() {
    	hintButton.setText("힌트");
    	hintButton.setEnabled(true);
    	hintButton.setBackground(UIManager.getColor("Button.background")); // 운영체제에 따른 색상 차이 제거 (기본 버튼 사용)
    }

    private void updateKeyboardButton(char letter) {
        int index = letter - 'A';
        if (index >= 0 && index < 26) {
            letterButtons[index].setEnabled(false);
            letterButtons[index].setBackground(Color.GRAY);
        }
    }
    private void resetKeyboard() {
        for (JButton button : letterButtons) {
            button.setEnabled(true);
            button.setBackground(UIManager.getColor("Button.background")); // 운영체제에 따른 색상 차이 제거 (기본 버튼 사용)
        }
    }
    private void setDeleteChars() { // 삭제 문자들 받아와서 키보드 비활성화
    	ArrayList<Character> deletedChars = wordManager.deleteRandomChar();
    	for(Character c : deletedChars) {
    		int index = c - 'A';
    		letterButtons[index].setEnabled(false);
    		letterButtons[index].setBackground(Color.gray);
    	}
    }
}
