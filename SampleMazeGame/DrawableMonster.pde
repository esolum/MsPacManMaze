import java.awt.*;
/**
 * Write a description of class DrawableMonster here.
 * 
 * @author (your name) 
 * @version (a version number or a date)
 */
public class DrawableMonster extends Monster implements Drawable
{
	public DrawableMonster(Maze maze, long seed)
	{
	    super(maze, seed);
	}
	
	public DrawableMonster(Maze maze)
	{
	    super(maze);
	}
	
	public DrawableMonster(Maze maze, Square location)
	{
		super(maze, location);
	}
    
    public void draw()
    {   
        Square loc = location();
        float ssize = Square.SQUARE_SIZE;
        int x = loc.x(), y = loc.y();
        
        if (loc.inView()) 
        {
           pushMatrix();
           translate(x+ssize/2, y+ssize/2);
        
           // head
           fill(120);
           ellipse(0, -10, 25, 25);
        
           // eyes
           fill(255);
           ellipse(0, -10, 20, 18);
           fill(120);
           ellipse(0, -17, 20, 10);
           fill(0);
           ellipse(-2, -10, 2, 2);
           ellipse(2, -10, 2, 2);
        
           // muzzle / jowels
           fill(180);
           ellipse(0, -3, 22, 10);
           fill(120);
           rect(-7, 0, 14, 13);
           ellipse(0, 10, 20, 10);
           fill(180);
           ellipse(-10, 5, 7, 15);
           ellipse(10, 5, 7, 15);
        
           // nose
           fill(0);
           ellipse(0, -6, 8, 5);
        
           // teeth
           fill(255);
           triangle(-7, 0, -5, -5, -3, 0);
           triangle(7, 0, 5, -5, 3, 0);
        
           // ears
           fill(120);
           ellipse(-10, -20, 6, 3);
           ellipse(10, -20, 6, 3);
        
           popMatrix();
        }
    }
}
