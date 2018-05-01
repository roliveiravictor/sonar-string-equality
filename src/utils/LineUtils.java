package utils;

import enumerator.Conditionals;
import model.SwapperModel;
import enumerator.Patterns;
import enumerator.PrimitiveTypes;
import enumerator.ReservedWords;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import org.apache.commons.lang3.StringUtils;

/**
 *
 * @author victor.rocha
 * 
 */
public class LineUtils {
    
    private static final String PATTERN_OBJECT_EQUALS = Patterns.OBJECT_EQUALS.getName();
    private static final String PATTERN_TAB = Patterns.TAB.getName();
    private static final String PATTERN_OBJECT_EQUALS_IGNORE = Patterns.OBJECT_EQUALS_IGNORE_CASE.getName();
    private static final String PATTERN_LEFT_PARENTHESES = Patterns.LEFT_PARENTHESES.getName();
    
    private static final int FIRST_INDEX = 0;
    
    public static boolean isToEqualize(String string){
        return string.contains(PATTERN_OBJECT_EQUALS) 
                && isNotAlreadyEqualized(string) 
                && isNotRarePattern(string)
                && hasQuotesInLine(string);
    }
    
    public static String swapEqualizer(String line){
        line = getFormattedLineToSwap(line);
        
        String[] wordsFromLine = line.split(Patterns.WHITE_SPACE.getName());
        
        Integer currentWordPosition = 0;
        for(String currentWord : wordsFromLine){
            if(isToEqualize(currentWord)){
                String toReplaceCurrentWord = "";
                toReplaceCurrentWord = getIntroductionStatements(currentWord);
                
                String brokenEquals = breakEquals(currentWord);
                
                if(isConditionStatement(currentWord)){
                    currentWord = currentWord.replaceFirst("\\(", "");
                }
                
                SwapperModel swapper = new SwapperModel();
                swapper.setCurrentWord(currentWord);
                swapper.setReplacementWord(toReplaceCurrentWord);
                swapper.setBrokenEquals(brokenEquals);
                
                toReplaceCurrentWord = getAssembledSwapper(swapper);
                
                wordsFromLine[currentWordPosition] = toReplaceCurrentWord;
            }
            
            currentWordPosition++;
        }
        
        return formattedNewLine(wordsFromLine);
    }

    private static String getIntroductionStatements(String currentWord) {
        String leftParenthesesPattern = Patterns.LEFT_PARENTHESES.getName();
        String exclamationPattern = Patterns.EXCLAMATION_POINT.getName();
        
        String toReplaceCurrentWord = "";
        
        if(isConditionStatement(currentWord)){
            toReplaceCurrentWord = leftParenthesesPattern;
        }

        if(isNegativeStatement(currentWord)){
            toReplaceCurrentWord += exclamationPattern;
        }
        
        return toReplaceCurrentWord;
    }

    private static String breakEquals(String currentWord) {
        boolean isEqualsIgnoreStatement = currentWord.contains(PATTERN_OBJECT_EQUALS_IGNORE);
        
        String[] brokenEquals = null;
        if(isEqualsIgnoreStatement){
            brokenEquals = currentWord.split(PATTERN_OBJECT_EQUALS_IGNORE);
        } else if(currentWord.contains(PATTERN_OBJECT_EQUALS)){
            brokenEquals = currentWord.split(PATTERN_OBJECT_EQUALS);
        }

        if(isConditionStatement(currentWord)){
            String firstParenthesesRegex = Patterns.REGEX_FIRST_PARENTHESES.getName();
            brokenEquals[FIRST_INDEX] = brokenEquals[FIRST_INDEX].replaceFirst(firstParenthesesRegex, "");
        }

        String exclamationPattern = Patterns.EXCLAMATION_POINT.getName();
        if(isNegativeStatement(currentWord)){
            brokenEquals[FIRST_INDEX] = brokenEquals[FIRST_INDEX].replace(exclamationPattern, "");
        }
        
        return brokenEquals[FIRST_INDEX];
    }
    
