import java.awt.*;
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
