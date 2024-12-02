package hangman;

import javax.swing.*;
import java.awt.*;

public class HangmanPanel extends JPanel {
    private int wrongAttempts;

    public void setWrongAttempts(int wrongAttempts) {
        this.wrongAttempts = wrongAttempts;
        repaint();
    }
    public HangmanPanel() {
        setPreferredSize(new Dimension(400, 400)); // 원하는 크기로 설정
        setOpaque(false);
    }


    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);

        // 배경을 지우지 않음으로써 투명도를 유지
        Graphics2D g2d = (Graphics2D) g.create();

        // 행맨 그림 그리기
        g2d.setStroke(new BasicStroke(5)); // 두께 설정
        g2d.setColor(Color.WHITE);
        int width = getWidth();
        int height = getHeight();

        // 단두대 기본 구조
        g2d.drawLine(width / 2, height - 20, width / 2, 20); // 기둥
        g2d.drawLine(width / 2, 20, width / 2 + 100, 20); // 가로대
        g2d.drawLine(width / 2 + 100, 20, width / 2 + 100, 50); // 밧줄

        // 오답 횟수에 따라 행맨 그리기
        if (wrongAttempts >= 1) {
            // 머리
            g2d.drawOval(width / 2 + 80, 50, 40, 40);
        }
        if (wrongAttempts >= 2) {
            // 몸통
            g2d.drawLine(width / 2 + 100, 90, width / 2 + 100, 150);
        }
        if (wrongAttempts >= 3) {
            // 왼쪽 팔
            g2d.drawLine(width / 2 + 100, 100, width / 2 + 70, 130);
        }
        if (wrongAttempts >= 4) {
            // 오른쪽 팔
            g2d.drawLine(width / 2 + 100, 100, width / 2 + 130, 130);
        }
        if (wrongAttempts >= 5) {
            // 왼쪽 다리
            g2d.drawLine(width / 2 + 100, 150, width / 2 + 80, 200);
        }
        if (wrongAttempts >= 6) {
            // 오른쪽 다리
            g2d.drawLine(width / 2 + 100, 150, width / 2 + 120, 200);
        }

        g2d.dispose();
    }

}
