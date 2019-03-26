package dice3d.models.cuboids;
import java.awt.BasicStroke;
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
import dice3d.math.Vector;
import dice3d.math.Plane;
import dice3d.models.Edge;
import dice3d.models.Vertex;
import gui.Resources;

@SuppressWarnings("unused")
public class Dice extends Cube {

	private int NumberRolled;
	private Vertex[][] face = new Vertex[6][4];
	BufferedImage imgs[] = new BufferedImage[6];
	private int randomisation = 0;
	public int diceCnt;

	/**
	 * make a new dice a position x, y, z with the size and set all internal values accordingly
	 * @param x
	 * @param y
	 * @param z
	 * @param size
	 */
	public Dice(double x, double y, double z, int size, int cnt) {
		super(x,y,z, size);
		
		diceCnt = cnt;
		
		try {
			for(int i=0 ; i < 6; i++)
				imgs[i] = Resources.loadImage("dice" + (i + 1) + ".jpg");
		} catch (IOException e) {
			e.printStackTrace();
		}
		
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

	/**
	 * draw the dice
	 */
	public void draw(Graphics2D g) {
		drawDice(g);
	}

	/**
	 * reset the dice and roll it again
	 */
	public void reset() {
		for (Vertex v : vertices) {
			v.reset();
			Vector random = new Vector(Math.random()*diceCnt+diceCnt+2, -Math.random()*15 -5.5, Math.random()*2*(3-diceCnt)+(3-diceCnt));
			v.positionOld.sub(random);
		}
	}
	
	/**
	 * update the dice's position
	 * best called every tick
	 * also update the face being upside accessible via getNumberRolled()
	 */
	public void update(World w) {
		for (Vertex v : vertices) v.update(w);
		for (int i = 0; i < 5; i++) {
			for (Edge e : edges) e.update(w);
			for ( Cuboid c2 : w.cuboids ) if ( this != c2 ) updateCollision(w, c2);
		}
		double lowestHeight = 3210;
		int lowestFace = 0;
		for (int i = 0; i < face.length; i++) {
			Vertex[] facee = face[i];
			double height = 0;
			for (Vertex v : facee) height += v.position.y;
			if (height < lowestHeight) {
				lowestHeight = height;
				lowestFace = (i);
			}
		}
		NumberRolled = lowestFace+1;
	}
	
	/**
	 * first call me when notMoving() == true
	 * if you want to get a valid result!
	 * @return the number thats on top of the dice
	 */
	public int getNumberRolled() {
		return NumberRolled;
	}
	
	/**
	 * draw the given image img on a given face f displayed in the Graphics2D element g  
	 * @param g
	 * @param img
	 * @param f
	 */
	private void drawFace(Graphics2D g, BufferedImage img, Vertex[] f) {
		Plane p = new Plane(f[0].position, f[1].position, f[3].position);
		for (double x = 0; x < img.getWidth(); x++) {
			for (double y = 0; y < img.getHeight(); y++) {
				Vector pos = new Vector(p.getStuetzVektor());
				Vector posHelp1 = new Vector(p.getRichtungsVektor1());
				Vector posHelp2 = new Vector(p.getRichtungsVektor2());
				posHelp1.scale((x+1)/60);
				posHelp2.scale((y+1)/60);
				pos.add(posHelp1);
				pos.add(posHelp2);
				int vx = (int) (World.projectionDistance * pos.x / pos.z);
				int vy = (int) (World.projectionDistance * pos.y / pos.z);
	        	Polygon polygon = new Polygon();
	        	polygon.addPoint(vx, vy);
	        	polygon.addPoint(vx+1, vy);
	        	polygon.addPoint(vx, vy+1);
	        	polygon.addPoint(vx+1, vy+1);
	        	g.setColor(new Color(img.getRGB((int)x, (int)y)));
	        	g.draw(polygon);
			}
		}
	}

	/**
	 * hide all points
	 */
	public void hide() {
		for (Vertex v : vertices) v.hide();
	}
	
	/**
	 * draw the shadow of the dice on the hardcoded height of 400
	 * @param g
	 */
	public void drawShadow(Graphics2D g) {
		 for (int f = 0; f < 6; f++) {
        	Polygon polygon = new Polygon();
            polygon.reset();
            for (int v = 0; v < 4; v++) {
            	Vertex point = face[f][v];
                double px = World.projectionDistance * (point.position.x / point.position.z);
                double py = World.projectionDistance * (400 / point.position.z);
                polygon.addPoint((int) px, (int) py);
            }
            g.setColor(new Color(30, 30, 30, 150));
            g.fill(polygon);
        }
	}
	
	/**
	 * draw the dice
	 * if possible with textures
	 * if not just write numbers on each visible face
	 * @param g
	 */
	private void drawDice(Graphics2D g) {
		for (int f = 0; f < 6; f++) {
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
			if (visible < 0) {
				g.setColor(Color.WHITE);
				if (imgs[f] != null) {
					drawFace(g, imgs[f], face[f]);
					g.setColor(Color.BLACK);
				} else {
					//if image load error write numbers!
					g.fill(polygon);
					g.draw(polygon);
					g.setColor(Color.BLACK);
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
