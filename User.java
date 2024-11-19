package hangman;

import java.util.Scanner;

public class User {
    private String id;
    private String pwd;
    private int score;
    void read(Scanner scan){
        id = scan.next();
        pwd = scan.next();
        score = 0;
    }

    public User (String id, String pwd){
        this.id = id;
        this.pwd = pwd;
    }

    public void updateScore(int newScore) {
        this.score = newScore;
    }

    public int getScore() {
        return this.score;
    }

    public boolean login(String inputId, String inputPwd) {
        return this.id.equals(inputId) && this.pwd.equals(inputPwd);
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getPwd() {
        return pwd;
    }

    public void setPwd(String pwd) {
        this.pwd = pwd;
    }

    public void setScore(int score) {
        this.score = score;
    }
}
