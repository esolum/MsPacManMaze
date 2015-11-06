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
