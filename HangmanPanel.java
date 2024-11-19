package hangman;

import javax.swing.*;
import java.awt.*;

public class HangmanPanel extends JPanel {
    private int wrongAttempts = 0;

    public void setWrongAttempts(int wrongAttempts) {
        this.wrongAttempts = wrongAttempts;
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        g.setColor(Color.BLACK);

        // 행거의 기본 구조 
        g.drawLine(50, 200, 150, 200); // 바닥
        g.drawLine(100, 200, 100, 50); // 기둥
        g.drawLine(100, 50, 200, 50);  // 상단 막대
        g.drawLine(200, 50, 200, 80);  // 매다는 줄

        // 틀린 시도에 따라 그림 그리기
        if (wrongAttempts > 0) g.drawOval(180, 80, 40, 40); // 머리
        if (wrongAttempts > 1) g.drawLine(200, 120, 200, 170); // 몸통
        if (wrongAttempts > 2) g.drawLine(200, 130, 170, 150); // 왼쪽 팔
        if (wrongAttempts > 3) g.drawLine(200, 130, 230, 150); // 오른쪽 팔
        if (wrongAttempts > 4) g.drawLine(200, 170, 180, 200); // 왼쪽 다리
        if (wrongAttempts > 5) {
            g.drawLine(200, 170, 220, 200); // 오른쪽 다리
            g.setColor(Color.RED);
            g.drawString("x_x", 190, 105); // 얼굴 표정
            g.drawString("Game Out", 170, 230); // Game Out 문구
        }
    }
}
