import processing.core.*; 
import processing.data.*; 
import processing.event.*; 
import processing.opengl.*; 

import java.awt.event.KeyEvent; 
import java.util.Random; 
import java.awt.event.*; 
import java.util.*; 
import java.util.*; 
import java.util.Random; 

import java.util.HashMap; 
import java.util.ArrayList; 
import java.io.File; 
import java.io.BufferedReader; 
import java.io.PrintWriter; 
import java.io.InputStream; 
import java.io.OutputStream; 
import java.io.IOException; 

public class MazeGame extends PApplet {




GameGUI gui;
final int ROWS = 10;
final int COLS = 10;
final int NUM_MONSTERS = 3;
final int NUM_TREASURES = 2;
int counter;
PFont f;

public void setup()
{
  // CHANGE the name of the Explorer passed to the gui constructor.
  gui = new GameGUI(ROWS, COLS, NUM_MONSTERS, NUM_TREASURES, "Mrs. PacMan");

  colorMode(RGB, 255, 255, 255, 100);
  size(COLS*Square.SQUARE_SIZE, ROWS*Square.SQUARE_SIZE);
  counter = 0;  

  // setup for final scene
  f = loadFont("AmericanTypewriter-CondensedBold-48.vlw");
}

public void draw()
{
  gui.draw();
  if (!gui.isGameOver())
  {
    // move the monsters once per second
    counter++;
    if (counter % 60 == 0)
    {
      gui.monsterMove();
    }
  }
}

public void keyPressed() 
{
  if (!gui.isGameOver())
  {
    if (key == CODED)
      switch (keyCode)
      { 
      case DOWN:
        gui.move(KeyEvent.VK_DOWN);
        break;
      case UP:
        gui.move(KeyEvent.VK_UP);
        break;
      case LEFT:
        gui.move(KeyEvent.VK_LEFT);
        break;
      case RIGHT:
        gui.move(KeyEvent.VK_RIGHT);
        break;
      }
  }
}

/**
 * Class GameGUI - Class to run the GUI for the maze game
 * 
 * @author Julie Workman 
 * @version April 25, 2013
 */
public class GameGUI 
{
  private int rows;
  private int cols;
  private int numMonsters;
  private int numTreasures;

  private DrawableMaze maze; 
  private boolean gameover = false;

  // for random maze generation
  int[][] cell= new int[200][100];
  int xdim = 5;
  int ydim = 5;

  public GameGUI(int r, int c, int m, int t, String name)
  {
    rows = r;
    cols = c;
    numMonsters = m;
    numTreasures = t;
    init(name);
  }

  public boolean isGameOver() {
    return gameover;
  }

  private void init(String name)
  {
    DrawableSquare[][] squares = new DrawableSquare[rows][cols];
    int[][] randMaze;
    int N = MazeGenerator.N;
    int E = MazeGenerator.E;
    int S = MazeGenerator.S;
    int W = MazeGenerator.W;

    MazeGenerator mg = new MazeGenerator();
    boolean up, right, down, left;

    // generate a maze stored as integer values (bitwise & of 1,2,4,8)
    randMaze = mg.generate_maze(rows, cols);

    // remove some walls to make it easier - loops and rooms
    for (int i=0; i<ROWS+COLS; i++)
    {
      int row = (int)random(rows-2)+1; 
      int col = (int)random(cols-2)+1;

      switch (i%4)
      {
      case 0:
        randMaze[row][col] &= N+E+S;
        randMaze[row][col-1] &= N+W+S;
        break;
      case 1: 
        randMaze[row][col] &= E+S+W;
        randMaze[row-1][col] &= E+N+W;
        break;
      case 2:
        randMaze[row][col] &= N+S+W;
        randMaze[row][col+1] &= N+S+E;
        break;
      default:
        randMaze[row][col] &= N+E+W;
        randMaze[row+1][col] &= S+E+W;
        break;
      }
    }

    // construct the maze squares with values from the randMaze
    for (int i=0; i<rows; i++) {
      for (int j=0; j<cols; j++) {
        up = ((randMaze[i][j] & N) == N);
        right = ((randMaze[i][j] & E) == E);
        down = ((randMaze[i][j] & S) == S);
        left = ((randMaze[i][j] & W) == W);

        squares[i][j] = new DrawableSquare(up, right, down, left, i, j);
      }
    }

    // construct the maze
    maze = new DrawableMaze(squares, rows, cols);

    // add monsters and treasures
    for (int i=0; i<numMonsters; i++)
      maze.addRandomOccupant(new DrawableMonster(maze));

    for (int i=0; i<numTreasures; i++)
      maze.addRandomOccupant(new DrawableTreasure(maze));

    // add the explorer  
    maze.setExplorer(new DrawableExplorer(squares[0][0], maze, name));
  }    

