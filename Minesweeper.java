import java.lang.*;
import java.util.*;

public class Minesweeper{
    public static void main(String[] args){
        Scanner cin = new Scanner(System.in);
        int x, y;
        int r, c, mines;
        Board b;
        while(true) {
            try {
                System.out.println("Please enter the size of the board followed by number of mines (-1 for default):");
                r = cin.nextInt();
                if (r == -1) {
                    b = new Board();
                } else {
                    c = cin.nextInt();
                    mines = cin.nextInt();
                    b = new Board(r, c, mines);
                }
                while (true) {
                    b.printBoard();
                    System.out.println("Enter coordinates - row and column(-1 to quit):");
                    x = cin.nextInt();
                    if (x == -1) {
                        System.out.println("Game Stopped :)");
                        break;
                    }
                    y = cin.nextInt();
                    int out = b.makeMove(x, y);
//                System.out.println("Out is " + out);
                    if (out == 0) {
                        continue;
                    } else if (out == 3) {
                        System.out.println("Invalid Point. Please try again...");
                    } else if (out == 2) {
                        System.out.println("You have already expored that cell. Please try another.");
                    } else if (out == 1) {
                        b.printTrueBoard();
                        System.out.println("You Win!!! :-)");
                        break;
                    } else {
                        b.printTrueBoard();
                        System.out.println("You Loose!!! :-(");
                        break;
                    }
                }
                System.out.println("Do you wish to play a new game..?(y or n):");
                if (cin.next().equals("y")) {
                    continue;
                } else {
                    break;
                }
            }
            catch(InputMismatchException ime){
                // report(ime);
                System.out.println("Oh ooh! Looks like there is invalid input.");
            }
            catch(IndexOutOfBoundsException iobe){
                // report(iobe);
                System.out.println("Oh ooh! Looks like there is an internal error.");
            }
            finally{
                System.out.println("Your error has been reported. Please restart the game...");
                System.exit(0);
            }
        }
        System.out.println("Thanks for playing me :)\nBye....\n\n");
    }
}

class Board{
    int rows, cols;
    char[][] board;
    int[][] visited;
    int visit;
    int mines;
    class Point{
        int x, y;
        public Point(int x, int y){
            this.x = x;
            this.y = y;
        }
        public boolean equals(Object p){
            if(this.x == ((Point)(p)).x && this.y == ((Point)(p)).y)
                return true;
            return false;
        }

        public String toString(){
            return "("+x+", "+y+")";
        }

        public int hashCode(){
            int code = toString().hashCode();
            return code;
        }
    }
    Set<Point> mineLocations;
    int moves;
    public Board(){
        rows = 9;
        cols = 9;
        board = new char[9][9];
        mines = 10;
        moves = 0;
        initBoard();
    }

    public Board(int rows, int cols, int mines){
        this.rows = rows;
        this.cols = cols;
        board = new char[rows][cols];
        this.mines = mines;
        moves = 0;
        initBoard();
    }

    public void initBoard(){
        visit = 0;
        mineLocations = new HashSet<>();
        visited = new int[rows][cols];
        int ij = 0;
        for(int i=0;i<this.rows;i++){
            for(int j=0;j<this.cols;j++){
                board[i][j] = 'X';
            }
        }
        Random rand = new Random();
        while(mineLocations.size() < mines){
            ij++;
            mineLocations.add(new Point(rand.nextInt(rows), rand.nextInt(cols)));
        }
//        System.out.println(mineLocations);
        return;
    }

    public void printTrueBoard(){
        for(int k = -1;k<this.rows;k++){
            if(k == -1){
                System.out.print("  ");
                continue;
            }
            System.out.print(k+" ");
        }
        System.out.println();
        for(int i=0; i<rows; i++){
            for(int j=-1;j<cols;j++){
                if(j == -1){
                    System.out.print(i+" ");
                    continue;
                }
                if(mineLocations.contains(new Point(i, j))){
                    System.out.print("B ");
                }
                else {
                    System.out.print(board[i][j] + " ");
                }
            }
            System.out.println();
        }
    }

    public void printBoard(){
        for(int k = -1;k<this.rows;k++){
            if(k == -1){
                System.out.print("  ");
                continue;
            }
            System.out.print(k+" ");
        }
        System.out.println();
        for(int i=0; i<rows; i++){
            for(int j=-1;j<cols;j++){
                if(j == -1){
                    System.out.print(i+" ");
                    continue;
                }
                System.out.print(board[i][j]+" ");
            }
            System.out.println();
        }
//        System.out.println(mineLocations);
    }

    /*
    Arguments:
    rx - row number (Zero indexed)
    cx - column number (Zero indexed)
    returns 0 if game continues.
    returns -1 if game ends
    returns 1 if player wins
    returns 2 if already explored move.
     */

    public int makeMove(int rx, int cx){
        if(rx < 0 || rx >= rows || cx < 0 || cx >= cols)
            return 3;
        if(mineLocations.contains(new Point(rx, cx))){
            return -1;
        }
        if(visited[rx][cx] == 1){
            return 2;
        }
        move(rx, cx);
        if(check())
            return 1;
        return 0;
    }

    private void move(int x, int y){
        if(x < 0 || x >= rows || y < 0 || y>= cols)
            return;
        if(visited[x][y] == 1){
            return;
        }
        visited[x][y] = 1;
        visit++;
        int count = 0;
        boolean[] flags = new boolean[8];
        for(int i=0;i<flags.length;i++){
            flags[i] = true;
        }
        if(x-1 >= 0 && y+1 < cols){
            if(mineLocations.contains(new Point(x-1, y+1))){
                count++;
                flags[0] = false;
            }
        }
        if(x-1 >= 0 && y-1 >= 0){
            if(mineLocations.contains(new Point(x-1, y-1))){
                count++;
                flags[1] = false;
            }
        }
        if(x+1 < rows && y-1 >= 0){
            if(mineLocations.contains(new Point(x+1, y-1))){
                count++;
                flags[2] = false;
            }
        }
        if(x+1 < rows && y+1 < cols){
            if(mineLocations.contains(new Point(x+1, y+1))){
                count++;
                flags[3] = false;
            }
        }
        if(x-1 >= 0){
            if(mineLocations.contains(new Point(x-1, y))){
                count++;
                flags[4] = false;
            }
        }
        if(y-1 >= 0){
            if(mineLocations.contains(new Point(x, y-1))){
                count++;
                flags[5] = false;
            }
        }
        if(x+1 < rows){
            if(mineLocations.contains(new Point(x+1, y))){
                count++;
                flags[6] = false;
            }
        }
        if(y+1 < cols){
            if(mineLocations.contains(new Point(x, y+1))){
                count++;
                flags[7] = false;
            }
        }
        if(count == 0){
            board[x][y] = ' ';
            if(flags[0]){
                move(x-1, y+1);
            }
            if(flags[1]){
                move(x-1, y-1);
            }
            if(flags[2]){
                move(x+1, y-1);
            }
            if(flags[3]){
                move(x+1, y+1);
            }
            if(flags[4]){
                move(x-1, y);
            }
            if(flags[5]){
                move(x, y-1);
            }
            if(flags[6]){
                move(x+1, y);
            }
            if(flags[7]){
                move(x, y+1);
            }
        }
        else{
            board[x][y] = (char)('0' + count);
        }
    }

    public boolean check(){
        if((visit + mines) == (rows*cols))
            return true;
        return false;
    }
}