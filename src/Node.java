
 /* ------------------------------------------------ 
  * Prog 4: 8 Tiles UI * 
  *
  * Class: CS 342, Fall 2016  
  * System: OS X, Eclipse IDE
  * Author Code Number: brio  
  *
  * -------------------------------------------------
  */

import java.util.Comparator;

public class Node implements Comparator<Board> {

    public int compare(Board b1, Board b2) {
        int diff = b1.getPathCost() - b2.getPathCost();
        if(diff < 0) return -1;
        else if(diff == 0) return 0;
        else return 1;
    }
    
}