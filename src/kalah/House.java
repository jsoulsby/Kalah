package kalah;

public class House {
    private int seeds;

    public House(int seeds){
        this.seeds = seeds;
    }

    public int getSeeds(){
        return seeds;
    }

    public void setSeeds(int seeds){
        this.seeds = seeds;
    }

    public boolean isEmpty(){
        if (seeds == 0){
            return true;
        }
        return false;
    }
}
