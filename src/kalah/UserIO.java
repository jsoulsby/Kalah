package kalah;

import com.qualitascorpus.testsupport.IO;

import static kalah.Kalah.*;

public class UserIO {

    private IO io;

    protected UserIO(IO io){
        this.io = io;
    }

    protected int readInputHouse(int player){
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

    protected void drawBoard(Board board){
        drawTopOrBotLine();
        drawP2Line(board);
        drawMidLine();
        drawP1Line(board);
        drawTopOrBotLine();
    }

    protected void drawMoveAgain(Board board){
        io.println("House is empty. Move again.");
        drawBoard(board);
    }

    protected void drawWinner(int p1Score, int p2Score, int winner, Board board){
            io.println("Game over");
            drawBoard(board);
            io.println("\tplayer 1:"+p1Score);
            io.println("\tplayer 2:"+p2Score);
            switch (winner){
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

    protected void drawGameOver(){
        io.println("Game over");
    }

    protected void drawTopOrBotLine(){
        io.print("+----+");
        for (int i = 0; i < NUMBER_OF_HOUSES; i++){
            io.print("-------+");
        }
        io.println("----+");
    }

    protected void drawP2Line(Board board){
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

    protected void drawMidLine(){
        io.print("|    |");
        for (int i = 0; i < NUMBER_OF_HOUSES - 1; i++){
            io.print("-------+");
        }
        io.println("-------|    |");
    }

    protected void drawP1Line(Board board){
        if (board.getPlayerTwoStore().getSeeds() < 10) {
            io.print("|  ");
        } else{
            io.print("| ");
        }
        io.print(board.getPlayerTwoStore().getSeeds() + " |");
        for (int i = 1; i < board.getBoardSize() + 1;  i++) {
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