  public void move(int key)
  {
    maze.getExplorer().move(key);
  }

  public void monsterMove()
  {
    maze.randMove();
  }

  /**
   * Draw method for GUI.
   */
  public void draw()
  {           
    int status = maze.gameStatus();

    if (status == Maze.ACTIVE)
    {
      maze.draw();
    }
    else if (status == Maze.EXPLORER_WIN) {
      maze.draw();
      gameover = true;

      DrawableExplorer winner = new DrawableExplorer(maze.getSquare(6, 4), maze, maze.getExplorer().name());
      pushMatrix();
      translate(-900, -1300);
      scale(5);
      winner.draw();
      popMatrix();     

      // tell them they won
      fill(255, 255, 0);
      textFont(f, 32);
      text(winner.name() + " wins!!!", 50, 150);
    }
    else if (status == Maze.MONSTER_WIN) 
    {
      maze.draw();
      gameover = true;

      // draw large monster
      DrawableMonster dog = new DrawableMonster(maze, maze.getSquare(6, 4));
      maze.getSquare(6, 4).setInView(true);
      pushMatrix();
      translate(-900, -1300);
      scale(5);
      dog.draw();
      popMatrix();

      // tell them they lost
      fill(255, 0, 0);
      textFont(f, 32);
      text("You lose!!! Ha ha ha!!!!", 50, 150);
    }
  }
}

/*
 * @author Erica Solum
 * @version Drawable Interface Program 4
 */

public interface Drawable 
{
   public void draw();
}
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
/*
 * @author Erica Solum
 * @version DrawableMaze Class Program 4
 */
public class DrawableMaze extends Maze implements Drawable
{
   public DrawableMaze (DrawableSquare[][] maze, int rows, int cols)
   {
       super(maze, rows, cols);
   }
   
   //Methods
   public void draw()
   {
      
      int numRows = rows();
      int numCols = cols();
      int numRandOccs = getNumRandOccupants();
       
        
       fill(0);  
       rect(0, 0, Square.SQUARE_SIZE*numCols, Square.SQUARE_SIZE*numRows);
     
      for(int i=0; i<numRows; i++)
      {
        for(int j=0; j<numCols; j++)
        {
          ((Drawable)getSquare(i, j)).draw();
        }
      }
      for(int i=0; i<numRandOccs; i++)
      {
        ((Drawable)getRandomOccupant(i)).draw();
      }
      ((Drawable)getExplorer()).draw();
        
   }
         

}
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
      fill(0xff73E6E2);
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

/*
 * @author Erica Solum
 * @version Explorer Class Program 4
 */




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


/**
 * Class that contains all the logic to model a Maze with Treasures, Monsters, and an Explorer.
 * 
 * @author Erica Solum 
 * @version Maze Class Program 4
 */
public class Maze
{
   // named constants
   public static final int ACTIVE = 0;
   public static final int EXPLORER_WIN = 1;
   public static final int MONSTER_WIN = 2;
    
    // instance variables
   private Square[][] maze;
   private ArrayList<RandomOccupant> randOccupants;
   private Explorer explorer;
   private int rows;
   private int cols;

   /**
    * Constructor for objects of class Maze
    */
   public Maze(Square[][] maze, int rows, int cols)
   {
      this.rows = rows;
      this.cols = cols;
      this.maze = maze;
      
      randOccupants = new ArrayList<RandomOccupant>();
      
      
   }
   
   // QUERIES
   public Square getSquare(int row, int col) { return maze[row][col]; }
   public int rows() {return rows;}
   public int cols() {return cols;}
   public String explorerName() {return explorer.name();}
   public Explorer getExplorer() {return explorer;}
    
   
   public RandomOccupant getRandomOccupant(int index) 
   {
      return randOccupants.get(index);
   }
   
   public int getNumRandOccupants() 
   {
      return randOccupants.size();
   }
   
   // COMMANDS
   
   public void addRandomOccupant(RandomOccupant ro)
   {
      randOccupants.add(ro);
   }
   
   public void setExplorer(Explorer e)
   {
      explorer = e;
   }
   