    private static boolean isStartWithConditional(String line){
        String lineAnalysis = line.replaceAll(Patterns.TAB.getName(), "");
               lineAnalysis = lineAnalysis.replaceAll(Patterns.WHITE_SPACE.getName(), "");
        
               return lineAnalysis.startsWith(Conditionals.IF.getName() + Patterns.LEFT_PARENTHESES.getName());
    }
         
    private static boolean isConditionStatement(String currentWord){
        String firstIndexStr = String.valueOf(currentWord.charAt(FIRST_INDEX));
        return PATTERN_LEFT_PARENTHESES.contains(firstIndexStr);
    }
    
    private static boolean isNegativeStatement(String currentWord){
        String exclamationPattern = Patterns.EXCLAMATION_POINT.getName();
        return currentWord.contains(exclamationPattern);
    }

    private static String getAssembledSwapper(SwapperModel swapper) {
        boolean isEqualsStatement = swapper.getCurrentWord().contains(PATTERN_OBJECT_EQUALS);
        boolean isEqualsIgnoreStatement = swapper.getCurrentWord().contains(PATTERN_OBJECT_EQUALS_IGNORE);
        
        if(isEqualsIgnoreStatement){
            swapper.setReplacementWord(swapper.getReplacementWord() + assembleByEqualsPattern(swapper, PATTERN_OBJECT_EQUALS_IGNORE));
        } else if(isEqualsStatement){
            swapper.setReplacementWord(swapper.getReplacementWord() + assembleByEqualsPattern(swapper, PATTERN_OBJECT_EQUALS));
        }
                
        return swapper.getReplacementWord();
    }
    
    private static String assembleByEqualsPattern(SwapperModel swapper, String equalsPattern){
        String rightParenthesesPattern = Patterns.RIGHT_PARENTHESES.getName();
        
        String currentWord = swapper.getCurrentWord();
        
        Pattern pattern = Pattern.compile(Patterns.REGEX_CONTENT_INSIDE_PARENTHESES.getName());
        Matcher matcher = pattern.matcher(currentWord);

        String stringToEqualize = "";
        while(matcher.find()) {
            int fixedIndex = 1;
            stringToEqualize = matcher.group(fixedIndex);
        }

        if(stringToEqualize.isEmpty())
            return "";
        
        cleanTabsFromBrokenEquals(swapper);
        stringToEqualize = getTabsToEqualizeString(swapper, stringToEqualize);
        
        String assembledReplacement = stringToEqualize + 
                                      equalsPattern +
                                      PATTERN_LEFT_PARENTHESES +
                                      swapper.getBrokenEquals() +
                                      rightParenthesesPattern;
        
        if(currentWord.contains(Patterns.DOUBLED_RIGHT_PARENTHESES.getName())){
            assembledReplacement += rightParenthesesPattern;
        }
        
        return assembledReplacement;
    }

    private static String formattedNewLine(String[] wordsFromLine) {
        StringBuilder newLineBuilder = new StringBuilder();
        for(String word : wordsFromLine){
            newLineBuilder.append(word + Patterns.WHITE_SPACE.getName());
        }
        
        String newLine = StringUtils.removeEnd(newLineBuilder.toString(), Patterns.WHITE_SPACE.getName());
        
        if(isSimpleLine(newLineBuilder)){
            newLine += Patterns.BREAKPOINT.getName();
        }
        
        return newLine;
    }

