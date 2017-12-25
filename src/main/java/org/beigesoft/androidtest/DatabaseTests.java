package org.beigesoft.androidtest;

/*
 * Copyright (c) 2016 Beigesoft ™
 *
 * Licensed under the GNU General Public License (GPL), Version 2.0
 * (the "License");
 * you may not use this file except in compliance with the License.
 *
 * You may obtain a copy of the License at
 *
 * http://www.gnu.org/licenses/old-licenses/gpl-2.0.en.html
 */

import java.util.Map;
import java.util.HashMap;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import static org.junit.Assert.assertEquals;

import org.beigesoft.android.log.Logger;
import org.beigesoft.android.sqlite.service.SrvDatabase;
import org.beigesoft.android.sqlite.service.CursorFactory;

import org.beigesoft.settings.MngSettings;
import org.beigesoft.orm.service.SrvOrmAndroid;
import org.beigesoft.orm.test.TestSimple;
import org.beigesoft.persistable.UserTomcat;
import org.beigesoft.persistable.RoleJetty;
import org.beigesoft.factory.FctConvertersToFromString;
import org.beigesoft.factory.FctFillersObjectFields;
import org.beigesoft.holder.IHolderForClassByName;
import org.beigesoft.holder.HolderRapiSetters;
import org.beigesoft.holder.HolderRapiGetters;
import org.beigesoft.holder.HolderRapiFields;
import org.beigesoft.persistable.UserTomcat;
import org.beigesoft.persistable.UserRoleTomcat;
import org.beigesoft.persistable.IdUserRoleTomcat;
import org.beigesoft.test.persistable.UserRoleTomcatPriority;
import org.beigesoft.service.IUtlReflection;
import org.beigesoft.properties.UtlProperties;
import org.beigesoft.service.UtlReflection;
import org.beigesoft.settings.MngSettings;
import org.beigesoft.orm.factory.FctBnCnvIbnToColumnValues;
import org.beigesoft.orm.factory.FctBcCnvEntityToColumnsValues;
import org.beigesoft.factory.FctBcFctSimpleEntities;
import org.beigesoft.orm.factory.FctBnCnvBnFromRs;
import org.beigesoft.model.ColumnsValues;
import org.beigesoft.orm.holder.HldCnvToColumnsValuesNames;
import org.beigesoft.orm.holder.HldCnvFromRsNames;
import org.beigesoft.service.SrvSqlEscape;
import org.beigesoft.service.HlpInsertUpdate;
import org.beigesoft.orm.service.FillerEntitiesFromRs;
import org.beigesoft.service.SrvSqlEscape;
import org.beigesoft.test.persistable.Department;
import org.beigesoft.holder.HolderRapiGetters;
import org.beigesoft.holder.HolderRapiFields;
import org.beigesoft.service.IUtlReflection;
import org.beigesoft.properties.UtlProperties;
import org.beigesoft.service.UtlReflection;
import org.beigesoft.orm.factory.FctBnCnvIbnToColumnValues;
import org.beigesoft.orm.factory.FctBcCnvEntityToColumnsValues;
import org.beigesoft.orm.holder.HldCnvToColumnsValuesNames;
import org.beigesoft.service.HlpInsertUpdate;

/**
 * <p>Tests of database service for Android.
 * </p>
 *
 * @author Yury Demidenko
 */
public class DatabaseTests extends android.test.AndroidTestCase {

  /**
   * <p>ID DB tests.</p>
   **/
  public static final Integer ID_DATABASE = 999;

  /**
   * <p>Database service.</p>
   **/
  private SrvDatabase srvDatabase;

  /**
   * <p>ORM service.</p>
   **/
  private SrvOrmAndroid<Cursor> srvOrm;

  /**
   * <p>Reflection service.</p>
   **/
  private IUtlReflection utlReflection = new UtlReflection();

