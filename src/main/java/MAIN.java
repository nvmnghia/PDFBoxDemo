package main.java;

import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.text.PDFTextStripperByArea;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by nghia on 7/3/2017.
 */
public class MAIN {
    public static void main(String[] args) throws IOException {
        PDDocument document = PDDocument.load(new File("D:\\Information Extraction\\data_sample_article_vnujs\\data_sample_article\\sharma test.pdf"));

        PDFTextStripperByArea areaStripper = new PDFTextStripperByArea();
        areaStripper.setSortByPosition(true);
        areaStripper.extractRegions(document.getPage(1));

    }
}
