package game.map;

import java.awt.Point;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Random;
import game.map.GameMap;

/**
 * Diese Klasse teilt Burgen in Königreiche auf
 */
public class Clustering {

    private Random random;
    private final List<Castle> allCastles;
    private final int kingdomCount;

    /**
     * Ein neues Clustering-Objekt erzeugen.
     * @param castles Die Liste von Burgen, die aufgeteilt werden sollen
     * @param kingdomCount Die Anzahl von Königreichen die generiert werden sollen
     */
    public Clustering(List<Castle> castles, int kingdomCount) {
        if (kingdomCount < 2)
            throw new IllegalArgumentException("Ungültige Anzahl an Königreichen");

        this.random = new Random();
        this.kingdomCount = kingdomCount;
        this.allCastles = Collections.unmodifiableList(castles);
    }

    /**
     * Gibt eine Liste von Königreichen zurück.
     * Jedes Königreich sollte dabei einen Index im Bereich 0-5 bekommen, damit die Burg richtig angezeigt werden kann.
     * Siehe auch {@link Kingdom#getType()}
     */
    public List<Kingdom> getPointsClusters() {
    	GameMap map = GameMap.getMap();
    	int width = map.getWidth();
    	int height = map.getHeight();
    	ArrayList<Kingdom> allKingdoms = new ArrayList<Kingdom>();
    	// Schritt 1
    	for(int i = 0; i < kingdomCount; i++) {
    		Kingdom kingdom = new Kingdom(i);
    		kingdom.setCenter(random.nextInt(width), random.nextInt(height));
    		allKingdoms.add(kingdom);
    	}
    	ArrayList<Point> prevCenterList = new ArrayList<Point>();
    	prevCenterList.add(new Point());
    	ArrayList<Point> newCenterList = new ArrayList<Point>();
    	// Schritt 4
    	while(!prevCenterList.equals(newCenterList)) {
	    	for(Kingdom kido : allKingdoms) {
	    		if(!kido.getCastles().isEmpty())
	    			kido.deleteCastles();
	    	}
	    	// Schritt 2
	    	for(Castle c : allCastles) {
	    		double smallestDist = width * height;
	    		Kingdom closestKingdom = null;
	    		for(Kingdom kido : allKingdoms) {
	    			if(c.distance(kido.getCenter()) < smallestDist) {
	    				closestKingdom = kido;
	    				smallestDist = c.distance(kido.getCenter());
	    			} 
	    		} c.setKingdom(closestKingdom);
	    	}
	    	prevCenterList.clear();
	    	prevCenterList.addAll(newCenterList);
	    	newCenterList.clear();
	    	// Schritt 3
	    	for(Kingdom kido : allKingdoms) {
	    		int avgX = 0;
	    		int avgY = 0;
	    		
	    		for(Castle burg : kido.getCastles()) {
	    			avgX += burg.getLocationOnMap().x;
	    			avgY += burg.getLocationOnMap().y;
	    		}
	    		avgX = avgX / kido.getCastles().size();
	    		avgY = avgY / kido.getCastles().size();
	    		
	    		kido.setCenter(avgX, avgY);
	    		newCenterList.add(kido.getCenter());
	    	}
    	} return allKingdoms;
    }
}
