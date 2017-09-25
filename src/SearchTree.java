
 /* ------------------------------------------------ 
  * Prog 4: 8 Tiles UI * 
  *
  * Class: CS 342, Fall 2016  
  * System: OS X, Eclipse IDE
  * Author Code Number: brio  
  *
  * -------------------------------------------------
  */

import java.io.*;
import java.util.ArrayList;
import java.util.PriorityQueue;

public class SearchTree implements Solver {

    private int expandedNodes = 0;  


    /**
     * Solve and return a list of solutions 
     */
    public ArrayList<Direction> solve(Board begin, Board end){
        ArrayList<Direction> sequence = new ArrayList<Direction>();

        ArrayList<Board> explored = new ArrayList<Board>();
        PriorityQueue<Board> priorityQueue = new PriorityQueue(20, new Node());
        
        begin.childNum = 0;
        begin.childCount = 0;
        begin.setHeuristic(tilesOutOfPlaceHeuristic(begin, end));
        priorityQueue.add(begin);

        while(priorityQueue.size() != 0) {
            Board node = priorityQueue.poll();

            // If we arrived at a solution, get the sequence 
            if(node.equals(end)) {
                sequence = node.getHistory();
                break;
            }

            explored.add(node);

            ArrayList<Board> children = node.expand();

            for(Board child : children) {
                child.setHeuristic(tilesOutOfPlaceHeuristic(child, end));

                if(!explored.contains(child) && !priorityQueue.contains(child)) {
                    priorityQueue.add(child);
                    expandedNodes++;
                }
            }

        }
        
        return sequence;
    }


    public int getExpandedNodesCount() {
        return expandedNodes;
    }

    /**
     * Heuristic for the number of tiles out of place 
     */
    public int tilesOutOfPlaceHeuristic(Board board, Board target) {
        int h = 0;
        for(int i = 0; i < board.getSize(); i++) {
            for(int j = 0; j < board.getSize(); j++) {
                if(board.getValue(i, j) != target.getValue(i , j))
                    h++;
            }
        }
        return h;
    }
}
