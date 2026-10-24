package es.upm.aled.lab2.kinematics;

import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author Cristina
 */
public class Segment {
	private double length;
	private double angle;
	private List<Segment> children;
	
	/**
	 * @param length
	 * @param angle
	 */
	public Segment(double length, double angle) {
		this.length = length;
		this.angle = angle;
		this.children = new ArrayList<Segment>();
	}

	public double getLength() {
		return length;
	}

	public void setLength(double length) {
		this.length = length;
	}

	public double getAngle() {
		return angle;
	}

	public void setAngle(double angle) {
		this.angle = angle;
	}

	public List<Segment> getChildren() {
		return children;
	}

	public void setChildren(List<Segment> children) {
		this.children = children;
	}
	
	public void addChild(Segment child) {
		if(!children.contains(child))
			children.add(child);
	}				
}