    private static boolean isNotRarePattern(String string) {
        Pattern stringWithWhiteSpacesPattern = Pattern.compile(Patterns.REGEX_STRING_WITH_SPACES.getName());
        Matcher stringWithWhiteSpacesMatcher = stringWithWhiteSpacesPattern.matcher(string);
        
        Pattern equalsNumberPattern = Pattern.compile(Patterns.REGEX_EQUALS_NUMBER.getName());
        Matcher equalsNumberMatcher = equalsNumberPattern.matcher(string);
        
        Pattern equalsIgnoreNumberPattern = Pattern.compile(Patterns.REGEX_EQUALS_IGNORE_CASE_NUMBER.getName());
        Matcher equalsIgnoreNumberMatcher = equalsIgnoreNumberPattern.matcher(string);
        
        return !(string.contains(Patterns.REGEX_REPLACE_EQUALS.getName()) 
                || string.contains(Patterns.GET_STRING.getName())
                || string.contains(Patterns.SUB_STRING.getName())
                || string.contains(Patterns.LONG_VALUE_OF.getName())
                || string.contains(Patterns.DATAHASH.getName())
                || string.contains(Patterns.SET.getName())
                || string.contains(Patterns.MINUS.getName())
                || string.contains(Patterns.NOT_WITH_LEFT_PARENTHESE.getName())
                || string.contains(Patterns.TRIM_EQUALS.getName())
                || equalsNumberMatcher.find()
                || equalsIgnoreNumberMatcher.find()
                || stringWithWhiteSpacesMatcher.find());
    }

    private static boolean isNotAlreadyEqualized(String string) {
        return !string.contains(Patterns.SONAR_EQUALIZED.getName());
    }

    private static boolean hasQuotesInLine(String string) {
        return string.contains(Patterns.QUOTE.getName());
    }

    private static void cleanTabsFromBrokenEquals(SwapperModel swapper) {
        if(swapper.getBrokenEquals().contains(PATTERN_TAB)){
            swapper.setBrokenEquals(swapper.getBrokenEquals().replace(PATTERN_TAB, ""));
        }
    }

    private static String getTabsToEqualizeString(SwapperModel swapper, String stringToEqualize) {
        Integer tabsCounter = 0;
        if(swapper.getCurrentWord().contains(PATTERN_TAB)){
            for(Character currentChar : swapper.getCurrentWord().toCharArray()){
                if(PATTERN_TAB.equals(currentChar.toString())){
                    tabsCounter++;
                }
            }
            
            for(int i = 0; i < tabsCounter; i++){
                stringToEqualize = PATTERN_TAB + stringToEqualize;
            }
        }
        return stringToEqualize;
    }

    private static boolean isSimpleLine(StringBuilder newLineBuilder) {
        return !(newLineBuilder.toString().contains(Conditionals.IF.getName())
               || newLineBuilder.toString().contains(Conditionals.AND.getName())
               || newLineBuilder.toString().contains(Conditionals.OR.getName())
               || newLineBuilder.toString().contains(Patterns.LEFT_BRACKET.getName()));
    }

    private static String getFormattedLineToSwap(String line) {
        if(line.contains(Patterns.ELSE_IF_WITHOUT_SPACE.getName())){
            line = line.replace(Patterns.ELSE_IF_WITHOUT_SPACE.getName(), Patterns.ELSE_IF_WITH_SPACE.getName());
        }
        
        if(line.contains(Conditionals.IF.getName() + Patterns.LEFT_PARENTHESES.getName())){
            line = line.replace(Conditionals.IF.getName() + Patterns.LEFT_PARENTHESES.getName(), 
                                Conditionals.IF.getName() + Patterns.WHITE_SPACE.getName() + Patterns.LEFT_PARENTHESES.getName());
        }

        if(line.contains(Patterns.OR_WITHOUT_SPACE.getName())){
            line = line.replace(Patterns.OR_WITHOUT_SPACE.getName(), Patterns.OR_WITH_SPACES.getName());
        }
        
        
        if(line.contains(Patterns.BRACKET_WITHOUT_SPACE.getName())){
            line = line.replace(Patterns.BRACKET_WITHOUT_SPACE.getName(), Patterns.BRACKET_WITH_SPACE.getName());
        }
        
        if(line.contains(Patterns.TABS_WITH_PARENTHESES.getName())){
            line = line.replace(Patterns.TABS_WITH_PARENTHESES.getName(), Patterns.DOUBLED_RIGHT_PARENTHESES.getName());
        }
        
        return line;
    }

}
