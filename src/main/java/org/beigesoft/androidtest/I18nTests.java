package org.beigesoft.androidtest;

/*
 * Copyright (c) 2018 Beigesoft ™
 *
 * Licensed under the GNU General Public License (GPL), Version 2.0
 * (the "License");
 * you may not use this file except in compliance with the License.
 *
 * You may obtain a copy of the License at
 *
 * http://www.gnu.org/licenses/old-licenses/gpl-2.0.en.html
 */

import org.beigesoft.android.log.Logger;
import org.beigesoft.service.I18NTest;

/**
 * <p>I18N tests for Android.
 * </p>
 *
 * @author Yury Demidenko
 */
public class I18nTests extends android.test.AndroidTestCase {

  public void testAll() throws Exception {
    I18NTest test = new I18NTest();
    Logger log = new Logger();
    log.setIsShowDebugMessages(true);
    test.setLog(log);
    test.tst1();
  }
}
