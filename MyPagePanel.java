package hangman;

import javax.swing.*;
import java.awt.*;

public class MyPagePanel extends JPanel {
    private Controller controller;
    private UserManager userManager;
    private JTextArea progressArea; // 진행 상황 표시용 텍스트 영역
    private Image backgroundImage;

    public MyPagePanel(Controller controller) {
        this.controller = controller;
        userManager = UserManager.getInstance();
        backgroundImage = new ImageIcon(getClass().getResource("hangmanback.jpeg")).getImage();
        setLayout(new BorderLayout());

        JLabel titleLabel = new JLabel("My Page", SwingConstants.CENTER);
        titleLabel.setFont(new Font("Serif", Font.BOLD, 36)); // 큰 글씨로 제목 설정
        titleLabel.setForeground(Color.WHITE); // 흰색 글씨로 변경

        // 텍스트 영역 설정
        progressArea = new JTextArea();
        progressArea.setEditable(false);
        progressArea.setForeground(Color.WHITE); // 텍스트 영역 흰색 글자
        progressArea.setFont(new Font("Serif", Font.PLAIN, 25)); // 글씨 크기 설정
        progressArea.setOpaque(false); // 배경 투명 처리

        // 스크롤 패인 설정
        JScrollPane scrollPane = new JScrollPane(progressArea);
        scrollPane.setOpaque(false);
        scrollPane.getViewport().setOpaque(false);
        scrollPane.setBorder(BorderFactory.createEmptyBorder(50, 100, 50, 100)); // 테두리 내부 여백 설정

        JPanel textPanel = new JPanel(new BorderLayout());
        textPanel.add(scrollPane, BorderLayout.CENTER);
        textPanel.setBackground(new Color(0, 0, 0, 100)); // 반투명 검정색 배경

        JButton backButton = new JButton("메인메뉴로 돌아가기");
        backButton.addActionListener(e -> controller.showMainMenu());

        add(titleLabel, BorderLayout.NORTH);
        add(textPanel, BorderLayout.CENTER); // 중앙에 텍스트 패널 추가
        add(backButton, BorderLayout.SOUTH);
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        g.drawImage(backgroundImage, 0, 0, getWidth(), getHeight(), this); // 배경 이미지 그리기
    }

    // MyPage 데이터를 로드하는 메서드
    public void loadMyPage() {
        String progress = userManager.GetUserData(); // 진행 상황 가져오기
        progressArea.setText(progress); // 텍스트 영역에 표시
    }
}
