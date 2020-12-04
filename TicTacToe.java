/*
  The assignment was to clean up this file : https://github.com/getmubarak/cleancode/blob/master/TicTacToe.java
  Assignment Incomplete . 
*/

import java.util.Arrays;
import java.util.InputMismatchException;
import java.util.Scanner;

class Board {

    public int SIZE = 9;
    public int ROWS = 3;
    public String[] board; // TODO - public breaking abstraction . Used from outside

    public int NUM_WINS = 8;
    private int[][] winCombinations = {
        { 0, 1, 2 },
        { 3, 4, 5 },
        { 6, 7, 8 },
        { 0, 3, 6 },
        { 1, 4, 7 },
        { 2, 5, 8 },
        { 0, 4, 8 },
        { 2, 4, 6 }
    };

    public Board() {
        board = new String[SIZE];
        for (int a = 0; a < SIZE; a++) {
            board[a] = String.valueOf(a + 1);
        }
    }

    public String GetNthWinCombination(int n) {
        String line = "";
        for (int i = 0; i < ROWS; ++i) {
            line += board[winCombinations[n][i]];
        }
        return line;
    }

    public boolean isSlotVacant(int slotNumber) {
        return board[slotNumber - 1].equals(String.valueOf(slotNumber));
    }

    public boolean isValidSlot(int slotNumber) {
        return 0 < slotNumber && slotNumber <= SIZE;
    }

    public int countEmptySlots() {
        int count = 0;
        for (int a = 0; a < SIZE; a++) {
            if (isSlotVacant(a + 1)) count++;
        }
        return count;
    }

    public boolean hasEmptySlot() {
        return countEmptySlots() >= 1;
    }
};

enum GameState {
    inProgress,
    draw,
    xWon,
    oWon
}

class InputOutput {

    private Scanner in ;

    public InputOutput() { 
      in = new Scanner(System.in);
    }

    public void DisplayBoard(Board board1) {

        System.out.println("/---|---|---\\");
        for (int i = 0, k = 0; i < board1.ROWS; ++i) {
            for (int j = 0; j < board1.ROWS; ++j, ++k) {
                System.out.print("| " + board1.board[k] + " ");
            }
            System.out.println("|");
            System.out.println("|-----------|");
        }
    }

    public void DisplayWelcomeMessage(Board board1) { 
        System.out.println("Welcome to 2 Player Tic Tac Toe.");
        System.out.println("--------------------------------");
        DisplayBoard(board1); // TODO - Tight coupling
        System.out.println("X's will play first. ");
    }

    public void DisplayInputPrompt(String turn) {
        System.out.println(turn + "'s turn; enter a slot number to place " + turn + " in : ");
    }

    public void DisplayInputTypeError() {
        System.out.println("Invalid input; re-enter slot number : ");
    }

    public void DisplayInputRangeError(int size) {
        System.out.println("Invalid input; re-enter slot number between 1 to " + size + " : ");
    }

    public void DisplayNonEmptySlotError() {
        System.out.println("Slot already taken; re-enter slot number:");
    }

    public void DisplayDraw() {
        System.out.println("It's a draw! Thanks for playing.");
    }

    public int GetInputSlotNumber() { // TODO - Magic number -1 for failure
        int numInput = -1;
        try {
            numInput = in.nextInt();
        } catch (InputMismatchException e) {
            in.nextLine();
            DisplayInputTypeError();
        }
        return numInput;
    }

    public void DisplayWinner(GameState winnerState) {
        String winner;
        if (winnerState == GameState.xWon)
            winner = "X";
        else
            winner = "O";
        System.out.println("Congratulations! " + winner + "'s have won! Thanks for playing.");

    }
};

class TicTacToeGame {

    private InputOutput io;
    private Board mBoard;
    private GameState mState;
    private String turn;

    public TicTacToeGame() {
        io = new InputOutput();
        mBoard = new Board();
        mState = GameState.inProgress;
        turn = "X";
    }

    private void toggleTurn() {
        if (turn.equals("X")) {
            turn = "O";
        } else {
            turn = "X";
        }
    }

    private void checkWinner() {

        if (mState != GameState.inProgress) return;

        for (int a = 0; a < mBoard.NUM_WINS && mState == GameState.inProgress; ++a) {
            String line = mBoard.GetNthWinCombination(a);

            if (line.equals("XXX")) {
                mState = GameState.xWon;
            } else if (line.equals("OOO")) {
                mState = GameState.oWon;
            }
        }

        if (mState == GameState.inProgress && !mBoard.hasEmptySlot()) {
            mState = GameState.draw;
        }
    }

    private int GetValidInput(String turn) {
        int numInput = -1;
        io.DisplayInputPrompt(turn);
        while (true) {
            numInput = io.GetInputSlotNumber();
            if (!mBoard.isValidSlot(numInput)) {
                io.DisplayInputRangeError(mBoard.SIZE);
                continue;
            }
            if (!mBoard.isSlotVacant(numInput)) {
                io.DisplayNonEmptySlotError();
                continue;
            }
            break;
        }
        return numInput;
    }

    public void DisplayGameOver() {
        if (mState == GameState.draw) {
            io.DisplayDraw();
        } else if (mState == GameState.xWon || mState == GameState.oWon) {
            io.DisplayWinner(mState);
        }
    }

    public void PlayGame() {
        io.DisplayWelcomeMessage(mBoard);
        while (mState == GameState.inProgress) {
            int numInput = GetValidInput(turn);
            mBoard.board[numInput - 1] = turn;
            toggleTurn();
            io.DisplayBoard(mBoard);
            checkWinner();
        }
        DisplayGameOver();
    }

}

public class Main {
    public static void main(String[] args) {
        TicTacToeGame game = new TicTacToeGame();
        game.PlayGame();
    }
}
