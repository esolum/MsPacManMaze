/* 
 * @author Erica Solum
 * @version Square Class Program 4
 */

//DONE WITH THIS

public class Square 
{
   //Constants
   public static final int SQUARE_SIZE = 50;
   public static final int UP = 0;
   public static final int RIGHT = 1;
   public static final int DOWN = 2;
   public static final int LEFT = 3;
   
   //Instance Variables
   private boolean[] walls = new boolean[4];
   private boolean seen = false;
   private boolean inView = false;
   private int row;
   private int column;
   private Treasure treasure;
   private boolean visited = false;
   
   
   
   //Constructor
   public Square(boolean up, boolean right, boolean down, boolean left, 
         int row, int col)
   {
      walls[UP] = up;
      walls[RIGHT] = right;
      walls[DOWN] = down;
      walls[LEFT] = left;
      
      this.row = row;
      this.column = col;
   }
   
   //Methods
   public boolean wall(int direction) 
   {
      return walls[direction];
      
   }
   
   public boolean seen()
   {
      return seen;
   }
   public boolean visited()
   {
     return visited;
   }
   public boolean inView()
   {
      return inView;
   }
   public int row()
   {
      return row;
   }
   public int col()
   {
      return column;
   }
   public Treasure treasure()
   {
      return treasure;
   }
   public int x()
   {
      return column * SQUARE_SIZE;
      
   }
   public int y()
   {
      
      return row * SQUARE_SIZE;
      
   }
   public void setInView(boolean inView)
   {
      this.inView = inView;
      if(inView)
      {
         seen = true;
      }
      
   }
   public void setVisited(boolean v)
   {
      visited = v;
   }
     
   public void setTreasure(Treasure t)
   {
      treasure = t;
   }
   public void enter()
   {
      visited = true;
      if(treasure != null)
      {
         treasure.setFound();
      }
   }
}