  /**
   * <p>Perform simple (non-concurrence) tests.</p>
   * @throws Exception an exception
   **/
  public void testAll() throws Exception {
    Context context = getContext();
    Logger log = new Logger();
    log.setIsShowDebugMessages(false);
    SQLiteDatabase db = context.openOrCreateDatabase ("dbtest.sqlite",
     Context.MODE_PRIVATE, new CursorFactory());
    srvDatabase = new SrvDatabase();
    srvDatabase.setSqliteDatabase(db);
    srvDatabase.setLogger(log);
    srvOrm = new SrvOrmAndroid<Cursor>();
    srvOrm.setSrvDatabase(srvDatabase);
    srvOrm.setNewDatabaseId(999);
    srvOrm.setHlpInsertUpdate(new HlpInsertUpdate());
    srvOrm.setLogger(log);
    srvOrm.setUtlReflection(getUtlReflection());
    MngSettings mngSettings = new MngSettings();
    mngSettings.setLogger(log);
    mngSettings.setUtlProperties(new UtlProperties());
    mngSettings.setUtlReflection(getUtlReflection());
    srvOrm.setLogger(log);
    srvOrm.setMngSettings(mngSettings);
    log.debug(null, DatabaseTests.class, 
      "loading configuration: beige-orm, persistence-sqlite.xml");
    srvOrm.loadConfiguration("beige-orm", "persistence-sqlite.xml");
    FctBnCnvIbnToColumnValues facConvFields = new FctBnCnvIbnToColumnValues();
    facConvFields.setUtlReflection(getUtlReflection());
    facConvFields.setTablesMap(srvOrm.getTablesMap());
    HolderRapiGetters hrg = new HolderRapiGetters();
    hrg.setUtlReflection(getUtlReflection());
    facConvFields.setGettersRapiHolder(hrg);
    HolderRapiFields hrf = new HolderRapiFields();
    hrf.setUtlReflection(getUtlReflection());
    facConvFields.setFieldsRapiHolder(hrf);
    facConvFields.setSrvSqlEscape(new SrvSqlEscape());
    FctBcCnvEntityToColumnsValues fcetcv = new FctBcCnvEntityToColumnsValues();
    HldCnvToColumnsValuesNames hldConvFld = new HldCnvToColumnsValuesNames();
    hldConvFld.setFieldsRapiHolder(hrf);
    fcetcv.setLogger(log);
    fcetcv.setTablesMap(srvOrm.getTablesMap());
    fcetcv.setFieldsConvertersNamesHolder(hldConvFld);
    fcetcv.setGettersRapiHolder(hrg);
    fcetcv.setFieldsRapiHolder(hrf);
    fcetcv.setFieldsConvertersFatory(facConvFields);
    srvOrm.setFactoryCnvEntityToColumnsValues(fcetcv);
    FillerEntitiesFromRs<Cursor> fillerEntitiesFromRs = new FillerEntitiesFromRs<Cursor>();
    fillerEntitiesFromRs.setTablesMap(srvOrm.getTablesMap());
    fillerEntitiesFromRs.setLogger(log);
    fillerEntitiesFromRs.setFieldsRapiHolder(hrf);
    FctFillersObjectFields fctFillersObjectFields = new FctFillersObjectFields();
    fctFillersObjectFields.setUtlReflection(getUtlReflection());
    HolderRapiSetters hrs = new HolderRapiSetters();
    hrs.setUtlReflection(getUtlReflection());
    fctFillersObjectFields.setSettersRapiHolder(hrs);
    fillerEntitiesFromRs.setFillersFieldsFactory(fctFillersObjectFields);
    srvOrm.setFctFillersObjectFields(fctFillersObjectFields);
    FctBnCnvBnFromRs<Cursor> fctBnCnvBnFromRs = new FctBnCnvBnFromRs<Cursor>();
    FctBcFctSimpleEntities fctBcFctSimpleEntities = new FctBcFctSimpleEntities();
    fctBcFctSimpleEntities.setSrvDatabase(srvDatabase);
    srvOrm.setEntitiesFactoriesFatory(fctBcFctSimpleEntities);
    fctBnCnvBnFromRs.setEntitiesFactoriesFatory(fctBcFctSimpleEntities);
    fctBnCnvBnFromRs.setFillersFieldsFactory(fctFillersObjectFields);
    fctBnCnvBnFromRs.setTablesMap(srvOrm.getTablesMap());
    fctBnCnvBnFromRs.setFieldsRapiHolder(hrf);
    fctBnCnvBnFromRs.setFillerObjectsFromRs(fillerEntitiesFromRs);
    fillerEntitiesFromRs.setConvertersFieldsFatory(fctBnCnvBnFromRs);
    HldCnvFromRsNames hldCnvFromRsNames = new HldCnvFromRsNames();
    hldCnvFromRsNames.setFieldsRapiHolder(hrf);
    fillerEntitiesFromRs.setFieldConverterNamesHolder(hldCnvFromRsNames);
    srvOrm.setFillerEntitiesFromRs(fillerEntitiesFromRs);
    assertEquals("integer not null primary key autoincrement",
      srvOrm.getMngSettings().getFieldsSettings()
                .get(RoleJetty.class).get("itsId")
                  .get("definition"));
    TestSimple<Cursor> testSimple = new TestSimple<Cursor>();
    testSimple.setSrvDatabase(srvDatabase);
    testSimple.setLogger(log);
    testSimple.setSrvOrm(srvOrm);
    testSimple.doTest1();
    //test standard SQL queries - insert, update, delete:
    srvDatabase.setIsAutocommit(true);
    String uTinsert = "insert into USERTOMCAT values ('admin777', 'admin777');";
    Map<String, Object> addParam = new HashMap<String, Object>();
    UserTomcat ut = new UserTomcat();
    ut.setItsId("admin777");
    UserTomcat utr = srvOrm.retrieveEntity(addParam, ut);
    if (utr != null) {
      srvOrm.deleteEntity(addParam, utr);
      utr = srvOrm.retrieveEntity(addParam, ut);
      assertNull(utr);
    }
    srvDatabase.executeQuery(uTinsert);
    utr = srvOrm.retrieveEntity(addParam, ut);
    assertNotNull(utr);
    assertEquals("admin777", utr.getItsPassword());
    srvDatabase.executeQuery("update USERTOMCAT set ITSPASSWORD='admin111' where ITSUSER='admin777';");
    utr = srvOrm.retrieveEntity(addParam, ut);
    assertNotNull(utr);
    assertEquals("admin111", utr.getItsPassword());
    srvDatabase.executeQuery("delete from USERTOMCAT where ITSUSER='admin777';");
    utr = srvOrm.retrieveEntity(addParam, ut);
    assertNull(utr);
  }

