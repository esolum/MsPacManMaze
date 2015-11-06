import java.util.*;
/**
 * Write a description of class Maze here.
 * 
 * @author (your name) 
 * @version (a version number or a date)
 */
public class Maze
{
    // named constants
    public static final int ACTIVE = 0;
    public static final int EXPLORER_WIN = 1;
    public static final int MONSTER_WIN = 2;
    
    // instance variables
	private Square[][] maze;
	private ArrayList<RandomOccupant> randOccupants;
	private Explorer explorer;
	private int rows;
	private int cols;

	/**
	 * Constructor for objects of class Maze
	 */
	public Maze(Square[][] maze, int rows, int cols)
	{
	    this.maze = maze;
		this.rows = rows;
		this.cols = cols;    
		
		randOccupants = new ArrayList<RandomOccupant>();
	}
	
	// queries
	public Square getSquare(int row, int col) { return maze[row][col]; }
	public int rows() {return rows;}
	public int cols() {return cols;}
	public String explorerName() {return explorer.name();}
    public Explorer getExplorer() {return explorer;}
	public RandomOccupant getRandomOccupant(int index) {return randOccupants.get(index);}
	public int getNumRandOccupants() {return randOccupants.size();}
	
	public void addRandomOccupant(RandomOccupant ro) { randOccupants.add(ro);}
	public void setExplorer(Explorer e) {explorer = e;}
	
	public void explorerMove(int key)
	{
	    explorer.move(key);
	}
	
	public void randMove()
	{
	    for (int i=0; i<randOccupants.size(); i++)
	       randOccupants.get(i).move();
	}
	
    public int gameStatus()
    {
        int status = ACTIVE;
        
        if (foundAllTreasures()) {
            status = EXPLORER_WIN;
        }
        else {
            for (int i=0; i<randOccupants.size(); i++)
               if (randOccupants.get(i) instanceof Monster && 
                   randOccupants.get(i).location() == explorer.location())
                  status = MONSTER_WIN;
        }
        return status;
    }
	
	private boolean foundAllTreasures()
	{        
	    boolean foundAll = true;
        // check for win
        for (int i=0; i<randOccupants.size();i++)
           if (randOccupants.get(i) instanceof Treasure && !((Treasure)randOccupants.get(i)).found())
              foundAll = false;  
              
        return foundAll;
    }
    
    public void lookAround(Square s) 
    {
        int row = s.row();
        int col = s.col();
        
        resetInView();
        s.setInView(true);
        if (!maze[row][col].wall(Square.UP) && row-1 >= 0)
           maze[row-1][col].setInView(true);
        if (!maze[row][col].wall(Square.RIGHT) && col+1 < cols)
           maze[row][col+1].setInView(true);
        if (!maze[row][col].wall(Square.DOWN) && row+1 < rows)
           maze[row+1][col].setInView(true);
        if (!maze[row][col].wall(Square.LEFT) && col-1 >= 0)
           maze[row][col-1].setInView(true);
        if ((!maze[row][col].wall(Square.LEFT) && (col-1 >= 0) && !maze[row][col-1].wall(Square.UP)) ||
            (!maze[row][col].wall(Square.UP) && (row-1 >= 0) && !maze[row-1][col].wall(Square.LEFT)))
           maze[row-1][col-1].setInView(true);
        if ((!maze[row][col].wall(Square.LEFT) && (col-1 >= 0) && !maze[row][col-1].wall(Square.DOWN)) ||
            (!maze[row][col].wall(Square.DOWN) && (row+1 < rows) && !maze[row+1][col].wall(Square.LEFT)))
           maze[row+1][col-1].setInView(true);
        if ((!maze[row][col].wall(Square.RIGHT) && (col+1 < cols) && !maze[row][col+1].wall(Square.UP)) ||
            (!maze[row][col].wall(Square.UP) && (row-1 >= 0) && !maze[row-1][col].wall(Square.RIGHT)))
           maze[row-1][col+1].setInView(true);
        if ((!maze[row][col].wall(Square.RIGHT) && (col+1 < cols) && !maze[row][col+1].wall(Square.DOWN)) ||
            (!maze[row][col].wall(Square.DOWN) && (row+1 < rows) && !maze[row+1][col].wall(Square.RIGHT)))
           maze[row+1][col+1].setInView(true);
           
    }
    
    private void resetInView()
    {
        for (int i = 0; i<rows; i++) {
            for (int j = 0; j<cols; j++) {
                maze[i][j].setInView(false);
            }
        }
    }
}
