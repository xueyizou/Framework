package modeling;

/**
 *
 * @author rl576
 */
public class Waypoint extends Entity
{
	private Waypoint nextPoint; //the id of the point to go to after this waypoint
	
	/** Constructor for Waypoint
	 * 
	 * @param ID the id of the waypoint
	 * @param next the id of the point that should be travelled to after this waypoint is reached
	 */
	public Waypoint(int ID, Waypoint next)
	{
		super(ID, Constants.EntityType.TWAYPOINT);
		nextPoint = next;
	}
	
	/**
	 * A method which returns the id of the point to go to after this waypoint has
	 * been reached
	 * 
	 * @return the id of the next point for the uas to travel to
	 */
	public Waypoint getNextWaypoint() {return nextPoint;}
	public void setNextWaypoint(Waypoint next) {nextPoint = next;}
}
