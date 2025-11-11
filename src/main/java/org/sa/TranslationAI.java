package org.sa;

public class TranslationAI {
  public String getPoetryLineTranslation(String poeticLine, String language) {
    if (poeticLine.trim().isEmpty()) return ""; //ai cannot handle whitespace
    return new AiClient().getAnswer(
        "Provide only with " + language + " language translation " +
            "(nothing more: no extra punctuation or words) of this poetic line: \n" +
            poeticLine);
  }
}
