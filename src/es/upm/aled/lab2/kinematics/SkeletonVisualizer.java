package es.upm.aled.lab2.kinematics;

import java.io.IOException;
import java.util.List;

import javax.swing.Timer;

import es.upm.aled.lab2.gui.SkeletonPanel;
import es.upm.aled.lab2.utils.FileUtils;

/**
 * Tests the SkeletonPanel and ForwardKinematic classes by drawing a sample
 * full-body exoskeleton and performing a looping animating from a sequence of
 * movements taken from a file.
 * 
 * @author rgarciacarmona
 */
public class SkeletonVisualizer {

	/**
	 * Creates a sample exoskeleton with 10 Segments and animates it. The path were
	 * the file containing the animation is passed as the first argument. This file
	 * is in CSV format, with the first column indicating the frame of animation
	 * (modulo 256), and the next 8 columns storing the angles (in degrees) that the
	 * following body parts must assume (in this order): LeftUpperArm,
	 * RightUpperArm, LeftForearm, RightForearm, LeftThigh, RightThigh, LeftCalf,
	 * RightCalf
	 * 
	 * @param args The file containing the animation data.
	 */
	public static void main(String[] args) {
		// Kinematic chain definition
		// Hip (root)
		Segment hip = new Segment(0, Math.toRadians(0)); // Si cambias el primer número cambias la longitud de los segmentos
		// Torso
		Segment torso = new Segment(120, Math.toRadians(-90)); // Upwards, in screen coordinates, Y is down
		hip.addChild(torso);
		// Neck
		Segment neck = new Segment(40, Math.toRadians(0));
		torso.addChild(neck);
		// Arms
		Segment leftUpperArm = new Segment(80, Math.toRadians(90));
		Segment rightUpperArm = new Segment(80, Math.toRadians(-90));
		torso.addChild(leftUpperArm);
		torso.addChild(rightUpperArm);
		Segment leftForearm = new Segment(70, Math.toRadians(-20));
		Segment rightForearm = new Segment(70, Math.toRadians(20));
		leftUpperArm.addChild(leftForearm);
		rightUpperArm.addChild(rightForearm);
		// Legs
		Segment leftThigh = new Segment(100, Math.toRadians(120));
		Segment rightThigh = new Segment(100, Math.toRadians(60));
		hip.addChild(leftThigh);
		hip.addChild(rightThigh);
		Segment leftCalf = new Segment(90, Math.toRadians(10));
		Segment rightCalf = new Segment(90, Math.toRadians(-10));
		leftThigh.addChild(leftCalf);
		rightThigh.addChild(rightCalf);
		
		//Si quieres dedos tendrás que añadir hijos a Forearm

		// Exoskeleton drawing
		SkeletonPanel panel = new SkeletonPanel(hip, 600, 600);

		// Loop animation
		// Load motion sequence from file
		if (args.length > 0) {
			try {
				// Load frame
				List<double[]> frames = FileUtils.loadMotion(args[0]);
				// Run animation
				final int[] frameIndex = { 0 };
				// 5 frames per second
				new Timer(200, e -> {
					double[] angles = frames.get(frameIndex[0]);

					// Apply angles to joints
					leftUpperArm.setAngle(angles[0]);
					rightUpperArm.setAngle(angles[1]);
					leftForearm.setAngle(angles[2]);
					rightForearm.setAngle(angles[3]);
					leftThigh.setAngle(angles[4]);
					rightThigh.setAngle(angles[5]);
					leftCalf.setAngle(angles[6]);
					rightCalf.setAngle(angles[7]);

					// Update drawing
					panel.repaint();

					// Perform the motion in loop
					frameIndex[0] = (frameIndex[0] + 1) % frames.size();
				}).start();
			} catch (IOException e) {
				System.out.println("Error accessing file.");
			}

		}
	}
}
