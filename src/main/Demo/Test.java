package main.Demo;

import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.PDPage;
import org.apache.pdfbox.pdmodel.PDPageContentStream;
import org.apache.pdfbox.pdmodel.font.PDType1Font;
import org.apache.pdfbox.text.PDFTextStripper;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Random;

/**
 * Created by nghia on 6/24/2017.
 */

public class Test {
    public static void main(String[] args) throws IOException {
        PDDocument document = PDDocument.load(new File("D:\\Information Extraction\\data_sample_article_vnujs\\data_sample_article\\2866-5176-1-SM.pdf"));

        StyleExtractor extractor = new StyleExtractor(document);
        String content = extractor.getText();
        System.out.println("Content: " + content);

        System.out.println("\n__________________________________________________________________________\n");
        System.out.println("Title: " + extractor.getTitle());

        System.out.println("\n__________________________________________________________________________\n");
        System.out.println("Sentences:");
        ArrayList<StyledSentence> allStyledSentences = extractor.getAllStyledSentences();
        for(int i = 0; i < allStyledSentences.size(); i++) {
            System.out.println(allStyledSentences.get(i).getSentence());
        }

//        System.out.println("\n__________________________________________________________________________\n");
//        System.out.println("word: ");
//        StyledWord[] list = extractor.getAllStyledWords();
//        for (int i = 0; i < list.length; i++) {
//            System.out.print(list[i].getWord() + " " + (list[i].isBold() ? "Bold " : "") + (list[i].isCapital() ? "Capital " : "") + list[i].getStyle().getFontSize() + " "
//                             + list[i].getStyle().getFont().getName() + " " + list[i].getStyle().getX() + " " + list[i].getStyle().getY() + "    ");
//            if (i%10 == 0) {
//                System.out.println("");
//            }
//        }
//
//        PDPage page = document.getPage(0);
    }
}
