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

import android.os.Environment;

import java.util.ArrayList;
import java.util.LinkedHashSet;
import java.util.Map;
import java.util.Date;
import java.util.Calendar;
import java.util.TimeZone;
import java.io.File;
import java.io.FileOutputStream;
import java.io.ByteArrayInputStream;

import static org.junit.Assert.assertTrue;
import static org.junit.Assert.assertEquals;

import org.junit.Test;

import org.beigesoft.loga.Loga;
import org.beigesoft.zlib.ZLibStreamer;
import org.beigesoft.pdf.model.EFontS14;
import org.beigesoft.pdf.model.ERegisteredTtfFont;
import org.beigesoft.pdf.model.PdfDocument;
import org.beigesoft.pdf.model.HasPdfContent;
import org.beigesoft.pdf.service.PdfFactory;
import org.beigesoft.pdf.service.PdfMaker;
import org.beigesoft.doc.model.EUnitOfMeasure;
import org.beigesoft.doc.model.Document;
import org.beigesoft.doc.model.DocTable;
import org.beigesoft.doc.model.DocImage;
import org.beigesoft.doc.model.MetricsString;
import org.beigesoft.doc.model.TableCell;
import org.beigesoft.doc.model.EAlignHorizontal;
import org.beigesoft.doc.model.EWraping;
import org.beigesoft.doc.model.EPageSize;
import org.beigesoft.doc.model.EPageOrientation;
import org.beigesoft.ttf.model.TtfFont;
import org.beigesoft.ttf.service.TtfCompactFontMaker;
import org.beigesoft.ttf.service.TtfInputStream;
import org.beigesoft.doc.service.DocumentMaker;
import org.beigesoft.graphic.service.AndroidImageLoader;

/**
 * <p>Write image into PDF tests.</p>
 *
 * @author Yury Demidenko
 */
public class WritePdfImageTest extends android.test.AndroidTestCase {

  Loga logger;

  PdfFactory factory;

  PdfMaker<HasPdfContent> pdfMaker;
  
  DocumentMaker<HasPdfContent> docMaker;
    
  public WritePdfImageTest() throws Exception {
    this.logger = new Loga();
    this.logger.setDbgSh(true);
    this.factory = new PdfFactory();
    this.factory.setLog(this.logger);
    this.factory.init();
    this.factory.lazyGetFctElement().setFctImageRgb(new AndroidImageLoader());
    this.pdfMaker = this.factory.lazyGetPdfMaker();
    this.docMaker = this.factory.lazyGetDocumentMaker();
  }

