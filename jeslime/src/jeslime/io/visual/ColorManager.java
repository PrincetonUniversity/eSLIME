package jeslime.io.visual;
import java.awt.Color;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;

import structural.GeneralParameters;


public class ColorManager {

	private Map<Integer, Color> colorMap = new HashMap<Integer, Color>();
	
	private Random random;
	public ColorManager(GeneralParameters p) {
		random = p.getRandom();
	}

	public Color basicColor(int state) {
		
		if (!colorMap.containsKey(state)) {
			makeColor(state);
		}
		
		return colorMap.get(state);

	}

	private void makeColor(int state) {
		float r = random.nextFloat();
		float g = random.nextFloat();
		float b = random.nextFloat();
		
		Color c = new Color(r, g, b);
		
		colorMap.put(state, c);
	}

	public Color highlightColor(int state) {
		Color basic = basicColor(state);
		
		int r0 = basic.getRed();
		int g0 = basic.getGreen();
		int b0 = basic.getBlue();
		
		int r1 = ((255 - r0) * 3 / 4) + r0;
		int g1 = ((255 - g0) * 3 / 4) + g0;
		int b1 = ((255 - b0) * 3 / 4) + b0;
		
		Color highlight = new Color(r1, g1, b1);
		
		return highlight;
	}
}
