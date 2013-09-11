package postprocess;

import java.util.HashSet;

import geometries.Geometry;
import io.ConditionViewer;
import io.HexMapWriter;
import io.StateReader;
import control.Parameters;

public class ImageSequence {

	private boolean processAll;
	private HashSet<Integer> frameSet;
	
	private Geometry geom;
	private Parameters p;
	
	private HexMapWriter mapWriter;
	
	public ImageSequence(Geometry geom, Parameters p) {
		System.out.print("Initializing image sequence writer...");

		this.geom = geom;
		this.p = p;
		processAll = true;
		mapWriter = new HexMapWriter(p);
		
		System.out.println("done.");
	}
	
	public ImageSequence(Geometry geom, Parameters p, int[] frames) {
		processAll = false;
		this.geom = geom;
		this.p = p;

		for (int i = 0; i < frames.length; i++)
			frameSet.add(frames[i]);
	}
	
	public void generate() {
		System.out.println("Generating image sequence.");
		StateReader reader = new StateReader(p, geom);
		
		System.out.println("B");
		ConditionViewer condition = reader.next();
		
		System.out.println("C");

		Integer frame = 0;
		
		while (condition != null) {
			System.out.println("   Rendering frame " + frame);
			if (processAll || frameSet.contains(frame)) {
				mapWriter.renderImage(condition, frame + ".png");
			}
			frame++;
			condition = reader.next();
		}
		
		System.out.println("D");

	}

}
