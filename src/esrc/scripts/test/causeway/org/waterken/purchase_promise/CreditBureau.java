package org.waterken.purchase_promise;

import org.ref_send.promise.Promise;

public interface CreditBureau {

    Promise<Boolean> doCreditCheck(String name);
}
