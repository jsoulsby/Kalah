package kalah;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;

public class Board{
    private House[][] houses;
    private House playerOneStore;
    private House playerTwoStore;

    public Board(int numberOfHouses, int startingSeeds){
        playerOneStore = new House(0);
        playerTwoStore = new House(0);
        houses = new House[2][numberOfHouses];

        for(int i=0; i<2; i++){
            for(int j=0; j<numberOfHouses; j++){
                houses[i][j] = new House(startingSeeds);
            }
        }
    }

    public House getHouse(int player, int number){
        if (player == 1) {
            return houses[1][number - 1];
        } else {
            return houses[0][number -1];
        }
    }

    public House[] getPlayerHouses(int player){
        if (player == 1) {
            return houses[1];
        } else {
            return houses[0];
        }
    }

    public void incrementSeeds(House house, int seeds){
        house.setSeeds(house.getSeeds() + seeds);
    }

    public House getPlayerOneStore(){
        return playerOneStore;
    }

    public House getPlayerTwoStore(){
        return playerTwoStore;
    }
}
