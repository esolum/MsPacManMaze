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
