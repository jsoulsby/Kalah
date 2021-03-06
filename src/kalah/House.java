package kalah;

public class House implements Pit {
    private int seeds;

    protected House(int seeds){
        this.seeds = seeds;
    }

    @Override
    public int getSeeds(){
        return seeds;
    }

    @Override
    public void incrementSeeds(){
        seeds += 1;
    }

    @Override
    public void emptySeeds(){
        seeds = 0;
    }

    @Override
    public boolean isEmpty(){
        if (seeds == 0){
            return true;
        }
        return false;
    }
}
