package gml.reader.model;

public class Punkt {

	public double X;
	public double Y;
	/*
	 * KONSTRUKTOR
	 */
	public Punkt (double x, double y){
		this.X = x;
		this.Y = y;
	}

	public double getX() {
		return X;
	}

	public void setX(int x) {
		X = x;
	}

	public double getY() {
		return Y;
	}

	public void setY(int y) {
		Y = y;
	}

}
