import java.util.Random;
/**
 * Write a description of class RandomOccupant here.
 * 
 * @author (your name) 
 * @version (a version number or a date)
 */
public class RandomOccupant extends Occupant
{
    // instance variables - replace the example below with your own
    private Random rand;
    private Maze maze;
    
    /**
     * Constructor for objects of class Explorer
     */
    public RandomOccupant(Maze maze, long seed)
    {
        int row, col;
        
        rand = new Random(seed);
        row = rand.nextInt(maze.rows());
        col = rand.nextInt(maze.cols());
        
        moveTo(maze.getSquare(row,col));
        this.maze = maze;
    }
    
    public RandomOccupant(Maze maze)
    {
        int row, col;
        
        rand = new Random();
        row = rand.nextInt(maze.rows());
        col = rand.nextInt(maze.cols());
        
        moveTo(maze.getSquare(row,col));
        this.maze = maze;
    }
    
    public RandomOccupant(Maze maze, Square location)
    {
        super(location);
        this.maze = maze;
        rand = new Random();
    }
    
    public void move()
    {
        int direction;
        int row = location().row();
        int col = location().col();
        
        do {
            direction = rand.nextInt(4);
        } while (location().wall(direction));
         
        switch (direction)
        {
            case Square.UP:
                row--; break;
            case Square.RIGHT:
                col++; break;
            case Square.DOWN:
                row++; break;
            case Square.LEFT:
                col--; break;
            default: break;
        }
        
        moveTo(maze.getSquare(row,col)); 
    }
}
