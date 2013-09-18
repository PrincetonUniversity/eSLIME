package control;

import io.parameters.ParameterLoader;

import java.io.File;

import structural.GeneralParameters;
import structural.postprocess.ImageSequence;
import geometries.Geometry;
import models.*;

/**
 * Use to create arbitrary entry points during testing and development.
 * 
 * @author dbborens
 *
 */
public class ManualRunner implements Runnable {

	public static void main(String[] args) {
		ManualRunner runner = new ManualRunner();
		runner.run();
	}

	@Override
	public void run() {
		
		GeneralParameters p = null;
		try {
			File f = new File("/Users/dbborens/github/jeSLIME/jeslime/projects/sandbox.xml");
			ParameterLoader pp = new ParameterLoader(f);
			p = new GeneralParameters(pp);
		} catch (Exception ex) {
			throw new RuntimeException(ex);
		}
		
		Model model = new ExponentialGrowth(p);
		
		Geometry geom = model.getGeometry();
		
		model.initialize();
		model.go();
		
		ImageSequence imgSequence = new ImageSequence(geom, p);
		
		imgSequence.generate();
		
	}
}
