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
	
	public ImageSequence(Geometry geom, GeneralParameters p) {
		System.out.print("Initializing image sequence writer...");

		this.geom = geom;
		this.p = p;
		mapWriter = new HexMapWriter(p);
		
		System.out.println("done.");
	}
	
	public void generate() {
		System.out.println("Generating image sequence.");
		StateReader reader = new StateReader(p, geom);
		
		System.out.println("B");
		ConditionViewer condition = reader.next();
		
		System.out.println("C");

		int frame;
		
		while (condition != null) {
			frame = condition.getFrame();
			System.out.println("   Rendering frame " + frame);
			if (p.isFrame(frame)) {
				mapWriter.renderImage(condition, frame + ".png");
			}
			condition = reader.next();
		}
		
		System.out.println("D");

	}

}
