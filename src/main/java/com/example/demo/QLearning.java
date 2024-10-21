package com.example.demo;
import java.util.Arrays;
import java.util.Random;
import java.util.ArrayList;

public class QLearning {
    private  int[][] maze;
    private  int numStates;
    private  int[][] R;
    private  double[][] Q;
    int size;
    int len;
    int srartRow;
    int startCol;
    static int goalState;
    double gamma=0.8;


    QLearning(Room room)
    {
        maze=room.getMaze();
        len=maze.length;
        size = len*len;
        R=new int[size][size];
        Q = new double [size] [size];
        generateR(room.getGoalRow(), room.getGoalCol());
        trainAgent();
    }


    // Алгоритм Q-навчання
    public void trainAgent() {
        Random rand = new Random();

        for (int episode = 0; episode < 1000; episode++) {
            // Вибір випадкового початкового стану
            int row, column;
            do {
                column = rand.nextInt(len);
                row = rand.nextInt(len);
            } while (maze[row][column]!=0);

            int state =toState(row, column);
            boolean cont=true;
            //Переходимо до наступних вершин
            while (cont) {
                // Вибираємо доступну дію
                int action = selectAction(state, rand);

                // Оновлюємо значення в матриці Q
                int nextState = action;
                double qValue = Q[state][action];
                double maxQValueNextState = getMaxQ(nextState);

                Q[state][action] = R[state][action]+gamma*maxQValueNextState;
                if(state==goalState) cont = false;

                // Переходимо до нового стану
                state = nextState;
            }
        }
        printQMatrix();
    }

    // Вибір доступної дії
    private int selectAction(int state, Random rand) {
        ArrayList<Integer> possibleActions = new ArrayList<>();
        for (int action = 0; action < size; action++) {
            if (R[state][action] != -1) { // Тільки стани 0 або 100
                possibleActions.add(action);
            }
        }

        // Вибір випадкової дії з доступних
        if (possibleActions.size() > 0) {
            int randomIndex = rand.nextInt(possibleActions.size());
            return possibleActions.get(randomIndex);
        }
        return state;
    }

    // Мксимальне Q для формули
    private double getMaxQ(int state) {
        double maxQ = Double.NEGATIVE_INFINITY;
        for (int action = 0; action < size; action++) {
            if (R[state][action] != -1 && Q[state][action] > maxQ) {
                maxQ = Q[state][action];
            }
        }
        return maxQ;
    }
 //Ініціалізація R
    private void generateR(int goalRow, int goalCol) {
        int n = maze.length;
        goalState = toState(goalRow, goalCol);

        for (int row = 0; row < n; row++) {
            for (int col = 0; col < n; col++) {
                int currentState = toState(row, col);
                    for (int l=0; l<size; l++)
                    {
                        R[currentState][l]=-1;
                    }
                if (maze[row][col]==0){
                    if(row>0) if(maze[row-1][col]==0)//Верх
                    {
                        int nextState=toState(row-1, col);
                        if(nextState==goalState)R[currentState][nextState]=100;
                        else R[currentState][nextState]=0;
                    }
                    if(row+1<n) if(maze[row+1][col]==0)//Вниз
                    {
                        int nextState=toState(row+1, col);
                        if(nextState==goalState)R[currentState][nextState]=100;
                        else R[currentState][nextState]=0;
                    }
                    if(col+1<n) if(maze[row][col+1]==0)//Вправо
                    {
                        int nextState=toState(row, col+1);
                        if(nextState==goalState)R[currentState][nextState]=100;
                        else R[currentState][nextState]=0;
                    }
                    if(col>0) if(maze[row][col-1]==0)//Вліво
                    {
                        int nextState=toState(row, col-1);
                        if(nextState==goalState)R[currentState][nextState]=100;
                        else R[currentState][nextState]=0;
                    }

                }
            }
        }
        R[goalState][goalState]=100;
        printMatrix(R);

    }

    public int toState(int row, int column)
    {
        return maze.length*row+column;
    }
    public double[][] getQ() {
        return Q;
    }

    public int[][] getR() {
        return R;
    }

    public void printMatrix(int[][] matrix) {
        for (int i = 0; i < matrix.length; i++) {
            for (int j = 0; j < matrix[i].length; j++) {
                System.out.print(matrix[i][j] + "\t");
            }
            System.out.println();
        }
    }

    public void printQMatrix() {
        System.out.println("Q matrix:");
        for (int i = 0; i < Q.length; i++) {
            for (int j = 0; j < Q[i].length; j++) {
                System.out.printf("%.2f\t", Q[i][j]);
            }
            System.out.println();
        }
    }

    public void setSrartRow(int srartRow) {
        this.srartRow = srartRow;
    }

    public void setStartCol(int startCol) {
        this.startCol = startCol;
    }
    public ArrayList<Integer> findOptimalPath(int startRow, int startCol) {
        ArrayList<Integer> path = new ArrayList<>();
        int currentState = toState(startRow, startCol);
        path.add(currentState);

        // Продовжуємо поки не досягнемо цільового стану
        while (currentState != goalState) {
            int bestAction = getBestAction(currentState);
            currentState = bestAction;
            path.add(currentState);
        }

        return path;
    }

    private int getBestAction(int state) {
        double maxQValue = Double.NEGATIVE_INFINITY;
        int bestAction = state;

        // Знаходимо дію з максимальним значенням Q для поточного стану
        for (int action = 0; action < size; action++) {
            if (Q[state][action] > maxQValue && R[state][action] != -1) {
                maxQValue = Q[state][action];
                bestAction = action;
            }
        }

        return bestAction;
    }

    public int getLen() {
        return len;
    }
}
