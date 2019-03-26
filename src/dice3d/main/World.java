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
	public boolean finished = false;
	
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
		long intervalInMS = 15; // update every 15ms
		
		for(Cuboid c : cuboids) c.hide();

		new Timer().scheduleAtFixedRate(new TimerTask() {
			int notMovinCnt = 0;
			@Override
			public void run() {
				for(Cuboid c : cuboids) {
					if(c == floor) continue;
					if(!c.notMoving()) c.update(w);
					else notMovinCnt++;
				}
				if(notMovinCnt >= 3) {
					for(Cuboid c : cuboids) {
						if(c == floor) continue;
						col = false;
						finished = true;
					}
				}
				notMovinCnt = 0;
			}
		}, delayInMS, intervalInMS);
	}
	
	/**
	 * roll 
	 * @param cnt
	 */
	public void roll(int cnt) {
		for(Cuboid c : cuboids) if(c != floor) ((Dice)c).hide();
		finished = false;
		switch(cnt) {
			case 1:
				((Dice)this.cuboids.get(2)).reset();
				break;
			case 2:
				((Dice)this.cuboids.get(1)).reset();
				((Dice)this.cuboids.get(3)).reset();
				break;
			case 3:
				((Dice)this.cuboids.get(1)).reset();
				((Dice)this.cuboids.get(2)).reset();
				((Dice)this.cuboids.get(3)).reset();
				break;
			default:
				System.out.println("Jan hat gesagt, es gibt max 3 Wuerfel!");
		}
	}
}
