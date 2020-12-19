// (World, Actor, GreenfootImage, and Greenfoot)
import greenfoot.Actor;
import greenfoot.World;

/**
 * A variation of an actor that maintains a precise location (using doubles for the co-ordinates
 * instead of ints). This allows small precise movements (e.g. movements of 1 pixel or less) that do
 * not lose precision.
 * 
 * source: Mover/SmoothMover classes of Greenfoot scenarios + edits
 * 
 * @author Poul Henriksen
 * @author Michael Kolling
 * @author Neil Brown
 * 
 * @version 3.0
 */
public abstract class SmoothMover extends Actor {
	private double exactX = 0.0;
	private double exactY = 0.0;
	private Vector movement = new Vector();

	public SmoothMover() {
		this(new Vector());
	}

	public SmoothMover(Vector speed) {
		this.movement = speed;
	}

	/**
	 * move forward with the given speed
	 */
	public void move() {
		move(1);
	}

	@Override
	protected void addedToWorld(World world) {
		this.exactX = getX();
		this.exactY = getY();
	}

	/**
	 * Move forward by the specified distance. (Overrides the method in Actor).
	 */
	@Override
	public void move(int steps) {
		double nextX = exactX + movement.getX() * steps;
		double nextY = exactY + movement.getY() * steps;
		if(nextX >= getWorld().getWidth() || nextX < 0) {
			nextX = exactX;
		}
		if(nextY >= getWorld().getHeight() || nextY < 0) {
			nextY = exactY;
		}
		setLocation(nextX, nextY);
	}

	/**
	 * If the World is not bounded, there is no need to check for world borders. So the movement is done
	 * respectively.<br>
	 * <br>
	 * (yes this is a instance variable for a bounded world but it is not accessible outside the
	 * greenfoot package, which is not so cool, maybe a change request for this is a good thing)
	 * 
	 * @param steps how many steps forward the SmoothMover should go.
	 */
	public void moveInUnBoundedWorld(int steps) {
		double nextX = exactX + movement.getX() * steps;
		double nextY = exactY + movement.getY() * steps;
		setLocation(nextX, nextY);
	}

	public void setLocation(double x, double y) {
		this.exactX = x;
		this.exactY = y;
		super.setLocation((int) x, (int) y);
	}

	public void setLocation(int x, int y) {
		setLocation((double) x, (double) y);
	}

	/**
	 * Ändert die aktuelle Bewegung durch Hinzufügen eines neuen Vektors zu der bestehenden Bewegung.
	 */
	public void addForce(Vector force) {
		movement.add(force);
	}

	/**
	 * Erhöht die Geschwindigkeit dieses SmoothMovers um den gegebenen Faktor. (Faktoren kleiner als 1
	 * verlangsamen die Geschwindigkeit.) Die Richtung bleibt unverändert.
	 */
	public void accelerate(double factor) {
		movement.scale(factor);
		if(movement.getLength() < 0.15) {
			movement.setNeutral();
		}
	}

	/**
	 * Liefert die Geschwindigkeit dieses Akteurs zurück.
	 */
	public double getSpeed() {
		return movement.getLength();
	}

	/**
	 * Setzt die Geschwindigkeit auf einen bestimmten Wert (s)
	 * 
	 * @param s - Vector-Länge/Geschwindigkeit
	 * @author (your name)
	 */
	public void setSpeed(double s) {
		movement.setLength(s);
	}

	/**
	 * Liefert die aktuelle Bewegung dieses Objekts (als Vektor) zurück.
	 */
	public Vector getMovement() {
		return movement;
	}

	/**
	 * Gibt <code>true</code> zurück, wenn sich der Actor am Weltrand befindet.
	 */
	public boolean atWorldEdge() {
		boolean atEdge = false;
		if(getX() <= 0 || getX() >= getWorld().getWidth() - 1) {
			atEdge = true;
		} else if(getY() <= 0 || getY() >= getWorld().getHeight() - 1) {
			atEdge = true;
		}
		return (atEdge);
	}

	/**
	 * Prüft, ob wir auf den Rand des Universums gestoßen sind. Wenn ja, wird abgeprallt.
	 */
	public void bounceAtEdge() {
		if(getX() <= 0 || getX() >= getWorld().getWidth() - 1) {
			setLocation((double) getX(), (double) getY());
			getMovement().revertHorizontal();
		} else if(getY() <= 0 || getY() >= getWorld().getHeight() - 1) {
			setLocation((double) getX(), (double) getY());
			getMovement().revertVertical();
		}
	}

	/**
	 * Wenn der Weltrand erreicht ist soll sich der Actor nicht weiterbewegen.
	 */
	public void stopAtWorldEdge() {
		if(atWorldEdge()) {
			this.setSpeed(0.0);
			if(getX() <= 0) {
				setLocation(1.0, getExactY());
			} else if(getX() >= getWorld().getWidth() - 1) {
				setLocation(getWorld().getWidth() - 2, getExactY());
			} else if(getY() <= 0) {
				setLocation(getExactX(), 1.0);
			} else if(getY() >= getWorld().getHeight() - 1) {
				setLocation(getExactX(), getWorld().getHeight() - 2);
			}
		}
	}

	/**
	 * Stoppt die Bewegung, die ein Actor gerade ausführt.
	 * 
	 * @author (Simon Häber)
	 */
	public void stop() {
		this.movement.setNeutral();
	}

	/**
	 * Setzt die Richtung des Vectors auf eine eine Bestimmte Grad-Zahl Damit keine Fehler einstehen,
	 * schrängt die Funktion den Bereich auf [0..359] ein
	 * 
	 * @param angle - Grad-Zahl in Grad
	 * @author (Simon Häber)
	 */
	public void setDirection(int angle) {
		if(angle > 359) {
			angle = angle - 360;
		} else if(angle < 0) {
			angle = angle + 360;
		}
		this.movement.setDirection(angle);
	}

	/**
	 * Berechnet Entfernung zwischen a und b; beides Double
	 * 
	 */
	public static double calcDistance(double a, double b) {
		return (b - a);
	}

	/**
	 * Berechnet Entfernung zwischen a und b; beides Integer
	 * 
	 * @author (Simon Häber)
	 */
	public static int calcDistance(int a, int b) {
		return (b - a);
	}

	/**
	 * Invertiert die Bewegung an der vertikalen Achse
	 */
	public void bounceVertical() {
		setLocation((double) getX(), (double) getY());
		getMovement().revertVertical();
	}

	/**
	 * Invertiert die Bewegung an der horizontalen Achse
	 */
	public void bounceHorizontal() {
		setLocation((double) getX(), (double) getY());
		getMovement().revertHorizontal();
	}

	/**
	 * Return the exact x-coordinate (as a double).
	 */
	public double getExactX() {
		return exactX;
	}

	/**
	 * Return the exact y-coordinate (as a double).
	 */
	public double getExactY() {
		return exactY;
	}

	public boolean hasReachedDestination(int destinationX, int destinationY, double toleranceRange) {
		double dx = calcDistance(getExactX(), destinationX);
		double dy = calcDistance(getExactY(), destinationY);
		double remainingDistance = Math.hypot(dx, dy);
		if(remainingDistance <= toleranceRange) {
			return true;
		}
		return false;
	}
}
