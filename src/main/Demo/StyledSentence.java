package main.Demo;

import java.util.ArrayList;

/**
 * Created by nghia on 6/26/2017.
 */
public class StyledSentence {
    private String sentence = "";
    private ArrayList<StyledWord> styledWords;

    // Constructors
    public StyledSentence() {
    }

    public StyledSentence(ArrayList<StyledWord> styledWords) {
        this.styledWords = styledWords;

        // Generate normal string from styledWords
        for(int i = 0; i < styledWords.size(); i++) {
            this.sentence = this.sentence + styledWords.get(i).getWord() + " ";
        }
    }

    // Getters
    public String getSentence() {
        return sentence;
    }

    public ArrayList<StyledWord> getStyledWords() {
        return styledWords;
    }
}
