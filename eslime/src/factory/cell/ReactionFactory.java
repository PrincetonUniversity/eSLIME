/*
 *  Copyright (c) 2014 David Bruce Borenstein and the Trustees of
 *  Princeton University. All rights reserved.
 */

package factory.cell;

import org.dom4j.Element;
import structural.utilities.XmlUtil;

/**
 * Created by dbborens on 1/8/15.
 */
public abstract class ReactionFactory {

    public static Reaction instantiate(Element root) {
        double exp = XmlUtil.getDouble(root, "exp", 0.0);
        double inj = XmlUtil.getDouble(root, "inj", 0.0);
        String id = root.element("target").getTextTrim();

        return new Reaction(inj, exp, id);
    }

}
