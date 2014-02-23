package structural;

import structural.GeneralParameters;

import java.util.Random;

/**
 * Created by dbborens on 12/10/13.
 */
public class MockGeneralParameters extends GeneralParameters{

    public MockGeneralParameters() {
        super();
    }

    /* Instance path */

    private String instancePath;

    public String getInstancePath() {
        return instancePath;
    }

    public void setInstancePath(String instancePath) {
        this.instancePath = instancePath;
    }

    /* Path */

    private String path;

    public void setPath(String path) {
        this.path = path;
    }

    public String getPath() {
        return path;
    }

    @Override
    public int T() {
        return T;
    }

    public void setT(int t) {
        T = t;
    }

    private int T;

    @Override
    public String getProjectXML() {
        return "";
    }

    public void initializeRandom(long randomSeed) {
        random = new Random(randomSeed);
    }
}
