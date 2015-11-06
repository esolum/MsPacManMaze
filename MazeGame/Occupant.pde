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
