package hangman;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Scanner;

public class UserManager {
	private Map<String, String> userAccount;
	private Map<String, Integer> userScore;
	private String currentUser; //현재 로그인한 유저
	
	public UserManager() {
		currentUser = null;
		userAccount = new HashMap<>();
		userScore = new HashMap<>();
		readUserAccount();
		readUserScore();
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
    private void readUserScore() {
    	Scanner fileIn = openFile("Score.txt");
    	while(fileIn.hasNext()) {
    		String id = fileIn.next();
    		Integer score = fileIn.nextInt();
    		
    		if(userAccount.containsKey(id)) // 존재하지 않는 ID 확인 (DB관점으로 보면 userAccount 테이블의 기본키인 id 를 외부키로 참조해오는 과정)
    			userScore.put(id, score);
    	}
    	fileIn.close();
    }
    
    public boolean login(String id, String pwd){
        if(userAccount.containsKey(id)) {
        	if(pwd.equals(userAccount.get(id))) {
        		currentUser = id; // login User 저장
        		return true;
        	}
        	return false;
        }
        return false;
    }
    
    public void updateScore(int score) {
    	int currentScore = userScore.get(currentUser);
    	if(score > currentScore) 
    		userScore.put(currentUser, score); //더 높은 총점 획득시 수정
    }

    public List<Map.Entry<String, Integer>> getUserScoreList() { // 정렬된 리스트 반환
    	List<Map.Entry<String, Integer>> sortedEntries = new ArrayList<>();
    	
    	for(Map.Entry<String, Integer> enrty : userScore.entrySet()) { // 0점 제외
    		if(enrty.getValue() > 0) 
    			sortedEntries.add(enrty);
    	}
    	
        sortedEntries.sort(Map.Entry.comparingByValue(Comparator.reverseOrder()));
        return sortedEntries;
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
