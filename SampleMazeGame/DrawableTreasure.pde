import java.awt.*;
/**
 * Write a description of class DrawableTreasure here.
 * 
 * @author (your name) 
 * @version (a version number or a date)
 */
public class DrawableTreasure extends Treasure implements Drawable
{
	public DrawableTreasure(Maze maze, long seed)
	{
	    super(maze, seed);
	}
	
	public DrawableTreasure(Maze maze)
	{
	    super(maze);
	}
	
	public DrawableTreasure(Maze maze, Square location)
	{
		super(maze, location);
	}  

    public void draw()
    {   
        Square loc = location();
        color cheese;
        color holes;
        
        if (loc.seen()) {
           pushMatrix();
           translate(loc.x()+25, loc.y()+25);
           
           if (found())
           {
              cheese = color(230, 230, 0);
              holes = color(#FF7800);
           }
           else 
           {
              cheese = color(128);
              holes = color(100);
           }
           fill(cheese);
           rect(-15, -15, 30, 30);
           fill(holes);
           ellipse(-8, 5, 10, 10);
           ellipse(5, -4, 12, 8);
           ellipse(-7, -9, 5, 5);
           ellipse(8, 10, 5, 5);
           
           popMatrix();
        }
    }
}
