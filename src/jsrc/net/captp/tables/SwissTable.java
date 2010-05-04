package net.captp.tables;

// Copyright 2002 Combex, Inc. under the terms of the MIT X license
// found at http://www.opensource.org/licenses/mit-license.html ...............

import org.erights.e.develop.assertion.T;
import org.erights.e.elib.base.ValueThunk;
import org.erights.e.elib.ref.Ref;
import org.erights.e.elib.tables.FlexSet;
import org.erights.e.elib.tables.WeakKeyMap;
import org.erights.e.elib.tables.WeakValueMap;
import org.erights.e.elib.util.OneArgFunc;
import org.erights.e.meta.java.math.BigIntegerSugar;

import java.math.BigInteger;
import java.security.SecureRandom;

/**
 * A weak-value table mapping from SwissNumbers to references.
 * <p/>
 * There are two cases: 1) NEAR references to Selfish objects. 2) Everything
 * else. For case #1, a backwards weak-key table is also maintained, such that
 * multiple registrations of a NEAR Selfish object will always yield the same
 * SwissNumber. This SwissNumber can then be (and is) used remotely to
 * represent the sameness identity of resolved references to this Selfish
 * object. Case #1 is used for both live and sturdy references.
 * <p/>
 * Case #2 is used only for sturdy references. The table only maps from
 * SwissNumbers to references, not vice versa, so each registration assigns a
 * new SwissNumber.
 *
 * @author Mark S. Miller
 */
public class SwissTable {

    /**
     * Maps from NEAR Selfish objects to SwissNumbers.
     */
    private final WeakKeyMap mySelfishToSwiss;

    /**
     * Maps from SwissNumber to anything.
     * <p/>
     * Note: can't handle null values.
     */
    private final WeakValueMap mySwissToRef;

    /**
     * Provides new unguessable SwissNumbers.
     */
    private final SecureRandom myEntropy;

    /**
     * OneArgFuncs that handle lookup faulting.
     */
    private final FlexSet mySwissDBs;

    static private final int SWISSDATA_SIZE = 20;

    /**
     *
     */
    public SwissTable(SecureRandom entropy) {
        mySelfishToSwiss = new WeakKeyMap(Object.class, BigInteger.class);
        mySwissToRef = new WeakValueMap(BigInteger.class, Object.class);
        myEntropy = entropy;
        mySwissDBs = FlexSet.fromType(OneArgFunc.class);
    }

    /**
     * Lookup an object by SwissNumber. <p>
     * <p/>
     * If not found, throw an IndexOutOfBoundsException. This is necessary
     * since null is a valid return value. (By decree, the SwissNumber 0
     * designates null.)
     */
    public Object lookupSwiss(BigInteger swissNum)
      throws IndexOutOfBoundsException {
        if (0 == swissNum.signum()) {
            //Since Weak*Maps can't handle nulls, we handle it ourselves.
            return null;
        }
        Object optResult = mySwissToRef.fetch(swissNum, ValueThunk.NULL_THUNK);
        if (null != optResult) {
            return optResult;
        }
        //Since we handle null ourselves, we know that a null optResult means
        //a lookup miss.
        BigInteger swissHash = BigIntegerSugar.cryptoHash(swissNum);
        OneArgFunc[] dbs =
          (OneArgFunc[])mySwissDBs.getElements(OneArgFunc.class);
        for (int i = 0; i < dbs.length; i++) {
            //give each fault handler a chance
            dbs[i].run(swissHash);
        }
        //try one more time
        return mySwissToRef.get(swissNum);
    }

    /**
     * A SwissDB is able to supplement the SwissTable's internal mySwissToRef
     * table with further storage that gets faulted on demand. <p>
     * <p/>
     * When the SwissTable's lookupSwiss fails to find the swissNum in the
     * internal table, it invokes each of its registered swissDBs with a hash
     * of the swissNumber being looked up. This is known as a swissHash, and
     * represents the identity of the object without providing any authority to
     * access the object. A swissDB which has stored a representation of the
     * object elsewhere should then register the object using registerIdentity
     * or registerSwiss, both of which require the swissBase -- the archash of
     * the swissNumber being looked up. In other words,
     * <pre>
     *     swissBase cryptoHash() -> swissNum
     *     swissNum crytoHash()   -> swissHash
     * </pre><p>
     * <p/>
     * If an already registered swissDB is re-registered, an exception is
     * thrown.
     */
    public void addFaultHandler(OneArgFunc swissDB) {
        mySwissDBs.addElement(swissDB, true);
    }

    /**
     * Removes a registered (by addFaultHandler) swissDB. <p>
     * <p/>
     * If not there, this method does nothing.
     */
    public void removeFaultHandler(OneArgFunc swissDB) {
        mySwissDBs.remove(swissDB);
    }

