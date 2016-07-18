package org.zstack.test.core.scheduler;

import org.junit.Before;
import org.junit.Assert;
import org.junit.Test;
import org.quartz.SchedulerException;
import org.springframework.beans.factory.annotation.Autowired;
import org.zstack.core.cloudbus.CloudBus;
import org.zstack.core.componentloader.ComponentLoader;
import org.zstack.core.db.DatabaseFacade;
import org.zstack.core.scheduler.SchedulerFacade;
import org.zstack.core.scheduler.SchedulerVO;
import org.zstack.header.identity.SessionInventory;
import org.zstack.header.storage.snapshot.VolumeSnapshotVO;
import org.zstack.header.vm.VmInstanceInventory;
import org.zstack.simulator.kvm.VolumeSnapshotKvmSimulator;
import org.zstack.test.Api;
import org.zstack.test.ApiSenderException;
import org.zstack.test.DBUtil;
import org.zstack.test.WebBeanConstructor;
import org.zstack.test.deployer.Deployer;

import java.util.List;
import java.util.concurrent.TimeUnit;

/**
 * Created by root on 7/15/16.
 */
public class TestSchedulerDelete {
    ComponentLoader loader;
    Api api;
    @Autowired
    SchedulerFacade scheduler;
    DatabaseFacade dbf;
    CloudBus bus;
    Deployer deployer;
    SessionInventory session;
    VolumeSnapshotKvmSimulator snapshotKvmSimulator;

    @Before
    public void setUp() throws Exception {
        DBUtil.reDeployDB();
        WebBeanConstructor con = new WebBeanConstructor();
        deployer = new Deployer("deployerXml/kvm/TestCreateVmOnKvm.xml", con);
        deployer.addSpringConfig("KVMRelated.xml");
        deployer.addSpringConfig("SchedulerFacade.xml");
        deployer.addSpringConfig("imagestore.xml");
        deployer.build();
        api = deployer.getApi();
        loader = deployer.getComponentLoader();
        bus = loader.getComponent(CloudBus.class);
        dbf = loader.getComponent(DatabaseFacade.class);
        scheduler = loader.getComponent(SchedulerFacade.class);
        snapshotKvmSimulator = loader.getComponent(VolumeSnapshotKvmSimulator.class);
        session = api.loginAsAdmin();
    }

    @Test
    public void test() throws InterruptedException, ApiSenderException, SchedulerException {
        Assert.assertNotNull(scheduler);
        VmInstanceInventory vm = deployer.vms.get("TestVm");
        String volUuid = vm.getRootVolumeUuid();
        api.createScheduler(volUuid, session);
        TimeUnit.SECONDS.sleep(20);
        long counter = dbf.count(VolumeSnapshotVO.class);
        Assert.assertEquals(2,counter);
        SchedulerVO firstRecord = dbf.listAll(SchedulerVO.class).get(0);
        api.deleteScheduler(firstRecord.getUuid(), session);
        long counter2 = dbf.count(SchedulerVO.class);
        Assert.assertEquals(0, counter2);

    }

}
