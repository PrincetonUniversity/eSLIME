/*
 *  Copyright (c) 2014 David Bruce Borenstein and the Trustees of
 *  Princeton University. All rights reserved.
 */

package factory.control.run;

import org.dom4j.Document;
import org.dom4j.Element;
import org.dom4j.io.SAXReader;

import java.io.File;

/**
 * Converts a text input file into a root element.
 * <p/>
 * Created by dbborens on 11/23/14.
 */
public abstract class DocumentFactory {

    public static Element instantiate(String projectPath) {
        try {
            File f = new File(projectPath);
            SAXReader reader = new SAXReader();
            Document document = reader.read(f);
            Element root = document.getRootElement();
            return root;
        } catch (Exception ex) {
            throw new RuntimeException(ex);
        }
    }
}
