/*
 * @author Erica Solum
 * @version DrawableMonster Class Program 4
 */

public class DrawableMonster extends Monster implements Drawable
{
   //Constructors
   public DrawableMonster(Maze maze)
   {
      super(maze);
   }
   public DrawableMonster(Maze maze, long seed)
   {
      super(maze, seed);
   }
   public DrawableMonster(Maze maze, Square location)
   {
      super(maze, location);
   }
   
   //Methods
   public void draw()
   {
     if(location().inView())
     {
      pushMatrix();
      translate((int) location().x() + Square.SQUARE_SIZE/7, (int) location().y() + Square.SQUARE_SIZE/6);
      //scale(1.3);
      translate(-6, -4);
      noStroke();
      fill(#73E6E2);
      ellipse(25, 20, 28, 30);
      ellipse(16, 26, 11, 28);
      ellipse(25, 26, 11, 28);
      ellipse(34, 26, 11, 28);
      fill(255);
      ellipse(18, 19, 9, 11);
      ellipse(32, 19, 9, 11);
      fill(0, 0, 200);
      ellipse(32, 21, 5, 5);
      ellipse(18, 21, 5, 5);
      popMatrix();
     }
      
      
      
   }

}
