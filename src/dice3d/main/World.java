package dice3d.main;

import java.util.ArrayList;
import java.util.Timer;
import java.util.TimerTask;

import dice3d.math.Vector;
import dice3d.models.cuboids.Cuboid;
import dice3d.models.cuboids.Dice;

public class World {

	public static final double projectionDistance = 1000;
	public Vector gravity;

	public ArrayList<Cuboid> cuboids;
	public Cuboid floor;

	public World() {
		World w = this;
		gravity = new Vector(0, .13, 0);
		
		cuboids = new ArrayList<Cuboid>();
		
		floor =  new Cuboid(10, 400, 600, 700, 1000, 50);
		cuboids.add(floor);

		cuboids.add(new Dice(250, 210, 800, 60));
		cuboids.add(new Dice(150, 210, 800, 60));
		cuboids.add(new Dice(50, 210, 800, 60));

		long delayInMS = 500;  // start updating after 500ms
		long intervalInMS = 15; // update every 15ms
		
		for(Cuboid c : cuboids) c.reset();

		new Timer().scheduleAtFixedRate(new TimerTask() {
			@Override
			public void run() {
				for ( Cuboid c : cuboids ) {
					if  (  c == floor ) continue;
					if  ( !c.collided ) c.update(w);
				}
			}
		}, delayInMS, intervalInMS);

	}

}
