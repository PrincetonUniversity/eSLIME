package structural.postprocess;

import java.util.HashSet;

import structural.GeneralParameters;

import geometries.Geometry;
import io.deserialize.ConditionViewer;
import io.deserialize.StateReader;
import io.visual.HexMapWriter;

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
		
		int oldFrame = -1;
		double oldGillespie = -1D;
		while (condition != null) {
			frame = condition.getFrame();
			System.out.println("   *** " + oldFrame + " " + oldGillespie + " " + frame + " " + condition.getGillespie());

			System.out.println("   Rendering frame " + frame);
			if (p.isFrame(frame)) {
				mapWriter.renderImage(condition, frame + ".png");
			}
			oldFrame = frame;
			oldGillespie = condition.getGillespie();
			condition = reader.next();
		}

	}

}
