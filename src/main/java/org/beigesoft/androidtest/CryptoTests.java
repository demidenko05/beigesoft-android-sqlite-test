package org.beigesoft.androidtest;

/*
 * Copyright (c) 2017 Beigesoft ™
 *
 * Licensed under the GNU General Public License (GPL), Version 2.0
 * (the "License");
 * you may not use this file except in compliance with the License.
 *
 * You may obtain a copy of the License at
 *
 * http://www.gnu.org/licenses/old-licenses/gpl-2.0.en.html
 */

import java.io.InputStream;
import java.io.OutputStream;
import java.io.FileOutputStream;
import java.io.ByteArrayInputStream;
import java.io.BufferedOutputStream;
import java.io.BufferedInputStream;
import java.io.File;

import android.content.Context;
import android.content.ContextWrapper;
import android.content.res.AssetManager;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertArrayEquals;

import org.beigesoft.android.log.Logger;
import org.beigesoft.crypto.CryptoTest;
import org.beigesoft.ajetty.crypto.CryptoServiceSc;

/**
 * <p>Crypto tests for Android inner crypto-providers.
 * </p>
 *
 * @author Yury Demidenko
 */
public class CryptoTests extends android.test.AndroidTestCase {

  public void testAll() throws Exception {
    CryptoTest crt = new CryptoTest();
    Logger log = new Logger();
    log.setIsShowDebugMessages(false);
    crt.setLog(log);
    crt.setCryptoService(new CryptoServiceSc());
    ContextWrapper cw = new ContextWrapper(getContext());
    AssetManager assetManager = getContext().getAssets();
    InputStream ins = null;
    OutputStream outs = null;
    try {
      ins = assetManager.open("bobs-pizza-nfs.sqlite");
      outs = new BufferedOutputStream(new FileOutputStream(cw.getFilesDir().getAbsolutePath() + "/bobs-pizza-nfs.sqlite"));
      byte[] data = new byte[1024];
      int count;
      while ((count = ins.read(data)) != -1) {
        outs.write(data, 0, count);
      }
      outs.flush();
    } finally {
      if (ins != null) {
        try {
          ins.close();
        } catch (Exception e2) {
          e2.printStackTrace();
        }
      }
      if (outs != null) {
        try {
          outs.close();
        } catch (Exception e3) {
          e3.printStackTrace();
        }
      }
    }
    crt.setKsPath(cw.getFilesDir().getAbsolutePath());
    crt.testRsaFly();
    crt.testRsaAesFly();
    crt.testRsaAesBc();
    crt.testRsaAesBcRealData();
  }
}
