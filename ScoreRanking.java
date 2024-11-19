package hangman;
import java.io.*;
import java.util.*;

public class ScoreRanking {
    public static void main(String[] args) {
        String filePath = "scores.txt";  // 점수 파일 경로
        Map<String, Integer> scoreMap = new HashMap<>();
        List<Map.Entry<String, Integer>> sortedEntries;

        // 파일에서 점수 읽기
        try (BufferedReader reader = new BufferedReader(new FileReader(filePath))) {
            String line;
            while ((line = reader.readLine()) != null) {
                String[] parts = line.split(" ");
                String username = parts[0];
                int score = Integer.parseInt(parts[1]);
                if (score > 0) {  // 0점은 무시
                    scoreMap.put(username, score);
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
            return;
        }

        // Map.Entry 리스트로 변환 후 점수 기준 내림차순 정렬
        sortedEntries = new ArrayList<>(scoreMap.entrySet());
        sortedEntries.sort(Map.Entry.comparingByValue(Comparator.reverseOrder()));

        // 순위 매기기 및 출력
        rankAndPrint(sortedEntries);
    }

    private static void rankAndPrint(List<Map.Entry<String, Integer>> entries) {
        int rank = 1;
        int previousScore = entries.get(0).getValue(); // 최초 점수
        int cumulativeRank = 1; // 동일 점수 사용자 수

        for (Map.Entry<String, Integer> entry : entries) {
            if (entry.getValue() != previousScore) {  // 이전 점수와 다를 경우 순위 업데이트
                rank += cumulativeRank;
                cumulativeRank = 0;
            }
            System.out.println(rank +"등" +  ": " + entry.getKey() + " - Score: " + entry.getValue());
            previousScore = entry.getValue();
            cumulativeRank++;
        }
    }
}
