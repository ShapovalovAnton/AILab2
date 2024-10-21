package com.example.demo;

import javafx.concurrent.Task;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Label;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.GridPane;
import javafx.scene.control.Slider;
import javafx.application.Platform;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.scene.control.Button;
import java.util.ArrayList;

public class HelloController {
    private QLearning Q_Learning;
    private Room room;
    boolean createGoal=false;
    boolean isEditable=true;
    boolean isPath=false;
    Rectangle goal;
    Rectangle start;

    @FXML
    private GridPane RoomGridPane;
    @FXML
    private Button CreateGoalButton;
    @FXML
    private Button CreateRoomButton;
    @FXML
    private Button StartLearningButton;
    @FXML
    private Slider SizeRoomSlider;
    @FXML
    private Label SizeRoomLabel;
    @FXML
    private Label GoalModeLabel;

    @FXML
    private Label LearningLabel;

    public void initialize() {
        room = new Room();

        this.SizeRoomSlider.valueProperty().addListener((observable, oldValue, newValue) ->
        {
            SizeRoomLabel.setText("Розмір: "+String.format("%.0f", newValue)+"х"+String.format("%.0f", newValue));
        });

        int goalRow=room.getGoalRow();
        int goalCol=room.getGoalCol();
        updateGridPane(goalRow,goalCol,0,0);
    }

    public void onCreateRoomButtonClick()
    {
        int size=(int) SizeRoomSlider.getValue();
        room.setWIDTH(size);
        room.setHEIGHT(size);
        room.setCellSize(630/size);
        room.setGoalCol(size-1);
        room.setGoalRow(size-1);
        int[][] maze = new int[size][size];
        room.setMaze(maze);
        RoomGridPane.getChildren().clear();
        int goalRow=room.getGoalRow();
        int goalCol=room.getGoalCol();
        updateGridPane(goalRow,goalCol,0,0);

    }

    public void updateGridPane(int goalRow, int goalCol, int currentRow, int currentCol)
    {
        for (int row = 0; row < room.getHEIGHT(); row++) {
            for (int col = 0; col < room.getWIDTH(); col++) {
                Rectangle cell = new Rectangle(room.getCellSize(), room.getCellSize());
                cell.setStroke(Color.BLACK);
                if (room.getMaze()[row][col] == 1) {
                    cell.setFill(Color.BLACK);
                } else {
                    cell.setFill(Color.LIGHTGRAY);
                }
                if(row==goalRow&&col==goalCol) {
                    cell.setFill(Color.RED);
                    goal=cell;
                }
                RoomGridPane.add(cell, col, row);
                cell.setOnMouseClicked(event -> handleCellClick(event));
                if(isPath&&row==currentRow&&col==currentCol) {
                    cell.setFill(Color.BLUE);
                }
            }
        }

    }

    private void handleCellClick(MouseEvent event) {
        Rectangle rect = (Rectangle) event.getSource();
        int column = GridPane.getColumnIndex(rect);
        int row = GridPane.getRowIndex(rect);
        if(isEditable)
        {
            int[][] maze = room.getMaze();
            if(!createGoal){
                if (rect.getFill() == Color.BLACK) {
                    rect.setFill(Color.LIGHTGRAY);
                    maze[row][column]=0;
                } else if (rect.getFill() == Color.LIGHTGRAY){
                    rect.setFill(Color.BLACK);
                    maze[row][column]=1;

                }
            } else{
                rect.setFill(Color.RED);
                maze[row][column]=0;
                goal.setFill(Color.LIGHTGRAY);
                room.setGoalCol(column);
                room.setGoalRow(row);
                goal=rect;
            }
        }
        else{
            if (rect.getFill() == Color.LIGHTGRAY){
                rect.setFill(Color.BLUE);
                if(start!=null) start.setFill(Color.LIGHTGRAY);
                start=rect;
                Q_Learning.setSrartRow(row);
                Q_Learning.setStartCol(column);
            }

        }


    }

    public void onCreateGoalButtonClick()
    {
        if(createGoal){
            GoalModeLabel.setText("Режим встановлення перешкод");
            createGoal=false;
            CreateGoalButton.setText("Встановити ціль");
        } else{
            GoalModeLabel.setText("Режим встановлення цілі");
            createGoal=true;
            CreateGoalButton.setText("Встановити перешкоду");
        }


    }
    public void onStartLearningButtonClick()
    {
        Q_Learning= new QLearning(room);
        isEditable=false;
        StartLearningButton.setText("СТАРТ");
        StartLearningButton.setOnAction(event -> onStartButtonClick());
        SizeRoomSlider.setVisible(false);
        GoalModeLabel.setVisible(false);
        CreateGoalButton.setVisible(false);
        CreateRoomButton.setVisible(false);
        LearningLabel.setVisible(true);


    }

    public void onStartButtonClick()
    {
        if(start!=null){
            int startRow = Q_Learning.srartRow;
            int startCol = Q_Learning.startCol;
            int len = Q_Learning.getLen();

            ArrayList<Integer> optimalPath = Q_Learning.findOptimalPath(startRow, startCol);

            System.out.println("Оптимальний шлях:");
            for (int state : optimalPath) {
                int row = state / len;
                int col = state % len;
                System.out.println("Рядок: " + row + ", Колонка: " + col);
            }
            ArrayList<int[]> optimalPathCoordinates = new ArrayList<>();

            for (int state : optimalPath) {
                int row = state / len;
                int col = state % len;
                optimalPathCoordinates.add(new int[] {row, col});
            }
            isPath=true;

            Task<Void> task = new Task<Void>() {
                @Override
                public Void call() throws InterruptedException {
                    for (int[] coordinates : optimalPathCoordinates) {
                        int row = coordinates[0];
                        int col = coordinates[1];

                        Platform.runLater(() -> updateGridPane(room.getGoalRow(), room.getGoalCol(), row, col));

                        Thread.sleep(300);
                    }

                    return null;
                }
            };

            new Thread(task).start();
        }
    }


    public void printMatrix(int[][] matrix) {
        for (int i = 0; i < matrix.length; i++) {
            for (int j = 0; j < matrix[i].length; j++) {
                System.out.print(matrix[i][j] + "\t");
            }
            System.out.println();
        }
    }
}