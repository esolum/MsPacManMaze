/*
 * @author Erica Solum
 * @version DrawableTreasure Class Program 4
 */

public class DrawableTreasure extends Treasure implements Drawable
{
  //Constructors
  public DrawableTreasure(Maze maze)
  {
    super(maze);
  }

  public DrawableTreasure(Maze maze, long seed)
  {
    super(maze, seed);
  }

  public DrawableTreasure(Maze maze, Square location)
  {
    super(maze, location);
  }

  //Methods
  public void draw()
  {
    if(location().inView())
    {
       if(!found())
       {
          pushMatrix();
          noStroke();
          fill(255);
          ellipse((int) location().x() + Square.SQUARE_SIZE/3, (int) location().y() + Square.SQUARE_SIZE/3, 10, 10);
          popMatrix();
       }
    }
  }
}

