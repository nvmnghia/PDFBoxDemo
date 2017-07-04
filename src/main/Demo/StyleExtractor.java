package main.Demo;

import com.sun.javafx.css.Style;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.text.PDFTextStripper;
import org.apache.pdfbox.text.PDFTextStripperByArea;
import org.apache.pdfbox.text.TextPosition;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Random;

/**
 * Created by nghia on 6/24/2017.
 */

public class StyleExtractor extends PDFTextStripper {
    private PDDocument document;

    private ArrayList<StyledCharacter> allStyledCharacters = new ArrayList<>();    // Stores the style of each character of the whole document
    private StyledWord[] allStyledWords;
    private ArrayList<StyledSentence> allStyledSentences = new ArrayList<>();

    private String text;
    private String[] words;
    private String title = "";

    private float ignoreHeaderY;
    private float ignoreFooterY;

    private float defaultFontSize;

    /**
     * Instantiate a new PDFTextStripper object.
     * Universal function: it does everything to initialize all important variable of the class
     *
     * @throws IOException If there is an error loading the properties.
     */

    public StyleExtractor(PDDocument document) throws IOException {
        this.document = document;

        // This very function is the foundation of the code.
        // It generates both text (raw text data with line break)
        // and allStyledCharacters (style information of each character).
        text = super.getText(document);

        words = clearControlChar(text).split(" ");

        mapCharacterToWord();

        processTitle();

        setDefaultFontSize();

        mapWordToSentence();

        // Experimental
        // findHeaderFooter();
    }

    private void findHeaderFooter() {
       ignoreHeaderY = allStyledWords[0].getStyle().getY();    // The first letter read must belong to the header
       ignoreFooterY = allStyledWords[allStyledWords.length - 1].getStyle().getY();    // The last letter read must belong to the footer
    }

    public ArrayList<StyledCharacter> getAllStyledCharacters() {
        return allStyledCharacters;
    }

    public ArrayList<StyledSentence> getAllStyledSentences() {
        return allStyledSentences;
    }

    public StyledWord[] getAllStyledWords() {
        return allStyledWords;
    }

    public String getText() {
        return text;
    }

    @Override
    protected void processTextPosition(TextPosition text) {
        // Control characters are ignored (This method is called only for non-control characters).
        // However, spaces are also ignored in this Override method. Why?
        // The StyleExtractor will split the parsed raw text into words (aka StyledWord) by the space character.
        // Therefore, style information for space is NOT recorded.
        if (!text.getUnicode().equals(" ")) {
            allStyledCharacters.add(new StyledCharacter(text));
        }

        super.processTextPosition(text);
    }

    public String getTitle() {
        return title;
    }

    private void mapCharacterToWord() {
        int styledCharacterCounter = 0;

        allStyledWords = new StyledWord[words.length];

        for(int i = 0; i < allStyledWords.length; i++) {
            String word = words[i];
            StyledCharacter[] wordCharacter = new StyledCharacter[word.length()];

            for(int j = 0; j < wordCharacter.length; j++) {    // Group processed StyledCharacter together to create a StyledWord
                wordCharacter[j] = allStyledCharacters.get(styledCharacterCounter);
                styledCharacterCounter++;
            }

            allStyledWords[i] = new StyledWord(word, wordCharacter);
        }
    }

    private void mapWordToSentence() {
        ArrayList<StyledWord> tempSentenceWord = new ArrayList<>();

        float previousPositionY = allStyledWords[0].getStyle().getY();
        float previousFontSize = allStyledWords[0].getStyle().getFontSize();

        setDefaultFontSize();

        for(int i = 0; i < allStyledWords.length; i++) {
            tempSentenceWord.add(allStyledWords[i]);

            // Determine sentence ending
            if (allStyledWords[i].getWord().endsWith(".")){
                allStyledSentences.add(new StyledSentence(tempSentenceWord));
                tempSentenceWord = new ArrayList<>();    // New sentence

            } else if ((Math.abs(allStyledWords[i].getStyle().getY() - previousPositionY) > 1)
                    && (allStyledWords[i].getStyle().getFontSize() != previousFontSize)) {    // New line (NOT subscript/superscript)
                allStyledSentences.add(new StyledSentence(tempSentenceWord));
                tempSentenceWord = new ArrayList<>();
            }

            // Update previous variable
            previousPositionY = allStyledWords[i].getStyle().getY();
            previousFontSize = allStyledWords[i].getStyle().getFontSize();
        }
    }

    private void setDefaultFontSize() {
        // This method randomly examines 20% of allStyledWords to pick the most popular font (aka default font)
        Random random = new Random();
        int bound = allStyledWords.length;
        int numOfTest = allStyledWords.length / 5;
        int i, test;

        // Each element of the array counts the usage of a font. The font size is the index number
        // Assumption: font size does not exceed 40
        int[] fontSizeUsageCounter = new int[40];

        // Examine words
        for (i = 0; i < numOfTest; i++) {
            test = (int) allStyledWords[random.nextInt(bound)].getStyle().getFontSize();
            fontSizeUsageCounter[test] = fontSizeUsageCounter[test] + 1;
        }

        // Select the most popular font
        int tempDefaultFontSize = 0;
        for (i = 1; i < 40; i++) {
            if (fontSizeUsageCounter[tempDefaultFontSize] < fontSizeUsageCounter[i]) {
                tempDefaultFontSize = i;
            }
        }
        defaultFontSize = tempDefaultFontSize;
    }

    private void processTitle() {
        // First find the words with the largest font size
        float largestFontSize = allStyledWords[0].getStyle().getFontSize();
        for (int i = 1; i < allStyledWords.length; i++) {
            if (largestFontSize < allStyledWords[i].getStyle().getFontSize()) {
                largestFontSize = allStyledWords[i].getStyle().getFontSize();
            }
        }

        // Those words contain the title
        for(int i = 0; i < allStyledWords.length; i++) {
            if (allStyledWords[i].getStyle().getFontSize() == largestFontSize) {    // One such word found. Must be the title
                for(int j = i; j < allStyledWords.length; j++) {    // Only add consecutive words
                    if (allStyledWords[j].getStyle().getFontSize() == largestFontSize) {
                        title = title + allStyledWords[j].getWord() + " ";
                    } else {
                        break;
                    }
                }
                break;    // The title is completed
            }
        }
    }

    private static String clearControlChar(String content) {
        String temp = content.replaceAll("\n|\t", " ");    // Replace all \n and ]t with only one space.
        // Space letter will later be used to split the whole content string into words
        temp = temp.replaceAll("[\\p{Cntrl}]", "");     // Remove all other control characters

        // One problem with the \n and \t replacement is that it can cause double space.
        // A double space will eventually lead to empty string when split() is performed.
        // Therefore double space will be cleaned here:
        String clearedTemp = "";
        for(int i = 0; i < temp.length(); i++){
            clearedTemp = clearedTemp + String.valueOf(temp.charAt(i));

            if(temp.charAt(i) == ' ') {
                for(int j = i+1; j < temp.length(); j++) {
                    if (temp.charAt(j) == ' ') {
                        i++;    // Ignore the immediately subsequent space
                    } else {
                        break;    // If a character other than a space is met, break the loop. i++ is wrong.
                    }
                }
            }
        }

        return clearedTemp;
    }
}
