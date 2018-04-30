package kalah;

import com.qualitascorpus.testsupport.IO;
import com.qualitascorpus.testsupport.MockIO;

/**
 * This class is the starting point for a Kalah implementation using
 * the test infrastructure.
 */
public class Kalah {

    public final int QUIT_GAME = -1;
    public final int TIE = 0;
    public final int P1WIN = 1;
    public final int P2WIN = 2;
    public final int P1 = 1;
    public final int P2 = 2;
    public final int NUMBER_OF_HOUSES = 6;
    public final int STARTING_SEEDS = 4;

    public static void main(String[] args) {
        new Kalah().play(new MockIO());
    }

    public void play(IO io) {
        initialiseBoard(NUMBER_OF_HOUSES, STARTING_SEEDS);
        drawBoard(io);
        while (!isGameOver()){
            makeTurn(io);
        }
        if (!quitGame){
            drawWinner(io);
        }
    }

    private Board board;
    private int player;
    private boolean quitGame = false;
    private int playerOneScore;
    private int playerTwoScore;

    public void initialiseBoard(int numberOfHouses, int startingSeeds) {
        player = P1;
        playerOneScore = 0;
        playerTwoScore = 0;
        board = new Board(numberOfHouses, startingSeeds);
    }

    public void makeTurn(IO io){
        int house = readInputHouse(io);
        if (house != QUIT_GAME){
            if (selectedHouseIsEmpty(house)){
                drawMoveAgain(io);
            } else {
                int seeds = board.getHouse(player,house).getSeeds();

                // Keep track of wraps
                int playerCounter = player;

                // Empty seeds of selected house
                board.getHouse(player,house).setSeeds(0);

                // Loop while there are seeds left to distribute, starts at selected house + 1 if P1's turn
                // otherwise starts at selected house -1 if P2's turn
                while (seeds > 0){
                    // Determine what side of board we are on
                    if (playerCounter == P1) {
                        house++;
                        //Check if we need to wrap at a store
                        if (house == NUMBER_OF_HOUSES + 1) {
                            playerCounter = P2;

                            // Only add to store if it is the correct players turn
                            if (player == P1) {
                                board.incrementSeeds(board.getPlayerOneStore(), 1);
                            } else {
                                //Compensate for not putting anything in the store, essentially skipping the store
                                seeds++;
                            }
                        } else {
                            board.incrementSeeds(board.getHouse(P1, house), 1);
                            //Check if a capture has been made and change player
                            if (seeds == 1) {
                                if (player == P1) {
                                    checkCapture(P1, house);
                                }
                                changePlayer();
                            }
                        }
                    }else {
                        house--;
                        //Check if we need to wrap at a store
                        if (house < 1){
                            playerCounter = P1;
                            if (player == P2) {
                                board.incrementSeeds(board.getPlayerTwoStore(), 1);
                            } else {
                                //Compensate for not putting anything in the store, essentially skipping the store
                                seeds++;
                            }
                        } else {
                            board.incrementSeeds(board.getHouse(P2,house), 1);
                            //Check if a capture has been made and change player
                            if (seeds == 1) {
                                if(player == P2) {
                                    checkCapture(P2, house);
                                }
                                changePlayer();
                            }
                        }
                    }
                    seeds--;
                }
                drawBoard(io);
            }
        } else {
            quitGame(io);
        }
    }

    public int readInputHouse(IO io){
        int house;
        if (player == P1){
            house = io.readInteger("Player P1's turn - Specify house number or 'q' to quit: ", 1, NUMBER_OF_HOUSES, QUIT_GAME, "q");
        } else {
            house = io.readInteger("Player P2's turn - Specify house number or 'q' to quit: ", 1, NUMBER_OF_HOUSES, QUIT_GAME, "q");
            if (house != QUIT_GAME) {
                house = (NUMBER_OF_HOUSES + 1 - house);
            }
        }
        return house;
    }

    public boolean selectedHouseIsEmpty(int house){
        if (board.getHouse(player,house).isEmpty()) {
            return true;
        }
        return false;
    }

    public void drawMoveAgain(IO io){
        io.println("House is empty. Move again.");
        drawBoard(io);
    }

