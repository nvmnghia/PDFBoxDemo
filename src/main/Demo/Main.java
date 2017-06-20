package main.Demo;

import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.PDPage;
import org.apache.pdfbox.pdmodel.PDPageContentStream;
import org.apache.pdfbox.pdmodel.font.*;
import org.apache.pdfbox.text.PDFTextStripper;

import java.io.File;
import java.io.IOException;

/**
 * Created by nghia on 6/20/2017.
 */
public class Main {
    public static void main(String[] args) throws IOException {
        String path = "D:\\Information Extraction\\PDFBox\\test\\my_doc.pdf";

        // Create document
        PDDocument document = new PDDocument();
        document.addPage(new PDPage());
        document.save(path);
        document.close();

        // Load document
        File inputDocument = new File(path);
        document = PDDocument.load(inputDocument);

        // Create stream
        PDPage frontPage = document.getPage(0);
        PDPageContentStream pageContentStream = new PDPageContentStream(document, frontPage);
        pageContentStream.setLeading(14.5f);    // Otherwise the text will stay at only one line; lines overlap each other

        // Add text
        pageContentStream.beginText();
        pageContentStream.setFont(PDType1Font.HELVETICA_BOLD_OBLIQUE, 14);
        pageContentStream.newLineAtOffset(250, 700);

        pageContentStream.showText("Much to learn you still have, my young Vietnamese. Oh let me try this stuff");
        pageContentStream.newLine();
        pageContentStream.showText("hey what if");

        pageContentStream.endText();

        // Close stream
        pageContentStream.close();

        // Save document
        document.save(path);
        document.close();

        // Read it out to check
        document = PDDocument.load(inputDocument);
        PDFTextStripper stripper = new PDFTextStripper();
        String content  = stripper.getText(document);
        System.out.println(content);
        document.close();

        // Split content string
        String[] lines = content.split("\n");
        System.out.println(lines[0] + "\n");
        System.out.println(lines[1]);
    }
}
