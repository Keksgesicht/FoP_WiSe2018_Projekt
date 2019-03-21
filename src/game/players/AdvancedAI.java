package game.players;

import java.awt.Color;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import base.Edge;
import base.Graph;
import base.Node;
import game.AI;
import game.Game;
import game.map.Castle;
import game.map.GameMap;
import gui.AttackThread;

public class AdvancedAI extends AI {

	private List<Castle> prioCastle;
	
	
	
	public AdvancedAI(String name, Color color) {
		super(name, color);
	}

	private void update() {
		// Listen werden neu angepasst 
	}
	
	/**
	 * In der ersten Runde, sprich Auswahl der Burgen, wird eine Liste erstellt die bestimmte Burgen priorisiert
	 * und absteigend in einer neuen Liste sotiert und ausgibt 
	 * @param c Die Liste der Burgen die noch auswählbar sind
	 * @return List<Castle> Eine Liste aus Burgen die nach priotität sotiert wird
	 */
	
	private List<Castle> verteilenListe(List<Castle> c,Game game) {
		int k0 = 0;
		int k1 = 0;
		int k2 = 0;
		int k3 = 0;
		int k4 = 0;
		int k5 = 0;
		int counter1 = 0;
		int counter2 = 0;
		Castle u;
		List<Castle> prio = new ArrayList<Castle>();
			if (GameMap.getMap().getGraph().getAllValues().size() == c.size()) { // Wurden schon Burgen von anderen Spielern ausgewählt ? Nein
				for(int i = 1;i < c.size();i++) {
				u = c.get(i);
				while (prio.size() != c.size()) {
					if (prio.size() == 0) {
						prio.add(u);
					} else {
						for(int i2 = 0;i2 < prio.size();i2++) {							
						   for(int i3 = 0;i3 < GameMap.getMap().getGraph().getEdges(GameMap.getMap().getGraph().getNode(prio.get(i2))).size();i3++) {
							   System.out.println(GameMap.getMap().getGraph().getEdges(GameMap.getMap().getGraph().getNode(prio.get(i2))).size());
							if ( GameMap.getMap().getGraph().getEdges(GameMap.getMap().getGraph().getNode(prio.get(i2))).get(i3).getNodeA() != GameMap.getMap().getGraph().getEdges(GameMap.getMap().getGraph().getNode(prio.get(i2))).get(i3).getNodeB()) {
								counter1++;
							}
							
							if ( GameMap.getMap().getGraph().getEdges(GameMap.getMap().getGraph().getNode(u)).get(i3).getNodeA() != GameMap.getMap().getGraph().getEdges(GameMap.getMap().getGraph().getNode(u)).get(i3).getNodeB()) {
								counter2++;
							}
							if (counter1 >= counter2) {
								prio.add(i2, u);
							}
						  }
							
						}
					}
				}	
			 }	
			} else {  // Wurden schon Burgen von anderen Spielern ausgewählt ? Ja
			  if (this.getCastles(game).size() == 0) {  // Hat man schon selbst Burgen ausgewählt ? Nein
				for (Castle e : c) {
					if (e.getKingdom().equals(0)) {
						k0++;
					} else if (e.getKingdom().equals(1)) {
						k1++;
					} else if (e.getKingdom().equals(2)) {
						k2++;
					} else if (e.getKingdom().equals(3)) {
						k3++;
					} else if (e.getKingdom().equals(4)) {
						k4++;
					} else if (e.getKingdom().equals(5)) {
						k5++;
					}
					
				}
				
				ArrayList<Integer> x = new ArrayList<Integer>();
				x.add(k0);
				x.add(k1);
				x.add(k2);
				x.add(k3);
				x.add(k4);
				x.add(k5);
				x.stream().sorted();
				int kFinal = x.get(x.size()-1);
				
				for(int g = 0;g < c.size();g++) {
					if (c.get(g).getKingdom().getType() != kFinal) {
						c.set(g, null);
					}
				}
				
				for(int i = 0;i < c.size();i++) {
					u = c.get(i);
					while (prio.size() != c.size()) {
						if (prio.size() == 0) {
							prio.add(u);
						} else {
							for(int i2 = 0;i2 < prio.size();i2++) {
							   for(int i3 = 0;i3 < GameMap.getMap().getGraph().getEdges(GameMap.getMap().getGraph().getNode(prio.get(i2))).size();i3++) {
								if ( GameMap.getMap().getGraph().getEdges(GameMap.getMap().getGraph().getNode(prio.get(i2))).get(i3).getNodeA() != GameMap.getMap().getGraph().getEdges(GameMap.getMap().getGraph().getNode(prio.get(i2))).get(i3).getNodeB()) {
									counter1++;
								}
								
								if ( GameMap.getMap().getGraph().getEdges(GameMap.getMap().getGraph().getNode(u)).get(i3).getNodeA() != GameMap.getMap().getGraph().getEdges(GameMap.getMap().getGraph().getNode(u)).get(i3).getNodeB()) {
									counter2++;
								}
								if (counter1 >= counter2) {
									prio.add(i2, u);
								}
							  }
								
							}
						}
					}
				 }	
				
			} else { // Hat man schon selbst Burgen ausgewählt ? Ja
				
				
				
				
			}
		} 
	  
		return prio;
	}
	
	
	protected void actions(Game game) throws InterruptedException {
		
		if(game.getRound() == 1) {
			 
			 List<Castle> availableCastles = game.getMap().getCastles().stream().filter(c -> c.getOwner() == null).collect(Collectors.toList());
	         while(availableCastles.size() > 0 && getRemainingTroops() > 0) {
	        	 
	                sleep(1000);

	                List<Castle> prioList = verteilenListe(availableCastles,game);
	                System.out.println(prioList.size());
	                game.chooseCastle(prioList.get(0), this);
	           }
	         
		} else {
			
		}
		 
	}
	
}




