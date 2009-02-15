package org.waterken.purchase_promise;

import java.io.Serializable;

import org.joe_e.Struct;
import org.ref_send.promise.Fulfilled;
import org.ref_send.promise.Promise;
import org.ref_send.promise.eventual.Eventual;

public final class InventoryMaker {
    private InventoryMaker() {}
    
    static public Inventory
    make(final Eventual _) {
        class InventoryX extends Struct implements Inventory, Serializable {
            static private final long serialVersionUID = 1L;
            
            public Promise<Boolean>
            isAvailable(String partNo) {
                _.log.comment("is available");
                return Fulfilled.ref(true);
            }

            public Promise<Boolean>
            placeOrder(String buyer, String partNo) {
                _.log.comment("placing order");
                return Fulfilled.ref(true);
            }
        }
        return new InventoryX();
    }
}
