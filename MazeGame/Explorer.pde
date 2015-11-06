import java.awt.event.KeyEvent;

/**
 * Explorer for the Maze game.
 * 
 * @author Julie Hatalsky 
 * @version V1.0
 * @version April 22, 2007
 */
public class Explorer extends Occupant
{
    private Maze maze;
    private String name;
    
    /**
     * Constructor for Explorer objects<br>
     * <b>Ensure:</b> this.location() == location<br>
     * <b>Ensure:</b> this.name() == name<br>
     * <br>
     * The Explorer object is constructed and the explorer 
     * looks around the maze from its current location.
     * 
     * @param start the starting location for the Occupant
     */
	public Explorer(Square location, Maze maze, String name)
	{
		super(location);
		this.name = name;
		this.maze = maze;
		
		maze.lookAround(location);
	}
	
	public String name() {return name;}
	
	public void move(int key)
	{
	    int direction = -1;
            int row = location().row();
        int col = location().col();
        
        switch (key)
        {
            case KeyEvent.VK_UP:
            case KeyEvent.VK_KP_UP:
                direction = Square.UP; 
                row--; break;
            case KeyEvent.VK_RIGHT:
            case KeyEvent.VK_KP_RIGHT:
                direction = Square.RIGHT; 
                col++; break;
            case KeyEvent.VK_DOWN:
            case KeyEvent.VK_KP_DOWN:
                direction = Square.DOWN; 
                row++; break;
            case KeyEvent.VK_LEFT:
            case KeyEvent.VK_KP_LEFT:
                direction = Square.LEFT; 
                col--; break;
            default: return;
        }
        
        if (!location().wall(direction) && direction != -1) {
            moveTo(maze.getSquare(row,col));
        }
    }
    
    public void moveTo(Square s) {
        super.moveTo(s);
        s.enter();
        maze.lookAround(s);
    }
}
