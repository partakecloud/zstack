package org.zstack.test.storage.backup;

import junit.framework.Assert;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.zstack.core.componentloader.ComponentLoader;
import org.zstack.core.db.DatabaseFacade;
import org.zstack.header.simulator.storage.backup.SimulatorBackupStorageDetails;
import org.zstack.header.storage.backup.BackupStorageInventory;
import org.zstack.header.storage.backup.BackupStorageVO;
import org.zstack.test.Api;
import org.zstack.test.ApiSenderException;
import org.zstack.test.BeanConstructor;
import org.zstack.test.DBUtil;
import org.zstack.utils.Utils;
import org.zstack.utils.data.SizeUnit;
import org.zstack.utils.logging.CLogger;

public class TestDeleteBackupStorage {
    CLogger logger = Utils.getLogger(TestDeleteBackupStorage.class);
    Api api;
    ComponentLoader loader;
    DatabaseFacade dbf;

    @Before
    public void setUp() throws Exception {
        DBUtil.reDeployDB();
        BeanConstructor con = new BeanConstructor();
        /* This loads spring application context */
        loader = con.addXml("PortalForUnitTest.xml").addXml("Simulator.xml").addXml("BackupStorageManager.xml").addXml("AccountManager.xml").build();
        dbf = loader.getComponent(DatabaseFacade.class);
        api = new Api();
        api.startServer();
    }

    @After
    public void tearDown() throws Exception {
        api.stopServer();
    }

    @Test
    public void test() throws ApiSenderException {
        SimulatorBackupStorageDetails ss = new SimulatorBackupStorageDetails();
        ss.setTotalCapacity(SizeUnit.GIGABYTE.toByte(100));
        ss.setUsedCapacity(0);
        ss.setUrl("nfs://simulator/backupstorage/");
        BackupStorageInventory inv = api.createSimulatorBackupStorage(1, ss).get(0);
        api.deleteBackupStorage(inv.getUuid());
        BackupStorageVO vo = dbf.findByUuid(inv.getUuid(), BackupStorageVO.class);
        Assert.assertEquals(null, vo);
    }
}
