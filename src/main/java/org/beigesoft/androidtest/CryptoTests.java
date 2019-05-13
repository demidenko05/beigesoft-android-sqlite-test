/*
BSD 2-Clause License

Copyright (c) 2019, Beigesoft™
All rights reserved.

Redistribution and use in source and binary forms, with or without
modification, are permitted provided that the following conditions are met:

* Redistributions of source code must retain the above copyright notice, this
  list of conditions and the following disclaimer.

* Redistributions in binary form must reproduce the above copyright notice,
  this list of conditions and the following disclaimer in the documentation
  and/or other materials provided with the distribution.

THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS"
AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE
IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE ARE
DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT HOLDER OR CONTRIBUTORS BE LIABLE
FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL
DAMAGES (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR
SERVICES; LOSS OF USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER
CAUSED AND ON ANY THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY,
OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE
OF THIS SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
 */

package org.beigesoft.androidtest;

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

import org.beigesoft.loga.Loga;
import org.beigesoft.crypto.CryptoTest;
import org.beigesoft.ajetty.crypto.CryptoService;

/**
 * <p>Crypto tests for Android inner crypto-providers.
 * </p>
 *
 * @author Yury Demidenko
 */
public class CryptoTests extends android.test.AndroidTestCase {

  public void testAll() throws Exception {
    CryptoTest crt = new CryptoTest();
    Loga log = new Loga();
    crt.setLog(log);
    crt.setCryptoService(new CryptoService());
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
