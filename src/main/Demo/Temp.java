package main.Demo;

import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.PDPage;
import org.apache.pdfbox.pdmodel.PDPageContentStream;
import org.apache.pdfbox.pdmodel.font.*;
import org.apache.pdfbox.text.PDFTextStripper;
import org.apache.pdfbox.text.TextPosition;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Scanner;

/**
 * Created by nghia on 6/19/2017.
 */

public class Temp {
    private static final int ENGLISH = 0;
    private static final int VIETNAMESE = 1;

    private static final String ABSTRACT_PREFIX[] = {"Abstract: ", "Tóm tắt: "};
    private static final String KEYWORDS_PREFIX[] = {"Keywords: ", "Từ khóa: "};

    public static void main(String[] args) throws IOException {
        // Get folder path
        System.out.print("Path: ");
        Scanner scanner = new Scanner(System.in);
        String path = scanner.nextLine();
        scanner.close();

//        // Create document
//        PDDocument document = new PDDocument();
//        document.addPage(new PDPage());
//        document.save(path);
//        document.close();

        // Load document
        File inputDocument = new File(path);
        PDDocument document = PDDocument.load(inputDocument);

//        // Create stream
//        PDPage frontPage = document.getPage(0);
//        PDPageContentStream pageContentStream = new PDPageContentStream(document, frontPage);
//        pageContentStream.setLeading(14.5f);    // Otherwise the text will stay at only one line; lines overlap each other
//
//        // Add text
//        pageContentStream.beginText();
//        pageContentStream.setFont(PDType1Font.HELVETICA_BOLD_OBLIQUE, 14);
//        pageContentStream.newLineAtOffset(250, 700);
//
//        pageContentStream.showText("Much to learn you still have, my young Vietnamese. Oh let me try this stuff");
//        pageContentStream.newLine();
//        pageContentStream.showText("hey what if");
//
//        pageContentStream.endText();
//
//        // Close stream
//        pageContentStream.close();
//
//        // Save document
//        document.save(path);
//        document.close();

        // Read it out to check
        document = PDDocument.load(inputDocument);
        PDFTextStripper stripper = new PDFTextStripper();
        String content = stripper.getText(document);
        System.out.println(content);
        document.close();

        System.out.println("\n\n-------------------------------------------------------------------\n\n");

        // Rearrange lines into paragraphs
        String[] paragraphs = rearrangeParagraph(content);
        for (int i = 0; i < paragraphs.length; i++){
            System.out.println("Para " + i + ":    " + paragraphs[i] + "---------------------------\n\n");
        }
    }

    private static String[] rearrangeParagraph(String content) {
        String[] paragraphs = content.split("\n\n");

        return paragraphs;
    }

    private static String findAbstract(String[] lines, int language) {
        String abstractParagraph = "";

        // Prefix to search Abstract & Keyword sections
        String abstractPrefix = ABSTRACT_PREFIX[language];
        String keywordsPrefix = KEYWORDS_PREFIX[language];

        for(int i = 0; i < lines.length; i++) {
            if(lines[i].startsWith(abstractPrefix)) {
                // Found abstract section
                abstractParagraph = abstractParagraph + lines[i].substring(abstractPrefix.length()) + "\n";

                // The section may span several consecutive lines. Additional search here
                for(int j = i + 1; j < lines.length; j++) {
                    if(lines[j].startsWith(keywordsPrefix) || lines[j].startsWith("1.") || lines[j].startsWith("I.")){
                        break;    // Body/Keyword section is reached
                    } else {
                        abstractParagraph = abstractParagraph + lines[j] + "\n";    // Examined paragraph still belongs to the abstract section
                    }
                }

                break;
            }
        }

        return abstractParagraph;
    }
}
