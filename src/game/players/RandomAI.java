package game.players;

import java.awt.Color;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.TreeMap;
import java.util.stream.Collectors;

import base.Edge;
import base.Graph;
import base.Node;
import game.Game;
import game.map.Castle;
import gui.AttackThread;
import gui.components.MapPanel.Action;

/**
 * @author Jan Braun
 * This AI is copy of the BasicAI that also respects the rules of the Game of Castle
 * @see game.players.BasicAI
 */
public class RandomAI extends BasicAI {

    public RandomAI(String name, Color color) {
        super(name, color);
    }
    
    /**
     * @param castles the List where the return value comes from 
     * @return picks a random Castle which also has the lowest amount of troops
     */
    Castle getCastleWithFewestTroops(List<Castle> castles) {
    	// https://stackoverflow.com/questions/40113233/list-stream-get-items-with-lowest-price
    	castles = castles.stream().collect(Collectors.groupingBy(Castle::getTroopCount, TreeMap::new, Collectors.toList())).firstEntry().getValue();
    	Collections.shuffle(castles); // I also would call it RandomAI
    	return castles.get(0);
    }

    @Override
    protected void actions(Game game) throws InterruptedException {
        if(game.getRound() == 1) {
            List<Castle> availableCastles = game.getMap().getCastles().stream().filter(c -> c.getOwner() == null).collect(Collectors.toList());
            while(availableCastles.size() > 0 && getRemainingTroops() > 0) {

                sleep(10);

                Castle randomCastle = availableCastles.remove(this.getRandom().nextInt(availableCastles.size()));
                game.chooseCastle(randomCastle, this);
            }
        } else {

            // 1. Distribute remaining troops
            
            Castle fewestTroops = getCastleWithFewestTroops(this.getCastles(game));
            sleep(5);
            game.addTroops(this, fewestTroops, getRemainingTroops());
            sleep(5);
            boolean attackWon;

            do {
            	Graph<Castle> graph = game.getMap().getGraph();
                List<Castle> castleNearEnemy = new ArrayList<Castle>();
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
            	
                // 2. Move troops from inside to border
            	List<Castle> castleMove = new ArrayList<Castle>(castleNearEnemy);
                castleloop: for (Castle castle : this.getCastles(game)) {
                    if (!castleMove.contains(castle) && castle.getTroopCount() > 1) {
                    	fewestTroops = null;
                    	do { // avoid teleporting
                    		if(castleMove.isEmpty())
                    			continue castleloop;
                    		fewestTroops = getCastleWithFewestTroops(castleMove);
                    		castleMove.remove(fewestTroops);
                    	} while(!game.isPath(castle, fewestTroops, Action.MOVING));
                        game.moveTroops(castle, fewestTroops, castle.getTroopCount() - 1);
                    }
                }

                // 3. attack!
                attackWon = false;
                for (Castle castle : castleNearEnemy) {
                    if(castle.getTroopCount() < 2)
                        continue;

                    Node<Castle> node = graph.getNode(castle);
                    for (Castle otherCastle : graph.getNodes(node).stream().map(n -> n.getValue()).collect(Collectors.toList())) {
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
