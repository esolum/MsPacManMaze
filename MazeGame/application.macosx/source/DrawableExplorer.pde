/*
 * @author Erica Solum
 * @version DrawableExplorer Class Program 4
 */


public class DrawableExplorer extends Explorer implements Drawable
{
   //Constructor
   public DrawableExplorer(Square location, Maze maze, String name)
   {
      super(location, maze, name);
   }
   
   //Methods
   public void draw()
   {
      Square loc = location();
        
        int x = loc.x(), y = loc.y();

        pushMatrix();
        noStroke();
        
        translate(x + Square.SQUARE_SIZE/2, y+ Square.SQUARE_SIZE/2);
        fill(255, 230, 0);
        ellipse(0, 0, 30, 30);
        fill(255, 0, 0);
      pushMatrix();
      rotate(7*PI / 4);
         translate(-9, -19);
         triangle(0, 0, 10, 5, 0, 10);
         triangle(16, 0, 5, 5, 16, 10);
      popMatrix();
      fill(50);
      triangle(0, 0, 20, -10, 20, 10); 
      popMatrix();
      
      
      
      
   }
         

}
