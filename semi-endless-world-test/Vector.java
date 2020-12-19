/**
 * Ein 2D-Vektor.
 * 
 * @author Poul Henriksen
 * @author Michael Kolling
 * 
 * @version 2.0
 */
public final class Vector {
	private double dx;
	private double dy;
	private int direction;
	private double length;

	/**
	 * Erzeugt einen neuen neutralen Vektor.
	 */
	public Vector() {
	}

	/**
	 * Erzeugt einen Vektor einer gegebenen Richtung und Länge. Die Richtung sollte
	 * im Bereich [0..359] liegen, wobei 0 OSTEN ist und die Grade im Uhrzeigersinn
	 * zunehmen.
	 */
	public Vector(int direction, double length) {
		this.length = length;
		this.direction = direction;
		updateCartesian();
	}

	/**
	 * Erzeugt einen Vektor durch Angabe der X- und Y-Abstände zwischen Anfangs- und
	 * Endpunkt.
	 */
	public Vector(double dx, double dy, double customLength) {
		this.dx = dx;
		this.dy = dy;
		updatePolar();
		this.setLength(customLength);
	}

	/**
	 * Setzt die Richtung dieses Vektors, ohne die Länge zu verändern.
	 */
	public void setDirection(int direction) {
		this.direction = direction;
		updateCartesian();
	}

	/**
	 * Addiert einen weiteren Vektor zu diesem Vektor.
	 */
	public void add(Vector other) {
		dx += other.dx;
		dy += other.dy;
		updatePolar();
	}

	/**
	 * Setzt die Länge dieses Vektors, ohne die Richtung zu verändern.
	 */
	public void setLength(double length) {
		this.length = length;
		updateCartesian();
	}

	/**
	 * Skaliert diesen Vektor hoch (Faktor > 1) oder herunter (Faktor < 1). Die
	 * Richtung wird beibehalten.
	 */
	public void scale(double factor) {
		length = length * factor;
		updateCartesian();
	}

	/**
	 * Setzt diesen Vektor auf den neutralen Vektor (Länge 0).
	 */
	public void setNeutral() {
		dx = 0.0;
		dy = 0.0;
		length = 0.0;
		direction = 0;
	}

	/**
	 * Kehrt die horizontale Komponente dieses Bewegungsvektors um.
	 */
	public void revertHorizontal() {
		dx = -dx;
		updatePolar();
	}

	/**
	 * Kehrt die vertikale Komponente dieses Bewegungsvektors um.
	 */
	public void revertVertical() {
		dy = -dy;
		updatePolar();
	}

	/**
	 * Liefert den X-Wert dieses Vektors zurück (Anfangs- bis Endpunkt).
	 */
	public double getX() {
		return dx;
	}

	/**
	 * Liefert den Y-Wert dieses Vektors zurück (Anfangs- bis Endpunkt).
	 */
	public double getY() {
		return dy;
	}

	/**
	 * Liefert die Richtung dieses Vektors (in Grad) zurück. 0 ist OSTEN.
	 */
	public int getDirection() {
		return direction;
	}

	/**
	 * Liefert die Länge dieses Vektors zurück.
	 */
	public double getLength() {
		return length;
	}

	/**
	 * Aktualisiert die Richtung und die Länge aus den aktuellen Werten von dx, dy.
	 */
	private void updatePolar() {
		this.direction = (int) Math.toDegrees(Math.atan2(dy, dx));
		this.length = Math.sqrt(dx * dx + dy * dy);
	}

	/**
	 * Aktualisiert dx und dy aus der aktuellen Richtung und Länge.
	 */
	private void updateCartesian() {
		dx = length * Math.cos(Math.toRadians(direction));
		dy = length * Math.sin(Math.toRadians(direction));
	}
}
