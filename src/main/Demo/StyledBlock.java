package main.Demo;

import java.util.ArrayList;

/**
 * Created by nghia on 7/9/2017.
 */
public class StyledBlock {
    private ArrayList<StyledSentence> allStyledSentences;

    // Constructors

    public StyledBlock() {
    }

    public StyledBlock(ArrayList<StyledSentence> allStyledSentences) {
        this.allStyledSentences = allStyledSentences;
    }

    // Getter

    public ArrayList<StyledSentence> getAllStyledSentences() {
        return allStyledSentences;
    }
}
