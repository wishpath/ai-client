package org.sa;

public class WordAI {
  public static String getWordTopic(String word) {
    return new AiClient().getAnswer(
        "Answer with a single word only, no other symbols.\n" +
            "What is a general topic of the word: \"" +
            word +
            "\"\n");
  }

  public static String getWordTopic(String word, String topicSuggestions) {
    return new AiClient().getAnswer(
        "Which 3 provided general topics does the word \"" +
            word + "\" fit best, and why? Explain each. Provided topics: \n" +
            topicSuggestions +
            "\nBe concise.");
  }

  public static String getWordDefinition(String word) {
    return new AiClient().getAnswer("Define " + word + ". Use up to 10 words");
  }

  public static String getWordPartOfSpeech(String word) {
    return new AiClient().getAnswer(
        "if the word \"" +
            word +
            "\" is primarily not a noun, tell what part of the speech it is. Use only one word.");
  }
}
