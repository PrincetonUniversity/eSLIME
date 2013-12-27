package structural;

import structural.GeneralParameters;

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
}
