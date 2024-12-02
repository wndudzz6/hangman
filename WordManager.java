package hangman;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Random;
import java.util.Scanner;
import java.io.File;

public class WordManager {
	private final String alphabet = "ABCDEFGHIJKLMNOPQRSTUVWXYZ";
	private final int maxLevel = 5;
	private Random rand = new Random();
	private HashMap<Integer, LinkedHashMap<String, String>> gameWords;
	private HashMap<String, String> hints;
	private String currentWord;
	private Integer currentStage;
	private Integer currentLevel;

	public WordManager() {
		currentStage = 1;
		currentLevel = 1;
		initGameWords();
	}
	
	private void initGameWords() { // Words 텍스트파일에서 단어, 카테고리, 힌트 받아오기
		Scanner fileIn = openFile("Words.txt");
		gameWords = new HashMap<>();
		hints = new HashMap<>();
		LinkedHashMap<String, String> stage1 = new LinkedHashMap<>();
		LinkedHashMap<String, String> stage2 = new LinkedHashMap<>();
		LinkedHashMap<String, String> stage3 = new LinkedHashMap<>();
		while(fileIn.hasNext()) {
			int stageLevel = fileIn.nextInt();
			String word = fileIn.next();
			String category = fileIn.next();
			String hint = fileIn.next();
			switch(stageLevel) {
			case 1:
				stage1.put(word, category);
				break;
			case 2:
				stage2.put(word, category);
				break;
			case 3:
				stage3.put(word, category);
				break;
			default:
				break;
			}
			hints.put(word, hint);
		}
		gameWords.put(1, stage1);
		gameWords.put(2, stage2);
		gameWords.put(3, stage3);
		fileIn.close();
	}
	
	public void setStageLevel(Pair<Integer, Integer> p) {
		currentStage = p.getFirstValue();
		currentLevel = p.getSecondValue();
	}
	
	private Integer getRandomIndex(int size) { // 랜덤한 index 값을 꺼내온 뒤 제거
		int range = size / maxLevel; // 단계별 레벨의 수(현재 상수 5)로 범위 설정
		int min = range * (currentLevel-1);
		int max = min + range;
		int randomIndex = rand.nextInt(min,max);
		
		return randomIndex;
	}
	public String getWord() { //getRandomIndex 이용해서 단계,레벨에 맞는 단어 랜덤으로 반환
		LinkedHashMap<String, String> words = gameWords.get(currentStage);
		List<String> LevelKeys = new ArrayList<>(words.keySet());
		
		currentWord = LevelKeys.get(getRandomIndex(LevelKeys.size()));
		
		return currentWord;
	}
	public String getCategory() {
		LinkedHashMap<String, String> words = gameWords.get(currentStage);
		return words.get(currentWord);
	}
	public String getHint() {
		return hints.get(currentWord);
	}
	
	
	public char getAnswerChar(StringBuilder currentGuess) { // 찾지 못한 정답 알파벳 하나 반환
		ArrayList<Character> notFoundChars = new ArrayList<>();
		
		for(Character c : currentWord.toCharArray()) { // 현재 단어 알파벳 하나씩 확인
			if(currentGuess.indexOf(String.valueOf(c)) < 0) { // 아직 찾지 못한 알파벳인 경우 추가
				notFoundChars.add(c);
			}
		}
		
		if (!notFoundChars.isEmpty()) { // 예외처리
	        int randomIndex = rand.nextInt(notFoundChars.size());
	        return notFoundChars.get(randomIndex);
	    } else {
	        return '\0';
	    }
	}
	
	public ArrayList<Character> deleteRandomChar() { // 삭제할 랜덤 문자들 저장한 배열 반환
		ArrayList<Character> deleteChars = new ArrayList<>(); // 반환 용 배열
		ArrayList<Character> nonIncludedChars = new ArrayList<>();
		int deleteNum;
		
		for(char c : alphabet.toCharArray()) {
			if(currentWord.indexOf(c) < 0) {
				nonIncludedChars.add(c);
			}
		}
		Collections.shuffle(nonIncludedChars); //랜덤으로 섞어주기
		
		switch(currentStage) {
		case 1:
			deleteNum = 5;
			break;
		case 2:
			deleteNum = 3;
			break;
		case 3:
			deleteNum = 1;
			break;
		default: 
			deleteNum = 0;
		}
		
		for(int i=0; (i < deleteNum) && (i < nonIncludedChars.size()); i++) {
			deleteChars.add(nonIncludedChars.get(i));
		}
		
		return deleteChars;
	}
	
	public void nextLevel() {
		if (currentLevel < maxLevel) {
			currentLevel++;
		} else if (currentStage < gameWords.size()) { //gameWords.size == 현재 스테이지 개수
			currentStage++;
			currentLevel = 1;
		}
	}

	public boolean isGameFinished() {
		return currentStage == gameWords.size() && currentLevel == maxLevel;
	}

	public Integer getCurrentLevel() {
		return currentLevel;
	}
	public Integer getCurrentStage() {
		return currentStage;
	}
	
	private Scanner openFile(String fileName) {
		Scanner fileIn = null;
		try {
			fileIn = new Scanner(new File(fileName));
		} catch(Exception e) {
			System.out.printf("%s is not exist.\n",fileName);
			throw new RuntimeException(e);
		}
		return fileIn;
	}
}
