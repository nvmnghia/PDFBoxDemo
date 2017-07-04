package main.Demo;

import org.apache.pdfbox.text.TextPosition;

/**
 * Created by nghia on 6/24/2017.
 */

// Store the style of each character of the whole document
// Obviously, control character doesn't have any style!

public class StyledCharacter {

    private TextPosition style;
    private boolean isBold;
    private boolean isCapital;

    // Constructors
    public StyledCharacter() {
    }

    public StyledCharacter(TextPosition style) {
        this.style = style;
        isBold = style.getFont().getName().toLowerCase().contains("bold");
        isCapital = Character.isUpperCase(style.getUnicode().charAt(0));
    }

    public boolean isBold() {
        return isBold;
    }

    public boolean isCapital() {
        return isCapital;
    }

    public TextPosition getStyle() {
        return style;
    }
}
