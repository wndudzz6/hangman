package hangman;

import java.io.PrintWriter;
import java.util.Scanner;

public class UserData {
	private int clearedWordsCount; // 지금까지 맞춘 단어 개수
	private int currentGameLevel; // 현재 진행중인 게임 난이도
	private int currentScore; // 현재 점수
	private Integer bestScore; // 최고 점수
	private Pair<Integer, Integer> currentProgress; // 현재 진행 스테이지
	private Pair<Integer, Integer> topProgress; // 최고 클리어 스테이지
	
	public UserData(){
		currentProgress = new Pair<>();
		topProgress = new Pair<>();
	}
	
	public void read(Scanner scan) {
		Integer first, second;
		clearedWordsCount = scan.nextInt();
		currentGameLevel = scan.nextInt();
		currentScore = scan.nextInt();
		bestScore = scan.nextInt();
		
		first = scan.nextInt();
		second = scan.nextInt();
		currentProgress.makePair(first, second);
		
		first = scan.nextInt();
		second = scan.nextInt();
		topProgress.makePair(first, second);
	}
	public void write(PrintWriter writer) { // txt 파일에 정보 저장할 때 사용
		writer.print(clearedWordsCount + " ");
	    writer.print(currentGameLevel + " ");
	    writer.print(currentScore + " ");
	    writer.print(bestScore + " ");
	    writer.print(currentProgress.getFirstValue() + " " + currentProgress.getSecondValue() + " ");
	    writer.print(topProgress.getFirstValue() + " " + topProgress.getSecondValue() + "\n");
	}

	public int getClearedWordsCount() {
		return clearedWordsCount;
	}
	public int getCurrentGameLevel() {
		return currentGameLevel;
	}
	public int getCurrentScore() {
		return currentScore;
	}
	public Integer getBestScore() {
		return bestScore;
	}
	public Pair<Integer, Integer> getCurrentProgress() {
		return currentProgress;
	}
	public Pair<Integer, Integer> getTopProgress() {
		return topProgress;
	}
	
	public void increaseClearedWordsCount() {
		++clearedWordsCount;
	}
	public void setCurrentGameLevel(int gameLevel) {
		currentGameLevel = gameLevel;
	}
	public void setCurrentScore(int currentScore) {
		this.currentScore = currentScore;
	}
	public void setBestScore(int bestScore) {
		this.bestScore = bestScore;
	}
	public void setCurrentProgress(Pair<Integer, Integer> currentProgress) {
		this.currentProgress = currentProgress;
	}
	public void setTopProgress(Pair<Integer, Integer> topProgress) {
		this.topProgress = topProgress;
	}
}
