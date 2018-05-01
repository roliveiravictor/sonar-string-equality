package enumerator;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

/**
 *
 * @author victor.rocha
 */
public enum Patterns {

    REGEX_EQUALS_NUMBER(".equals\\([0-9]*\\)"),
    REGEX_EQUALS_IGNORE_CASE_NUMBER(".equalsIgnoreCase\\([0-9]*\\)"),
    REGEX_REPLACE_EQUALS("\", \"\").equals"),
    REGEX_STRING_WITH_SPACES(".equals(.*?)\\((?=(?:(?:[^\"]*\"){2})*[^\"]*$)\"(?=[^\"]* )[^\"]+\""),
    REGEX_FIRST_PARENTHESES ("[(]"),
    REGEX_CONTENT_INSIDE_PARENTHESES ("\\((.*?)\\)"),
    REGEX_CONTENT_INSIDE_QUOTES ("\\(\"(.*?)\"\\)"),
    
    LEFT_PARENTHESES ("("),
    RIGHT_PARENTHESES (")"),
    DOUBLED_RIGHT_PARENTHESES ("))"),
    TABS_WITH_PARENTHESES (")\t)"),
    NOT_WITH_LEFT_PARENTHESE ("!("),
    
    LEFT_BRACKET("{"),
    BRACKET_WITHOUT_SPACE("){"),
    BRACKET_WITH_SPACE(") {"),
    
    OR_WITHOUT_SPACE (")||"),
    OR_WITH_SPACES (") || "),
    
    LONG_VALUE_OF("Long.valueOf(\""),
    DATAHASH(".getDataHash("),
    
    ELSE_IF_WITHOUT_SPACE("}else"),
    ELSE_IF_WITH_SPACE("} else"),
    
    OBJECT_EQUALS (".equals"),
    OBJECT_EQUALS_IGNORE_CASE (".equalsIgnoreCase"),

    TRIM_EQUALS(".trim().equals"),
    
    GET_STRING(".getString("),
    SUB_STRING(".substring("),

    
    SONAR_EQUALIZED("\".equals"),
    SET(".set"),
    WHITE_SPACE (" "),
    BREAKPOINT (";"),
    QUOTE ("\""),
    EXCLAMATION_POINT ("!"),
    MINUS ("-"),
    TAB ("\t");
    
    private String name;

    public String getName() {
        return name;
    }

    private Patterns(String name) {
        this.name = name;
    }
}
