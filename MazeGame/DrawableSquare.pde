import java.awt.*;

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
    
    void draw() 
    {        
      
       if (seen()) 
       {
          color grass;
           
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
   
    void drawHedge(int x, int y, int dir) 
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
          curveVertex(SQUARE_SIZE/6.0, SQUARE_SIZE/10.0);
          curveVertex(SQUARE_SIZE/3.0, SQUARE_SIZE/14.0);
          curveVertex(SQUARE_SIZE*3/5.0, SQUARE_SIZE/9.0);
          curveVertex(SQUARE_SIZE*3/4.0, SQUARE_SIZE/14.0);
          curveVertex(SQUARE_SIZE*7/8.0, SQUARE_SIZE/14.0);
          curveVertex(SQUARE_SIZE, 0);
          curveVertex(SQUARE_SIZE, 0);
          endShape(CLOSE);
          
          // add berries
          fill(#F56E8B); // pink
          ellipse(SQUARE_SIZE/4.0, SQUARE_SIZE/22.0, 3, 3);
          ellipse(SQUARE_SIZE*3/5.0, SQUARE_SIZE/17.0, 3, 3);
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
   
      void draw() 
      {
         float x, y, t = 0;
      
         // flower center
         noStroke();
         fill(#FFF773);
         ellipse(cx, cy, 4, 4);
      
         // petals
         fill(240);
         for (int i=0; i<13; i++) {
            x = cx + 4*cos(t);
            y = cy + 4*sin(t);
            ellipse(x,y,2,2);
            t += 0.5;
         }
      }
   }
}
