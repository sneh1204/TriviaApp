package com.example.midtermmakeup;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.Serializable;
import java.util.ArrayList;

public class Trivia implements Serializable {

    private String trivia_id;
    private String title;
    private ArrayList<TriviaQuestion> triviaQuestions = new ArrayList<>();


    public Trivia(JSONObject jsonObject) throws JSONException {
        this.trivia_id = jsonObject.getString("trivia_id");
        this.title = jsonObject.getString("title");
        this.description = jsonObject.getString("description");
    }

    public ArrayList<TriviaQuestion> getTriviaQuestions() {
        return triviaQuestions;
    }

    public void setQuestions(JSONObject jsonObject) throws JSONException{
        JSONArray jsonArray = jsonObject.getJSONArray("questions");
        this.triviaQuestions.clear();
        for (int i = 0; i < jsonArray.length(); i++) {
            this.triviaQuestions.add(new TriviaQuestion(jsonArray.getJSONObject(i)));
        }
    }

    public void setTriviaQuestions(ArrayList<TriviaQuestion> triviaQuestions) {
        this.triviaQuestions = triviaQuestions;
    }

    @Override
    public String toString() {
        return "Trivia{" +
                "trivia_id='" + trivia_id + '\'' +
                ", title='" + title + '\'' +
                ", triviaQuestions=" + triviaQuestions +
                ", description='" + description + '\'' +
                '}';
    }

    public String getTrivia_id() {
        return trivia_id;
    }

    public void setTrivia_id(String trivia_id) {
        this.trivia_id = trivia_id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    private String description;

}

class TriviaQuestion implements Serializable{
    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    @Override
    public String toString() {
        return "TriviaQuestion{" +
                "id='" + id + '\'' +
                ", text='" + text + '\'' +
                ", url='" + url + '\'' +
                ", triviaAnswers=" + triviaAnswers +
                '}';
    }

    public TriviaQuestion(JSONObject jsonObject) throws JSONException {
        this.id = jsonObject.getString("question_id");
        this.text = jsonObject.getString("question_text");
        this.url = jsonObject.getString("question_url");
        this.triviaAnswers.clear();
        JSONArray jsonArray = jsonObject.getJSONArray("answers");
        for (int i = 0; i < jsonArray.length(); i++) {
            this.triviaAnswers.add(new TriviaAnswer(jsonArray.getJSONObject(i)));
        }
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public ArrayList<TriviaAnswer> getTriviaAnswers() {
        return triviaAnswers;
    }

    public void setTriviaAnswers(ArrayList<TriviaAnswer> triviaAnswers) {
        this.triviaAnswers = triviaAnswers;
    }

    private String id, text, url = null;
    private ArrayList<TriviaAnswer> triviaAnswers = new ArrayList<>();

}

class TriviaAnswer implements Serializable{

    private String id;

    @Override
    public String toString() {
        return text;
    }

    public TriviaAnswer(JSONObject jsonObject) throws JSONException {
        this.id = jsonObject.getString("answer_id");
        this.text = jsonObject.getString("answer_text");
    }

    private String text;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }
}
