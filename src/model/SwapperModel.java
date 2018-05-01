package model;

/**
 *
 * @author victor.rocha
 */
public class SwapperModel {
    
    private String currentWord;
    private String replacementWord;
    private String brokenEquals;

    public SwapperModel() {
    }

    public String getCurrentWord() {
        return currentWord;
    }

    public void setCurrentWord(String currentWord) {
        this.currentWord = currentWord;
    }

    public String getReplacementWord() {
        return replacementWord;
    }

    public void setReplacementWord(String replacementWord) {
        this.replacementWord = replacementWord;
    }

    public String getBrokenEquals() {
        return brokenEquals;
    }

    public void setBrokenEquals(String brokenEquals) {
        this.brokenEquals = brokenEquals;
    }
    
    
    
}
