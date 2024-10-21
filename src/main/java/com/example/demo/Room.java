package com.example.demo;

public class Room {
    private  double CELL_SIZE = 90;
    private  int WIDTH = 7;
    private  int HEIGHT = 7;
    private int goalRow = 6;
    private int goalCol = 6;

    private  int[][] maze = {
            {0, 1, 0, 0, 0, 1, 0},
            {0, 1, 0, 1, 1, 1, 0},
            {0, 0, 0, 0, 1, 0, 0},
            {1, 1, 0, 0, 1, 0, 1},
            {0, 0, 0, 0, 0, 0, 0},
            {0, 1, 1, 1, 1, 0, 1},
            {0, 0, 0, 0, 0, 0, 0},
    };

    public void setHEIGHT(int HEIGHT) {
        this.HEIGHT = HEIGHT;
    }

    public void setWIDTH(int WIDTH) {
        this.WIDTH = WIDTH;
    }

    public  void setCellSize(int cellSize) {
        CELL_SIZE = cellSize;
    }

    public void setMaze(int[][] maze) {
        this.maze = maze;
    }

    public  int getHEIGHT() {
        return HEIGHT;
    }

    public  double getCellSize() {
        return CELL_SIZE;
    }

    public  int[][] getMaze() {
        return maze;
    }

    public  int getWIDTH() {
        return WIDTH;
    }

    public void setGoalCol(int goalCol) {
        this.goalCol = goalCol;
    }

    public int getGoalCol() {
        return goalCol;
    }

    public void setGoalRow(int goalRow) {
        this.goalRow = goalRow;
    }

    public int getGoalRow() {
        return goalRow;
    }
}
