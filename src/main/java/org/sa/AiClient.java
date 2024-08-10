package org.sa;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;

public class AiClient {

  private static final String CHARSET = "utf-8";
  private static final String API_URL = "https://api.openai.com/v1/chat/completions";
  private String gptModelType = "gpt-4o";
  private String wholeResponse;
  private String answer;

  public AiClient OpenAiGptApi(String gptModel) {
    gptModelType = gptModel;
    return this;
  }

  //https://platform.openai.com/docs/models

  public AiClient setModelGpt35Turbo() {
    this.gptModelType = "gpt-3.5-turbo";
    return this;
  }

  public AiClient setModelGpt4o() {
    this.gptModelType = "gtp-4o";
    return this;
  }

  public AiClient setModelGpt4oMini() {
    this.gptModelType = "gpt-4o-mini";
    return this;
  }

  public AiClient setModelGpt4Turbo() {
    this.gptModelType = "gpt-4-turbo";
    return this;
  }

  public String getAnswer(String question) {
    try {
      return actionsToGetAnswer(question);
    } catch (IOException e) {
      throw new RuntimeException("Error fetching answer from Open AI", e);
    }
  }

  private String actionsToGetAnswer(String question) throws IOException {
    HttpURLConnection connection = null;
    try {
      connection = createConnection();
      sendRequest(connection, question);
      getResponse(connection);
      extractAnswerFromResponse();
      return answer;
    } finally {
      if (connection != null) {
        connection.disconnect();
      }
    }
  }

  private static HttpURLConnection createConnection() throws IOException {
    HttpURLConnection con = (HttpURLConnection) new URL(API_URL).openConnection();
    con.setRequestMethod("POST");
    con.setRequestProperty("Authorization", "Bearer " + getOpenaiAPIKeyAsMyWindowsEnvironmentVariable());
    con.setRequestProperty("Content-Type", "application/json; utf-8");
    con.setDoOutput(true);
    return con;
  }

  private static String getOpenaiAPIKeyAsMyWindowsEnvironmentVariable() {
    return System.getenv("OPENAI_API_KEY");
  }

  private void sendRequest(HttpURLConnection connection, String question) throws IOException {
    JSONObject jsonBody = new JSONObject();
    jsonBody.put("model", gptModelType);

    JSONArray messages = new JSONArray();
    JSONObject message = new JSONObject();
    message.put("role", "user");
    message.put("content", question);
    messages.put(message);

    jsonBody.put("messages", messages);

    try (OutputStreamWriter writer = new OutputStreamWriter(connection.getOutputStream(), CHARSET)) {
      writer.write(jsonBody.toString());
      writer.flush();
    }
  }

  private void getResponse(HttpURLConnection connection) throws IOException {
    int responseCode = connection.getResponseCode();
    if (responseCode == HttpURLConnection.HTTP_OK) {
      try (BufferedReader in = new BufferedReader(new InputStreamReader(connection.getInputStream(), CHARSET))) {
        wholeResponse = readAllLines(in);
      }
    } else {
      try (BufferedReader in = new BufferedReader(new InputStreamReader(connection.getErrorStream(), CHARSET))) {
        String errorResponse = readAllLines(in);
        throw new IOException("HTTP error code: " + responseCode + ", error response: " + errorResponse);
      }
    }
  }

  private static String readAllLines(BufferedReader reader) throws IOException {
    StringBuilder response = new StringBuilder();
    String line;
    while ((line = reader.readLine()) != null) {
      response.append(line.trim());
    }
    return response.toString();
  }

  private void extractAnswerFromResponse() {
    JSONObject jsonResponse = new JSONObject(wholeResponse);
    JSONArray choices = jsonResponse.getJSONArray("choices");
    JSONObject firstChoice = choices.getJSONObject(0);
    JSONObject message = firstChoice.getJSONObject("message");
    answer = message.getString("content").trim();
  }
}

