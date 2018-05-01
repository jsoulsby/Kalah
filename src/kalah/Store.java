package kalah;

public class Store implements Pit{

    private int seeds;

    protected Store(int seeds){
        this.seeds = seeds;
    }

    public void addCapturedSeeds(int seeds){
        this.seeds += seeds;
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
