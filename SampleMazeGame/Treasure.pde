/**
 * Write a description of class Treasure here.
 * 
 * @author (your name) 
 * @version (a version number or a date)
 */
public class Treasure extends RandomOccupant
{
    private boolean found = false;
    
	/**
	 * Constructor for objects of class Explorer
	 */
	public Treasure(Maze maze, long seed)
	{
	    super(maze, seed);
		location().setTreasure(this);
	}
	
	public Treasure(Maze maze)
	{
	    super(maze);
		location().setTreasure(this);
	}
	
	public Treasure(Maze maze, Square location)
	{
		super(maze, location);
		location().setTreasure(this);
	}
	
	public boolean found() {return found;}
	public void setFound() {found = true;}
	public void move() {}
}