   public void explorerMove(int key)
   {
      explorer.move(key);
   }
   
   public void randMove()
   {
      for (RandomOccupant r: randOccupants)
      {
         r.move();
      }
   }
   
   /**
    * Returns the status of the game.
    *
    * If all treasures have been found, return EXPLORER_WIN.
    * If not, check each maze occupant, if it is a Monster and
    *   it is in the same location as the Explorer, return
    *   MONSTER_WIN.  Note that you can use == to check locations, do you know why?
    * Otherwise, return ACTIVE.
    */
   public int gameStatus()
   {
      int status = ACTIVE;
      if(foundAllTreasures())
      {
         status = EXPLORER_WIN;
      }
      else
      {
         for(int i = 0; i<randOccupants.size(); i++)
         {
            if(randOccupants.get(i) instanceof Monster && (randOccupants.get(i).location() == explorer.location()))
            {
               status = MONSTER_WIN;
            }
         }
         
      }
        
      return status;
   }
   
   private boolean foundAllTreasures()
   {
      boolean foundAll = true;
        
      for(int i = 0; i<randOccupants.size(); i++)
      {
         if(randOccupants.get(i) instanceof Treasure && !((Treasure)randOccupants.get(i)).found())
         {
            
               foundAll = false;
         }
         
      }
              
      return foundAll;
   }
    
   public void lookAround(Square s)
   {
      int row = s.row();
      int col = s.col();
        
      // Clear what was previously in view
      resetInView();
        
      // Set the current square so that we are viewing it (obviously)
      s.setInView(true);
        
      
      //Adjacent
      if(!s.wall(Square.RIGHT) && col+1 < cols)
      {
         maze[row][col+1].setInView(true);
      }
      if(!s.wall(Square.UP) && (row-1 >=0))
      {
         maze[row-1][col].setInView(true);
      }
      if(!s.wall(Square.DOWN) && (row+1 <rows))
      {
         maze[row+1][col].setInView(true);
      }
      if(!s.wall(Square.LEFT) && (col-1 >= 0))
      {
         maze[row][col-1].setInView(true);
      }
      //Diagonals
      
      if ((!s.wall(Square.UP) && (row-1 >= 0) && !maze[row-1][col].wall(Square.LEFT)) || 
         (!s.wall(Square.LEFT) && (col-1 >= 0) && !maze[row][col-1].wall(Square.UP)))
      {
         maze[row-1][col-1].setInView(true);
      }
      if ((!s.wall(Square.DOWN) && (row+1 < rows) && !maze[row+1][col].wall(Square.LEFT)) || 
         (!s.wall(Square.LEFT) && (col-1 >= 0) && !maze[row][col-1].wall(Square.DOWN)))
      {
         maze[row+1][col-1].setInView(true);
      }
      if ((!s.wall(Square.UP) && (row-1 >= 0) && !maze[row-1][col].wall(Square.RIGHT)) || 
         (!s.wall(Square.RIGHT) && (col+1 < cols) && !maze[row][col+1].wall(Square.UP)))
      {
         maze[row-1][col+1].setInView(true);
      }
      if ( (!s.wall(Square.DOWN) && (row+1 < rows) && !maze[row+1][col].wall(Square.RIGHT)) || 
            (!s.wall(Square.RIGHT) && (col+1 < cols) && !maze[row][col+1].wall(Square.DOWN)))
      {
         maze[row+1][col+1].setInView(true);
      }
      
      

   }
    
   private void resetInView()
   {
      for (int i = 0; i<rows; i++) {
         for (int j = 0; j<cols; j++) {
            maze[i][j].setInView(false);
         }
      }
   }
}

/**
 * Class to generate a random maze.
 * 
 * @author Julie Workman 
 * @version October 4, 2009
 */
public class MazeGenerator
{
    private int ROWS;
    private int COLS;                                 // dimensions of maze
    private int[][] maze;                                       // the maze of cells
    private int[][] Stack;                                      // cell stack to hold a list of cell locations
    public static final int N = 1;                          // direction constants
    public static final int E = 2;
    public static final int S = 4;
    public static final int W = 8;  
    
    private void init_cells()
    {
        int i, j;

        // create a maze of cells
        maze = new int[ROWS][COLS];

        // set all walls of each cell in maze by setting bits :  N E S W
        for (i = 0; i < ROWS; i++)
            for (j = 0; j < COLS; j++)
                maze[i][j] = (N + E + S + W);

        // create stack for storing previously visited locations
        Stack = new int[ROWS*COLS][2];

        // initialize stack
        for (i = 0; i < ROWS*COLS; i++)
            for (j = 0; j < 2; j++)
                Stack[i][j] = 0;
    }

