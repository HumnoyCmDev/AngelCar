package com.beta.cls.angelcar.manager.bus;


import com.squareup.otto.Bus;

/**
 * Created by humnoy on 20/1/59.
 */
public class BusProvider {
    private static Bus bus;
    private BusProvider() {}
    public static Bus getInstance() {
        if (bus == null)
            bus = new Bus();
        return bus;
    }
}
