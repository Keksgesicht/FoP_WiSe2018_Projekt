package dice3d.models.cuboids;
import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Polygon;
import java.awt.geom.AffineTransform;
import java.awt.image.AffineTransformOp;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

import javax.imageio.ImageIO;

import dice3d.main.World;
import dice3d.models.Vertex;

@SuppressWarnings("unused")
public class Dice extends Cube {

	private int NumberRolled = 1;
	private Vertex[][] face = new Vertex[6][4];
	BufferedImage imgs[] = new BufferedImage[6];


	public Dice(double x, double y, double z, int size) {
		super(x,y,z, size);
		
		/*
		 * try { imgs[0] = ImageIO.read(new File("res/dice1.jpg")); imgs[1] =
		 * ImageIO.read(new File("res/dice2.jpg")); imgs[2] = ImageIO.read(new
		 * File("res/dice3.jpg")); imgs[3] = ImageIO.read(new File("res/dice4.jpg"));
		 * imgs[4] = ImageIO.read(new File("res/dice5.jpg")); imgs[5] = ImageIO.read(new
		 * File("res/dice6.jpg")); } catch (IOException e) {}
		 */
		
		//1
		face[0][0] = vertices.get(0); // a 
		face[0][1] = vertices.get(1); // b
		face[0][2] = vertices.get(2); // c ...
		face[0][3] = vertices.get(3);

		//2
		face[1][0] = vertices.get(1);
		face[1][1] = vertices.get(5);
		face[1][2] = vertices.get(6);
		face[1][3] = vertices.get(2);

		//6
		face[5][0] = vertices.get(5);
		face[5][1] = vertices.get(4);
		face[5][2] = vertices.get(7);
		face[5][3] = vertices.get(6);

		//5
		face[4][0] = vertices.get(4);
		face[4][1] = vertices.get(0);
		face[4][2] = vertices.get(3);
		face[4][3] = vertices.get(7);

		//4
		face[3][0] = vertices.get(3);
		face[3][1] = vertices.get(2);
		face[3][2] = vertices.get(6);
		face[3][3] = vertices.get(7);

		//3
		face[2][0] = vertices.get(1);
		face[2][1] = vertices.get(0);
		face[2][2] = vertices.get(4);
		face[2][3] = vertices.get(5);
	}


	public void draw(Graphics2D g) {
		drawDice(g);
		// draw all edges, even inner ones
		// for(Edge e: edges) {
		// e.draw(g);
		// }

		// for (Vertex point : vertices) {
		// point.draw(g);
		// }
	}

	public void reset() {
		for (Vertex v : vertices) {
			v.reset();
		}
		NumberRolled++;
		collided = false;
	}
	
	public int getNumberRolled() {
		return NumberRolled;
	}

	private void drawDice(Graphics2D g) {
		for (int f = 0; f < 6; f++) {
			// each face as polygon
			Polygon polygon = new Polygon();

			for (int v = 0; v < 4; v++) {
				Vertex vertex = face[f][v];
				double vx = World.projectionDistance * vertex.position.x / vertex.position.z;
				double vy = World.projectionDistance * vertex.position.y / vertex.position.z;

				polygon.addPoint((int) vx, (int) vy);
			}

			// back-face culling
			double v1x = polygon.xpoints[0] - polygon.xpoints[1];
			double v1y = polygon.ypoints[0] - polygon.ypoints[1];
			double v2x = polygon.xpoints[2] - polygon.xpoints[1];
			double v2y = polygon.ypoints[2] - polygon.ypoints[1];
			double visible = v1x * v2y - v1y * v2x;
			//if (true) {
			if (visible < 0) {
				g.setColor(Color.WHITE);
				if(collided) {
					g.setColor(Color.RED);
				}
				//g.fill(polygon);
				g.setColor(Color.BLACK);
				if (imgs[f] != null) {
//				    g.setClip(polygon);
				    List<Integer> xList = new ArrayList<Integer>();
				    for (int i : polygon.xpoints) xList.add(i);
				    xList.stream().sorted().collect(Collectors.toList());
				    List<Integer> yList = new ArrayList<Integer>();
				    for (int i : polygon.ypoints) yList.add(i);
				    yList.stream().sorted().collect(Collectors.toList());
				    
//				    int xskew = (int) (v1x > v2x ? v2x : v1x);
//				    int yskew = (int) (v1y > v2y ? v2y : v1y);
				    int xlen  = (int) (v1x < v2x ? v2x : v1x);
				    int ylen  = (int) (v1y < v2y ? v2y : v1y);
				    
				    AffineTransform tx = new AffineTransform();
//				    tx.shear(xskew, yskew);
				    AffineTransformOp op = new AffineTransformOp(tx,
				            AffineTransformOp.TYPE_BILINEAR);
				    BufferedImage imgToUse = op.filter(imgs[f], null);
				    g.drawImage(imgToUse, xList.get(0), yList.get(0), Math.abs(xlen), Math.abs(ylen), null);
//				    g.setClip(null);
				    g.draw(polygon);
				} else {
					g.draw(polygon);
					// draw number of each face (that is visible)
					// calc center of each face/square
					double xs = 0d;
					double ys = 0d;
	
					for (int i = 0; i < 4; i++) {
						xs += polygon.xpoints[i];
						ys += polygon.ypoints[i];
					}
					xs = xs / 4;
					ys = ys / 4;
					g.drawString(f + 1 + "", (int) xs, (int) ys);
				}
			}
		}

	}

}
