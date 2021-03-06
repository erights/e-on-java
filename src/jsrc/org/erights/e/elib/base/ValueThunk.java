// Copyright 2004 Hewlett Packard, Inc. under the terms of the MIT X license
// found at http://www.opensource.org/licenses/mit-license.html ...............

package org.erights.e.elib.base;

import org.erights.e.elib.prim.StaticMaker;
import org.erights.e.elib.serial.JOSSPassByConstruction;
import org.erights.e.elib.tables.Selfless;

/**
 * A PassByCopy thunk which just returns its value.
 *
 * @author Mark S. Miller
 */
public class ValueThunk implements Thunk, Selfless, JOSSPassByConstruction {

    static private final long serialVersionUID = -7305418990437314179L;

    static public final Thunk NULL_THUNK = new ValueThunk(null);

    private final Object myValue;

    /**
     * @param value
     */
    public ValueThunk(Object value) {
        myValue = value;
    }

    /**
     * Uses 'makeValueThunk(value)'
     */
    public Object[] getSpreadUncall() {
        Object[] result = {StaticMaker.make(ValueThunk.class), "run", myValue};
        return result;
    }

    /**
     * Returns the value.
     */
    public Object run() {
        return myValue;
    }
}
