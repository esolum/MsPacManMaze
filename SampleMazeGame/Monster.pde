/**
 * Write a description of class Monster here.
 * 
 * @author (your name) 
 * @version (a version number or a date)
 */
public class Monster extends RandomOccupant
{   
	/**
	 * Constructor for objects of class Explorer
	 */
	public Monster(Maze maze, long seed)
	{
	    super(maze, seed);
	}
	
	public Monster(Maze maze)
	{
	    super(maze);
	} 
	
	public Monster(Maze maze, Square location)
	{
		super(maze, location);
	}
}
