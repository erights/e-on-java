package org.erights.e.elib.tables;

/*
The contents of this file are subject to the Electric Communities E Open
Source Code License Version 1.0 (the "License"); you may not use this file
except in compliance with the License. You may obtain a copy of the License
at http://www.communities.com/EL/.

Software distributed under the License is distributed on an "AS IS" basis,
WITHOUT WARRANTY OF ANY KIND, either express or implied. See the License for
the specific language governing rights and limitations under the License.

The Original Code is the Distributed E Language Implementation, released
July 20, 1998.

The Initial Developer of the Original Code is Electric Communities.
Copyright (C) 1998 Electric Communities. All Rights Reserved.

Contributor(s): ______________________________________.
*/

/**
 * @author Mark S. Miller
 */
class VoidColumn extends Column {

    private final int myCapacity;

    /**
     *
     */
    VoidColumn(int capacity) {
        super();
        myCapacity = capacity;
    }

    /**
     *
     */
    protected Column diverge(Class membType) {
        return this;
    }

    /**
     *
     */
    Object get(int pos) {
        return null;
    }

    /**
     *
     */
    Class memberType() {
        return Void.class;
    }

    /**
     *
     */
    Column newVacant(int capacity) {
        return new VoidColumn(capacity);
    }

    /**
     *
     */
    int capacity() {
        return myCapacity;
    }

    /**
     *
     */
    void put(int pos, Object value) {
        if (value != null) {
            throw new ArrayStoreException("only nulls allowed");
        }
    }

    /**
     *
     */
    void vacate(int pos) {
        //don't need to do anything
    }
}
