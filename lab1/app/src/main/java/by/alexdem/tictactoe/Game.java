package by.alexdem.tictactoe;

import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.widget.Button;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;

public class Game {
    private Player firstPlayer;
    private Player secondPlayer;
    private Player activePlayer;
    private int boardSize = 3;
    private char[][] board;
    private TableLayout boardView;
    private boolean isGameEnd = false;
    private TextView statusView;
    private int strokeNum = 0;

    Game(TableLayout boardView, TextView statusView, Player one, Player two, Button refreshBtn) {
        this.statusView = statusView;
        board = new char[boardSize][boardSize];
        firstPlayer = one;
        secondPlayer = two;
        activePlayer = one;
        this.boardView = boardView;

        refreshBtn.setOnClickListener(l -> {
            board = new char[boardSize][boardSize];
            for(int i = 0; i < boardView.getChildCount(); i++) {
                TableRow gameBoardRow = (TableRow) boardView.getChildAt(i);
                for (int j = 0; j < gameBoardRow.getChildCount(); j++) {
                    TextView boardCell =  (TextView)gameBoardRow.getChildAt(j);
                    boardCell.setBackgroundColor(ContextCompat.getColor(boardView.getContext(), R.color.colorPrimary));
                    boardCell.setText("");
                    statusView.setText("");
                    strokeNum = 0;
                    activePlayer = firstPlayer;
                    isGameEnd = false;
                }
            }
        });

        for(int i = 0; i < boardView.getChildCount(); i++) {
            TableRow gameBoardRow = (TableRow) boardView.getChildAt(i);
            for (int j = 0; j < gameBoardRow.getChildCount(); j++) {
                TextView boardCell =  (TextView)gameBoardRow.getChildAt(j);
                int finalI = i;
                int finalJ = j;
                boardCell.setOnClickListener((l) ->{
                    if(!isGameEnd) stroke(finalI, finalJ, boardCell);
                });
            }
        }
    }

    private void stroke(int i, int j, TextView boardCell) {
        if(board[i][j] == 0) {
            char symbol = activePlayer.getSymbol();
            board[i][j] = symbol;
            boardCell.setText(String.valueOf(symbol));
            strokeNum++;
            checkGameEnd(symbol);
            activePlayer = (activePlayer == firstPlayer)?secondPlayer:firstPlayer;

        }
    }

    private void checkGameEnd(char symbol) {
        //check rows and columns
        for(int i = 0; i < boardSize; i++) {
            int rowCount = 0;
            int colCount = 0;
            for(int j = 0; j < boardSize; j++) {
                if(board[i][j] == symbol) rowCount++;
                if(board[j][i] == symbol) colCount++;
            }
            if(rowCount == 3) {
                gameEnd(i,0,i,1,i,2);
                return;
            }
            if(colCount == 3) {
                gameEnd(0,i,1,i,2,i);
                return;
            }
        }
        //check diagonals
        if(board[0][0] == symbol && board[1][1] == symbol && board[2][2] == symbol) {
            gameEnd(0,0,1,1,2,2);
            return;
        }
        if(board[0][2] == symbol && board[1][1] == symbol && board[2][0] == symbol) {
            gameEnd(0,2,1,1,2,0);
            return;
        }
        checkForDraw();
    }

    private void gameEnd(int i0, int j0, int i1, int j1, int i2, int j2) {
        ((TableRow)boardView.getChildAt(i0)).getChildAt(j0).setBackgroundColor(ContextCompat.getColor(boardView.getContext(), R.color.colorAccent));
        ((TableRow)boardView.getChildAt(i1)).getChildAt(j1).setBackgroundColor(ContextCompat.getColor(boardView.getContext(), R.color.colorAccent));
        ((TableRow)boardView.getChildAt(i2)).getChildAt(j2).setBackgroundColor(ContextCompat.getColor(boardView.getContext(), R.color.colorAccent));
        statusView.setText((activePlayer == firstPlayer)?"First player won":"Second player won");
        isGameEnd = true;
    }

    public void setStateFromBundle(Bundle bundle) {
        isGameEnd = bundle.getBoolean("isGameEnd");
        if(bundle.getBoolean("active")) {
            activePlayer = firstPlayer;
        } else {
            activePlayer = secondPlayer;
        }
        strokeNum = bundle.getInt("strokeNum");
        board = (char[][]) bundle.getSerializable("board");
        for(int i = 0; i < boardView.getChildCount(); i++) {
            TableRow gameBoardRow = (TableRow) boardView.getChildAt(i);
            for (int j = 0; j < gameBoardRow.getChildCount(); j++) {
                TextView boardCell =  (TextView)gameBoardRow.getChildAt(j);
                boardCell.setText(String.valueOf(board[i][j]));
            }
        }
    }

    private void checkForDraw() {
        if(strokeNum == 9) {
            statusView.setText("Draw!");
            isGameEnd = true;
        }
    }

    public Player getFirstPlayer() {
        return firstPlayer;
    }

    public void setFirstPlayer(Player firstPlayer) {
        this.firstPlayer = firstPlayer;
    }

    public Player getSecondPlayer() {
        return secondPlayer;
    }

    public void setSecondPlayer(Player secondPlayer) {
        this.secondPlayer = secondPlayer;
    }

    public Player getActivePlayer() {
        return activePlayer;
    }

    public void setActivePlayer(Player activePlayer) {
        this.activePlayer = activePlayer;
    }

    public int getBoardSize() {
        return boardSize;
    }

    public void setBoardSize(int boardSize) {
        this.boardSize = boardSize;
    }

    public char[][] getBoard() {
        return board;
    }

    public void setBoard(char[][] board) {
        this.board = board;
    }

    public TableLayout getBoardView() {
        return boardView;
    }

    public void setBoardView(TableLayout boardView) {
        this.boardView = boardView;
    }

    public boolean isGameEnd() {
        return isGameEnd;
    }

    public void setGameEnd(boolean gameEnd) {
        isGameEnd = gameEnd;
    }

    public TextView getStatusView() {
        return statusView;
    }

    public void setStatusView(TextView statusView) {
        this.statusView = statusView;
    }

    public int getStrokeNum() {
        return strokeNum;
    }

    public void setStrokeNum(int strokeNum) {
        this.strokeNum = strokeNum;
    }
}
