package structural;

import jeslime.io.project.ProjectLoader;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashSet;
import java.util.Set;
import java.util.Random;

import org.dom4j.Attribute;
import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.DocumentHelper;
import org.dom4j.Element;

/**
 * The parameter object returns general parameters for the simulation.
 * 
 * @author dbborens
 *
 */
public class GeneralParameters {



	// Dimensions
	private int maxStep;
	private int instances;
	
	// Path variables
	private String basePath;		// Path as specified
	private String path;			// May contain a time stamp
	private String instancePath;	// Includes instance number�(if applies)
	
	// Output flags
	
	private boolean lineageMap;				// Visualize lineages as they grow
	// Output frames (blank means all)
	private Set<Integer> frames;
	
	// Instantiated members
	private double epsilon;			// Minimum measurable FP delta

	private Random random;			// Random number generator
	private long randomSeed;
	// State members
	private int instance;
	
	private String projectXML;
	
	private String version;
	
	public GeneralParameters(ProjectLoader loader) {
		calcEpsilon();
		load(loader);
		instance = 0;
		updateInstancePath();
		
		
	}
	
	private void updateInstancePath() {		
		if (instances == 1) {
			instancePath = path;
		} else {
			instancePath = path + '/' + instance + '/';
		}
			
	}

	/**
	 * Signals to the parameters object that it should
	 * generate a new random number seed and advance the
	 * instance counter.
	 */
	public void advance() {
		instance++;
		updateInstancePath();
	}
	
	// Pull in a single-datum element
	private String get(Element g, String key) {
		Element vElem = g.element(key);
		if (vElem == null) {
			throw new IllegalArgumentException("General parameter " + 
					key + " not defined.");
		}
		
		Object value = vElem.getData();
		
		return value.toString();
			
	}
	
	private void load(ProjectLoader loader) {
		Element g = loader.getElement("general");
		
		version = loader.getVersion();
		
		// Load dimensions
		loadDimensions(g);

		// Load base path and (if applicable) time stamped path
		loadPaths(g);
		
		// Load output flags
		loadFlags(g);
		
		// Load output frames (or none)
		loadOutputFrames(g);
		
		// Initialize random-number generator
		loadRandom(g);
		
		System.out.println("Load: project XML is null?" + (loader.toString() == null));
		projectXML = loader.toString();
	}

	private void loadRandom(Element g) {
		String rseed = get(g, "random-seed");
			if (rseed.equals("*")) {
				randomSeed = System.currentTimeMillis();
				random = new Random(randomSeed);
			} else {
				if (instances != 1) {
					throw new IllegalArgumentException("You may only specify a random number seed if you are running a single replicate.");
				}
				randomSeed = Long.valueOf(rseed);
				random = new Random(randomSeed);
				
			}	
	}

	private void loadOutputFrames(Element g) {
		Element fElem = g.element("output-frames");

		frames = new HashSet<Integer>();
		
		// Did we specify an automatic frame pattern?
		if (fElem.element("auto") != null) {
			Element auto = fElem.element("auto");
			
			// All -- render every frame. IF YOU USE A HUGE MAX STEP, THIS
			// *WILL* CRASH THE PROGRAM.
			if (auto.attribute("mode").getData().toString().equalsIgnoreCase("all")) {
				for (int i = 0; i <= maxStep; i++) {
					frames.add(i);
				}
				System.out.println();
			// Linear mode -- step n frames from the first one
			} else if (auto.attribute("mode").getData().toString().equalsIgnoreCase("linear")) {
				Attribute stepAttr = auto.attribute("step");
				if (stepAttr == null) {
					throw new IllegalArgumentException("Linear mode requires a 'step' argument.");
				}
				
				int step = Integer.valueOf(stepAttr.getData().toString());
				
				for (int i = 0; i <= maxStep; i += step) {
					frames.add(i);
				}
				
			// Log mode -- 1 frame per OOM
			} else if (auto.attribute("mode").getData().toString().equalsIgnoreCase("log")) {
				frames.add(0);
				for (int i = 1; i <= maxStep; i *= 10) {
					frames.add(i);
				}
				
			// Decilog mode -- 10 frames per OOM
			} else if (auto.attribute("mode").getData().toString().equalsIgnoreCase("decilog")) {
				frames.add(0);
				int i = 1;
				int magnitude = 1;
				while (i <= maxStep) {
					for (int j = 1; i <= maxStep && j < 10; j++) {
						frames.add(i);
						i += magnitude;
					}
					magnitude *= 10;
				}
				
			// Unrecognized auto mode
			} else {
				throw new IllegalArgumentException("Automatic frameset mode '" + 
						auto.attribute("mode").getData().toString()
						+ "' not recognized.");
			}
		}

		for (Object o : fElem.elements("frame")) {
			Element e = (Element) o;
			
			Integer frame = Integer.valueOf(e.getData().toString());
			frames.add(frame);
		}
		
		if (frames.size() == 0) {
			System.err.println("WARNING! No frames specified.");
		}
	}
	
