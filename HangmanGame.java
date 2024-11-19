package hangman;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;
import java.util.HashSet;

public class HangmanGame extends JPanel {
	private Controller controller;
    private WordManager wordManager;
    private GameTimer gameTimer;
    private HashSet<Character> guessedLetters;
    private String currentWord;
    private StringBuilder currentGuess;
    private int wrongAttempts;
    private final int maxAttempts = 6;
    private int scoreSum;
    private int score;
    private String guessState;


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
    
    public HangmanGame(Controller controller) {
        this.controller = controller;
        this.wordManager = new WordManager();
        this.guessedLetters = new HashSet<>();
        initializeUI();
        this.gameTimer = new GameTimer(timeLabel); // GameTimer클래스가 JLabel(timeLabel)을 넘겨받아 내부 timer 로 화면 갱신
    }

    private void initializeUI() {
        setLayout(new GridBagLayout());
        GridBagConstraints[] gbc = new GridBagConstraints[9];
        
        stageLabel = new JLabel("", SwingConstants.CENTER);
        
        categoryLabel = new JLabel("Category: ", SwingConstants.CENTER);

        displayWordLabel = new JLabel("", SwingConstants.CENTER);
        displayWordLabel.setFont(new Font("Serif", Font.BOLD, 24));

        scoreLabel = new JLabel("Score: " + score, SwingConstants.CENTER);
        
        attemptsLabel = new JLabel("Attempts left: " + (maxAttempts - wrongAttempts));
        
        timeLabel = new JLabel("Time: 0s", SwingConstants.CENTER);
        
        hintButton = new JButton(String.valueOf("Hint"));
        hintButton.addActionListener(e -> getHint());

        hangmanPanel = new HangmanPanel();
        keyboardPanel = new JPanel(new GridLayout(3, 9)); 
        letterButtons = new JButton[26];

        // 알파벳 버튼 생성
        for (int i = 0; i < 26; i++) {
            char letter = (char)('A' + i);
            letterButtons[i] = new JButton(String.valueOf(letter));
            letterButtons[i].setPreferredSize(new Dimension(45, 45));
            letterButtons[i].addActionListener(e -> processGuess(String.valueOf(letter)));
            keyboardPanel.add(letterButtons[i]);
        }
        
        for(int i=0; i < 9; i++) { //GridBagLayout 내부 요소 초기화
        	gbc[i] = new GridBagConstraints();
        }
        
        // Stage
        gbc[0].gridx = 0;
        gbc[0].gridy = 0;
        gbc[0].gridwidth = 1;
        gbc[0].gridheight = 1;
        gbc[0].weightx = 1;
        gbc[0].weighty = 0.1;
        gbc[0].fill = GridBagConstraints.BOTH;
        add(stageLabel, gbc[0]);

        // Category
        gbc[1].gridx = 0;
        gbc[1].gridy = 1;
        gbc[1].gridwidth = 1;
        gbc[1].gridheight = 1;
        gbc[1].weightx = 1;
        gbc[1].weighty = 0.1;
        gbc[1].fill = GridBagConstraints.BOTH;
        add(categoryLabel, gbc[1]);

        // DisplayWord
        gbc[2].gridx = 2;
        gbc[2].gridy = 0;
        gbc[2].gridwidth = 2;
        gbc[2].gridheight = 1;
        gbc[2].weightx = 2;
        gbc[2].weighty = 0.1;
        gbc[2].fill = GridBagConstraints.BOTH;
        add(displayWordLabel, gbc[2]);

        // Score
        gbc[3].gridx = 2;
        gbc[3].gridy = 1;
        gbc[3].gridwidth = 2;
        gbc[3].gridheight = 1;
        gbc[3].weightx = 2;
        gbc[3].weighty = 0.1;
        gbc[3].fill = GridBagConstraints.BOTH;
        add(scoreLabel, gbc[3]);

        // Time
        gbc[4].gridx = 5;
        gbc[4].gridy = 0;
        gbc[4].gridwidth = 1;
        gbc[4].gridheight = 1;
        gbc[4].weightx = 1;
        gbc[4].weighty = 0.1;
        gbc[4].fill = GridBagConstraints.BOTH;
        add(timeLabel, gbc[4]);

        // Hint
        gbc[5].gridx = 5;
        gbc[5].gridy = 1;
        gbc[5].gridwidth = 1;
        gbc[5].gridheight = 1;
        gbc[5].weightx = 1;
        gbc[5].weighty = 0.1;
        gbc[5].fill = GridBagConstraints.BOTH;
        add(hintButton, gbc[5]);

        // HangMan Game
        gbc[6].gridx = 1;
        gbc[6].gridy = 3;
        gbc[6].gridwidth = 4;
        gbc[6].gridheight = 4;
        gbc[6].weightx = 4;
        gbc[6].weighty = 4;
        gbc[6].fill = GridBagConstraints.BOTH;
        add(hangmanPanel, gbc[6]);

        // Attempt Left
        gbc[7].gridx = 5;
        gbc[7].gridy = 3;
        gbc[7].gridwidth = 1;
        gbc[7].gridheight = 1;
        gbc[7].weightx = 1;
        gbc[7].weighty = 1;
        gbc[7].fill = GridBagConstraints.BOTH;
        add(attemptsLabel, gbc[7]);

        // Keyboard Panel
        gbc[8].gridx = 0;
        gbc[8].gridy = 8;
        gbc[8].gridwidth = 6;
        gbc[8].gridheight = 3;
        gbc[8].weightx = 6;
        gbc[8].weighty = 2;
        gbc[8].fill = GridBagConstraints.BOTH;
        add(keyboardPanel, gbc[8]);
    }
    
