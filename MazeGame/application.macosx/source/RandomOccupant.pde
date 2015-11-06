/*
 * @author Erica Solum
 * @version RandomOccupant Class Program 4
 */

import java.util.Random;

public abstract class RandomOccupant extends Occupant 
{
   private Random initialLoc;
   private Maze m;
   
   //Constructors
   public RandomOccupant(Maze maze)
   {
      initialLoc = new Random();
      int locX = initialLoc.nextInt(maze.rows());
      int locY = initialLoc.nextInt(maze.cols());
      moveTo(maze.getSquare(locX,locY));
      m = maze;
   }
   public RandomOccupant(Maze maze, long seed)
   {
      
      initialLoc = new Random(seed);
      int locX = initialLoc.nextInt(maze.rows());
      int locY = initialLoc.nextInt(maze.cols());
      moveTo(maze.getSquare(locX,locY));
      
      m = maze;
      
   }
   public RandomOccupant(Maze maze, Square location)
   {
      super(location);
      m = maze;
      initialLoc = new Random();
   }
   
   //Methods
   public void move()
   {
      int randDirection;
      
      
      int curRow = location().row();
      int curCol = location().col();
      do
      {
         randDirection = initialLoc.nextInt(4);
      } while(location().wall(randDirection));
      
      if(randDirection == Square.UP)
      {
         moveTo(m.getSquare(curRow-1, curCol));
      }
      if(randDirection == Square.DOWN)
      {
         moveTo(m.getSquare(curRow+1, curCol));
      }
      if(randDirection == Square.LEFT)
      {
         moveTo(m.getSquare(curRow, curCol-1));
      }
      if(randDirection == Square.RIGHT)
      {
         moveTo(m.getSquare(curRow, curCol+1));
      }
     
      
      
   }
   

}