	private void loadFlags(Element g) {
		lineageMap = Boolean.valueOf(get(g, "write-lineage-map"));
	}

	private void loadPaths(Element g) {
		basePath = get(g, "path");
		boolean isStamp = Boolean.valueOf(get(g, "date-stamp"));
		
		if (isStamp) {
			path = basePath + '/' + date() + '/';
		} else {
			path = basePath;
		}
	}

	private void loadDimensions(Element g) {
		//width = Integer.valueOf(get(g, "width"));
		//height = Integer.valueOf(get(g, "height"));
		maxStep = Integer.valueOf(get(g, "max-step"));
		instances = Integer.valueOf(get(g, "instances"));
	}

	
	/**
	 * Find the machine epsilon for this computer (i.e., the value at which
	 * double-precision floating points can no longer be distinguished.)
	 * 
	 * Adapted from the Wikipedia article "Machine epsilon" (retrieved 3/18/2012)
	 * 
	 */
	private void calcEpsilon() {
        double machEps = 1.0d;
        
        do {
           machEps /= 2d;
        } while (1d + (machEps/2d) != 1d);
        
        epsilon = machEps;
	}
	
	/**
	 * Determines whether two doubles are equal to within machine epsilon.
	 * 
	 * @param p
	 * @param q
	 */
	public boolean epsilonEquals(double p, double q) {
		if (Math.abs(p - q) < epsilon)
			return true;
		
		return false;
	}
	
	public double epsilon() {
		return epsilon;
	}
	
	public String getLatticeShape() {
		return "HexRing";
	}
	
	private String date() {
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd/HH'h'mm'm'ss's'");
		Date date = new Date();
		return sdf.format(date);
	}
	
	public String getVersion() {
		return version;
	}

	/**
	 * Returns system width. Due to frequent calls, this
	 * getter's name has been shortened.
	 * 
	 * @return
	 */
	/*public int W() {
		return width;
	}*/

	/**
	 * Returns system height. Due to frequent calls, this getter's
	 * name has been shortened.
	 * @return
	 */
	/*public int H() {
		return height;
	}*/

	/**
	 * Returns max time step. Due to frequent calls, this getter's
	 * name has been shortened.
	 * @return
	 */
	public int T() {
		return maxStep;
	}

	public int getNumInstances() {
		return instances;
	}

	public String getBasePath() {
		return basePath;
	}

	public String getPath() {
		return path;
	}

	public String getInstancePath() {
		return instancePath;
	}

	public boolean isLineageMap() {
		return lineageMap;
	}

	public double getEpsilon() {
		return epsilon;
	}

	public Random getRandom() {
		return random;
	}

	public long getRandomSeed() {
		return randomSeed;
	}

	public int getInstance() {
		return instance;
	}
	
	public HashSet<Integer> getFrames() {
		return new HashSet<Integer>(frames);
	}
	
	/**
	 * Returns true if the specified frame is contained
	 * in the list of frames for visualization.
	 * @param frame
	 * @return
	 */
	public boolean isFrame(Integer frame) {
		return frames.contains(frame);
	}
	
	/**
	 * Returns the XML representation of the project, as loaded
	 * initially. 
	 * @return
	 */
	public String getProjectXML() {
		return projectXML;
	}
	
	/**
	 * Returns an XML project encoding the current instance's
	 * path and random number generator. The resulting XML file
	 * should deterministically reproduce the specified simulation
	 * instance.
	 * 
	 * @return
	 */
	public String getInstanceXML() {
		// Load original XML
		try {
			Document replica = DocumentHelper.parseText(projectXML);
			Element rr = replica.getRootElement();
			Element re = rr.element("general");
			// Change path to instance path
			re.element("path").setText(instancePath);			
			
			// Disable date stamp
			re.element("date-stamp").setText("false");
			
			// Change random number key to instance key
			String seed = Long.valueOf(randomSeed).toString();
			re.element("random-seed").setText(seed);
			
			// Change replicates to 1
			re.element("instances").setText("1");
			
			return replica.asXML();
		} catch (DocumentException e) {
			throw new RuntimeException(e);
		}
	}
}
