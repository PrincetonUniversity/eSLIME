package structural.postprocess;

import java.util.HashSet;

import structural.GeneralParameters;
import geometry.Geometry;
import jeslime.io.deserialize.ConditionViewer;
import jeslime.io.deserialize.StateReader;
import jeslime.io.visual.HexMapWriter;

public class ImageSequence {
	
	private Geometry geom;
	private GeneralParameters p;
	
	private HexMapWriter mapWriter;
	
	// The simulation being visualized may be a single instance of a multi-
	// replicate project. The ImageSequence is currently agnostic to that fact,
	// so we just pass in a path.
	private String path;
	
	public ImageSequence(String path, Geometry geom, GeneralParameters p) {
		System.out.print("Initializing image sequence writer...");

		this.geom = geom;
		this.p = p;
		this.path = path;
		
		mapWriter = new HexMapWriter(p, path, geom);
		
		System.out.println("done.");
	}
	
	public void generate() {
		System.out.println("Generating image sequence.");
		StateReader reader = new StateReader(path, p, geom);
		
		ConditionViewer condition = reader.next();

		int frame;
		
		while (condition != null) {
			frame = condition.getFrame();
			
			if (p.isFrame(frame)) {
				mapWriter.renderImage(condition, frame + ".png");
			}
			
			condition = reader.next();
		}

	}

}
