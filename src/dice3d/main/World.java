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

	public boolean col;
	
	public World() {
		World w = this;
		gravity = new Vector(0, .3, 0);
		
		cuboids = new ArrayList<Cuboid>();
		
		floor =  new Cuboid(-1000, 400, -1000, 20000, 20000, 50);
		cuboids.add(floor);

		cuboids.add(new Dice(50, 310, 800, 61, 0));
		cuboids.add(new Dice(150, 310, 800, 61, 1));
		cuboids.add(new Dice(250, 310, 800, 61, 2));

		long delayInMS = 500;  // start updating after 500ms
		long intervalInMS = 1; // update every 15ms
		
		for(Cuboid c : cuboids) c.reset();

		
		new Timer().scheduleAtFixedRate(new TimerTask() {
			int notMovinCnt = 0; int colCnt = 0; int ThroughCnt = 0;
			int[] rolled = new int[6];
			@Override
			public void run() {
				for ( Cuboid c : cuboids ) {
					if (  c == floor  ) continue;
					if (!c.notMoving()) c.update(w);
					else {
						notMovinCnt++;
					}
				}
				if(notMovinCnt >= 3) for ( Cuboid c : cuboids ) {
						if (  c == floor  ) continue;
						c.reset();
						if (col) colCnt++;
						col = false;
						ThroughCnt++;
						rolled[((Dice)c).getNumberRolled()-1]++;
						System.out.println(colCnt + " ~ \" ~ " + ThroughCnt + "  \\ -:- /  " + "1: " + rolled[0] + ", 2: " + rolled[1] + ", 3: " + rolled[2] + ", 4: " + rolled[3] + ", 5: " + rolled[4] + ", 6: " + rolled[5]);
					}
				notMovinCnt = 0;
			}
		}, delayInMS, intervalInMS);
	}
}
