import processing.core.*; 
import processing.data.*; 
import processing.opengl.*; 

import java.awt.event.KeyEvent.*; 
import java.util.Random; 
import java.awt.*; 
import java.awt.*; 
import java.awt.*; 
import java.awt.*; 
import java.awt.*; 
import java.awt.*; 
import java.awt.event.KeyEvent; 
import java.util.*; 
import java.util.*; 
import java.util.Random; 

import java.applet.*; 
import java.awt.Dimension; 
import java.awt.Frame; 
import java.awt.event.MouseEvent; 
import java.awt.event.KeyEvent; 
import java.awt.event.FocusEvent; 
import java.awt.Image; 
import java.io.*; 
import java.net.*; 
import java.text.*; 
import java.util.*; 
import java.util.zip.*; 
import java.util.regex.*; 

public class MazeGame extends PApplet {




GameGUI gui;
final int ROWS = 10;
final int COLS = 10;
final int NUM_MONSTERS = 3;
final int NUM_TREASURES = 2;
int counter;
PFont f;
PSys fireW[];
int numF, fireFrame, numA;

public void setup()
{
   gui = new GameGUI(ROWS, COLS, NUM_MONSTERS, NUM_TREASURES, "Kitty");
   
   colorMode(RGB, 255, 255, 255, 100);
   size(COLS*Square.SQUARE_SIZE, ROWS*Square.SQUARE_SIZE);
   counter = 0;  
   
   // setup for final scenes
   f = loadFont("AmericanTypewriter-CondensedBold-48.vlw");
   numF = 6;
   fireW = new PSys[numF];
   for (int i= 0; i < numF; i++) 
   {
      fireW[i] = new PSys(100, new PVector(random(width/2,width*4/5), random(height/4), 0));
   }
   fireFrame = 0;
   numA = 1;
}

public void draw()
{
   gui.draw();
   if (!gui.isGameOver())
   {
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
 * Class GameGUI - write a description of the class here
 * 
 * @author (your name) 
 * @version (a version number)
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
    
    public boolean isGameOver() {return gameover;}
    
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
            text(winner.name() + " wins!!! Meowwww!!!", 50, 150);  
            
            // stagger the start of the fireworks
            if (fireFrame%20 ==0 && numA < numF) 
            {
               numA++;
            }
            
            // main firework loop
            for (int i= 0; i < numA; i++) 
            {
               fireW[i].run();
               if (fireW[i].dead()) 
               {
                  fireW[i] = new PSys(100, new PVector(random(width/3,width), random(height/4), 0));
               }
            }
            fireFrame++;
        }
        else if (status == Maze.MONSTER_WIN) {
            maze.draw();
            gameover = true;
            
            // draw large monster
            DrawableMonster dog = new DrawableMonster(maze, maze.getSquare(6, 4));
            maze.getSquare(6,4).setInView(true);
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


//Example for the firework lab for CSC 123 - zj wood

//define a particle
class Particle {
  
  PVector loc;
  PVector vel;
  PVector accel;
  float r;
  float life;
  PVector pcolor;
  
  //constructor
  Particle(PVector start, PVector in_c) {
    accel = new PVector(0, 0.05f, 0); //gravity
    vel = new PVector(random(-1, 1), random(-2, 0), 0);
    pcolor = in_c.get();
    pcolor.x += random(-18, 18);
    pcolor.y += random(-18, 18);
    pcolor.z += random(-18, 18);
    loc = start.get();
    r = 8.0f;
    life = 100;
  }
  
    //constructor
  Particle(PVector start) {
    accel = new PVector(0, 0.05f, 0); //gravity
    vel = new PVector(random(-2, 2), random(-6, 0), 0);
    pcolor = new PVector(random(255), random(255), random(255));
    loc = start.get();
    r = 8.0f;
    life = 100;
  }
  
  //what to do each frame
  public void run() {
    updateP();
    renderP();
  }
  
  public void stopDraw() {
    renderP();
  }
  
  //how to move
  public void updateP() {
    vel.add(accel);
    loc.add(vel);
    life -= 1.0f;
    r -= 0.05f;
  }
  
  //how to draw a particle
  public void renderP() {
    pushMatrix();
      ellipseMode(CENTER);
      stroke(pcolor.x, pcolor.y, pcolor.z, life*2);
      fill(pcolor.x, pcolor.y, pcolor.z, life/1.2f);
      translate(loc.x, loc.y);
      rotate(vel.heading2D());
      ellipse(0, 0, vel.mag()*1.5f*r, r);
    popMatrix();
  }
  
  public boolean alive() {
    if (life <= 0.0f) {
      return false;
    } else {
      return true;
    }
  }
} //end of particle object definition


//define a group of particles as a particleSys
class PSys{
  ArrayList particles; //all the particles
  PVector source; //where all the particles emit from
  PVector shade;
  
  //constructor
  PSys(int num, PVector init_loc) {
    particles = new ArrayList();
    source = init_loc.get();
    shade = new PVector(random(255), random(255), random(255));
    for (int i=0; i < num; i++) {
      particles.add(new Particle(source, shade));
    }
  }
    
    //what to do each frame
    public void run() {
      //go through backwards for deletes
      for (int i=particles.size()-1; i >=0; i--) {
        Particle p = (Particle) particles.get(i);
        //update each particle per frame
        p.run();
        if ( !p.alive()) {
          particles.remove(i);
        }
      }
    }
    
    public void stopDraw() {
      for (int i=0; i < particles.size(); i++) {
        Particle p = (Particle) particles.get(i);
        p.stopDraw();
      }
    }
    
    
    //options for adding particles to the system - default
    public void addParticle() {
      particles.add(new Particle(source));
    }
    
    //add at a specific point
    public void addParticle(float x, float y) {
      particles.add(new Particle(new PVector(x, y)));
    }
    
    //add for an already defined particle
    public void addParticle(Particle p) {
      particles.add(p);
    }
    
    //is particle still populated?
    public boolean dead() {
        if (particles.isEmpty() ) {
          return true;
        } else {
          return false;
        }
    }
}

//declare a particle system
/*PSys fireW[];
int frame;
int numF, numA;

void setup() {
  size(500, 500);
  colorMode(RGB, 255, 255, 255, 100);
  numF = 6;
  fireW = new PSys[numF];
  for (int i= 0; i < numF; i++) {
    fireW[i] = new PSys(100, new PVector(random(width-20), random(height/2), 0));
  }
  smooth();
  frame = 0;
  frameRate(40);
  numA = 1;
}

void draw() {
  background(0);
  if (mousePressed !=true) {

    if (frame%20 ==0 && numA < numF) {
      numA++;
    }
    
    for (int i= 0; i < numA; i++) {
      fireW[i].run();
      if (fireW[i].dead() == true) {
        fireW[i] = new PSys(100, new PVector(random(width-20), random(height/2), 0));
      }
    }
      
    frame++;
 
  } else {

    for (int i= 0; i < numF; i++) {
      fireW[i].stopDraw();
    }
    //saveFrame("Fireworks.jpg");
  
  }

  
}*/

/**
 * An interface for objects that know how to draw themselves.
 * 
 * @author Julie Hatalsky 
 * @version 1.0
 */
// RUBRIC
//
// interface Drawable is worth 2 points, broken up as follows:
// 1 point for correct class definition (see #1, below)
// 1 point for correct draw method specification (see #2, below)

// RUBRIC ref #1:
public interface Drawable
{
	/**
	 * Tells the caller how to draw the object.
	 * 
	 * @param  g	a Graphics object
	 */
	// RUBRIC ref #2:
	public void draw();
}

/**
 * Write a description of class DrawableExplorer here.
 * 
 * @author (your name) 
 * @version (a version number or a date)
 */
public class DrawableExplorer extends Explorer implements Drawable
{
    public DrawableExplorer(Square location, Maze maze, String name)
    {
        super(location, maze, name);
    }
    
    public void draw()
    {   
        Square loc = location();
        float ssize = Square.SQUARE_SIZE;
        int x = loc.x(), y = loc.y();

        pushMatrix();
        translate(x+ssize/2, y+ssize/2);
        
        // ears
        fill(160, 100, 70);   // brown
        triangle(-12, -5, -14, -20, -5, -12);
        triangle(12, -5, 14, -20, 5, -12);
        fill(255,100,100);
        triangle(-10, -5, -12, -17, -7, -10);
        triangle(10, -5, 12, -17, 7, -10);
        
        // head
        fill(160, 100, 70);   // brown
        ellipse(0, 0, 3*ssize/5, 3*ssize/5);
        
        // nose
        fill(255,100,100);
        triangle(-ssize/10, ssize/20, ssize/10, ssize/20, 0, 3*ssize/20);
        
        // eyes
        fill(0);
        ellipse(-ssize/8, -ssize/10, 7, 7);
        ellipse(ssize/8, -ssize/10, 7, 7);  
       
        popMatrix();
    }
}

/**
 * Write a description of class DrawableMaze here.
 * 
 * @author (your name) 
 * @version (a version number or a date)
 */
public class DrawableMaze extends Maze implements Drawable
{
    /**
     * Constructor for objects of class DrawableMaze
     */
    public DrawableMaze(DrawableSquare[][] maze, int rows, int cols)
    {
        super(maze, rows, cols);
    }

    public void draw()
    {   
        int rows = rows();
        int cols = cols();
        int numRandOccupants = getNumRandOccupants();
        
        fill(35, 118, 28);  // dark green 
        rect(0, 0, Square.SQUARE_SIZE*cols, Square.SQUARE_SIZE*rows);
        
        for (int i = 0; i<rows; i++) {
            for (int j = 0; j<cols; j++) {
                ((Drawable)getSquare(i,j)).draw();
            }
        }
        
        for (int i=0; i<numRandOccupants; i++)
           ((Drawable)getRandomOccupant(i)).draw();
           
        ((Drawable)getExplorer()).draw();
    }
}

/**
 * Write a description of class DrawableMonster here.
 * 
 * @author (your name) 
 * @version (a version number or a date)
 */
public class DrawableMonster extends Monster implements Drawable
{
	public DrawableMonster(Maze maze, long seed)
	{
	    super(maze, seed);
	}
	
	public DrawableMonster(Maze maze)
	{
	    super(maze);
	}
	
	public DrawableMonster(Maze maze, Square location)
	{
		super(maze, location);
	}
    
    public void draw()
    {   
        Square loc = location();
        float ssize = Square.SQUARE_SIZE;
        int x = loc.x(), y = loc.y();
        
        if (loc.inView()) 
        {
           pushMatrix();
           translate(x+ssize/2, y+ssize/2);
        
           // head
           fill(120);
           ellipse(0, -10, 25, 25);
        
           // eyes
           fill(255);
           ellipse(0, -10, 20, 18);
           fill(120);
           ellipse(0, -17, 20, 10);
           fill(0);
           ellipse(-2, -10, 2, 2);
           ellipse(2, -10, 2, 2);
        
           // muzzle / jowels
           fill(180);
           ellipse(0, -3, 22, 10);
           fill(120);
           rect(-7, 0, 14, 13);
           ellipse(0, 10, 20, 10);
           fill(180);
           ellipse(-10, 5, 7, 15);
           ellipse(10, 5, 7, 15);
        
           // nose
           fill(0);
           ellipse(0, -6, 8, 5);
        
           // teeth
           fill(255);
           triangle(-7, 0, -5, -5, -3, 0);
           triangle(7, 0, 5, -5, 3, 0);
        
           // ears
           fill(120);
           ellipse(-10, -20, 6, 3);
           ellipse(10, -20, 6, 3);
        
           popMatrix();
        }
    }
}


/**
 * Write a description of class DrawableSquare here.
 * 
 * @author (your name) 
 * @version (a version number or a date)
 */
public class DrawableSquare extends Square implements Drawable
{
    Flower[] flowers;
    /**
     * Constructor for objects of class DrawableSquare
     */
    public DrawableSquare(boolean up, boolean right, boolean down, boolean left, int row, int col)
    {
        super(up, right, down, left, row, col);
        
        // create flowers
        flowers = new Flower[2];
        for (int i=0; i<2; i++)
        {
           flowers[i] = new Flower(random(x()+5, x()+SQUARE_SIZE-5),random(y()+5, y()+SQUARE_SIZE-5));
        }
    }
    
    public void draw() 
    {        
      
       if (seen()) 
       {
          int grass;
           
          if (inView())
             grass = color(100, 255, 80);
          else
             grass = color(20, 170, 20);
              
          fill(grass);
          rect(x(), y(), SQUARE_SIZE, SQUARE_SIZE);
          
          // draw flowers
          for (int i=0; i<2; i++)
          {
             flowers[i].draw();
          }
           
          // draw hedges (walls)
          for (int dir=UP; dir<=LEFT; dir++) 
          {
             if (wall(dir)) 
             {
                drawHedge(x(),y(),dir);
             }
          }
       }
    }
   
    public void drawHedge(int x, int y, int dir) 
    {
       noStroke();
       pushMatrix();
          // translate and rotate to correct position
          if (dir == UP) {
             translate(x,y);
          }
          else if (dir == RIGHT) {
             translate(x+SQUARE_SIZE, y);
             rotate(PI/2);
          }
          else if (dir == DOWN) {
             translate(x+SQUARE_SIZE, y+SQUARE_SIZE);
             rotate(PI);
          }
          else {
             translate(x, y+SQUARE_SIZE);
             rotate(PI*3/2);
          }
            
          // draw the hedge
          fill(25, 98, 21);  // dark green     
          beginShape();
          curveVertex(0,0);
          curveVertex(0,0);
          curveVertex(SQUARE_SIZE/6.0f, SQUARE_SIZE/10.0f);
          curveVertex(SQUARE_SIZE/3.0f, SQUARE_SIZE/14.0f);
          curveVertex(SQUARE_SIZE*3/5.0f, SQUARE_SIZE/9.0f);
          curveVertex(SQUARE_SIZE*3/4.0f, SQUARE_SIZE/14.0f);
          curveVertex(SQUARE_SIZE*7/8.0f, SQUARE_SIZE/14.0f);
          curveVertex(SQUARE_SIZE, 0);
          curveVertex(SQUARE_SIZE, 0);
          endShape(CLOSE);
          
          // add berries
          fill(0xffF56E8B); // pink
          ellipse(SQUARE_SIZE/4.0f, SQUARE_SIZE/22.0f, 3, 3);
          ellipse(SQUARE_SIZE*3/5.0f, SQUARE_SIZE/17.0f, 3, 3);
       popMatrix();
    }
    
   // inner class to scatter flowers around
   class Flower 
   {
      final float cx;
      final float cy;
   
      public Flower(float x, float y) 
      {
         cx = x;
         cy = y;
      }
   
      public void draw() 
      {
         float x, y, t = 0;
      
         // flower center
         noStroke();
         fill(0xffFFF773);
         ellipse(cx, cy, 4, 4);
      
         // petals
         fill(240);
         for (int i=0; i<13; i++) {
            x = cx + 4*cos(t);
            y = cy + 4*sin(t);
            ellipse(x,y,2,2);
            t += 0.5f;
         }
      }
   }
}

/**
 * Write a description of class DrawableTreasure here.
 * 
 * @author (your name) 
 * @version (a version number or a date)
 */
public class DrawableTreasure extends Treasure implements Drawable
{
	public DrawableTreasure(Maze maze, long seed)
	{
	    super(maze, seed);
	}
	
	public DrawableTreasure(Maze maze)
	{
	    super(maze);
	}
	
	public DrawableTreasure(Maze maze, Square location)
	{
		super(maze, location);
	}  

    public void draw()
    {   
        Square loc = location();
        int cheese;
        int holes;
        
        if (loc.seen()) {
           pushMatrix();
           translate(loc.x()+25, loc.y()+25);
           
           if (found())
           {
              cheese = color(230, 230, 0);
              holes = color(0xffFF7800);
           }
           else 
           {
              cheese = color(128);
              holes = color(100);
           }
           fill(cheese);
           rect(-15, -15, 30, 30);
           fill(holes);
           ellipse(-8, 5, 10, 10);
           ellipse(5, -4, 12, 8);
           ellipse(-7, -9, 5, 5);
           ellipse(8, 10, 5, 5);
           
           popMatrix();
        }
    }
}


/**
 * Explorer for the Maze game.
 * 
 * @author Julie Hatalsky 
 * @version V1.0
 * @version April 22, 2007
 */
public class Explorer extends Occupant
{
    private Maze maze;
    private String name;
    
    /**
     * Constructor for Explorer objects<br>
     * <b>Ensure:</b> this.location() == location<br>
     * <b>Ensure:</b> this.name() == name<br>
     * <br>
     * The Explorer object is constructed and the explorer 
     * looks around the maze from its current location.
     * 
     * @param start the starting location for the Occupant
     */
	public Explorer(Square location, Maze maze, String name)
	{
		super(location);
		this.name = name;
		this.maze = maze;
		
		maze.lookAround(location);
	}
	
	public String name() {return name;}
	
	public void move(int key)
	{
	    int direction = -1;
            int row = location().row();
        int col = location().col();
        
        switch (key)
        {
            case KeyEvent.VK_UP:
            case KeyEvent.VK_KP_UP:
                direction = Square.UP; 
                row--; break;
            case KeyEvent.VK_RIGHT:
            case KeyEvent.VK_KP_RIGHT:
                direction = Square.RIGHT; 
                col++; break;
            case KeyEvent.VK_DOWN:
            case KeyEvent.VK_KP_DOWN:
                direction = Square.DOWN; 
                row++; break;
            case KeyEvent.VK_LEFT:
            case KeyEvent.VK_KP_LEFT:
                direction = Square.LEFT; 
                col--; break;
            default: return;
        }
        
        if (!location().wall(direction) && direction != -1) {
            moveTo(maze.getSquare(row,col));
        }
    }
    
    public void moveTo(Square s) {
        super.moveTo(s);
        s.enter();
        maze.lookAround(s);
    }
}

/**
 * Write a description of class Maze here.
 * 
 * @author (your name) 
 * @version (a version number or a date)
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
	    this.maze = maze;
		this.rows = rows;
		this.cols = cols;    
		
		randOccupants = new ArrayList<RandomOccupant>();
	}
	
	// queries
	public Square getSquare(int row, int col) { return maze[row][col]; }
	public int rows() {return rows;}
	public int cols() {return cols;}
	public String explorerName() {return explorer.name();}
    public Explorer getExplorer() {return explorer;}
	public RandomOccupant getRandomOccupant(int index) {return randOccupants.get(index);}
	public int getNumRandOccupants() {return randOccupants.size();}
	
	public void addRandomOccupant(RandomOccupant ro) { randOccupants.add(ro);}
	public void setExplorer(Explorer e) {explorer = e;}
	
	public void explorerMove(int key)
	{
	    explorer.move(key);
	}
	
	public void randMove()
	{
	    for (int i=0; i<randOccupants.size(); i++)
	       randOccupants.get(i).move();
	}
	
    public int gameStatus()
    {
        int status = ACTIVE;
        
        if (foundAllTreasures()) {
            status = EXPLORER_WIN;
        }
        else {
            for (int i=0; i<randOccupants.size(); i++)
               if (randOccupants.get(i) instanceof Monster && 
                   randOccupants.get(i).location() == explorer.location())
                  status = MONSTER_WIN;
        }
        return status;
    }
	
	private boolean foundAllTreasures()
	{        
	    boolean foundAll = true;
        // check for win
        for (int i=0; i<randOccupants.size();i++)
           if (randOccupants.get(i) instanceof Treasure && !((Treasure)randOccupants.get(i)).found())
              foundAll = false;  
              
        return foundAll;
    }
    
    public void lookAround(Square s) 
    {
        int row = s.row();
        int col = s.col();
        
        resetInView();
        s.setInView(true);
        if (!maze[row][col].wall(Square.UP) && row-1 >= 0)
           maze[row-1][col].setInView(true);
        if (!maze[row][col].wall(Square.RIGHT) && col+1 < cols)
           maze[row][col+1].setInView(true);
        if (!maze[row][col].wall(Square.DOWN) && row+1 < rows)
           maze[row+1][col].setInView(true);
        if (!maze[row][col].wall(Square.LEFT) && col-1 >= 0)
           maze[row][col-1].setInView(true);
        if ((!maze[row][col].wall(Square.LEFT) && (col-1 >= 0) && !maze[row][col-1].wall(Square.UP)) ||
            (!maze[row][col].wall(Square.UP) && (row-1 >= 0) && !maze[row-1][col].wall(Square.LEFT)))
           maze[row-1][col-1].setInView(true);
        if ((!maze[row][col].wall(Square.LEFT) && (col-1 >= 0) && !maze[row][col-1].wall(Square.DOWN)) ||
            (!maze[row][col].wall(Square.DOWN) && (row+1 < rows) && !maze[row+1][col].wall(Square.LEFT)))
           maze[row+1][col-1].setInView(true);
        if ((!maze[row][col].wall(Square.RIGHT) && (col+1 < cols) && !maze[row][col+1].wall(Square.UP)) ||
            (!maze[row][col].wall(Square.UP) && (row-1 >= 0) && !maze[row-1][col].wall(Square.RIGHT)))
           maze[row-1][col+1].setInView(true);
        if ((!maze[row][col].wall(Square.RIGHT) && (col+1 < cols) && !maze[row][col+1].wall(Square.DOWN)) ||
            (!maze[row][col].wall(Square.DOWN) && (row+1 < rows) && !maze[row+1][col].wall(Square.RIGHT)))
           maze[row+1][col+1].setInView(true);
           
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
 * Write a description of class MazeGenerator here.
 * 
 * @author (your name) 
 * @version (a version number or a date)
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
/**
 * Write a description of class Monster here.
 * 
 * @author (your name) 
 * @version (a version number or a date)
 */
public class Monster extends RandomOccupant
{   
	/**
	 * Constructor for objects of class Explorer
	 */
	public Monster(Maze maze, long seed)
	{
	    super(maze, seed);
	}
	
	public Monster(Maze maze)
	{
	    super(maze);
	} 
	
	public Monster(Maze maze, Square location)
	{
		super(maze, location);
	}
}
/**
 * A class modeling an occupant of a maze.
 * 
 * @author Julie Hatalsky 
 * @version V1.0
 * @version April 22, 2007
 */
public class Occupant
{
    private Square location;

    /**
     * Default constructor for Occupants.<br>
     * <b>Ensure:</b> this.location() == null
     */
    public Occupant() {}
    /**
     * Constructor for Occupants used to initialize the starting location.<br>
     * <b>Ensure:</b> this.location() == start
     * 
     * @param start the starting location for the Occupant
     */
    public Occupant(Square start)
    {
       location = start;
    }
    
    /**
     * Query method for the Occupant's location.
     * 
     * @return the Maze Square the Occupant is currently in
     */
    public Square location() {return location;}

    /**
     * Moves the Occupant to a different Square in the Maze<br>
     * <b>Ensure:</b> this.location() == location
     * 
     * @param location the Sqaure to move the Occupant to
     */
    public void moveTo(Square location)
    {
        this.location = location;
    }
}

/**
 * Write a description of class RandomOccupant here.
 * 
 * @author (your name) 
 * @version (a version number or a date)
 */
public class RandomOccupant extends Occupant
{
    // instance variables - replace the example below with your own
    private Random rand;
    private Maze maze;
    
    /**
     * Constructor for objects of class Explorer
     */
    public RandomOccupant(Maze maze, long seed)
    {
        int row, col;
        
        rand = new Random(seed);
        row = rand.nextInt(maze.rows());
        col = rand.nextInt(maze.cols());
        
        moveTo(maze.getSquare(row,col));
        this.maze = maze;
    }
    
    public RandomOccupant(Maze maze)
    {
        int row, col;
        
        rand = new Random();
        row = rand.nextInt(maze.rows());
        col = rand.nextInt(maze.cols());
        
        moveTo(maze.getSquare(row,col));
        this.maze = maze;
    }
    
    public RandomOccupant(Maze maze, Square location)
    {
        super(location);
        this.maze = maze;
        rand = new Random();
    }
    
    public void move()
    {
        int direction;
        int row = location().row();
        int col = location().col();
        
        do {
            direction = rand.nextInt(4);
        } while (location().wall(direction));
         
        switch (direction)
        {
            case Square.UP:
                row--; break;
            case Square.RIGHT:
                col++; break;
            case Square.DOWN:
                row++; break;
            case Square.LEFT:
                col--; break;
            default: break;
        }
        
        moveTo(maze.getSquare(row,col)); 
    }
}
/**
 * A Square in a maze.
 * 
 * @author Julie Hatalsky
 * @version April 17, 2007
 */
public class Square
{
    public static final int SQUARE_SIZE = 50;
    public static final int UP = 0;
    public static final int RIGHT = 1;
    public static final int DOWN = 2;
    public static final int LEFT = 3;
    
    
    private boolean[] walls = new boolean[4];
    private boolean seen = false;
    private boolean inView = false;
    private int row, col;
    private Treasure treasure;

    /**
     * Constructor for objects of class Square
     */
    public Square(boolean up, boolean right, boolean down, boolean left, int row, int col)
    {
       walls[UP] = up;
       walls[RIGHT] = right;
       walls[DOWN] = down;
       walls[LEFT] = left;
       this.row = row;
       this.col = col;
    }
    
    // Queries
    public boolean wall(int direction) {return walls[direction];}
    public boolean seen() {return seen;}
    public boolean inView() {return inView;}
    public int row() {return row;}
    public int col() {return col;}
    public int x() {return col*SQUARE_SIZE;}
    public int y() {return row*SQUARE_SIZE;}
    public Treasure treasure() {return treasure;}
    
    // Commands
    public void setInView(boolean inView) 
    {   
        this.inView = inView;
        if (inView)
           seen = true;
    }
    public void setTreasure(Treasure t) {treasure = t;}
    public void enter() 
    {
        if (treasure != null)
           treasure.setFound();
    }
}
/**
 * Write a description of class Treasure here.
 * 
 * @author (your name) 
 * @version (a version number or a date)
 */
public class Treasure extends RandomOccupant
{
    private boolean found = false;
    
	/**
	 * Constructor for objects of class Explorer
	 */
	public Treasure(Maze maze, long seed)
	{
	    super(maze, seed);
		location().setTreasure(this);
	}
	
	public Treasure(Maze maze)
	{
	    super(maze);
		location().setTreasure(this);
	}
	
	public Treasure(Maze maze, Square location)
	{
		super(maze, location);
		location().setTreasure(this);
	}
	
	public boolean found() {return found;}
	public void setFound() {found = true;}
	public void move() {}
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
