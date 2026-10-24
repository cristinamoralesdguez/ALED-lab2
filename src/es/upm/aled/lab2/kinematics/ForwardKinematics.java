package es.upm.aled.lab2.kinematics;

import es.upm.aled.lab2.gui.Node;

/**
 * This class implements a forward kinematics algorithm using recursion. It
 * expects a tree of Segments (defined by its length and angle with respect to
 * the previous Segment in the tree) and returns a tree of Nodes (defined by
 * their absolute coordinates in a 2-dimensional space).
 * 
 * @author rgarciacarmona
 */
public class ForwardKinematics {

	/**
	 * Returns a tree of Nodes to be used by SkeletonPanel to draw the position of
	 * an exoskeleton. This method is the public facade to a recursive method that
	 * builds the result from a tree of Segments defined by their angle and length,
	 * and the relationship between them (which Segment is children of which).
	 * 
	 * @param root    The root of the tree of Segments.
	 * @param originX The X coordinate for the origin point of the tree.
	 * @param originY The Y coordinate for the origin point of the tree.
	 * @return The tree of Nodes that represent the exoskeleton position in absolute
	 *         coordinates.
	 */
	// Public method: returns the root of the position tree
	public static Node computePositions(Segment root, double originX, double originY) { // Método fachada
		return computePositions(root, originX, originY, 0.0); // Te piden que devuelva los mismos parámetros que recibe y un ángulo de 0.0. LLamas al método de abajo que es donde está la recursividad de verdad.
	}

	// Private helper method that implements the recursive algorithm
	private static Node computePositions(Segment link, double baseX, double baseY, double accumulatedAngle) { // El ángulo se va acumulando de un cálculo al siguiente
		// Me pasan el segmento raíz y tengo que devolver tal nodo que tenga guardado quienes son sus hijos.
		long startTime = System.nanoTime();
		accumulatedAngle += link.getAngle();
		double x = baseX + link.getLength()*Math.cos(accumulatedAngle);
		double y = baseY + link.getLength()*Math.sin(accumulatedAngle);
		Node node = new Node(x,y);
		//Para cierto segmento genero el nodo de a donde llega. Codigo común
		
		if(link.getChildren().isEmpty()) {
			long runningTime = System.nanoTime() - startTime;
			System.out.println("Tiempo de computePositions para un segmento con " + link.getChildren().size() + " hijos: " + runningTime + " nanosegundos");
			return node; // Si el segmento en el que estoy no tiene hijos, hemos terminado.
		}
		
		for(Segment child: link.getChildren())
			node.addChild(computePositions(child, x, y, accumulatedAngle)); // Recorro los hijos y a cada uno le pido si puede convertir a nodo el a dónde llega. Luego ños alado como hijos
	
		long runningTime = System.nanoTime()-startTime;
		System.out.println("Tiempo ed computePositions para un segmento con " + link.getChildren().size() + " hijos: " + runningTime + " nanosegundos");
		return node;
	}
}