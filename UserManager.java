package hangman;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Scanner;

public class UserManager {
	static private UserManager userManager = null; // Singleton
	private Map<String, String> userAccount;
	private Map<String, UserData> userData;
	private String currentUser; //현재 로그인한 유저
	private UserData currentUserData; // 현재 유저의 정보
	
	private UserManager() {
		currentUser = null;
		userAccount = new HashMap<>();
		userData = new HashMap<>();
		readUserAccount();
		readUserData();
	}
	
	static public UserManager getInstance() { // Singleton
		if(userManager == null)
			userManager = new UserManager();
		return userManager;
	}

    private void readUserAccount() {
    	Scanner fileIn = openFile("User.txt");
    	
    	while(fileIn.hasNext()) {
    		String id = fileIn.next();
    		String password = fileIn.next();
    		userAccount.put(id, password);
    	}
    	
    	fileIn.close();
    }
    private void readUserData() {
    	Scanner fileIn = openFile("UserData.txt");
    	
    	while(fileIn.hasNext()) {
    		String id = fileIn.next();
    		if(userAccount.containsKey(id)) {// 존재하지 않는 ID 확인
    			UserData data = new UserData();
    			data.read(fileIn);
        		userData.put(id, data);
    		}
    	}
    	
    	fileIn.close();
    }
    
    public boolean login(String id, String pwd){
        if(userAccount.containsKey(id)) {
        	if(pwd.equals(userAccount.get(id))) {
        		currentUser = id; // login User 저장
        		currentUserData = userData.get(id);
        		return true;
        	}
        }
        return false;
    }
    
    public String getCurrentUser() {
		return this.currentUser;
	}
    public int getClearedWordsCount() {
    	return currentUserData.getClearedWordsCount();
    }
    public int getCurrentGameLevel() {
    	return currentUserData.getCurrentGameLevel();
    }
    public int getCurrentScore() {
    	return currentUserData.getCurrentScore();
    }
    public int getBestScore() {
    	return currentUserData.getBestScore();
    }
    public Pair<Integer, Integer> getUserCurrentProgress() {
    	return currentUserData.getCurrentProgress();
    }
    public Pair<Integer, Integer> getUserTopProgress() {
    	return currentUserData.getTopProgress();
    }
    
    
    public void reflectDataUpdate(UserData updatedUserData) {
    	userData.put(currentUser, updatedUserData);
    }
    
    
    public void updateCurrentGameLevel(int gameLevel) {
    	currentUserData.setCurrentGameLevel(gameLevel);
    	reflectDataUpdate(currentUserData);
    }
    public void updateClearedWordsCount() {
    	currentUserData.increaseClearedWordsCount();
    	reflectDataUpdate(currentUserData);
    }
    public void updateScore(int currentScore) {
    	currentUserData.setCurrentScore(currentScore);
    	
    	int BestScore = currentUserData.getBestScore(); //최고 점수 갱신
    	if(currentScore > BestScore) {
    		currentUserData.setBestScore(currentScore);
    	}
    	
    	reflectDataUpdate(currentUserData);
    }
    public void updateCurrentProgress(Integer stage, Integer level) {
    	Pair<Integer, Integer> currentProgress = new Pair<>();
    	
    	currentProgress.makePair(stage, level);
    	currentUserData.setCurrentProgress(currentProgress);
    	
    	reflectDataUpdate(currentUserData);
    }
    public void updateTopProgress(Integer stage, Integer level) {
    	Pair<Integer, Integer> topProgress = currentUserData.getTopProgress();
    	
    	if(stage < topProgress.getFirstValue() || 
    	  (stage == topProgress.getFirstValue() && level <= topProgress.getSecondValue())) {
    		return;
    	}

    	Pair<Integer, Integer> newTopProgress = new Pair<>();
		newTopProgress.makePair(stage, level);
		currentUserData.setTopProgress(newTopProgress);
		reflectDataUpdate(currentUserData);
    }
    public void updateGameFinish() {
    	Pair<Integer, Integer> currentProgress = new Pair<>();
    	currentProgress.makePair(1, 1);
    	
    	currentUserData.setCurrentGameLevel(0);
    	currentUserData.setCurrentProgress(currentProgress);
    	currentUserData.setCurrentScore(0);
    	reflectDataUpdate(currentUserData);
    }
    

