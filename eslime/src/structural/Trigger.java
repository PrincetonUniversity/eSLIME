/*
 *  Copyright (c) 2014 David Bruce Borenstein and the Trustees of
 *  Princeton University. All rights reserved.
 */

package structural;

/**
 * Presumably, Java has some API for this. However, I can't find one.
 * I suppose it's the same as "Runnable," but that seemed totally
 * unclear for many use cases.
 * <p>
 * Created by dbborens on 12/31/14.
 */
public interface Trigger {
    public void go();
}
