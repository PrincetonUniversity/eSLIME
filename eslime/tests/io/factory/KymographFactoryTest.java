/*
 *
 *  Copyright (c) 2014, David Bruce Borenstein and the Trustees of
 *  Princeton University.
 *
 *  Except where otherwise noted, this work is subject to a Creative Commons
 *  Attribution (CC BY 4.0) license.
 *
 *  Attribute (BY): You must attribute the work in the manner specified
 *  by the author or licensor (but not in any way that suggests that they
 *  endorse you or your use of the work).
 *
 *  The Licensor offers the Licensed Material as-is and as-available, and
 *  makes no representations or warranties of any kind concerning the
 *  Licensed Material, whether express, implied, statutory, or other.
 *
 *  For the full license, please visit:
 *  http:creativecommons.org/licenses/by/4.0/legalcode
 * /
 */

/*
 *
 *  Copyright (c) 2014, David Bruce Borenstein and the Trustees of
 *  Princeton University.
 *
 *  Except where otherwise noted, this work is subject to a Creative Commons
 *  Attribution (CC BY 4.0) license.
 *
 *  Attribute (BY): You must attribute the work in the manner specified
 *  by the author or licensor (but not in any way that suggests that they
 *  endorse you or your use of the work).
 *
 *  The Licensor offers the Licensed Material as-is and as-available, and
 *  makes no representations or warranties of any kind concerning the
 *  Licensed Material, whether express, implied, statutory, or other.
 *
 *  For the full license, please visit:
 *  http:creativecommons.org/licenses/by/4.0/legalcode
 * /
 */

package io.factory;

import control.GeneralParameters;
import factory.io.visual.kymograph.KymographFactory;
import geometry.Geometry;
import geometry.boundaries.Boundary;
import geometry.boundaries.Periodic;
import geometry.lattice.Lattice;
import geometry.lattice.LinearLattice;
import geometry.shape.Line;
import geometry.shape.Shape;
import io.visual.VisualizationProperties;
import io.visual.color.ColorManager;
import io.visual.color.DefaultColorManager;
import io.visual.glyph.MockGlyph;
import io.visual.highlight.HighlightManager;
import io.visual.kymograph.Kymograph;
import layers.MockLayerManager;
import layers.cell.CellLayer;
import org.dom4j.Element;
import test.EslimeTestCase;

/**
 * Created by dbborens on 4/3/14.
 */
public class KymographFactoryTest extends EslimeTestCase {
    private Element root;
    private Geometry geom;
    private GeneralParameters p;

    @Override
    protected void setUp() throws Exception {
        super.setUp();
        Lattice lattice = new LinearLattice();
        MockLayerManager layerManager = new MockLayerManager();
        Shape shape = new Line(lattice, 10);
        Boundary boundary = new Periodic(shape, lattice);
        geom = new Geometry(lattice, shape, boundary);
        CellLayer layer = new CellLayer(geom);
        layerManager.setCellLayer(layer);
        root = readXmlFile("factories/KymographFactoryTest.xml");
        p = makeMockGeneralParameters();
    }


    public void testTypicalCase() throws Exception {
        Element typicalCase = root.element("typical-case");
        Kymograph actual = KymographFactory.instantiate(typicalCase, p);
        actual.init(geom, new double[1], new int[1]);

        Kymograph expected = makeTypicalCase();
        assertEquals(expected, actual);
    }

    public void testMinimalCase() throws Exception {
        Element minimalCase = root.element("minimal-case");
        Kymograph actual = KymographFactory.instantiate(minimalCase, p);
        actual.init(geom, new double[1], new int[1]);

        Kymograph expected = makeMinimalCase();
        assertEquals(expected, actual);
    }

    private Kymograph makeMinimalCase() {
        ColorManager colorManager = new DefaultColorManager();
        HighlightManager highlightManager = new HighlightManager();
        double edge = 10.0;

        VisualizationProperties mapState = new VisualizationProperties(colorManager, edge, 0);
        mapState.setHighlightManager(highlightManager);
        Kymograph kymograph = new Kymograph(mapState);
        kymograph.init(geom, new double[1], new int[1]);
        return kymograph;
    }

    private Kymograph makeTypicalCase() {
        ColorManager colorManager = new DefaultColorManager();
        HighlightManager highlightManager = new HighlightManager();
        highlightManager.setGlyph(0, new MockGlyph());
        double edge = 5.0;

        VisualizationProperties mapState = new VisualizationProperties(colorManager, edge, 0);
        mapState.setHighlightManager(highlightManager);

        Kymograph kymograph = new Kymograph(mapState);
        kymograph.init(geom, new double[1], new int[1]);
        return kymograph;
    }

}
