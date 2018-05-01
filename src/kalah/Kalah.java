package kalah;

import com.qualitascorpus.testsupport.IO;
import com.qualitascorpus.testsupport.MockIO;

/**
 * This class is the starting point for a Kalah implementation using
 * the test infrastructure.
 */
public class Kalah {

    public static final int QUIT_GAME = -1;
    public static final int TIE = 0;
    public static final int P1WIN = 1;
    public static final int P2WIN = 2;
    public static final int P1 = 1;
    public static final int P2 = 2;
    public static final int NUMBER_OF_HOUSES = 6;
    public static final int STARTING_SEEDS = 4;

    public static void main(String[] args) {
        new Kalah().play(new MockIO());
    }

    public void play(IO io) {
        initialiseGame(io);
        while (!isGameOver()){
            makeTurn();
        }
        if (!quitGame){
            userIO.drawWinner(playerOne.getScore(), playerTwo.getScore(), calculateWinner(), board);
        }
    }

    private Board board;
    private int currentPlayer;
    private boolean quitGame = false;
    private UserIO userIO;
    private Player playerOne;
    private Player playerTwo;



    protected void initialiseGame(IO io) {
        currentPlayer = P1;
        playerOne = new Player();
        playerTwo = new Player();
        board = new Board(NUMBER_OF_HOUSES, STARTING_SEEDS);
        userIO = new UserIO(io);
        userIO.drawBoard(board);
    }

    protected void makeTurn(){
        int selectedHouse = userIO.readInputHouse(currentPlayer);
        if (selectedHouse != QUIT_GAME){
            if (selectedHouseIsEmpty(selectedHouse)){
                userIO.drawMoveAgain(board);
            } else {
                int remainingSeeds = board.getHouse(currentPlayer,selectedHouse).getSeeds();

                // Keep track of wraps
                int playerCounter = currentPlayer;

                // Empty seeds of selected house
                board.getHouse(currentPlayer,selectedHouse).emptySeeds();

                // Loop while there are seeds left to distribute, starts at selected house + 1 if P1's turn
                // otherwise starts at selected house - 1 if P2's turn
                while (remainingSeeds > 0){
                    // Determine what side of board we are on
                    if (playerCounter == P1) {
                        selectedHouse++;
                        //Check if we need to wrap at a store
                        if (selectedHouse == NUMBER_OF_HOUSES + 1) {
                            playerCounter = P2;

                            // Only add to store if it is the correct players turn
                            if (currentPlayer == P1) {
                                board.getPlayerOneStore().incrementSeeds();
                            } else {
                                //Compensate for not putting anything in the store, essentially skipping the store
                                remainingSeeds++;
                            }
                        } else {
                            board.getHouse(P1, selectedHouse).incrementSeeds();
                            //Check if a capture has been made and change player
                            if (remainingSeeds == 1) {
                                if (currentPlayer == P1) {
                                    checkCapture(P1, selectedHouse);
                                }
                                changeCurrentPlayer();
                            }
                        }
                    }else {
                        selectedHouse--;
                        //Check if we need to wrap at a store
                        if (selectedHouse < 1){
                            playerCounter = P1;
                            if (currentPlayer == P2) {
                                board.getPlayerTwoStore().incrementSeeds();
                            } else {
                                //Compensate for not putting anything in the store, essentially skipping the store
                                remainingSeeds++;
                            }
                        } else {
                            board.getHouse(P2,selectedHouse).incrementSeeds();
                            //Check if a capture has been made and change player
                            if (remainingSeeds == 1) {
                                if(currentPlayer == P2) {
                                    checkCapture(P2, selectedHouse);
                                }
                                changeCurrentPlayer();
                            }
                        }
                    }
                    remainingSeeds--;
                }
                // Draw board and update scores after final seed is sown
                userIO.drawBoard(board);
                updatePlayerScores();
            }
        } else {
            // Player has quit game
            quitGame = true;
            userIO.drawGameOver();
            userIO.drawBoard(board);
        }
    }

    protected boolean selectedHouseIsEmpty(int house){
        return board.getHouse(currentPlayer,house).isEmpty();
    }

    protected void checkCapture(int player, int house){
        if (board.getHouse(player,house).getSeeds() == 1){
            if (player == P1) {
                if (board.getHouse(P2,house).getSeeds() > 0) {
                    board.getPlayerOneStore().addCapturedSeeds(board.getHouse(P2, house).getSeeds() + 1);
                    board.getHouse(P1, house).emptySeeds();
                    board.getHouse(P2, house).emptySeeds();
                }
            } else {
                if (board.getHouse(P1,house).getSeeds() > 0) {
                    board.getPlayerTwoStore().addCapturedSeeds(board.getHouse(P1, house).getSeeds() + 1);
                    board.getHouse(P1, house).emptySeeds();
                    board.getHouse(P2, house).emptySeeds();
                }
            }
        }
    }

    protected void changeCurrentPlayer(){
        if (currentPlayer == P1){
            currentPlayer = P2;
        } else {
            currentPlayer = P1;
        }
    }

    protected void updatePlayerScores(){
        int playerScore;
        // Update player 1 score
        playerScore = board.getPlayerOneStore().getSeeds();
        for (House house : board.getPlayerHouses(1)) {
            playerScore += house.getSeeds();
        }

        // Update player 2 score
        playerOne.setScore(playerScore);
        playerScore = board.getPlayerTwoStore().getSeeds();
        for (House house : board.getPlayerHouses(2)) {
            playerScore += house.getSeeds();
        }
        playerTwo.setScore(playerScore);
    }

    protected int calculateWinner(){
        int playerOneScore = playerOne.getScore();
        int playerTwoScore = playerTwo.getScore();
        if (playerOneScore > playerTwoScore){
            return P1WIN;
        } else if (playerOneScore < playerTwoScore){
            return P2WIN;
        } else {
            return TIE;
        }
    }

    protected boolean isGameOver(){
        // If game was quit prematurely
        if (quitGame){
            return true;
        }

        // Otherwise check if a player 1's houses are empty
        boolean flag = true;
        for(House house : board.getPlayerHouses(1)){
            if (house.getSeeds() != 0){
                flag = false;
            }
        }
        if (flag && currentPlayer == P1){
            return true;
        }

        // check if player 2's houses are empty
        flag = true;
        for(House house : board.getPlayerHouses(2)){
            if (house.getSeeds() != 0){
                flag = false;
            }
        }
        if (flag && currentPlayer == P2){
            return true;
        }

        // If we get here, game is not over
        return false;
    }
}