  //Simple getters and setters:
  /**
   * <p>Geter for srvDatabase.</p>
   * @return SrvDatabase
   **/
  public final SrvDatabase getSrvDatabase() {
    return this.srvDatabase;
  }

  /**
   * <p>Setter for srvDatabase.</p>
   * @param pSrvDatabase reference/value
   **/
  public final void setSrvDatabase(final SrvDatabase pSrvDatabase) {
    this.srvDatabase = pSrvDatabase;
  }

  /**
   * <p>Geter for srvOrm.</p>
   * @return SrvOrmAndroid
   **/
  public final SrvOrmAndroid<Cursor> getSrvOrm() {
    return this.srvOrm;
  }

  /**
   * <p>Setter for srvOrm.</p>
   * @param pSrvOrm reference
   **/
  public final void setSrvOrm(final SrvOrmAndroid<Cursor> pSrvOrm) {
    this.srvOrm = pSrvOrm;
  }
  /**
   * <p>Getter for utlReflection.</p>
   * @return IUtlReflection
   **/
  public final IUtlReflection getUtlReflection() {
    return this.utlReflection;
  }

  /**
   * <p>Setter for utlReflection.</p>
   * @param pUtlReflection reference
   **/
  public final void setUtlReflection(final IUtlReflection pUtlReflection) {
    this.utlReflection = pUtlReflection;
  }
}
