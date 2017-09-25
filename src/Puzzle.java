
 /* ------------------------------------------------ 
  * Prog 4: 8 Tiles UI * 
  *
  * Class: CS 342, Fall 2016  
  * System: OS X, Eclipse IDE
  * Author Code Number: brio  
  *
  * -------------------------------------------------
  */


import java.awt.Dimension;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import java.util.ArrayList;
import java.util.Random;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JButton;
import javax.swing.JRadioButton;
import javax.swing.JLabel;
import javax.swing.JTextField;
import javax.swing.JComboBox;
import javax.swing.ButtonGroup;
import javax.swing.BoxLayout;
import javax.swing.border.EmptyBorder;


public class Puzzle implements Runnable {

    private JFrame window;
    private PuzzleGUI puzzle;
    private JPanel optionsPanel;
    private JButton solveBtn;
    private JButton scrambleBtn;
    private JButton closeBtn;
    private int size = 3;
    private Solver solver;
    
    
    public Puzzle() {
        puzzle = new PuzzleGUI(createBoard(size), createBoard(size), size);
    }

    public Board createBoard(int size) {
	byte b[][] = new byte[size][size];

	byte k = 0;
	for(int i = 0; i < size; i++)
	    for(int j = 0; j < size; j++)
		b[j][i] = k++;

	return new Board(size, b);
    }

    
    public void setSolver(Solver solver) {
        this.solver = solver;
    }

    
    public void start() {
        solveBtn = new JButton("Solve");
        solveBtn.addActionListener(new ActionListener() {
		public void actionPerformed(ActionEvent e) {
		    System.out.print("Now solving...");

           solver = new SearchTree();

                    new Thread(Puzzle.this, "Execution of the solution").start();
		}

            });
    
        scrambleBtn = new JButton("Shuffle board");
        scrambleBtn.addActionListener(new ActionListener() {
		public void actionPerformed(ActionEvent e) {
                    scramble();
		}
            });
        
        closeBtn = new JButton("Exit");
        closeBtn.addActionListener(new ActionListener() {
		public void actionPerformed(ActionEvent e) {
                    System.exit(0);
		}
            });
      
        optionsPanel = new JPanel();
        optionsPanel.setLayout(new BoxLayout(optionsPanel, BoxLayout.Y_AXIS));
        optionsPanel.add(new JLabel("INSTRUCTIONS:"));
        optionsPanel.add(new JLabel("  1. Shuffle the board. "));
        optionsPanel.add(new JLabel("  2. Solve automatically using the solve button"));
        optionsPanel.add(new JLabel(" 3. Solve manually by using the arrow keys to point to the adjacent tile you want the blank tile to switch with"));
        optionsPanel.add(new JLabel("  "));
        optionsPanel.add(new JLabel(" "));
        optionsPanel.add(scrambleBtn);
        optionsPanel.add(solveBtn);
        optionsPanel.add(closeBtn);

        window = new JFrame("8-Tile Puzzle GUI - Project 4");
        window.setLayout(new BorderLayout());
        window.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        window.add(puzzle, BorderLayout.WEST);
        window.add(optionsPanel, BorderLayout.EAST);
        window.pack();
        window.setLocationRelativeTo(null);
        window.setResizable(false);
        window.setVisible(true);
        
        puzzle.start();
    }

    
    public void scramble() {
        Random r = new Random();
        int steps = r.nextInt(10) + 10;
        Direction dir = Direction.LEFT;
        for(int i = 0; i < steps; i++) {
            switch(r.nextInt(4)) {
            case 0:
                dir = Direction.LEFT;
                break;
            case 1:
                dir = Direction.RIGHT;
                break;
            case 2:
                dir = Direction.UP;
                break;
            case 3:
                dir = Direction.DOWN;
                break;
            }
            puzzle.getBoard().move(dir);
        }
        puzzle.getBoard().clearHistory();
        puzzle.checkObjectiveReached();
    }
    
    
    public void solve() {

        optionsPanel.repaint();
	ArrayList<Direction> sequence = solver.solve(
            (Board)puzzle.getBoard().clone(),
	    (Board)puzzle.getObjectiveBoard().clone());
	
        optionsPanel.repaint();

        if(sequence.isEmpty()) return;
        for(Direction dir : sequence) {
            puzzle.moveBlank(dir);
            try { Thread.sleep(500); }
            catch(InterruptedException e) {}
        }
    }

    
    public void run() {
        solve();
    }

}