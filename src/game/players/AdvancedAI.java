package game.players;

import java.awt.Color;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.stream.Collectors;

import base.Edge;
import base.Graph;
import base.Node;
import game.AI;
import game.Game;
import game.Player;
import game.map.Castle;
import game.map.GameMap;
import gui.AttackThread;

public class AdvancedAI extends AI {
	
	public AdvancedAI(String name, Color color) {
		super(name, color);
	}
 
	private Castle getCastleWithFewestTroops(List<Castle> castles) {
        Castle fewestTroops = castles.get(0);
        for(Castle castle : castles) {
            if(castle.getTroopCount() < fewestTroops.getTroopCount()) {
                fewestTroops = castle;
            }
        }

        return fewestTroops;
    }
	
	private List<Castle> priority(List<Castle> list) {
		 int k0 = 0;
		 int k1 = 0;
		 int k2 = 0;
		 int k3 = 0;
		 int k4 = 0;
		 int k5 = 0;
		 for (Castle e : list) {
				
				if (e.getKingdom().getType() == 0) {
					k0++;
				} else if (e.getKingdom().getType() == 1) {
					k1++;
				} else if (e.getKingdom().getType() == 2) {
					k2++;
				} else if (e.getKingdom().getType() == 3) {
					k3++;
				} else if (e.getKingdom().getType() == 4) {
					k4++;
				} else if (e.getKingdom().getType() == 5) {
					k5++;
				}
				
			}
            
         HashMap<String,Integer> x1 = new HashMap<String,Integer>();
			x1.put("k0",k0);
			x1.put("k1",k1);
			x1.put("k2",k2);
			x1.put("k3",k3);
			x1.put("k4",k4);
			x1.put("k5",k5);
			
			while(x1.values().contains(0)) {
			x1.values().remove(0);
			}
			
			String kFinal1 = Collections.min(x1.entrySet(), (entry1, entry2) -> entry1.getValue() - entry2.getValue()).getKey(); 
			
			int kFinalInt1 = 10;
			if (kFinal1 == "k0") {
				kFinalInt1 = 0;
			} else if (kFinal1 == "k1") {
				kFinalInt1 = 1;
			} else if (kFinal1 == "k2") { 
				kFinalInt1 = 2;
			} else if (kFinal1 == "k3") {
				kFinalInt1 = 3;
			} else if (kFinal1 == "k4") {
				kFinalInt1 = 4;
			} else if (kFinal1 == "k5") {
				kFinalInt1 = 5;
			}   
			
			
			List<Castle> priotemp1 = new ArrayList<>(); 
		    
			for(Castle c2 : list) {
				if (c2.getKingdom().getType() == kFinalInt1) {
					priotemp1.add(c2);
				}
			} 
			
			Collections.reverse(priotemp1);
			
			return priotemp1;
	}
	/**
	 * 
	 * @param list Liste der Edges die eine Burg hat
	 * @return counter Anzahl der Verbindungen die eine Burg zu einer anderen Burg hat,die zu einem andren Kingdom geh�rt
	 */
	private int getEdgeCounter(List<Edge<Castle>> list) {
		
	   int counter = 0;
	   
	   for (Edge<Castle> e : list) {
		   
		   if (e.getNodeA().getValue().getKingdom().getType() != e.getNodeB().getValue().getKingdom().getType()) {
			  counter ++;
			
		   }
	    }
		return counter;	
	}
	/**
	 * 
	 * @param list
	 * @param cast
	 * @return
	 */
	private HashMap<Castle,Integer> getConnection(List<Castle> list,Castle cast,HashMap<Castle,Integer> b) {
	    HashMap<Castle,Integer> b2 = new HashMap<Castle,Integer>();
	    Integer z = 0;
		for (Castle i : list) {
			z = b.get(i);
		   for (Edge<Castle> e : GameMap.getMap().getGraph().getEdges(GameMap.getMap().getGraph().getNode(i))) {
			 if (e.getNodeB().getValue().equals(cast) || e.getNodeA().getValue().equals(cast)) {
				z++;
			   }
		   }
		   
		   b2.put(i, z);
		   z = 0;
		}
		return b2;
	}
	
