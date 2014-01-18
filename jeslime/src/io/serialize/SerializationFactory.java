package io.serialize;

import layers.LayerManager;
import org.dom4j.Element;
import structural.GeneralParameters;

/**
 * Created by dbborens on 1/17/14.
 */
public abstract class SerializationFactory {
    public static Serializer instantiate(Element e, GeneralParameters p) {

        String writerClass = e.getName();

        // Cell writers
        if (writerClass.equalsIgnoreCase("cell-state-writer")) {
            CellStateWriter bsw = new CellStateWriter(p);
            return bsw;
        } else if (writerClass.equalsIgnoreCase("fixation-time")) {
            FixationTimeWriter ftw = new FixationTimeWriter(p);
            return ftw;
        } else if (writerClass.equalsIgnoreCase("parameter-writer")) {
            ParameterWriter pw = new ParameterWriter(p);
            return pw;
        } else if (writerClass.equalsIgnoreCase("progress-reporter")) {
            ProgressReporter pr = new ProgressReporter(p);
            return pr;
        } else if (writerClass.equalsIgnoreCase("frequency-writer")) {
            FrequencyWriter freq = new FrequencyWriter(p);
            return freq;
        } else if (writerClass.equalsIgnoreCase("interval-writer")) {
            IntervalWriter iw = new IntervalWriter(p);
            return iw;

        // General writers
        } else if (writerClass.equalsIgnoreCase("coordinate-indexer")) {
            CoordinateIndexer ce = new CoordinateIndexer(p);
            return ce;

        } else if (writerClass.equalsIgnoreCase("continuum-state-writer")) {
            ContinuumStateWriter csw = new ContinuumStateWriter(p);
            return csw;
        } else {
            throw new IllegalArgumentException("Unrecognized serialization '" + writerClass + "'");
        }

    }
}
