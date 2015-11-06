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
