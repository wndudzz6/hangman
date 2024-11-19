package hangman;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Random;
import java.util.Scanner;
import java.io.File;

public class WordManager {
	private final String alphabet = "ABCDEFGHIJKLMNOPQRSTUVWXYZ";
	private final int maxLevel = 5;
	private Random rand = new Random();
	private HashMap<Integer, LinkedHashMap<String, String>> gameWords;
	private HashMap<String, String> hints;
	private ArrayList<Integer> unvisitedIndex;
	private String currentWord;
	private int currentStage;
	private int currentLevel;

	public WordManager() {
		unvisitedIndex = new ArrayList<>();
		currentStage = 1;
		currentLevel = 1;
		initGameWords();
		resetVisit();
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
	private void resetVisit() { // 현재 Stage 단어 개수만큼 visit ArrayList에 index 값 넣어주기
		unvisitedIndex.clear();
		int currentLevelSize = gameWords.get(currentStage).size();
		
		for(int index=0; index < currentLevelSize; index++) {
			unvisitedIndex.add(index);
		}
	}
	
	public void resetStageLevel() { 
		currentStage = 1;
		currentLevel = 1;
	}
	
	private Integer getRandomIndex() { // 랜덤한 index 값을 꺼내온 뒤 제거
		int randNum = rand.nextInt(unvisitedIndex.size());
		int index = unvisitedIndex.get(randNum); 
		unvisitedIndex.remove(randNum);
		
		return index;
	}
	
	public String getWord() {
		LinkedHashMap<String, String> words = gameWords.get(currentStage);
		ArrayList<String> LevelKeys = new ArrayList<>(words.keySet());
		
		currentWord = LevelKeys.get(getRandomIndex()); // 현재 Stage 단어 랜덤으로 받아오기
		
		return currentWord;
	}
	public String getCategory() {
		LinkedHashMap<String, String> words = gameWords.get(currentStage);
		return words.get(currentWord);
	}
	public String getHint() {
		return hints.get(currentWord);
	}
	
	public ArrayList<Character> deleteRandomChar() { // 삭제할 랜덤 문자들 저장한 배열 반환
		ArrayList<Character> deleteChars = new ArrayList<>();
		ArrayList<Character> nonIncludedChars = new ArrayList<>();
		int deleteNum;
		
		for(char c : alphabet.toCharArray()) {
			if(currentWord.indexOf(c) < 0) {
				nonIncludedChars.add(c);
			}
		}
		
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
		
		for(int i=0; i<deleteNum; i++) {
			int idx = rand.nextInt(nonIncludedChars.size());
			deleteChars.add(nonIncludedChars.get(idx));
			nonIncludedChars.remove(idx);
		}
		return deleteChars;
	}
	
	public void nextLevel() {
		if (currentLevel < maxLevel) {
			currentLevel++;
		} else if (currentStage < gameWords.size()) {
			currentStage++;
			currentLevel = 1;
			resetVisit(); 
		}
	}

	public boolean isGameFinished() {
		return currentStage == gameWords.size() && currentLevel == maxLevel;
	}

	public int getCurrentLevel() {
		return currentLevel;
	}
	public int getCurrentStage() {
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
