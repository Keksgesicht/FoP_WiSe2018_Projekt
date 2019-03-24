package dice3d.math;

import java.util.ArrayList;
import java.util.List;

import dice3d.models.cuboids.Cuboid;

public class geometry {
	
	public static Vector calcOutgoingVector(Cuboid c, Vector vOutside, Vector vInside) {
		//get All faces
		List<plane> planes = new ArrayList<plane>();
		Vector vOutCpy = new Vector(vOutside);
		Vector vInCpy  = new Vector(vInside);
		planes.add(new plane(c.vertices.get(0).position, c.vertices.get(1).position, c.vertices.get(2).position));
		planes.add(new plane(c.vertices.get(1).position, c.vertices.get(5).position, c.vertices.get(6).position));
		planes.add(new plane(c.vertices.get(5).position, c.vertices.get(4).position, c.vertices.get(7).position));
		planes.add(new plane(c.vertices.get(4).position, c.vertices.get(0).position, c.vertices.get(3).position));
		planes.add(new plane(c.vertices.get(3).position, c.vertices.get(2).position, c.vertices.get(6).position));
		planes.add(new plane(c.vertices.get(1).position, c.vertices.get(0).position, c.vertices.get(4).position));
		line l = new line(vOutside, vInside);
		for (plane p : planes) {
			Vector intersection = l.intersects(p);
			if (intersection != null) {
				line dotLine = new line(vOutside, p.getNormalenvektor(), true);
				Vector dot = dotLine.intersects(p);
				intersection.sub(dot);
				dot.sub(intersection);
				vInCpy.sub(vOutside);
				dot.sub(vOutside);
				dot.normalize();
				dot.scale(vInCpy.getSize() * 2);
				vOutCpy.sub(dot);
				return vOutCpy;
			}
		}
		return vInside;
	}
}
