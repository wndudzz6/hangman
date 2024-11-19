package hangman;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.*;
import java.util.List;

public class RankingPanel extends JPanel {
    private Controller controller;
    private JList<String> rankingList;
    private JScrollPane scrollPane;
    private JButton backButton;
    private DefaultListModel<String> listModel;

    public RankingPanel(Controller controller) {
        this.controller = controller;
        listModel = new DefaultListModel<>();

        loadScores();

        rankingList = new JList<>(listModel);
        rankingList.setCellRenderer(new ListCellRenderer<String>() {
            @Override
            public Component getListCellRendererComponent(JList<? extends String> list, String value, int index, boolean isSelected, boolean cellHasFocus) {
                JLabel label = new JLabel(value);
                label.setHorizontalAlignment(JLabel.LEFT);
                label.setOpaque(true);
                Font font = label.getFont();
                float newSize = font.getSize() * 1.5f;  // 기존 폰트 크기의 1.5배
                label.setFont(font.deriveFont(newSize));
                if (isSelected) {
                    label.setBackground(list.getSelectionBackground());
                    label.setForeground(list.getSelectionForeground());
                } else {
                    label.setBackground(list.getBackground());
                    label.setForeground(list.getForeground());
                }
                return label;
            }
        });
        scrollPane = new JScrollPane(rankingList);
        backButton = new JButton("Back to Menu");

        backButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                controller.showMainMenu();  // 메인 메뉴로 돌아가기
            }
        });

        setLayout(new BorderLayout());
        add(scrollPane, BorderLayout.CENTER);
        add(backButton, BorderLayout.SOUTH);
    }

    public void loadScores() {
    	listModel.clear(); //기존 모델 초기화
        
        List<Map.Entry<String, Integer>> sortedEntries = controller.userManager.getUserScoreList(); // userManager로부터 저장되어 있는 점수 리스트 받아오기
        sortedEntries.sort(Map.Entry.comparingByValue(Comparator.reverseOrder()));

        int rank = 1;
        int previousScore = sortedEntries.isEmpty() ? 0 : sortedEntries.get(0).getValue(); // 최초 점수
        int cumulativeRank = 0; // 동일 점수 사용자 수

        for (Map.Entry<String, Integer> entry : sortedEntries) {
            if (entry.getValue() != previousScore) {  // 이전 점수와 다를 경우 순위 업데이트
                rank += cumulativeRank;
                cumulativeRank = 0;
            }
            listModel.addElement( rank +"등 " + "" + entry.getKey() + " - " + entry.getValue()+"점");
            previousScore = entry.getValue();
            cumulativeRank++;
        }
    }
}