    // use depth first search to create a maze
    public int[][] generate_maze(int rows, int cols)
    {
        int i, j, r, c;
        int[] curr = new int[2];
        Random rand = new Random();
        
        ROWS = rows;
        COLS = cols;
        
        init_cells();
    
        // arrays of single step movements between cells
        //          north    east     south    west    
        int[][] move = {{-1, 0}, { 0, 1}, { 1, 0}, { 0,-1}};
        int[][] next = {{0, 0}, { 0, 0}, { 0, 0}, { 0,0}};

        // choose a cell at random and make it the current cell
        r = rand.nextInt(ROWS);
        c = rand.nextInt(COLS);
    
        // current search location
        curr[0] = r;  
        curr[1] = c;
        int visited  = 1;
        int total = ROWS*COLS;
        int tos   = 0;                              // index for top of cell stack 
   
        while (visited < total)
        {
            //  find all neighbors of current cell with all walls intact
            j = 0;
            for (i = 0; i < 4; i++)
            {
                r = curr[0] + move[i][0];
                c = curr[1] + move[i][1];

                //  check for valid next cell
                if ((0 <= r) && (r < ROWS) && (0 <= c) && (c < COLS))
                {
                    // check if previously visited
                    if (((maze[r][c] & N) == N) && ((maze[r][c] & E)==E) && ((maze[r][c] & S)==S) && ((maze[r][c] & W)==W))
                    {
                        // not visited, so add to possible next cells
                        next[j][0] = r;
                        next[j][1] = c;
                        j++;
                    }
                }
            }
        
            if (j > 0)
            {
                // current cell has one or more unvisited neighbors, so choose one at random  
                // and knock down the wall between it and the current cell
                i = rand.nextInt(j);

                if ((next[i][0] - curr[0]) == 0)    // next on same row
                {
                    r = next[i][0];
                    if (next[i][1] > curr[1])       // move east
                    {
                        c = curr[1];
                        maze[r][c] &= ~E;           // clear E wall
                        c = next[i][1];
                        maze[r][c] &= ~W;           // clear W wall
                    }
                    else                            // move west
                    {
                        c = curr[1];
                        maze[r][c] &= ~W;           // clear W wall
                        c = next[i][1];
                        maze[r][c] &= ~E;           // clear E wall
                    }
                }
                else                                // next on same column
                {
                    c = next[i][1];
                    if (next[i][0] > curr[0])       // move south    
                    {
                        r = curr[0];
                        maze[r][c] &= ~S;           // clear S wall
                        r = next[i][0];
                        maze[r][c] &= ~N;           // clear N wall
                    }
                    else                            // move north
                    {
                        r = curr[0];
                        maze[r][c] &= ~N;           // clear N wall
                        r = next[i][0];
                        maze[r][c] &= ~S;           // clear S wall
                    }
                }

                tos++;                              // push current cell location
                Stack[tos][0] = curr[0];
                Stack[tos][1] = curr[1];

                curr[0] = next[i][0];               // make next cell the current cell
                curr[1] = next[i][1];

                visited++;                          // increment count of visited cells
            }
            else
            {
                // reached dead end, backtrack
                // pop the most recent cell from the cell stack            
                // and make it the current cell
                curr[0] = Stack[tos][0];
                curr[1] = Stack[tos][1];
                tos--;
            }
        }
        return maze;
    }
}
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
/*
 * @author Erica Solum 
 * @version Occupant Class Program 4
 */

//DONE WITH THIS

public abstract class Occupant 
{
   private Square location;
   
   //Constructors
   public Occupant()
   {
      
   }
   public Occupant(Square start)
   {
      location = start;
   }
   
   //Methods
   public Square location()
   {
    return location;
   }
   
   public void moveTo(Square newLoc)
   {
      location = newLoc; 
   }
   

}
/*
 * @author Erica Solum
 * @version RandomOccupant Class Program 4
 */



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
  static public void main(String[] passedArgs) {
    String[] appletArgs = new String[] { "MazeGame" };
    if (passedArgs != null) {
      PApplet.main(concat(appletArgs, passedArgs));
    } else {
      PApplet.main(appletArgs);
    }
  }
}
