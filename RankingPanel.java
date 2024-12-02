package hangman;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.*;
import java.util.List;

public class RankingPanel extends JPanel {
    private Controller controller;
    private UserManager userManager;
    private JList<String> rankingList;
    private JScrollPane scrollPane;
    private JButton backButton;
    private DefaultListModel<String> listModel;
    private JLabel titleLabel;
    private Image backgroundImage;

    public RankingPanel(Controller controller) {
        this.controller = controller;
        userManager = UserManager.getInstance();
        backgroundImage = new ImageIcon(getClass().getResource("hangmanback.jpeg")).getImage();
        listModel = new DefaultListModel<>();

        loadScores();

        rankingList = new JList<>(listModel);
        rankingList.setCellRenderer(new CustomCellRenderer());
        rankingList.setOpaque(false); // JList의 배경도 투명하게 설정

        scrollPane = new JScrollPane(rankingList);
        scrollPane.setOpaque(false); // JScrollPane 배경도 투명하게 설정
        scrollPane.getViewport().setOpaque(false); // Viewport도 투명하게 설정
        scrollPane.setBorder(BorderFactory.createEmptyBorder(100, 50, 100, 50)); // 스크롤 패널의 상하 패딩 설정

        JPanel listPanel = new JPanel(new BorderLayout());
        listPanel.add(scrollPane, BorderLayout.CENTER);
        listPanel.setBackground(new Color(0, 0, 0, 100)); // 순위 목록에 검정색 반투명 배경 추가

        backButton = new JButton("메인메뉴로 돌아가기");
        backButton.addActionListener(e -> controller.showMainMenu());

        titleLabel = new JLabel("Hangman Game Ranking", JLabel.CENTER);
        titleLabel.setFont(new Font("Serif", Font.BOLD, 36)); // 제목 글씨 크기 조정
        titleLabel.setForeground(Color.WHITE); // 흰색 글씨

        setLayout(new BorderLayout());
        add(titleLabel, BorderLayout.NORTH);
        add(listPanel, BorderLayout.CENTER); // 리스트 패널 추가
        add(backButton, BorderLayout.SOUTH);
    }

    private class CustomCellRenderer extends DefaultListCellRenderer {
        @Override
        public Component getListCellRendererComponent(JList<?> list, Object value, int index, boolean isSelected, boolean cellHasFocus) {
            JLabel label = (JLabel) super.getListCellRendererComponent(list, value, index, isSelected, cellHasFocus);
            label.setHorizontalAlignment(SwingConstants.CENTER);
            label.setOpaque(true); // 렌더러의 배경을 명시적으로 설정
            label.setForeground(Color.WHITE); // 선택되지 않은 아이템은 흰색 글씨
            label.setBackground(new Color(0, 0, 0, 0)); // 배경색 투명 설정
            Font font = new Font("Serif", Font.PLAIN, 25); // 폰트 "Serif"로 설정
            label.setFont(font); // 폰트 크기 18 설정

            if (isSelected) {
                label.setBackground(new Color(0, 0, 0, 0)); // 선택된 아이템 배경색 투명
            } else {
                label.setBackground(new Color(0, 0, 0, 0)); // 비선택 아이템 배경색 투명
            }
            return label;
        }
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        g.drawImage(backgroundImage, 0, 0, getWidth(), getHeight(), this); // 배경 이미지 그리기
    }

    
    public void loadScores() {
        listModel.clear(); // 기존 모델 초기화

        List<Pair<String, Integer>> sortedScoreList = userManager.getUserScoreList(); // userManager로부터 저장되어 있는 점수 리스트 받아오기

        int rank = 1;
        int previousScore = sortedScoreList.isEmpty() ? 0 : sortedScoreList.get(0).getSecondValue(); // 최초 점수
        int cumulativeRank = 0; // 동일 점수 사용자 수

        for (Pair<String, Integer> scoreSet : sortedScoreList) {
            if (scoreSet.getSecondValue() != previousScore) {  // 이전 점수와 다를 경우 순위 업데이트
                rank += cumulativeRank;
                cumulativeRank = 0;
            }
            listModel.addElement(rank + "등 " + scoreSet.getFirstValue() + " - " + scoreSet.getSecondValue() + "점");
            previousScore = scoreSet.getSecondValue();
            cumulativeRank++;
        }
    }
}
