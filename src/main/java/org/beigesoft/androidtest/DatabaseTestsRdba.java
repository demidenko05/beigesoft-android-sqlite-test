package org.beigesoft.androidtest;

import java.util.Map;
import java.util.HashMap;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import static org.junit.Assert.assertEquals;

import org.beigesoft.mdlp.UsTmc;
import org.beigesoft.fct.FctTstAndra;
import org.beigesoft.rdb.Tst1;
import org.beigesoft.rdb.IRdb;
import org.beigesoft.rdb.IOrm;

/**
 * <p>Tests of database service for Android. Multi-connections like(DB).</p>
 *
 * @author Yury Demidenko
 */
public class DatabaseTestsRdba extends android.test.AndroidTestCase {

  /**
   * <p>Perform simple (non-concurrence) tests.</p>
   * @throws Exception an exception
   **/
  public void testAll() throws Exception {
    Map<String, Object> rvs = new HashMap<String, Object>();
    FctTstAndra fctApp = new FctTstAndra();
    fctApp.init(rvs, new CtxAttr(getContext()));
    Tst1 tst1 = new Tst1();
    tst1.setFctApp(fctApp);
    tst1.tst1();
    //test standard SQL queries - insert, update, delete:
    IRdb rdb = (IRdb) fctApp.laz(rvs, IRdb.class.getSimpleName());
    IOrm orm = (IOrm) fctApp.laz(rvs, IOrm.class.getSimpleName());
    rdb.setAcmt(true);
    String uTinsert = "insert into USTMC (USR,PWD,VER) values ('admin777','admin777',1);";
    Map<String, Object> vs = new HashMap<String, Object>();
    UsTmc ut = new UsTmc();
    ut.setIid("admin777");
    orm.refrEnt(rvs, vs, ut);
    if (ut.getIid() != null) {
      orm.del(rvs, vs, ut);
      ut.setIid("admin777");
      orm.refrEnt(rvs, vs, ut);
      assertNull(ut.getIid());
    } else {
      ut.setIid("admin777");
    }
    rdb.exec(uTinsert);
    orm.refrEnt(rvs, vs, ut);
    assertNotNull(ut.getIid());
    assertEquals("admin777", ut.getPwd());
    rdb.exec("update USTMC set PWD='admin111' where USR='admin777';");
    orm.refrEnt(rvs, vs, ut);
    assertNotNull(ut.getIid());
    assertEquals("admin111", ut.getPwd());
    rdb.exec("delete from USTMC where USR='admin777';");
    orm.refrEnt(rvs, vs, ut);
    assertNull(ut.getIid());
    fctApp.release(rvs);
  }
}
