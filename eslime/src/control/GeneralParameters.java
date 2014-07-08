/*
 * Copyright (c) 2014, David Bruce Borenstein and the Trustees of
 * Princeton University.
 *
 * Except where otherwise noted, this work is subject to a Creative Commons
 * Attribution (CC BY 4.0) license.
 *
 * Attribute (BY): You must attribute the work in the manner specified
 * by the author or licensor (but not in any way that suggests that they
 * endorse you or your use of the work).
 *
 * The Licensor offers the Licensed Material as-is and as-available, and
 * makes no representations or warranties of any kind concerning the
 * Licensed Material, whether express, implied, statutory, or other.
 *
 * For the full license, please visit:
 * http://creativecommons.org/licenses/by/4.0/legalcode
 */

package control;

import io.loader.ProjectLoader;
import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.DocumentHelper;
import org.dom4j.Element;
import structural.utilities.XmlUtil;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Random;

/**
 * The parameter object returns general parameters for the simulation.
 *
 * @author dbborens
 */
public class GeneralParameters {


    protected Random random;            // Random number generator
    // Dimensions
    private int maxStep;
    private int instances;
    // Path variables
    private String basePath;        // Path as specified
    private String path;            // May contain a time stamp

    // Output flags
    private String instancePath;    // Includes instance number�(if applies)
    private boolean stateMap;                // Visualize lineages as they grow

    // Instantiated members
    private double epsilon;            // Minimum measurable FP delta
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

    // Minimal constructor for mock testing.
    public GeneralParameters() {
        calcEpsilon();
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
        randomSeed = System.currentTimeMillis();
        random = new Random(randomSeed);
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

    private void loadFlags(Element g) {
        stateMap = XmlUtil.getBoolean(g, "write-state-map");
    }

    private void loadPaths(Element g) {
        basePath = get(g, "path");
        String projectName = XmlUtil.getString(g, "project", "");
        boolean isStamp = XmlUtil.getBoolean(g, "date-stamp");

        if (isStamp) {
            path = basePath + '/' + date() + '/' + projectName + '/' + time() + '/';
        } else {
            path = basePath + projectName;
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
     * <p/>
     * Adapted from the Wikipedia article "Machine epsilon" (retrieved 3/18/2012)
     */
    private void calcEpsilon() {
        double machEps = 1.0d;

        do {
            machEps /= 2d;
        } while (1d + (machEps / 2d) != 1d);

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

    private String date() {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        Date date = new Date();
        return sdf.format(date);
    }

    public String time() {
        SimpleDateFormat sdf = new SimpleDateFormat("HH'h'mm'm'ss's'");
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
     *
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

    public boolean isStateMap() {
        return stateMap;
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

    /**
     * Returns the XML representation of the project, as loaded
     * initially.
     *
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
