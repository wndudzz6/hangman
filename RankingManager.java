package hangman;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.TreeMap;

public class RankingManager {
    /*ArrayList<User> rankingList;

    public RankingManager(ArrayList<User> users){
        this.rankingList = new ArrayList<>(users);
        updateRanking();
    }

    public void updateRanking(){
        rankingList.sort((u1, u2) -> Integer.compare(u1.getScore(), u2.getScore()));
    }

    public void printRanking() {
        System.out.println("==== 현재 랭킹 ====");
        int rank = 1;
        for (User user : rankingList) {
            System.out.println(rank + "위: " + user.getId() + " - 점수: " + user.getScore());
            rank++;
        }
    }

    public void updateUserScore(User user, int newScore) {
        System.out.println("사용자의 점수를 업데이트합니다: " + user.getId());
        user.updateScore(newScore);
        updateRanking();
        printRanking(); // 점수 변경 후 업데이트된 랭킹 출력
    }*/

}
