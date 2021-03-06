package org.zstack.header.vm;

import org.zstack.header.message.APIEvent;

/**
 * Created by frank on 10/17/2015.
 */
public class APIDetachIsoFromVmInstanceEvent extends APIEvent {
    private VmInstanceInventory inventory;

    public APIDetachIsoFromVmInstanceEvent() {
    }

    public APIDetachIsoFromVmInstanceEvent(String apiId) {
        super(apiId);
    }

    public VmInstanceInventory getInventory() {
        return inventory;
    }

    public void setInventory(VmInstanceInventory inventory) {
        this.inventory = inventory;
    }
}