    public List<Pair<String, Integer>> getUserScoreList() { // 정렬된 리스트 반환
    	List<Pair<String, Integer>> sortedScoreList = new ArrayList<>();
    	
    	for(String userId : userData.keySet()) { // 0점 제외
    		UserData data = userData.get(userId);
    		Integer bestScore = data.getBestScore();
    		
    		if(bestScore > 0) { 
    			Pair<String, Integer> scoreSet = new Pair<>();
    			scoreSet.makePair(userId, bestScore);
    			sortedScoreList.add(scoreSet);
    		}
    	}
    	
    	// 간단한 수식을 반환 + 부수효과 X (read-only)이기에 람다를 사용해서 정렬 (Comparator 작성 대신 사용)
    	Collections.sort(sortedScoreList, (first, second) -> second.getSecondValue() - first.getSecondValue());
    	
    	
    	if (sortedScoreList.size() > 10) {
            sortedScoreList = sortedScoreList.subList(0, 10);
        }
        return sortedScoreList;
    }
    
    
    public String GetUserData() {
    	if(currentUser == null) {
    		return "현재 로그인된 사용자가 없습니다.";
    	}
    	
    	int clearedWordsCount = currentUserData.getClearedWordsCount();
    	if (clearedWordsCount == 0) {
            return currentUser + "님의 진행 상태가 없습니다.";
        } else {
        	Pair<Integer, Integer> currentProgress = currentUserData.getCurrentProgress();
        	Pair<Integer, Integer> topProgress = currentUserData.getTopProgress();
            
            String currentProgressData = currentProgress.getFirstValue() + "-" + currentProgress.getSecondValue(); // 현재 진행 중인 스테이지
            String currentTopProgress = topProgress.getFirstValue() + "-" + topProgress.getSecondValue(); // 최고 클리어 스테이지
            String currentGameLevel = null; // 현재 진행 중인 게임 레벨
            int bestScore = currentUserData.getBestScore();
            
            switch(currentUserData.getCurrentGameLevel()) {
            case 0:
            	currentGameLevel = "미선택";
            	break;
            case 1:
            	currentGameLevel = "이지 모드";
            	break;
            case 2:
            	currentGameLevel = "노멀 모드";
            	break;
            case 3:
            	currentGameLevel = "하드 모드";
            	break;
            default:
            	currentGameLevel = "알 수 없음";
            	break;
            }
            

            return String.format(
                    "%s님의 진행 상태\n\n현재 진행 중인 단계: %s (%s)\n최고 클리어 단계: %s\n최고 점수: %d\n지금까지 맞춘 단어 개수: %d",
                    currentUser, currentProgressData, currentGameLevel, currentTopProgress, bestScore, clearedWordsCount
            );
        }
    }
    
    
    public void writeUserData() { // txt 파일에 변경사항 반영
        try (PrintWriter writer = new PrintWriter(new FileWriter("UserData.txt"))) {
            for (Map.Entry<String, UserData> entry : userData.entrySet()) {
                String userId = entry.getKey();
                UserData data = entry.getValue();
                
                writer.print(userId + " ");
                data.write(writer);
            }
        } catch (IOException e) {
            System.out.println("Error writing to UserData.txt: " + e.getMessage());
            throw new RuntimeException(e);
        }
    }
    
    
    Scanner openFile(String filename){
        Scanner filein = null;
        try{
            filein = new Scanner(new File(filename));
        }catch (IOException e) {
            System.out.println("File open failed : " + filename);
            throw new RuntimeException(e);
        }
        return filein;
    }
}
