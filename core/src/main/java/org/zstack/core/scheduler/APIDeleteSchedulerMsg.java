package org.zstack.core.scheduler;

import org.zstack.header.message.APIDeleteMessage;
import org.zstack.header.message.APIMessage;
import org.zstack.header.message.APIParam;

/**
 * Created by root on 7/15/16.
 */
public class APIDeleteSchedulerMsg extends APIDeleteMessage{

    @APIParam(resourceType = SchedulerVO.class)
    private String uuid;


    public APIDeleteSchedulerMsg () {

    }
    public String getUuid() {
        return uuid;
    }

    public void setUuid(String uuid) {
        this.uuid = uuid;
    }



}