    /**
     * Returns the SwissNumber which represents the identity of this near
     * Selfish object in this vat.
     * <p/>
     * If not 'Ref.isSelfish(obj)", then this will throw an Exception.
     * <p/>
     * This returns the unique SwissNumber which represents the designated near
     * selfish object's unique identity within this vat. If the object wasn't
     * yet associated with a SwissNumber, it will be now.
     */
    public BigInteger getIdentity(Object obj) {
        obj = Ref.resolution(obj);
        if (!Ref.isSelfish(obj)) {
            T.fail("Not Selfish: " + obj);
        }
        BigInteger result =
          (BigInteger)mySelfishToSwiss.fetch(obj, ValueThunk.NULL_THUNK);
        if (null == result) {
            result = nextSwiss();
            mySwissToRef.put(result, obj);
            mySelfishToSwiss.put(obj, result);
        }
        return result;
    }

    /**
     * Returns a SwissNumber with which this ref can be looked up.
     * <p/>
     * This method always assigns and returns a new unique SwissNumber (an
     * integer of some type), even for NEAR Selfish objects that already have
     * one, with one exception. The swissNumber for null is always 0.
     */
    public BigInteger getNewSwiss(Object ref) {
        ref = Ref.resolution(ref);
        if (null == ref) {
            return BigInteger.ZERO;
        }
        if (Ref.isSelfish(ref)) {
            return getIdentity(ref);
        }
        BigInteger result = nextSwiss();
        mySwissToRef.put(result, ref);
        return result;
    }

    /**
     * Registers obj to have the identity 'swissBase.cryptoHash()'. <p>
     * <p/>
     * The cryptoHash of a SwissBase is a SwissNumber, so we also say that the
     * archash of a SwissNumber is a SwissBase. (Of course, our security rests
     * on the assumption that the archash is infeasible to compute.) Since an
     * unconfined client of an object can often get its SwissNumber, something
     * more is needed to establish authority to associate an object with a
     * SwissNumber. For this "something more", we use knowledge of the archash
     * of the number. <p>
     * <p/>
     * The object is given the new identity 'swissBase cryptoHash()', assuming
     * this doesn't conflict with any existing registrations. If it does, an
     * exception is thrown.
     */
    public BigInteger registerIdentity(Object obj, BigInteger swissBase) {
        obj = Ref.resolution(obj);
        if (!Ref.isSelfish(obj)) {
            T.fail("Not Selfish: " + obj);
        }
        BigInteger result = BigIntegerSugar.cryptoHash(swissBase);
        Object oldObj =
          Ref.resolution(mySwissToRef.fetch(result, new ValueThunk(obj)));
        T.require(null == oldObj || oldObj == obj,
                  "SwissNumber already identifies a different object: ",
                  result);
        BigInteger oldSwiss =
          (BigInteger)mySelfishToSwiss.fetch(obj, new ValueThunk(result));
        T.require(oldSwiss.equals(result),
                  "Object already has a different identity: ",
                  oldSwiss + " vs " + result);
        mySelfishToSwiss.put(obj, result);
        mySwissToRef.put(result, obj);
        return result;
    }

    /**
     * Registers ref at 'swissBase.cryptoHash()'.
     * <p/>
     * registerNewSwiss() is to registerIdentity() as getNewSwiss() is to
     * getIdentity(). 'swissBase.cryptoHash()' must not already be registered,
     * or an exception will be thrown. If ref is null, an exception is thrown
     * (since we assume its infeasible to find the archash of zero).
     */
    public BigInteger registerNewSwiss(Object ref, BigInteger swissBase) {
        ref = Ref.resolution(ref);
        BigInteger result = BigIntegerSugar.cryptoHash(swissBase);
        if (null == ref) {
            //XXX In just the way the following is careful not to reveal more
            //than a swissHash in a thrown exception, we need to go through the
            //rest of the swissNumber and swissBase handling logic and make it
            //do likewise. Until
            // bugs.sieve.net/bugs/?func=detailbug&bug_id=125503&group_id=16380
            //is fixed, this problem is urgent.
            BigInteger swissHash = BigIntegerSugar.cryptoHash(result);
            T.fail("May not re-register null for swissHash: " + swissHash);
        }
        Object oldRef = mySwissToRef.fetch(result, ValueThunk.NULL_THUNK);
        if (null == oldRef) {
            mySwissToRef.put(result, ref);
        } else if (ref == oldRef) {
            // Registering the same object with the same base is cool.
            // XXX should we use Ref.same(..) instead of == ?
        } else {
            BigInteger swissHash = BigIntegerSugar.cryptoHash(result);
            T.fail("An object with swissHash " + swissHash +
              "is already registered");
        }
        return result;
    }

    /**
     * A convenience method typically used to obtain new SwissBases (archashes
     * of SwissNumbers).
     * <p/>
     * Since a client of SwissTable can obtain such entropy from the SwissTable
     * anyway, by registering objects, there's no loss of security in providing
     * this convenience method.
     */
    public BigInteger nextSwiss() {
        byte[] swissData = new byte[SWISSDATA_SIZE];
        myEntropy.nextBytes(swissData);
        return new BigInteger(1, swissData);
    }
}
