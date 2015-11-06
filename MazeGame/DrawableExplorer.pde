import java.awt.*;
/**
 * Write a description of class DrawableExplorer here.
 * 
 * @author (your name) 
 * @version (a version number or a date)
 */
public class DrawableExplorer extends Explorer implements Drawable
{
    public DrawableExplorer(Square location, Maze maze, String name)
    {
        super(location, maze, name);
    }
    
    public void draw()
    {   
        Square loc = location();
        float ssize = Square.SQUARE_SIZE;
        int x = loc.x(), y = loc.y();

        pushMatrix();
        translate(x+ssize/2, y+ssize/2);
        
        // ears
        fill(160, 100, 70);   // brown
        triangle(-12, -5, -14, -20, -5, -12);
        triangle(12, -5, 14, -20, 5, -12);
        fill(255,100,100);
        triangle(-10, -5, -12, -17, -7, -10);
        triangle(10, -5, 12, -17, 7, -10);
        
        // head
        fill(160, 100, 70);   // brown
        ellipse(0, 0, 3*ssize/5, 3*ssize/5);
        
        // nose
        fill(255,100,100);
        triangle(-ssize/10, ssize/20, ssize/10, ssize/20, 0, 3*ssize/20);
        
        // eyes
        fill(0);
        ellipse(-ssize/8, -ssize/10, 7, 7);
        ellipse(ssize/8, -ssize/10, 7, 7);  
       
        popMatrix();
    }
}