  public void testAll() throws Exception {
    Document<HasPdfContent> doc = this.factory.lazyGetFctDocument().createDoc(EPageSize.A4, EPageOrientation.PORTRAIT);
    PdfDocument<HasPdfContent> docPdf = this.factory.createPdfDoc(doc);
    DocImage<HasPdfContent> dimg = this.docMaker.addImage(doc, "/img/logo-web-store.png", 20, 150);
    dimg.setScale(0.5);
    this.pdfMaker.addImage(docPdf, dimg);
    this.pdfMaker.addFontTtf(docPdf, ERegisteredTtfFont.DEJAVUSANS.toString());
    this.docMaker.setFontSize(doc, 3.6);
    double width1dot = this.factory.lazyGetUomHelper().fromPoints(1.0, doc.getResolutionDpi(), doc.getUnitOfMeasure());
    doc.setBorder(width1dot);
    doc.setContentPadding(1.0);
    DocTable<HasPdfContent> dtbl = this.docMaker.addDocTable(doc, 2, 1);
    dtbl.getItsCells().get(0).setItsContent("Cell1 Cell1 Cell1 Cell1 Cell1 Cell1 Cell1 Cell1 Cell1 Cell1 Cell1");
    dtbl.getItsCells().get(1).setItsContent("Cell2 Cell2 Cell2 Cell2 Cell2 Cell2 Cell2 Cell2 Cell2 Cell2 Cell2");
    String fntNm = dtbl.getDocument().getFonts().get(0).getItsName();
    assertEquals(1, dtbl.getItsCells().get(0).getFontNumber());
    assertEquals(fntNm, ERegisteredTtfFont.DEJAVUSANS.toString());
    //unis: c-46 e-48 l-4f gids: 63, 65, 6c
    MetricsString msTst = this.factory.lazyGetEvalMetricsString().eval("Cell1 Cell1 Cell1 Cell1 Cell1 Cell1",
      fntNm, dtbl.getItsCells().get(0).getFontSize(), 10000.0, 0.0);
    assertEquals(60, msTst.getWidth(), 1);
    DocTable<HasPdfContent> dtbl1 = this.docMaker.addDocTable(doc, 3, 2);
    dtbl1.getItsCells().get(0).setItsContent("Cell0 Cell0 Cell0 Cell0 Cell0 Cell0 Cell0 Cell0 Cell0 Cell0 Cell0");
    dtbl1.getItsCells().get(1).setItsContent("Cell1 Cell1 Cell1 Cell1 Cell1 Cell1 Cell1 Cell1 Cell1 Cell1 Cell1");
    dtbl1.getItsCells().get(2).setItsContent("Cell2 Cell2 Cell2 Cell2 Cell2 Cell2 Cell2 Cell2 Cell2 Cell2 Cell2");
    dtbl1.getItsCells().get(3).setItsContent("Cell3 Cell3 Cell3 Cell3 Cell3 Cell3 Cell3 Cell3 Cell3 Cell3 Cell3");
    dtbl1.getItsCells().get(4).setItsContent("Cell4 Cell4 Cell4 Cell4 Cell4 Cell4 Cell4 Cell4 Cell4 Cell4 Cell4");
    dtbl1.getItsCells().get(5).setItsContent("Cell5 Cell5 Cell5 Cell5 Cell5 Cell5 Cell5 Cell5 Cell5 Cell5 Cell5");
    dtbl1.getItsCells().get(1).setAlignHorizontal(EAlignHorizontal.CENTER);
    dtbl1.getItsCells().get(2).setAlignHorizontal(EAlignHorizontal.RIGHT);
    DocTable<HasPdfContent> dtbl2 = this.docMaker.addDocTable(doc, 3, 3);
    int i = 0;
    for (TableCell cel : dtbl2.getItsCells()) {
      cel.setItsContent("T2 Cell Cell Cell Cell Cell Cell Cell Cell Cell Cell Cell Cell Cell" + (i++));
    }
    dtbl2.setY1(297.0 - 20.0 - 13);
    dtbl2.setIsY1Fixed(true);
    DocTable<HasPdfContent> dtbl3 = this.docMaker.addDocTable(doc, 3, 3);
    i = 0;
    for (TableCell cel : dtbl3.getItsCells()) {
      if (i != 1 && i != 5) {
        cel.setItsContent("T3 Cell Cell Cell Cell Cell Cell Cell Cell Cell Cell Cell Cell Cell");
      }
      i++;
    }
    dtbl3.getItsCells().get(0).setMergedCell(dtbl3.getItsCells().get(1));
    dtbl3.getItsCells().get(2).setMergedCell(dtbl3.getItsCells().get(5));
    DocTable<HasPdfContent> dtbl4 = this.docMaker.addDocTableCustomBorder(doc, 4, 1);
    dtbl4.getItsCells().get(0).setItsContent("Invoice #");
    dtbl4.getItsCells().get(0).setAlignHorizontal(EAlignHorizontal.RIGHT);
    dtbl4.getItsColumns().get(0).setWraping(null);
    dtbl4.getItsColumns().get(0).setIsWidthFixed(true);
    dtbl4.getItsColumns().get(0).setWidth(50);
    dtbl4.getItsCells().get(1).setItsContent("AA-68687");
    dtbl4.getItsCells().get(1).setIsShowBorderBottom(true);
    dtbl4.getItsCells().get(2).setItsContent("from");
    dtbl4.getItsColumns().get(2).setWraping(EWraping.WRAP_CONTENT);
    dtbl4.getItsCells().get(3).setItsContent("27 Jul 2017");
    dtbl4.getItsCells().get(3).setIsShowBorderBottom(true);
    dtbl4.setWraping(null);
    dtbl4.setIsWidthFixed(true);
    double contMaxWd = dtbl4.getStartPage().getWidth() - dtbl4.getStartPage()
      .getMarginLeft() - dtbl4.getStartPage().getMarginRight();
    dtbl4.setWidth(contMaxWd * 0.7);
    this.docMaker.deriveElements(doc);
    FileOutputStream fos = null;
    this.pdfMaker.prepareBeforeWrite(docPdf);
    this.pdfMaker.setIsCompressed(docPdf, false);
    String dir = Environment.getExternalStorageDirectory().getAbsolutePath() + File.separator;
    try {
      fos = new FileOutputStream(dir + "test-img.pdf");
      this.factory.lazyGetPdfWriter().write(null, docPdf, fos);
      fos.flush();
      fos.close();
    } finally {
      if (fos != null) {
        fos.close();
      }
    }
  }
  
}
