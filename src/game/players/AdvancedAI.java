package game.players;

import java.awt.Color;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
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
import gui.components.MapPanel.Action;

public class AdvancedAI extends AI {
	
	ArrayList<Castle> prioli;
	ArrayList<Castle> prioli2;
	Castle dick;
	Castle doof;
	
	public AdvancedAI(String name, Color color) {
		super(name, color);
	}
	
	private HashMap<Castle, Integer> getCastlesWithFewestTroops(List<Castle> castles) {
		HashMap<Castle, Integer> fewestTroops1 = new HashMap<Castle,Integer>();
        for(Castle castle : castles) {
            fewestTroops1.put(castle, castle.getTroopCount());
        }
        
        HashMap<Castle, Integer> fewestTroops = fewestTroops1.entrySet()
                .stream()
                .sorted(Collections.reverseOrder(Map.Entry.comparingByValue()))
                .collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue, (e1, e2) -> e2,LinkedHashMap::new));  
		   
        return fewestTroops;
    }
	
	private HashMap<Castle, Integer> getCastlesWithMostTroops(List<Castle> castles) {
		HashMap<Castle, Integer> mostTroops = new HashMap<Castle,Integer>();
		
        mostTroops = getCastlesWithFewestTroops(castles).entrySet()
                .stream()
                .sorted(Collections.reverseOrder(Map.Entry.comparingByValue()))
                .collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue, (e1, e2) -> e2,LinkedHashMap::new));;
                
        return mostTroops;
    }
	
	private HashMap<Castle, Integer> getKingdomeType(List<Castle> castles) {
		HashMap<Castle,Integer> kingdomeType = new HashMap<Castle,Integer>();
		for (Castle c : castles) {
			kingdomeType.put(c, c.getKingdom().getType());
		}
		return kingdomeType;
	}
	
	private ArrayList<Player> getStrongPlayer(List<Castle> castleNearEnemy,Game game) {
		ArrayList<Player> strongPlayer = new ArrayList<Player>();
		for (Player p : game.getPlayers()) {
			if (p != this) {
				strongPlayer.add(p);
			}
		}
		
		Collections.sort(strongPlayer, new Comparator<Player>() {
		    @Override
		    public int compare(Player o1, Player o2) {
		    	
		        return new Integer (o1.getCastles(game).size()).compareTo(new Integer (o2.getCastles(game).size())) ;
		    }
		});
		
		return strongPlayer;
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
	 * @return counter Anzahl der Verbindungen die eine Burg zu einer anderen Burg hat,die zu einem andren Kingdom gehï¿½rt
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
	 * @param c Die Liste der Burgen die noch auswï¿½hlbar sind
	 * @return List<Castle> Eine Liste aus Burgen die nach priotitï¿½t sotiert wird
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
		     
			  if (!b) { // Wurden schon Burgen von anderen ausgewï¿½hlt ? Nein  
				
			  if (this.getCastles(game).size() == 0) { // Hat man schon selbst Burgen ausgewï¿½hlt ? Nein
				  
			   for(Castle r : c) {
				
				   prioBurgen.put(r, getEdgeCounter(GameMap.getMap().getGraph().getEdges(GameMap.getMap().getGraph().getNode(r))));
					
			   }
			  
			   prioBurgen.forEach((k,v) -> { if (v==0) {trash.add(k);}});	  
			   
			   prioBurgen.keySet().removeAll(trash);
			  
			   int k01 = 0;
				int k11 = 0;
				int k21 = 0;
				int k31 = 0;
				int k41 = 0;
				int k51 = 0;
				
			   for (Castle e : game.getMap().getCastles()) {
					
					if (e.getKingdom().getType() == 0) {
						k01++;
					} else if (e.getKingdom().getType() == 1) {
						k11++;
					} else if (e.getKingdom().getType() == 2) {
						k21++;
					} else if (e.getKingdom().getType() == 3) {
						k31++;
					} else if (e.getKingdom().getType() == 4) {
						k41++;
					} else if (e.getKingdom().getType() == 5) {
						k51++;
					}
					
				}
				
				HashMap<String,Integer> x = new HashMap<String,Integer>();
				x.put("k0",k01);
				x.put("k1",k11);
				x.put("k2",k21);
				x.put("k3",k31);
				x.put("k4",k41);
				x.put("k5",k51);
				
				
				String kFinal = Collections.min(x.entrySet(), (entry1, entry2) -> entry1.getValue() - entry2.getValue()).getKey();
				
				int kFinalInt2 = 10;
				if (kFinal == "k0") {
					kFinalInt2 = 0;
				} else if (kFinal == "k1") {
					kFinalInt2 = 1;
				} else if (kFinal == "k2") {
					kFinalInt2 = 2;
				} else if (kFinal == "k3") {
					kFinalInt2 = 3;
				} else if (kFinal == "k4") {
					kFinalInt2 = 4;
				} else if (kFinal == "k5") {
					kFinalInt2 = 5;
				}
						   		   
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
			   
			   } else { // Hat man schon selbst Burgen ausgewï¿½hlt ? Ja  
				  
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
			  
			} else {  // Wurden schon Burgen von anderen ausgewï¿½hlt ? Ja
			  
			  if (this.getCastles(game).size() == 0) {  // Hat man schon selbst Burgen ausgewï¿½hlt ? Nein
				   
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
			} else { // Hat man schon selbst Burgen ausgewï¿½hlt ? Ja
				
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
				
			    if (b1) { // Burgen aus seinem eigenen Kï¿½nigreichg sind ï¿½brig 
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
				
			} else {    // Burgen aus seinem eigenen Kï¿½nigreichg sind nicht mehr ï¿½brig 
				
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
		System.out.println(game.getRound());
		if(game.getRound() == 1) {
			 
			 List<Castle> availableCastles = game.getMap().getCastles().stream().filter(c -> c.getOwner() == null).collect(Collectors.toList());
	         while(availableCastles.size() > 0 && getRemainingTroops() > 0) {
	        	 
	        	    List<Castle> availableCastles2 = game.getMap().getCastles().stream().filter(c -> c.getOwner() == null).collect(Collectors.toList());
	                sleep(1000);
                   
	                List<Castle> prioList = verteilenListe(availableCastles2,game); 
	       
	                game.chooseCastle(prioList.get(0), this);
	                
	      }
	 //-----------------------------------------------------------------------------------------------// Verteilen
		} else {
			
			// 1. Distribute remaining troops
            Graph<Castle> graph = game.getMap().getGraph();
            List<Castle> castleNearEnemy = new ArrayList<>();
            List<Castle> enemyNearCastle = new ArrayList<>();
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
            
            for(Castle castle : this.getCastles(game)) {
                Node<Castle> node = graph.getNode(castle);
                for(Edge<Castle> edge : graph.getEdges(node)) {
                    Castle otherCastle = edge.getOtherNode(node).getValue();
                    if(otherCastle.getOwner() != this) {
                    	enemyNearCastle.add(otherCastle);
                    }
                }
            }
        	
     	   
     	//--------------------------------------//Adding Troops
            while(this.getRemainingTroops() > 0) {
            	
            	//--------------------------------------// castleNearEnemy in verschiedene Prioritätslisten aufteilen
                //-----------------------------------//Prioliste 0 : remainingTroops + Truppen die man schon im Spiel hat
              	int h = 0;
              	for (Castle z0 :this.getCastles(game)) {
              	 	h += z0.getTroopCount();
              	}
              	h += this.getRemainingTroops()-this.getCastles(game).size();
            	   //-----------------------------------//Prioliste 1: mostTroops
            	   HashMap<Castle,Integer> mostTroops = getCastlesWithMostTroops(enemyNearCastle);
            	   
            	   //-----------------------------------//Prioliste 2: fewestTroops
            	   HashMap<Castle,Integer> fewestTroops = getCastlesWithFewestTroops(enemyNearCastle);
            	   
            	   //-----------------------------------//Prioliste 3: kingdomeTypess
            	   HashMap<Castle,Integer> sameKingdome = getKingdomeType(enemyNearCastle);
            	   
            	   //-----------------------------------//Prioliste 4: strongPlayer
            	   ArrayList<Player> strongPlayer = getStrongPlayer(enemyNearCastle,game); 
            	   
            	   //-----------------------------------//Auswertung der Listen:
            	   HashMap<Castle,Integer> Punkte = new HashMap<Castle,Integer>();
            	   
            	   for (Castle z : enemyNearCastle) {
            		   Punkte.put(z, 0) ;
            	   }
            	   
            	   int p = 0;
            	   for(Castle z1 : fewestTroops.keySet()) {
            		   if (z1.getTroopCount() == 1) {
            			   p = 30;
            		   } else if (z1.getTroopCount() > 1 && z1.getTroopCount() <= 10) {
            			   p = 10;
            		   } else if (z1.getTroopCount() > 10 && z1.getTroopCount() <= 20) {
            			   p = 5;
            		   }  else {
            			   p = 0;
            		   }
            		   Punkte.put(z1, Punkte.get(z1) + p); 
            	   }
            	     
            	   int k0 = 0;
      		   int k1 = 0;
      		   int k2 = 0;
      		   int k3 = 0;
      		   int k4 = 0;
      		   int k5 = 0;
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
      			
      			String kFinal = Collections.max(x1.entrySet(), (entry1, entry2) -> entry1.getValue() - entry2.getValue()).getKey();
            	     
      			int kFinalInt = 6;
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
  				
          	   for(Castle z1 : sameKingdome.keySet()) {
          		   if (kFinalInt == z1.getKingdom().getType()) {
          			   Punkte.put(z1, Punkte.get(z1) + 5); 
          		   }
          	   }
            	   
          	   int p2 = 0;
          	   for(Player player : strongPlayer) {
          		   for (Castle c1: Punkte.keySet()) {
          			   
          		        if (strongPlayer.size() == 2) {
          			         if (c1.getOwner() == strongPlayer.get(0)) {
          			        	 Punkte.put(c1, Punkte.get(c1));
          			          } else {
          			        	  Punkte.put(c1, Punkte.get(c1) + 5);
          			         }
          		         }
          		        
          		        
          		        if (strongPlayer.size() == 3) {
          		        	 if (c1.getOwner() == strongPlayer.get(0)) {
          			        	 Punkte.put(c1, Punkte.get(c1));
          			          } else if (c1.getOwner() == strongPlayer.get(1)) {
          			        	  Punkte.put(c1, Punkte.get(c1) + 5);
          			          } else {
          			        	  Punkte.put(c1, Punkte.get(c1) + 10);
          		              }
         		             }
          		        
          		        if (strongPlayer.size() == 4) {
         		        	 if (c1.getOwner() == strongPlayer.get(0)) {
         			        	 Punkte.put(c1, Punkte.get(c1) );
         			          } else if (c1.getOwner() == strongPlayer.get(1)) {
         			        	  Punkte.put(c1, Punkte.get(c1) + 5);
         			          } else if (c1.getOwner() == strongPlayer.get(2)) {
         			        	  Punkte.put(c1, Punkte.get(c1) + 10);
        		              } else {
        		            	 Punkte.put(c1, Punkte.get(c1) + 15);
        		              }
          		   }
          	   }
          	  }  
          		   
          	   prioli = new ArrayList<Castle>(Punkte.keySet());
          	          	   
  			   Collections.sort(prioli, new Comparator<Castle>() {
  				    
  				    public int compare(Castle s1, Castle s2) {
  				        Integer prio1 = Punkte.get(s1);
  				        Integer prio2 = Punkte.get(s2);
  				        return prio1.compareTo(prio2);
  				    }
  			   });	
  			   
  			   prioli2 = new ArrayList<Castle>();
  			   
  			   for(Castle t : this.getCastles(game)) {	   
  	                Node<Castle> node2 = graph.getNode(t);
  	                for(Edge<Castle> edge2 : graph.getEdges(node2)) {
  	                	Castle otherCastle2 = edge2.getOtherNode(node2).getValue();
  	                	for (Castle s : prioli) {
  	                      if(otherCastle2 == s) {
  	                    	prioli2.add(t);
  	                      }
  	                	}
  	                }
  			   }
			   
               sleep(500);
               game.addTroops(this, prioli2.get(0), 1);
            }
     //------------------------------------------------------------------------------------------------// Bewegen
            boolean attackWon;
            do {
                // 2. Move troops from inside to border
                List<Castle> castleMove = new ArrayList<Castle>(prioli2);
                castleloop: for (Castle castle : this.getCastles(game)) {
                    if (!castleMove.contains(castle) && castle.getTroopCount() > 1) {
                    	Castle fewestTroops = null;
                    	do { 
                    		if(castleMove.isEmpty())
                    			continue castleloop;
                    		fewestTroops = castleMove.get(0);
                    		castleMove.remove(fewestTroops);
                    	} while(!game.isPath(castle, fewestTroops, Action.MOVING));
                        game.moveTroops(castle, fewestTroops, castle.getTroopCount() - 1);
                    }
                }
     //------------------------------------------------------------------------------------------------// Angreifen
                // 3. attack!
                attackWon = false;
                
                List<Castle> xy = new ArrayList<Castle>(prioli2);
                
                Collections.reverse(xy);
                
                for (Castle castle : xy) {
                    if(castle.getTroopCount() < 2)
                        continue;
                    Node<Castle> node = graph.getNode(castle);
                    for (Castle otherCastle : graph.getNodes(node).stream().map(n -> n.getValue()).collect(Collectors.toList())) {
                        if (otherCastle.getOwner() != this && castle.getTroopCount() >= otherCastle.getTroopCount()) {
                            AttackThread attackThread = game.startAttack(castle, otherCastle, castle.getTroopCount() - 1);
                            dick = castle;
                            doof = otherCastle;
                            if(fastForward)
                                attackThread.fastForward();

                            attackThread.join();
                            attackWon = attackThread.getWinner() == this;
                            break;
                        }
                    }

                    if(attackWon) {
                    	game.moveTroops(dick, doof, dick.getTroopCount() - 1);
                    	break;
                    }
                    game.moveTroops(dick, doof, dick.getTroopCount() - 1);  	
                }
            } while(attackWon);
            
		}
		 
	}
}




