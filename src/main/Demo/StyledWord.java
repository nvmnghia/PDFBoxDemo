package main.Demo;

import org.apache.pdfbox.text.TextPosition;

/**
 * Created by nghia on 6/25/2017.
 */

// REDESIGN NEEDED: storing style of all characters consumes a huge amount of memory -> redundancy

public class StyledWord {
    // 1. Each word encompasses its following punctuation
    // 2. The getStyle function assumes that all characters in one word follow one style (the style of the first character)

    private String word = "";
    private StyledCharacter[] styledCharacters;

    // Constructors
    public StyledWord() {
    }

    public StyledWord(String word, StyledCharacter[] styledCharacters) {
        this.word = word;
        this.styledCharacters = styledCharacters;
    }

    // Getters
    public String getWord() {
        return word;
    }

    public StyledCharacter[] getStyledCharacters() {
        return styledCharacters;
    }

    public TextPosition getStyle() {
        return styledCharacters[0].getStyle();
    }

    public boolean isBold() {
        return styledCharacters[0].isBold();
    }


    public boolean isCapital() {
        return styledCharacters[0].isCapital();
    }
}
