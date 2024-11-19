package hangman;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class GameTimer {
    private Timer timer;
    private int elapsedSeconds;
    
    public GameTimer(JLabel timeLabel) {
    	elapsedSeconds = 0;
    	
        timer = new Timer(1000, new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                elapsedSeconds++;
                timeLabel.setText("Time: " + elapsedSeconds + "s");
            }
        });
    }

    public void start() {
        elapsedSeconds = 0;
        timer.start();
    }

    public void stop() {
        timer.stop();
    }

    public int getElapsedSeconds() { //경과시간 반환
        return elapsedSeconds;
    }
}