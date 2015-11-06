/*
 * @author Erica Solum  
 * @version Treasure Class Program 4
 */

// 100% CORRECT

public class Treasure extends RandomOccupant
{
   private boolean found = false;
   
   //Constructors
   public Treasure(Maze maze)
   {
      super(maze);
      found = false;
      location().setTreasure(this);
   }
   public Treasure(Maze maze, long seed)
   {
      super(maze, seed);
      found = false;
      location().setTreasure(this);
   }
   public Treasure(Maze maze, Square location)
   {
      super(maze, location);
      found = false;
      location().setTreasure(this);
   }
   
   //Methods
   public boolean found()
   {
      return found;
   }
   public void setFound()
   {
      found = true;
   }
   public void move()
   {
      //does nothing
   }
   

}
