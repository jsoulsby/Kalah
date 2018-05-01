package kalah;

public class Player {

    private int score;

    protected Player(){
        score = 0;
    }

    protected void setScore(int score){
        this.score = score;
    }

    protected int getScore(){
        return score;
    }
}
