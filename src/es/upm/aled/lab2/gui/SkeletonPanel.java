package es.upm.aled.lab2.gui;

import java.awt.Graphics;

import javax.swing.JFrame;
import javax.swing.JPanel;
import es.upm.aled.lab2.kinematics.*;

/**
 * Draws on a window an exoskeleton represented as a tree of Nodes.
 * 
 * @author rgarciacarmona
 */
public class SkeletonPanel extends JPanel {

	private static final long serialVersionUID = 8724301078499171545L;
	private Segment root;

	/**
	 * Shows a window containing a 2D representation of an exoskeleton. Each joint
	 * in the kinematic chain is represented with a dot, and each segment with a
	 * line that connects two dots together. The end effector is also represented as
	 * a dot. It can be animated to show the exoskeleton's movement by calling the
	 * repaint() method after modifying the attributes of the kinematic chain's
	 * elements.
	 * 
	 * This class uses ForwardKinematics to compute the positions of every element
	 * in the kinematic chain.
	 * 
	 * @param root  The first Segment of the kinematic chain.
	 * @param sizeX The width of the window.
	 * @param sizeY The height of the window.
	 */
	public SkeletonPanel(Segment root, int sizeX, int sizeY) {
		this.root = root;
		// Setup Swing window
		JFrame frame = new JFrame("Recursive Skeleton - Forward Kinematics");
		frame.add(this);
		frame.setSize(600, 600);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setVisible(true);
	}

	@Override // Está reprogramando un método heredado de padre
	protected void paintComponent(Graphics g) {
		// Draws the window
		super.paintComponent(g); // Quiere reprogramarlo pero guardando su uso de la clase padre
		float originX = getWidth() / 2;
		float originY = getHeight() / 2;

		// Computes the full tree of positions
		Node nodeRoot = ForwardKinematics.computePositions(root, originX, originY); // Convertiremos la información de segmentos en información de nodos. Me pasan info sobre el segmento raíz y dónde está aplicado y saco info sobre el nodo

		// Draw the tree of Nodes recursively
		drawSkeleton(g, originX, originY, nodeRoot); // Como origen le pasas el centro de la pantalla y como origen el nodo raíz (el primero de todos). Se empieza a pintar por el nodo raíz
	}

	private void drawSkeleton(Graphics g, double parentX, double parentY, Node node) { // Para pintar los hijos debes saber donde están los padres.
		g.fillOval((int) node.getX() - 4, (int) node.getY() - 4, 8, 8); // Pinta un círculo en las coordenadas del nodo que le has pasado (nodo raíz), recibe coordenadas x e y del nodo y les resta cuatro unidades. Luego los 8 son el ancho y el alto. Lo desplazas solo para darle volumen al punto
		g.drawLine((int) parentX, (int) parentY, (int) node.getX(), (int) node.getY()); // Ahora dibujará una línea. Esto sirve para pintar círculo y luego raya. Pintan una raya con el nodo del que viene.
		// Esas dos sentencias no dependen de cuándo estas llamando a drawSkeleton
		// Código común. Se ejecutarán sí o sí. Cualquier invocación al método los ejecutará incondicionalmente.
		if (node.getChildren().size() == 0) {
			return; // Si el nodo que le pasas ya no tiene hijos, cuando lo has pintando no tienes que seguir. Si el nodo del que viene no tiene hijo, has terminado.
		}
		// Caso base: Cuando llegas a la sentencia que no debes continuar
		// Hijo por hijo invocarás a drawSkeleton con el mismo controlador de gráficos. Del nodo en el que estás le pasas su X y su Y (haces la invocación recordándole el padre del siguiente que pintas, luego child es el nodo que pintarás ahora (el hijo))
		for (Node child : node.getChildren()) {
			drawSkeleton(g, node.getX(), node.getY(), child); // Llamada al propio método. Es decir, método recursivo
		// Vuelve a preguntarles por orden de atrás adelante para ir cerrando que no tienen más hijos.
		// Paso recursivo: Invocación del método a sí mismo.
		}
	}
}