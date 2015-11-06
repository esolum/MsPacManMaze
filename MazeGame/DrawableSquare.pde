/*
 * @author Erica Solum
 * @version DrawableSquare Class Program 4
 */

public class DrawableSquare extends Square implements Drawable
{
  //Constructors
  public DrawableSquare(boolean up, boolean right, boolean down, 
  boolean left, int row, int col)
  {

    super(up, right, down, left, row, col);
  }

  //Methods
  public void draw()
  {
    if(inView())
    {
      fill(50);
      noStroke();
      rect(x(), y(), SQUARE_SIZE, SQUARE_SIZE);
      if (!visited())
      {
        fill(255);
        ellipse((int) x() + Square.SQUARE_SIZE/3, (int) y() + Square.SQUARE_SIZE/3, 10, 10);
      }
    }

    // walls
    if(seen())
    {
      for(int dir=UP; dir<=LEFT; dir++) 
      {

        pushMatrix();
        stroke(0, 0, 200);
        strokeWeight(3);

        popMatrix();

        if (wall(dir)) 
      {
          if (dir==UP)
          {
            line(x(), y(), x() + SQUARE_SIZE, y());
          }
          else if (dir==DOWN)
          {
            line(x(), y() + SQUARE_SIZE, x()+SQUARE_SIZE, y() + SQUARE_SIZE);
          }
          else if (dir==LEFT)
          {
            line(x(), y(), x(), y() + SQUARE_SIZE);
          }
          else 
          {
            line(x() + SQUARE_SIZE, y(), x() + SQUARE_SIZE, y() + SQUARE_SIZE);
          }

          stroke(100, 100, 100);
        }
      }
    }
  }
}