    public void startGame() {
    	scoreSum = 0;
    	wordManager.resetStageLevel();
    	resetGame();
    }
    
    private void resetGame() {
    	timeLabel.setText("Time: 0s");
    	gameTimer.start();
        currentWord = wordManager.getWord(); 
        if (currentWord != null) {
            guessState = createUnderscoreString(currentWord); 
        } else {
            guessState = "";
        }
        currentGuess = new StringBuilder("_".repeat(currentWord.length()));
        wrongAttempts = 0;
        guessedLetters.clear();
        resetScore(); //현재 단계에 맞는 점수 설정
        resetHint();
        resetKeyboard();
        setDeleteChars();
        updateDisplay();
    }
    
    private void processGuess(String input) {
        char guess = input.charAt(0);
        
        guessedLetters.add(guess);
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

        // guessState 업데이트
        StringBuilder updatedState = new StringBuilder(guessState);
        for (int i = 0; i < currentWord.length(); i++) {
            if (currentGuess.charAt(i) != '_') {
                updatedState.setCharAt(i, currentGuess.charAt(i));
            }
        }
        guessState = updatedState.toString();

        updateDisplay();

        // 단어를 완전히 맞춘 경우
        if (currentGuess.toString().equals(currentWord)) {
        	gameTimer.stop();
        	scoreSum += getFinalScore();
            JOptionPane.showMessageDialog(this, "Congratulations! You completed the word: " + currentWord +
                    "\nCurrent Stage: " + wordManager.getCurrentStage() + "-" + wordManager.getCurrentLevel() +
                    "\nTime Bonus Score: " + timeBonusScore() +
                    "\nClear Score: " + getFinalScore() +
                    "\nCurrent Score Sum: " + scoreSum);
            // 다음 단계로 진행할 것인지 묻기
            if (!wordManager.isGameFinished()) {
                int response = JOptionPane.showConfirmDialog(this, "Would you like to play the next level?", "Next Level", JOptionPane.YES_NO_OPTION);
                if (response == JOptionPane.YES_OPTION) {
                    wordManager.nextLevel();  // 다음 레벨로 진행
                    resetGame();
                    return;
                }
            } else {
                JOptionPane.showMessageDialog(this, "You have completed all levels!");
            }
            controller.userManager.updateScore(scoreSum); // 최종점수 갱신
            controller.showMainMenu();
        }
    }
    private void updateWrongGuess(char guess) {
        wrongAttempts++;
        score = Math.max(0, score - 1);
        if (wrongAttempts >= maxAttempts) {
            score = 0;
            JOptionPane.showMessageDialog(this, "Game over! The word was: " + currentWord + ". Final Score: " + scoreSum);
            controller.userManager.updateScore(scoreSum); // 최종점수 갱신
            controller.showMainMenu();
        } else {
            updateDisplay();
        }
    }
    
    private void updateDisplay() {
    	stageLabel.setText(wordManager.getCurrentStage()+"-"+wordManager.getCurrentLevel());
    	categoryLabel.setText("Category: " + wordManager.getCategory());
        displayWordLabel.setText(String.join(" ", currentGuess.toString().split("")));
        attemptsLabel.setText("Attempts left: " + (maxAttempts - wrongAttempts)+"     ");
        scoreLabel.setText("Score: " + score);
        hangmanPanel.setWrongAttempts(wrongAttempts);
        hangmanPanel.repaint();
    }

    private void resetScore() { //단계별 점수 설정
    	int stage = wordManager.getCurrentStage();
    	switch(stage) {
    	case 1:
    		score = 15;
    		break;
    	case 2:
    		score = 30;
    		break;
    	case 3:
    		score = 50;
    		break;
    	default:
    		score = 0;
    		break;
    	}
    }
    private int timeBonusScore() { //경과시간에 따른 추가 점수 반환
    	int bonusScore = 0;
    	int elapsedTime = gameTimer.getElapsedSeconds();
    	
    	if(elapsedTime <= 30) {
    		bonusScore += 10;
    	} else if(elapsedTime <= 60) {
    		bonusScore += 5;
    	}
    	
    	return bonusScore;
    }
    private int getFinalScore() { //최종 점수 반환
    	int bonusScore = timeBonusScore();
    	int finalScore = score + bonusScore;
    	return finalScore;
    }
    
    private void getHint() { //hint 제공(점수 -5점)
    	score -= 5;
    	hintButton.setText(wordManager.getHint());
    	hintButton.setEnabled(false);
    	hintButton.setBackground(Color.GRAY);
    	updateDisplay();
    }
    private void resetHint() {
    	hintButton.setText("Hint");
    	hintButton.setEnabled(true);
    	hintButton.setBackground(null);
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
            button.setBackground(null);
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
    
    public String createUnderscoreString(String currentWord) {
        StringBuilder underscores = new StringBuilder();

        for (int i = 0; i < currentWord.length(); i++) {
            underscores.append('_');  
        }

        return underscores.toString();  
    }
}
