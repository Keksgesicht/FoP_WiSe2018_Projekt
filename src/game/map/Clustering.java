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
    	
    	for(int i = 0; i < kingdomCount; i++) {
    		Kingdom kingdom = new Kingdom(i);
    		kingdom.setCenter((int) Math.round(Math.random() * width), (int) Math.round(Math.random() * height));
    		allKingdoms.add(kingdom);
    	}
    	
    	ArrayList<Point> prevCenterList = new ArrayList<Point>();
    	prevCenterList.add(new Point());
    	ArrayList<Point> newCenterList = new ArrayList<Point>();
    	
    	while(!prevCenterList.equals(newCenterList)) {
	    	for(int kidoIndex = 0; kidoIndex < allKingdoms.size(); kidoIndex++) {
	    		if(!allKingdoms.get(kidoIndex).getCastles().isEmpty())
	    			allKingdoms.get(kidoIndex).deleteCastles();
	    	}
	    	
	    	for(int c = 0; c < allCastles.size(); c++) {
	    		double smallestDist = width * height;
	    		int closestKingdom = 0;
	    		for(int kidoIndex = 0; kidoIndex < allKingdoms.size(); kidoIndex++) {
	    			if(smallestDist > allCastles.get(c).distance(allKingdoms.get(kidoIndex).getCenter())) {
	    				closestKingdom = kidoIndex;
	    				smallestDist = allCastles.get(c).distance(allKingdoms.get(kidoIndex).getCenter());
	    			}
	    		}
	    		allKingdoms.get(closestKingdom).addCastle(allCastles.get(c));
	    	}
	    	
	    	prevCenterList.clear();
	    	prevCenterList.addAll(newCenterList);
	    	newCenterList.clear();
	    	
	    	for(Kingdom kido : allKingdoms) {
	    		int avgX = 0;
	    		int avgY = 0;
	    		
	    		if(!prevCenterList.isEmpty()) {
		    		avgX = prevCenterList.get(kido.getType()).x;
		    		avgY = prevCenterList.get(kido.getType()).y;
	    		}
	    		for(Castle burg : kido.getCastles()) {
	    			avgX += burg.getLocationOnMap().x;
	    			avgY += burg.getLocationOnMap().y;
	    		}
	    		avgX = avgX / kido.getCastles().size();
	    		avgY = avgY / kido.getCastles().size();
	    		//System.out.println("1st: Type:"+kido.getType()+ " prev: " + prevCenterList + " new: "+newCenterList);
	    		kido.setCenter(avgX, avgY); // Hier liegt das Problem, aber ich weiß nicht warum
	    		//System.out.println("Mid: Type:"+kido.getType()+ " prev: " + prevCenterList + " new: "+newCenterList);
	    		newCenterList.add(kido.getCenter());
	    		//System.out.println("end: Type:"+kido.getType()+ " prev: " + prevCenterList + " new: "+newCenterList);
	    	}
	    	//System.out.println("Ende eines Durchlaufs");
    	}
    	
        return allKingdoms;
    }
}