	/**
	 * In der ersten Runde, sprich Auswahl der Burgen, wird eine Liste erstellt die bestimmte Burgen priorisiert
	 * und absteigend in einer neuen Liste sotiert und ausgibt 
	 * @param c Die Liste der Burgen die noch ausw�hlbar sind
	 * @return List<Castle> Eine Liste aus Burgen die nach priotit�t sotiert wird
	 */
	
	private List<Castle> verteilenListe(List<Castle> c,Game game) {
		//------------------------------------------------------------------//
		int k0 = 0;
		int k1 = 0;
		int k2 = 0;
		int k3 = 0;
		int k4 = 0;
		int k5 = 0;
		boolean b1 = false;
		boolean b = false;
		HashMap<Castle,Integer> prioBurgen = new HashMap<Castle, Integer>();
		HashMap<Castle,Integer> conBurgen = new HashMap<Castle, Integer>();
		HashMap<Castle,Integer> m = new HashMap<Castle, Integer>(); 
		List<Castle> prio = new ArrayList<Castle>();
		List<Castle> trash = new ArrayList<Castle>();
		//-------------------------------------------------------------------//
		
		    for (Player p : game.getPlayers()) {
		    	if (p.getCastles(game).size() != 0 && p != this) {
		    		b = true;
		    		break;
		    	}
		     }
		     
			  if (!b) { // Wurden schon Burgen von anderen ausgew�hlt ? Nein 
				
			  if (this.getCastles(game).size() == 0) { // Hat man schon selbst Burgen ausgew�hlt ? Nein
				  
			   for(Castle r : c) {
				
				   prioBurgen.put(r, getEdgeCounter(GameMap.getMap().getGraph().getEdges(GameMap.getMap().getGraph().getNode(r))));
					
			   }
			  
			   prioBurgen.forEach((k,v) -> { if (v==0) {trash.add(k);}});	  
			  
			   prioBurgen.keySet().removeAll(trash);
			  
			   List<Castle> prio2 = new ArrayList<Castle>(prioBurgen.keySet());
			   Collections.sort(prio2, new Comparator<Castle>() {
				    
				    public int compare(Castle s1, Castle s2) {
				        Integer prio1 = prioBurgen.get(s1);
				        Integer prio2 = prioBurgen.get(s2);
				        return prio1.compareTo(prio2);
				    }
			   });
			
			   prio = prio2;
			 
			   return prio;
			   
			   } else { // Hat man schon selbst Burgen ausgew�hlt ? Ja  
				  
				  for (Castle e : this.getCastles(game)) {
						
						if (e.getKingdom().getType() == 0) {
							k0++;
						} else if (e.getKingdom().getType() == 1) {
							k1++;
						} else if (e.getKingdom().getType() == 2) {
							k2++;
						} else if (e.getKingdom().getType() == 3) {
							k3++;
						} else if (e.getKingdom().getType() == 4) {
							k4++;
						} else if (e.getKingdom().getType() == 5) {
							k5++;
						}
						
					}
					
					HashMap<String,Integer> x = new HashMap<String,Integer>();
					x.put("k0",k0);
					x.put("k1",k1);
					x.put("k2",k2);
					x.put("k3",k3);
					x.put("k4",k4);
					x.put("k5",k5);
					
					
					String kFinal = Collections.max(x.entrySet(), (entry1, entry2) -> entry1.getValue() - entry2.getValue()).getKey();
					
					int kFinalInt = 10;
					if (kFinal == "k0") {
						kFinalInt = 0;
					} else if (kFinal == "k1") {
						kFinalInt = 1;
					} else if (kFinal == "k2") {
						kFinalInt = 2;
					} else if (kFinal == "k3") {
						kFinalInt = 3;
					} else if (kFinal == "k4") {
						kFinalInt = 4;
					} else if (kFinal == "k5") {
						kFinalInt = 5;
					}
					
					
				    List<Castle> priotemp = new ArrayList<>(); 
				    
					for(Castle c2 : c) {
						if (c2.getKingdom().getType() == kFinalInt) {
							priotemp.add(c2);
						}
					}
					
					for (Castle o : priotemp) {
						conBurgen.put(o, 0);
					}
					
					for (Castle o1 : this.getCastles(game)) {
						
					m = getConnection(priotemp,o1,conBurgen);
					
					conBurgen = m;
					
					}
					
				    final HashMap<Castle,Integer> conBurg2 = m;					
				    
					List<Castle> prio3 = new ArrayList<Castle>(conBurg2.keySet());
					   Collections.sort(prio3, new Comparator<Castle>() {
						    
						    public int compare(Castle s1, Castle s2) {
						        Integer prio1 = conBurg2.get(s1);
						        Integer prio2 = conBurg2.get(s2);
						        return prio1.compareTo(prio2);
						    }
					   });
					 
					Collections.reverse(prio3);
					
					priotemp = prio3;
					
					return priotemp;  
			  }
			//--------------------------------------------------------------------------------------//  
			  
			} else {  // Wurden schon Burgen von anderen ausgew�hlt ? Ja
			  
			  if (this.getCastles(game).size() == 0) {  // Hat man schon selbst Burgen ausgew�hlt ? Nein
				   
				   for (Castle e : c) {
						
						if (e.getKingdom().getType() == 0) {
							k0++;
						} else if (e.getKingdom().getType() == 1) {
							k1++;
						} else if (e.getKingdom().getType() == 2) {
							k2++;
						} else if (e.getKingdom().getType() == 3) {
							k3++;
						} else if (e.getKingdom().getType() == 4) {
							k4++;
						} else if (e.getKingdom().getType() == 5) {
							k5++;
						}
						
					}
					
					HashMap<String,Integer> x = new HashMap<String,Integer>();
					x.put("k0",k0);
					x.put("k1",k1);
					x.put("k2",k2);
					x.put("k3",k3);
					x.put("k4",k4);
					x.put("k5",k5);
					
					
					String kFinal = Collections.max(x.entrySet(), (entry1, entry2) -> entry1.getValue() - entry2.getValue()).getKey();   
				   
					int kFinalInt = 10;
					if (kFinal == "k0") {
						kFinalInt = 0;
					} else if (kFinal == "k1") {
						kFinalInt = 1;
					} else if (kFinal == "k2") {
						kFinalInt = 2;
					} else if (kFinal == "k3") {
						kFinalInt = 3;
					} else if (kFinal == "k4") {
						kFinalInt = 4;
					} else if (kFinal == "k5") {
						kFinalInt = 5;
					}
					
					
				    List<Castle> priotemp = new ArrayList<>(); 
				    
					for(Castle c2 : c) {
						if (c2.getKingdom().getType() == kFinalInt) {
							priotemp.add(c2);
						}
					} 
				   
					for(Castle r : priotemp) {
							
						   prioBurgen.put(r, getEdgeCounter(GameMap.getMap().getGraph().getEdges(GameMap.getMap().getGraph().getNode(r))));
							
					   }
					  
					   prioBurgen.forEach((k,v) -> { if (v==0) {trash.add(k);}});	  
					  
					   prioBurgen.keySet().removeAll(trash);
					  
					   List<Castle> prio2 = new ArrayList<Castle>(prioBurgen.keySet());
					   Collections.sort(prio2, new Comparator<Castle>() {
						    
						    public int compare(Castle s1, Castle s2) {
						        Integer prio1 = prioBurgen.get(s1);
						        Integer prio2 = prioBurgen.get(s2);
						        return prio1.compareTo(prio2);
						    }
					   });
				    
				   
				   prio = prio2;
				
				return prio;
			//------------------------------------------------------------------//	
			} else { // Hat man schon selbst Burgen ausgew�hlt ? Ja
				
				for (Castle e : this.getCastles(game)) {
					
					if (e.getKingdom().getType() == 0) {
						k0++;
					} else if (e.getKingdom().getType() == 1) {
						k1++;
					} else if (e.getKingdom().getType() == 2) {
						k2++;
					} else if (e.getKingdom().getType() == 3) {
						k3++;
					} else if (e.getKingdom().getType() == 4) {
						k4++;
					} else if (e.getKingdom().getType() == 5) {
						k5++;
					}
					
				}
				
				HashMap<String,Integer> x = new HashMap<String,Integer>();
				x.put("k0",k0);
				x.put("k1",k1);
				x.put("k2",k2);
				x.put("k3",k3);
				x.put("k4",k4);
				x.put("k5",k5);
				
				
				String kFinal = Collections.max(x.entrySet(), (entry1, entry2) -> entry1.getValue() - entry2.getValue()).getKey();
				
				int kFinalInt = 10;
				if (kFinal == "k0") {
					kFinalInt = 0;
				} else if (kFinal == "k1") {
					kFinalInt = 1;
				} else if (kFinal == "k2") { 
					kFinalInt = 2;
				} else if (kFinal == "k3") {
					kFinalInt = 3;
				} else if (kFinal == "k4") {
					kFinalInt = 4;
				} else if (kFinal == "k5") {
					kFinalInt = 5;
				}
				
				for (Castle f : c) {
					if (f.getKingdom().getType() == kFinalInt) {
						b1 = true;
					}
				}
				
			    if (b1) { // Burgen aus seinem eigenen K�nigreichg sind �brig 
			    List<Castle> priotemp = new ArrayList<>(); 
			    
				for(Castle c2 : c) {
					if (c2.getKingdom().getType() == kFinalInt) {
						priotemp.add(c2);
					}
				}
				
				for (Castle o : priotemp) {
					conBurgen.put(o, 0);
				}
				
				for (Castle o1 : this.getCastles(game)) {
					
				m = getConnection(priotemp,o1,conBurgen);
				
				conBurgen = m;
				
				}
				
			    final HashMap<Castle,Integer> conBurg2 = m;					
			    
				List<Castle> prio3 = new ArrayList<Castle>(conBurg2.keySet());
				   Collections.sort(prio3, new Comparator<Castle>() {
					    
					    public int compare(Castle s1, Castle s2) {
					        Integer prio1 = conBurg2.get(s1);
					        Integer prio2 = conBurg2.get(s2);
					        return prio1.compareTo(prio2);
					    }
				   });
				 
				Collections.reverse(prio3);
				
				priotemp = prio3;
				
				return priotemp;
				
			} else {    // Burgen aus seinem eigenen K�nigreichg sind nicht mehr �brig 
				
                    for (Castle e : c) {
					
					if (e.getKingdom().getType() == 0) {
						k0++;
					} else if (e.getKingdom().getType() == 1) {
						k1++;
					} else if (e.getKingdom().getType() == 2) {
						k2++;
					} else if (e.getKingdom().getType() == 3) {
						k3++;
					} else if (e.getKingdom().getType() == 4) {
						k4++;
					} else if (e.getKingdom().getType() == 5) {
						k5++;
					}
					
				}
                   
                HashMap<String,Integer> x1 = new HashMap<String,Integer>();
   				x1.put("k0",k0);
   				x1.put("k1",k1);
   				x1.put("k2",k2);
   				x1.put("k3",k3);
   				x1.put("k4",k4);
   				x1.put("k5",k5);
   				
   				while(x1.values().contains(0)) {
   				x1.values().remove(0);
   				}
   				
   				String kFinal1 = Collections.min(x1.entrySet(), (entry1, entry2) -> entry1.getValue() - entry2.getValue()).getKey(); 
   				
   				int kFinalInt1 = 10;
   				if (kFinal1 == "k0") {
   					kFinalInt1 = 0;
   				} else if (kFinal1 == "k1") {
   					kFinalInt1 = 1;
   				} else if (kFinal1 == "k2") { 
   					kFinalInt1 = 2;
   				} else if (kFinal1 == "k3") {
   					kFinalInt1 = 3;
   				} else if (kFinal1 == "k4") {
   					kFinalInt1 = 4;
   				} else if (kFinal1 == "k5") {
   					kFinalInt1 = 5;
   				}   
				
   				
   				List<Castle> priotemp1 = new ArrayList<>(); 
			    
				for(Castle c2 : c) {
					if (c2.getKingdom().getType() == kFinalInt1) {
						priotemp1.add(c2);
					}
				} 
				
				Collections.reverse(priotemp1);
				
				if (priotemp1.size() < 3) {
					priotemp1.add(priority(game.getMap().getCastles().stream().filter(h -> h.getOwner() == null).collect(Collectors.toList())).get(0));
				}
				
				return priotemp1;
				
			}
		  }
		} 
	  
		
	}
	
	
	protected void actions(Game game) throws InterruptedException {
     //----------------------------------------------------------------------------------------------// Burgen verteilen
		if(game.getRound() == 1) {
			 
			 List<Castle> availableCastles = game.getMap().getCastles().stream().filter(c -> c.getOwner() == null).collect(Collectors.toList());
	         while(availableCastles.size() > 0 && getRemainingTroops() > 0) {
	        	 
	        	    List<Castle> availableCastles2 = game.getMap().getCastles().stream().filter(c -> c.getOwner() == null).collect(Collectors.toList());
	                sleep(1000);
                   
	                List<Castle> prioList = verteilenListe(availableCastles2,game);
	                game.chooseCastle(prioList.get(0), this);
	                
	           }
	 //-----------------------------------------------------------------------------------------------//    
		} else {
			
			// 1. Distribute remaining troops
            Graph<Castle> graph = game.getMap().getGraph();
            List<Castle> castleNearEnemy = new ArrayList<>();
            for(Castle castle : this.getCastles(game)) {
                Node<Castle> node = graph.getNode(castle);
                for(Edge<Castle> edge : graph.getEdges(node)) {
                    Castle otherCastle = edge.getOtherNode(node).getValue();
                    if(otherCastle.getOwner() != this) {
                        castleNearEnemy.add(castle);
                        break;
                    }
                }
            }

            while(this.getRemainingTroops() > 0) {
                Castle fewestTroops = getCastleWithFewestTroops(castleNearEnemy);
                sleep(500);
                game.addTroops(this, fewestTroops, 1);
            }

            boolean attackWon;

            do {
                // 2. Move troops from inside to border
                for (Castle castle : this.getCastles(game)) {
                    if (!castleNearEnemy.contains(castle) && castle.getTroopCount() > 1) {
                        Castle fewestTroops = getCastleWithFewestTroops(castleNearEnemy);
                        game.moveTroops(castle, fewestTroops, castle.getTroopCount() - 1);
                    }
                }

                // 3. attack!
                attackWon = false;
                for (Castle castle : castleNearEnemy) {
                    if(castle.getTroopCount() < 2)
                        continue;

                    Node<Castle> node = graph.getNode(castle);
                    for (Edge<Castle> edge : graph.getEdges(node)) {
                        Castle otherCastle = edge.getOtherNode(node).getValue();
                        if (otherCastle.getOwner() != this && castle.getTroopCount() >= otherCastle.getTroopCount()) {
                            AttackThread attackThread = game.startAttack(castle, otherCastle, castle.getTroopCount() - 1);
                            if(fastForward)
                                attackThread.fastForward();

                            attackThread.join();
                            attackWon = attackThread.getWinner() == this;
                            break;
                        }
                    }

                    if(attackWon)
                        break;
                }
            } while(attackWon);
            
		}
		 
	}
	
}




