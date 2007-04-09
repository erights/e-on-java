package org.erights.e.elib.ref;

// Copyright 2007 Kevin Reid under the terms of the MIT X license
// found at http://www.opensource.org/licenses/mit-license.html ...............

import org.erights.e.develop.assertion.T;
import org.erights.e.elib.oldeio.TextWriter;
import org.erights.e.elib.prim.E;
import org.erights.e.elib.prim.MirandaMethods;
import org.erights.e.elib.sealing.SealedBox;
import org.erights.e.elib.slot.FinalSlot;
import org.erights.e.elib.slot.Guard;
import org.erights.e.elib.tables.Equalizer;
import org.erights.e.elib.tables.NotSettledException;

import java.io.IOException;

/**
 * A Ref whose behavior and resolution are provided by other objects (handler
 * and resolution box).
 *
 * @author Kevin Reid
 */
public abstract class Proxy extends Ref {

    protected Object myHandler;
    protected Object myResolutionBox;
    private boolean committed = false;

    protected Proxy(Object handler, Object resolutionBox)
      throws NotSettledException {
        if (!Ref.isSettled(handler)) {
            throw new NotSettledException(
              "proxy handler: " + E.toString(handler));
        }
        myHandler = handler;
        myResolutionBox = resolutionBox;
    }

    public static Proxy run(Object handler,
                            Object resolutionBox,
                            boolean isFar) throws NotSettledException {
        if (isFar) {
            return new FarRef(handler, resolutionBox);
        } else {
            return new RemotePromise(handler, resolutionBox);
        }
    }

    public boolean equals(Object other) {
        if (other.getClass() != this.getClass()) {
            return false;
        }
        Proxy that = (Proxy)other;
        if (checkBox() || that.checkBox()) {
            T.fail("equals comparison of resolved proxy is impossible");
        }
        return Equalizer.make().isSameYet(myHandler, that.myHandler);
    }

    void commit() {
        if (committed) {
            T.fail("internal: proxy already committed");
        }
        committed = true;

        if (!(myResolutionBox instanceof FinalSlot)) {
            myResolutionBox = new FinalSlot(Ref.broken(E.asRTE(
              "Resolution promise of a Far ref handled by ~A didn't resolve to a simple slot, but ~A.")));
        }

        myHandler = null;
    }

    protected synchronized boolean checkBox() {
        if (committed) {
            return true;
        }
        myResolutionBox = Ref.resolution(myResolutionBox);
        boolean answer = Ref.isResolved(myResolutionBox);
        if (answer) {
            commit();
        }
        return answer;
    }

    abstract protected boolean isResolvedIfNotForwarding();

    abstract protected void __printOnIfNotForwarding(TextWriter out)
      throws IOException;

    // --- Forwarding methods ---

    public String state() {
        if (checkBox()) {
            return Ref.state(((FinalSlot)myResolutionBox).get());
        } else {
            return Ref.EVENTUAL;
        }
    }

    public boolean isResolved() {
        if (checkBox()) {
            return Ref.isResolved(((FinalSlot)myResolutionBox).get());
        } else {
            return this.isResolvedIfNotForwarding();
        }
    }

    public Throwable optProblem() {
        if (checkBox()) {
            return Ref.optProblem(((FinalSlot)myResolutionBox).get());
        } else {
            return null;
        }
    }

    public Ref resolutionRef() {
        if (checkBox()) {
            return Ref.toRef(((FinalSlot)myResolutionBox).get())
              .resolutionRef();
        } else {
            return this;
        }
    }

    public Object callAll(String verb, Object[] args) {
        if (checkBox()) {
            return E.callAll(((FinalSlot)myResolutionBox).get(), verb, args);
        } else {
            T.fail("not synchronously callable");
            return null;
        }
    }

    public Ref sendAll(String verb, Object[] args) {
        if (checkBox()) {
            return E.sendAll(((FinalSlot)myResolutionBox).get(), verb, args);
        } else {
            return E.send(myHandler, "handleSend", verb, args);
        }
    }

    public Throwable sendAllOnly(String verb, Object[] args) {
        if (checkBox()) {
            return E.sendAllOnly(((FinalSlot)myResolutionBox).get(),
                                 verb,
                                 args);
        } else {
            return E.sendOnly(myHandler, "handleSendOnly", verb, args);
        }
    }

    public Object __conformTo(Guard guard) {
        if (checkBox()) {
            return Ref.toRef(((FinalSlot)myResolutionBox).get())
              .__conformTo(guard);
        } else {
            return MirandaMethods.__conformTo(this, guard);
        }
    }

    public SealedBox __optSealedDispatch(Object brand) {
        if (checkBox()) {
            return Ref.toRef(((FinalSlot)myResolutionBox).get())
              .__optSealedDispatch(brand);
        } else {
            return MirandaMethods.__optSealedDispatch(this, brand);
        }
    }

    public void __printOn(TextWriter out) throws IOException {
        if (checkBox()) {
            Ref.toRef(((FinalSlot)myResolutionBox).get()).__printOn(out);
        } else {
            this.__printOnIfNotForwarding(out);
        }
    }

    // --- Other Ref protocol ---

    void setTarget(Ref newTarget) {
        T.fail("setTarget doesn't apply to proxies");
    }
}
