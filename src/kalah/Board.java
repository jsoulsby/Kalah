package kalah;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;

public class Board{
    private House[][] houses;
    private Store playerOneStore;
    private Store playerTwoStore;
    private int boardSize;

    protected Board(int numberOfHouses, int startingSeeds){
        playerOneStore = new Store(0);
        playerTwoStore = new Store(0);
        houses = new House[2][numberOfHouses];
        boardSize = numberOfHouses;

        for(int i=0; i<2; i++){
            for(int j=0; j<numberOfHouses; j++){
                houses[i][j] = new House(startingSeeds);
            }
        }
    }

    protected House getHouse(int player, int number){
        if (player == 1) {
            return houses[1][number - 1];
        } else {
            return houses[0][number -1];
        }
    }

    protected House[] getPlayerHouses(int player){
        if (player == 1) {
            return houses[1];
        } else {
            return houses[0];
        }
    }

    protected int getBoardSize(){
        return boardSize;
    }

    protected Store getPlayerOneStore(){
        return playerOneStore;
    }

    protected Store getPlayerTwoStore(){
        return playerTwoStore;
    }
}
