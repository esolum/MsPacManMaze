/*
 * @author Erica Solum
 * @version Monster Class Program 4
 */


public class Monster extends RandomOccupant
{
   //Constructors
   public Monster(Maze maze)
   {
      super(maze);
   }
   public Monster(Maze maze, long seed)
   {
      super(maze, seed);
   }
   public Monster(Maze maze, Square location)
   {
      super(maze, location);
   }

}
