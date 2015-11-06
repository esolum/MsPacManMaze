import java.awt.*;
/**
 * Write a description of class DrawableMaze here.
 * 
 * @author (your name) 
 * @version (a version number or a date)
 */
public class DrawableMaze extends Maze implements Drawable
{
    /**
     * Constructor for objects of class DrawableMaze
     */
    public DrawableMaze(DrawableSquare[][] maze, int rows, int cols)
    {
        super(maze, rows, cols);
    }

    public void draw()
    {   
        int rows = rows();
        int cols = cols();
        int numRandOccupants = getNumRandOccupants();
        
        fill(35, 118, 28);  // dark green 
        rect(0, 0, Square.SQUARE_SIZE*cols, Square.SQUARE_SIZE*rows);
        
        for (int i = 0; i<rows; i++) {
            for (int j = 0; j<cols; j++) {
                ((Drawable)getSquare(i,j)).draw();
            }
        }
        
        for (int i=0; i<numRandOccupants; i++)
           ((Drawable)getRandomOccupant(i)).draw();
           
        ((Drawable)getExplorer()).draw();
    }
}
