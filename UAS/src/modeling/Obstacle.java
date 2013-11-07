package modeling;
import sim.util.*;

/**
 *
 * @author Robert Lee
 */
public abstract class Obstacle extends Entity
{
	public Obstacle(int idNo, EntityType type)
	{
		super(idNo, type);
	}
	
	/**
	 * method which returns true or false if a provided coordinate is in the shape
	 * would have to be overwritten when implemented
	 */
	public boolean inShape(Double2D coord)
	{
		return false;
	}	
	
	public boolean inCollision(Double2D coord, double safeMargin)
	{
		
		return false;
		
	}
	/**
	 * Returns the distance from the closest part of the obstacle to the coord provided.
	 * 
	 * @param coord the coordinate the distance to be checked for
	 */
	public double obstacleToPoint(Double2D coord)
	{
		return Double.MAX_VALUE;
	}
}
