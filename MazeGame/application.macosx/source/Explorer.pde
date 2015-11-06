/*
 * @author Erica Solum
 * @version Explorer Class Program 4
 */

import java.awt.event.*;


public class Explorer extends Occupant
{
  private Maze maze;
  private String name;


  public Explorer(Square location, Maze maze, String name)
  {
    super(location);
    this.name = name;
    this.maze = maze;

    maze.lookAround(location);
  }

  public String name() 
  {
    return name;
  }

  public void move(int key)
  {
    int direction = -1;
    int row = location().row();
    int col = location().col();


    if (key == KeyEvent.VK_UP || key==KeyEvent.VK_KP_UP)
    {
      direction = Square.UP;
      row--;
    }
    else if (key == KeyEvent.VK_RIGHT || key==KeyEvent.VK_KP_RIGHT)
    {
      direction = Square.RIGHT;
      col++;
    }
    else if (key == KeyEvent.VK_DOWN || key==KeyEvent.VK_KP_DOWN)
    {
      direction = Square.DOWN;
      row++;
    }
    else 
    {
      direction = Square.LEFT;
      col--;
    }



    if (!location().wall(direction) && direction != -1) 
    {
      moveTo(maze.getSquare(row, col));
    }
  }

  public void moveTo(Square s) 
  {
          super.moveTo(s);
          s.enter();
          maze.lookAround(s);  
  }
}