    public void checkCapture(int player, int house){
        if (board.getHouse(player,house).getSeeds() == 1){
            if (player == P1) {
                if (board.getHouse(P2,house).getSeeds() > 0) {
                    board.incrementSeeds(board.getPlayerOneStore(), board.getHouse(P2, house).getSeeds() + 1);
                    board.getHouse(P1, house).setSeeds(0);
                    board.getHouse(P2, house).setSeeds(0);
                }
            } else {
                if (board.getHouse(P1,house).getSeeds() > 0) {
                    board.incrementSeeds(board.getPlayerTwoStore(), board.getHouse(P1, house).getSeeds() + 1);
                    board.getHouse(P1, house).setSeeds(0);
                    board.getHouse(P2, house).setSeeds(0);
                }
            }
        }
    }

    public void changePlayer(){
        if (player == P1){
            player = P2;
        } else {
            player = P1;
        }
    }

    public void quitGame(IO io){
        io.println("Game over");
        drawBoard(io);
        quitGame = true;
    }

    public int calculateWinner(){
        int playerOneScore = getPlayerScore(1);
        int playerTwoScore = getPlayerScore(2);
        if (playerOneScore > playerTwoScore){
            return P1WIN;
        } else if (playerOneScore < playerTwoScore){
            return P2WIN;
        } else {
            return TIE;
        }
    }

    public int getPlayerScore(int player){
        int playerScore;
        if (player == P1) {
            playerScore = board.getPlayerOneStore().getSeeds();
            for (House house : board.getPlayerHouses(1)) {
                playerScore += house.getSeeds();
            }
        } else {
            playerScore = board.getPlayerTwoStore().getSeeds();
            for (House house : board.getPlayerHouses(2)) {
                playerScore += house.getSeeds();
            }
        }
        return playerScore;
    }

    public void drawWinner(IO io){
        io.println("Game over");
        drawBoard(io);
        io.println("\tplayer 1:"+getPlayerScore(1));
        io.println("\tplayer 2:"+getPlayerScore(2));
        switch (calculateWinner()){
            case TIE:
                io.println("A tie!");
                break;
            case P1WIN:
                io.println("Player 1 wins!");
                break;
            case P2WIN:
                io.println("Player 2 wins!");
                break;
        }
    }

    public boolean isGameOver(){
        if (quitGame){
            return true;
        }
        boolean flag = true;

        for(House house : board.getPlayerHouses(1)){
            if (house.getSeeds() != 0){
                flag = false;
            }
        }
        if (flag && player == P1){
            return true;
        }
        flag = true;
        for(House house : board.getPlayerHouses(2)){
            if (house.getSeeds() != 0){
                flag = false;
            }
        }
        if (flag && player == P2){
            return true;
        }
        return false;
    }

    public void drawBoard(IO io) {
        drawTopOrBotLine(io);
        drawP2Line(io);
        drawMidLine(io);
        drawP1Line(io);
        drawTopOrBotLine(io);
    }

    public void drawTopOrBotLine(IO io){
        io.print("+----+");
        for (int i = 0; i < NUMBER_OF_HOUSES; i++){
            io.print("-------+");
        }
        io.println("----+");
    }

    public void drawP2Line(IO io){
        // Player 2 Store
        io.print("| P2 |");

        // Player 1 houses
        for (int i = 1; i < NUMBER_OF_HOUSES + 1;  i++) {
            io.print(" " + (NUMBER_OF_HOUSES+1-i));
            if (board.getHouse(2, i).getSeeds() > 9){
                io.print("[" + board.getHouse(2,i).getSeeds() + "] |");
            } else {
                io.print("[ "+ board.getHouse(2,i).getSeeds() + "] |");
            }
        }

        // Player 1 store seeds
        if (board.getPlayerOneStore().getSeeds() < 10) {
            io.println("  " + board.getPlayerOneStore().getSeeds() + " |");
        } else{
            io.println(" " + board.getPlayerOneStore().getSeeds() + " |");
        }
    }

    public void drawMidLine(IO io){
        io.print("|    |");
        for (int i = 0; i < NUMBER_OF_HOUSES - 1; i++){
            io.print("-------+");
        }
        io.println("-------|    |");
    }

    public void drawP1Line(IO io){
        if (board.getPlayerTwoStore().getSeeds() < 10) {
            io.print("|  ");
        } else{
            io.print("| ");
        }
        io.print(board.getPlayerTwoStore().getSeeds() + " |");
        for (int i = 1; i < NUMBER_OF_HOUSES + 1;  i++) {
            io.print(" " + i);
            if (board.getHouse(1, i).getSeeds() > 9){
                io.print("[" + board.getHouse(1,i).getSeeds() + "] |");
            } else {
                io.print("[ "+ board.getHouse(1,i).getSeeds() + "] |");
            }
        }
        io.println(" P1 |");
    }
}
