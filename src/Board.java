
 /* ------------------------------------------------ 
  * Prog 4: 8 Tiles UI * 
  *
  * Class: CS 342, Fall 2016  
  * System: OS X, Eclipse IDE
  * Author Code Number: brio  
  *
  * -------------------------------------------------
  */

import java.util.Arrays;
import java.util.ArrayList;

public class Board implements Cloneable, Comparable {

    private int n; //The matrix is n*n
    private byte matrix[][];

    // blank tile position
    private int blankX;
    private int blankY;
    //history of movements
    private ArrayList<Direction> history;


    // constants necessary for the A* algorithm
    private int f;
    private int g; 
    private int h;  // heuristic value 

    // to create the search tree
    public static int childCount;
    public int childNum;
    
    
    public Board(int n){
        if(n<3) n=3;
        this.n = n;
        this.matrix = new byte[n][n];
        history = new ArrayList<Direction>();
    }

    public Board(int n, byte matrix[][]){
        this.n = n;
        this.matrix = matrix;
        history = new ArrayList<Direction>();
        findBlankPosition();
        calculateCost();
    }

    /**
     * Find the blank tile
     */
    private void findBlankPosition(){
        for(int y = 0; y < n; y++) {
            for(int x = 0; x < n; x++) {
                if(matrix[x][y] == 0) {
                    blankX = x;
                    blankY = y;
                    return;
                }
            }
        }
    }

    public int getSize() {
        return n;
    }

    public int getBlankX() {
        return blankX;
    }

    public int getBlankY() {
        return blankY;
    }
    
    public byte getValue(int x, int y){
        return matrix[x][y];
    }

    public ArrayList<Direction> getHistory(){
        return history;
    }

    public void clearHistory() {
        history.clear();
    }


    public void setHeuristic(int h) {
        this.h = h;
    }
    
    public void setPathCost(int f) {
        this.g = g;
    }
    
    public int getPathCost() {
        return g + h;
    }

    public int getHeuristic() {
        return h;
    }

    public int getNodeCost() {
        return g;
    }

    private void calculateCost() {
        h = 0;
        for(int i = 0; i < n; i++) {
            for(int j = 0; j < n; j++) {
                if(matrix[i][j] == 0) continue;

                
            }
        }
    }

    /**
    * Check if the move is valid
    */
    public boolean checkMove(Direction dir){
        switch(dir){
            case UP: return (blankY-1)>=0;
            case DOWN: return (blankY+1)<n;
            case LEFT: return (blankX-1)>=0;
            case RIGHT: return (blankX+1)<n;
            default: return false;
        }
    }


    public boolean move(Direction dir){
        int newX = blankX;
        int newY = blankY;
        if(!checkMove(dir)) return false;
        // calculate new position of the blank space
        switch(dir) {
            case UP:
                newY--;
                break;
            case DOWN:
                newY++;
                break;
            case LEFT:
                newX--;
                break;
            case RIGHT:
                newX++;
                break;
            default:
                return false;
        }
        //moving the board 
        matrix[blankX][blankY] = matrix[newX][newY];
        matrix[newX][newY] = 0;

        blankX = newX;
        blankY = newY;

        history.add(dir);
        
        calculateCost();
        
        return true;
    }

    
    public ArrayList<Board> expand(){
        ArrayList<Board> s = new ArrayList<Board>();
        Board temp = null;
        //if you can move to the left
        if(checkMove(Direction.LEFT) && (history.size()==0 || history.get(history.size()-1)!=Direction.RIGHT)){
            try{
                temp = (Board)this.clone();
            }catch(Exception e){System.out.println("NoClonado");}
            temp.move(Direction.LEFT);
            temp.g = g + 1;
            temp.childNum = ++childCount;
            s.add(temp);
        }
        //if you can move to the right
        if(checkMove(Direction.RIGHT) && (history.size()==0 || history.get(history.size()-1)!=Direction.LEFT)){
            try{
                temp = (Board)this.clone();
            }catch(Exception e){System.out.println("NoClonado");}
            temp.move(Direction.RIGHT);
            temp.g = g + 1;
            temp.childNum = ++childCount;
            s.add(temp);
        }
        //if you can move up
        if(checkMove(Direction.DOWN) && (history.size()==0 || history.get(history.size()-1)!=Direction.UP)){
            try{
                temp = (Board)this.clone();
            }catch(Exception e){System.out.println("NoClonado");}
            temp.move(Direction.DOWN);
            temp.g = g + 1;
            temp.childNum = ++childCount;
            s.add(temp);
        }
        //if you can move down
        if(checkMove(Direction.UP) && (history.size()==0 || history.get(history.size()-1)!=Direction.DOWN)){
            try{
                temp = (Board)this.clone();
            }catch(Exception e){System.out.println("NoClonado");}
            temp.move(Direction.UP);
            temp.g = g + 1;
            temp.childNum = ++childCount;
            s.add(temp);
        }
        return s;
    }
    
    @Override
    public Object clone(){
        Board b = new Board(n);
        for(int i = 0; i < history.size(); i++)
            b.history.add(history.get(i));
        b.blankX = blankX;
        b.blankY = blankY;
        
        b.g = this.g;
        b.h = this.h;
        
        for(int i=0; i<n; i++){
            for(int j=0; j<n; j++){
                b.matrix[i][j] = matrix[i][j];
            }
        }
        return (Object)b;
    }

    
    public String toString(){
        String r = "";
        for (int i=0; i<n; i++) {
            for (int j=0; j<n; j++) {
                r += matrix[j][i]+",";
            }
            r += "\n";
        }
        return r;
    }

    
    public boolean equals(Board board) {
        boolean result = true;
        for(int i = 0; i < n; i++)
            result = result && Arrays.equals(matrix[i], board.matrix[i]);
        return result;
    }

    
    public int compareTo(Object ob){
        Board board = (Board)ob;
        int sumA = 0;
        int sumB = 0;
        int nn = n*n;
        int valPos = 1;

        for(int i=0; i<n; i++){
            for(int j=0; j<n; j++){
                sumA += matrix[j][i]*valPos;
                sumB += board.matrix[j][i]*valPos;
                valPos*=nn;
            }    
        }
        return sumA-sumB;
    }
}