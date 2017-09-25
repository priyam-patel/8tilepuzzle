
 /* ------------------------------------------------ 
  * Prog 4: 8 Tiles UI * 
  *
  * Class: CS 342, Fall 2016  
  * System: OS X, Eclipse IDE
  * Author Code Number: brio  
  *
  * -------------------------------------------------
  */

import java.util.ArrayList;

public interface Solver{
    public ArrayList<Direction> solve(Board begin, Board end);
}