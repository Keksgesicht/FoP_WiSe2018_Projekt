package gui.components;

import gui.Resources;

import javax.swing.*;

import dice3d.main.World;
import dice3d.models.cuboids.Cuboid;
import dice3d.models.cuboids.Dice;

import java.awt.*;
import java.util.Random;

@SuppressWarnings("serial")
public class DicePanel extends JPanel {

    private int diceValues[];
    private Random random;
    private Resources resources;
    private int numDices;
    private World cubeWorld;

    public DicePanel(Resources resources, World cubeWorld) {
        this.cubeWorld = cubeWorld;
        this.diceValues = new int[3];
        this.resources = resources;
        this.random = new Random(System.currentTimeMillis());
        this.generateRandom(3);
    }

    public int[] generateRandom(int numDices) {
        this.numDices = numDices;
        int[] result = new int[Math.min(numDices, diceValues.length)];

        for (int i = 0; i < Math.min(numDices, diceValues.length); i++) {
            diceValues[i] = random.nextInt(6) + 1;
            result[i] = diceValues[i];
        }

        repaint();
        return result;
    }

    public int[] generateRandom(int numDices, boolean animate) throws InterruptedException {
        if(animate) {
            this.numDices = numDices;
        	this.cubeWorld.roll(numDices);
        	//wait for dice to land
        	while (!this.cubeWorld.finished) {
        		Thread.sleep(1);
        		switch (Math.min(numDices, diceValues.length)){
	        		case 1:
	        			diceValues[0] = ((Dice)this.cubeWorld.cuboids.get(2)).getNumberRolled();
	            		break;
	        		case 2:
	        			diceValues[0] = ((Dice)this.cubeWorld.cuboids.get(1)).getNumberRolled();
	        			diceValues[1] = ((Dice)this.cubeWorld.cuboids.get(3)).getNumberRolled();
	            		break;
	        		case 3:
	        			diceValues[0] = ((Dice)this.cubeWorld.cuboids.get(1)).getNumberRolled();
	        			diceValues[1] = ((Dice)this.cubeWorld.cuboids.get(2)).getNumberRolled();
	        			diceValues[2] = ((Dice)this.cubeWorld.cuboids.get(3)).getNumberRolled();
	            		break;
	        	}
        		repaint();
        	}
        	int[] result = new int[Math.min(numDices, diceValues.length)];
        	switch (Math.min(numDices, diceValues.length)){
        		case 1:
        			diceValues[0] = ((Dice)this.cubeWorld.cuboids.get(2)).getNumberRolled();
            		result[0] = ((Dice)this.cubeWorld.cuboids.get(2)).getNumberRolled();
            		break;
        		case 2:
        			diceValues[0] = ((Dice)this.cubeWorld.cuboids.get(1)).getNumberRolled();
            		result[0] = ((Dice)this.cubeWorld.cuboids.get(1)).getNumberRolled();
        			diceValues[1] = ((Dice)this.cubeWorld.cuboids.get(3)).getNumberRolled();
            		result[1] = ((Dice)this.cubeWorld.cuboids.get(3)).getNumberRolled();
            		break;
        		case 3:
        			diceValues[0] = ((Dice)this.cubeWorld.cuboids.get(1)).getNumberRolled();
            		result[0] = ((Dice)this.cubeWorld.cuboids.get(1)).getNumberRolled();
        			diceValues[1] = ((Dice)this.cubeWorld.cuboids.get(2)).getNumberRolled();
            		result[1] = ((Dice)this.cubeWorld.cuboids.get(2)).getNumberRolled();
        			diceValues[2] = ((Dice)this.cubeWorld.cuboids.get(3)).getNumberRolled();
            		result[2] = ((Dice)this.cubeWorld.cuboids.get(3)).getNumberRolled();
            		break;
        	}
            long duration = 1500;
            long start = System.currentTimeMillis();
            long end = start + duration;
        	while(System.currentTimeMillis() < end) Thread.sleep(10);
    		for(Cuboid c : cubeWorld.cuboids) ((Dice)c).hide();
        	repaint();
        	return result;
        }
        return generateRandom(numDices);
    }

    @Override
    public void paintComponent(Graphics g) {
        super.paintComponent(g);

        int width = getWidth();
        int height = getHeight();
        int margin = 10;
        int diceCount = Math.min(diceValues.length, numDices);

        int diceSize = Math.min(height - 2 * margin, (width - (diceCount + 1) * margin) / diceCount);
        int offsetX = (width - 2 * margin - (diceCount * diceSize)) / 2;

        for(int i = 0; i < diceCount; i++) {
            int x = offsetX + i * (margin + diceSize);
            int y = margin;
            int value = (diceValues[i] - 1) % 6;
            g.drawImage(resources.getDice(value), x, y, diceSize, diceSize, null);
        }
    }
}
