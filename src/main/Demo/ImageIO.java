package main.Demo;

import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.PDPageContentStream;
import org.apache.pdfbox.pdmodel.graphics.image.PDImageXObject;

import java.io.File;
import java.io.IOException;

/**
 * Created by nghia on 6/20/2017.
 */
public class ImageIO {
    public static void main(String[] args) throws IOException {
        String path = "D:\\Information Extraction\\PDFBox\\test\\my_doc.pdf";

        // Load document
        PDDocument document = PDDocument.load(new File(path));

        // Load image
        PDImageXObject imageXObject = PDImageXObject.createFromFile("D:\\Multimedia\\Pictures\\2016\\Hết Tết - BlackBerry Z10.png", document);

        // Prepare ContentStream
        PDPageContentStream pageContentStream = new PDPageContentStream(document, document.getPage(0));

        // Draw image
        pageContentStream.drawImage(imageXObject, 100, 300, 200, 500);

        pageContentStream.close();

        document.save(path);
        document.close();
    }
}
