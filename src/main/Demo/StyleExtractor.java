package main.Demo;

import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.text.PDFTextStripper;
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
    private ArrayList<StyledBlock> allStyledBlock = new ArrayList<>();

    private String text;
    private String[] words;
    private String title = "";

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

        // mapWordToSentence();

        mapWordToBlock();
    }


    public ArrayList<StyledCharacter> getAllStyledCharacters() {
        return allStyledCharacters;
    }

    public StyledWord[] getAllStyledWords() {
        return allStyledWords;
    }

//    public ArrayList<StyledSentence> getAllStyledSentences() {
//        return allStyledSentences;
//    }

    public ArrayList<StyledBlock> getAllStyledBlocks() {
        return allStyledBlock;
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

    private String clearControlChar(String content) {
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

    private void mapWordToBlock() {
        // Iterate through all words. At each word, examine its positionY
        // to determine if the word is in the current block or in the next block

        ArrayList<StyledWord> currentSentenceWords = new ArrayList<>();
        ArrayList<StyledSentence> currentBlockSentences = new ArrayList<>();

        // Initialize variables for the first line
        float previousLine = allStyledWords[0].getStyle().getY();    // Words on the same line have positionX roughly equals to this value

        // Assumption: lines has double spacing at most. This method of guessing line space is applied at the beginning of each block,
        // as there is no way to know for sure the spacing of the block's line
        float previousLineSpace = allStyledWords[0].getStyle().getHeightDir() * 2;    // Maximum height of all character in this string.

        for (int i = 0; i < allStyledWords.length; i++) {
            StyledWord currentWord = allStyledWords[i];

            // Feed the approx() with 2 positionY
            // The threshold value is used to avoid the fluctuation in the line. Furthermore it can group subscript/superscript into the current line
            // Assumption: The distance from subscript/superscript to the current line is less than half of the maximum height of the line's characters
            if (approx(currentWord.getStyle().getY(), previousLine, currentWord.getStyle().getHeightDir() / 2)) {    // Same line
                currentSentenceWords.add(currentWord);

                if (!currentWord.isBold() && (currentWord.getWord().endsWith(".") || currentWord.getWord().endsWith("?") || currentWord.getWord().endsWith("!"))) {    // Sentence ends, and it's not index!
                    currentBlockSentences.add(new StyledSentence(currentSentenceWords));    // Ends completed sentence
                    currentSentenceWords = new ArrayList<>();    // Prepare new sentence
                }
            } else {    // New line
                float currentLine = currentWord.getStyle().getY();
                float currentLineSpace = Math.abs(currentLine - previousLine);    // NOTE: The document is read downward AND the origin is the UPPER LEFT corner

                if (approx(currentLineSpace, previousLineSpace, 2f)) {    // Same block, as the line spaces are roughly equal
                    previousLine = currentLine;
                    previousLineSpace = currentLineSpace;

                    // IMPORTANT: To avoid copying and pasting a large amount of code to add words to sentence,
                    // the previous 2 variables were updated and the word will be added in the next iteration.
                    i--;

//                    currentSentenceWords.add(currentWord);
//
//                    if (!currentWord.isBold() && (currentWord.getWord().endsWith(".") || currentWord.getWord().endsWith("?") || currentWord.getWord().endsWith("!"))) {    // Sentence ends, and it's not index!
//                        currentBlockSentences.add(new StyledSentence(currentSentenceWords));    // Ends completed sentence
//                        currentSentenceWords = new ArrayList<>();    // Prepare new sentence
//                    }
                } else {    // New block
                    if (currentSentenceWords.size() != 0) {    // The previous sentence/paragraph didn't end correctly (eg title. No one puts ending punctuation in the title!)
                        currentBlockSentences.add(new StyledSentence(currentSentenceWords));
                        currentSentenceWords = new ArrayList<>();
                    }

                    allStyledBlock.add(new StyledBlock(currentBlockSentences));
                    currentBlockSentences = new ArrayList<>();

                    previousLine = currentLine;
                    previousLineSpace = currentWord.getStyle().getHeightDir() * 2;

                    i--;

//                    currentSentenceWords.add(currentWord);
//
//                    if (!currentWord.isBold() && (currentWord.getWord().endsWith(".") || currentWord.getWord().endsWith("?") || currentWord.getWord().endsWith("!"))) {    // Sentence ends, and it's not index!
//                        currentBlockSentences.add(new StyledSentence(currentSentenceWords));    // Ends completed sentence
//                        currentSentenceWords = new ArrayList<>();    // Prepare new sentence
//                    }
                }
            }
        }
    }

    /**
     * Check if the inputs a and b are roughly equal
     *
     * @return The boolean value of the operator
     */
    private boolean approx(float a, float b, float threshold) {
        return Math.abs(a - b) < threshold;
    }
}
