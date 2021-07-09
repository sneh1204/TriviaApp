package com.example.midtermmakeup;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import okhttp3.ResponseBody;

public class MainActivity extends AppCompatActivity implements TriviaInfoFragment.ITriviaInfo, TriviaFragment.ITrivia, RegisterFragment.RegInterface, LoginFragment.LoginInterface {

    /**
     * Assignment #MidTerm Makeup
     * MainActivity.java
     * Sneh Jain
     */

    public final String BASE_URL = "https://www.theappsdr.com/api/";

    ProgressDialog dialog;

    public final String TAG = "debug";

    private final OkHttpClient client = new OkHttpClient();

    private User user = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        if(user == null) {
            sendLoginFragment();
        }else{
            sendTriviaFragment();
        }

    }

    public User getUser() {
        return user;
    }

    public void setUser(@Nullable User user) {
        this.user = user;
    }

    public void sendLoginFragment(){
        getSupportFragmentManager().beginTransaction()
                .replace(R.id.container_view, new LoginFragment())
                .commit();
    }

    public void sendTriviaFragment(){
        getSupportFragmentManager().beginTransaction()
                .replace(R.id.container_view, new TriviaFragment())
                .commit();
    }

    public void sendTriviaInfoFragment(Trivia trivia){
        getSupportFragmentManager().beginTransaction()
                .replace(R.id.container_view, TriviaInfoFragment.newInstance(trivia))
                .addToBackStack(null)
                .commit();
    }

    public void sendRegisterFragment(){
        getSupportFragmentManager().beginTransaction()
                .replace(R.id.container_view, new RegisterFragment())
                .commit();
    }

    private void toggleDialog(boolean toggle, @Nullable String msg){
        if(toggle){
            dialog = new ProgressDialog(MainActivity.this);
            if(msg != null)
                dialog.setMessage(msg);
            dialog.setCancelable(false);
            dialog.show();

        }else{
            dialog.dismiss();
        }
    }

    public void sendLoginRequest(APIResponse response, String... data){
        FormBody formBody = new FormBody.Builder()
                .add("email", data[0])
                .add("password", data[1])
                .build();
        Request request = new Request.Builder()
                .url(BASE_URL + "login")
                .post(formBody)
                .build();
        sendRequest(request, response);
    }

    public void sendRegRequest(APIResponse response, String... data){
        FormBody formBody = new FormBody.Builder()
                .add("fname", data[0])
                .add("lname", data[1])
                .add("email", data[2])
                .add("password", data[3])
                .build();
        Request request = new Request.Builder()
                .url(BASE_URL + "signup")
                .post(formBody)
                .build();
        sendRequest(request, response);
    }

    public void sendTriviaIdRequest(APIResponse response, String id, String token){
        Request request = new Request.Builder()
                .url(BASE_URL + "trivia/"+id)
                .addHeader("Authorization", "BEARER " + token)
                .build();
        sendRequest(request, response);
    }

    public void goBack(){
        getSupportFragmentManager().popBackStack();
    }

    public void sendTriviaRequest(APIResponse response, String token){
        Request request = new Request.Builder()
                .url(BASE_URL + "trivias")
                .addHeader("Authorization", "BEARER " + token)
                .build();
        sendRequest(request, response);
    }

    public void checkTriviaAnswer(APIResponse response, String token, String... data){
        FormBody formBody = new FormBody.Builder()
                .add("question_id", data[0])
                .add("answer_id", data[1])
                .build();
        Request request = new Request.Builder()
                .url(BASE_URL + "trivia/check-answer")
                .addHeader("Authorization", "BEARER " + token)
                .post(formBody)
                .build();
        sendRequest(request, response);
    }

    public void showAlertDialog(String alert){
        runOnUiThread(() -> {
            new AlertDialog.Builder(this)
                    .setTitle(R.string.title)
                    .setMessage(alert)
                    .setPositiveButton("Okay", null)
                    .show();
        });
    }

    private void sendRequest(Request request, APIResponse callback) {
        toggleDialog(true, getString(R.string.loading));
        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(@NotNull Call call, @NotNull IOException e) {
                e.printStackTrace();
                runOnUiThread(() -> {
                    toggleDialog(false, null);
                    showAlertDialog(e.toString());
                });
            }

            @Override
            public void onResponse(@NotNull Call call, @NotNull Response response) throws IOException {
                runOnUiThread(() -> toggleDialog(false, null));

                ResponseBody responseBody = response.body();
                JSONObject jsonObject;
                if (responseBody != null) {
                    try {
                        jsonObject = new JSONObject(responseBody.string());
                        if(jsonObject.getString("status").equals("ok") || jsonObject.getString("status").equals("success")){
                            runOnUiThread(() -> callback.onResponse(jsonObject));
                        }else{
                            if(jsonObject.has("message")) {
                                showAlertDialog(jsonObject.getString("message"));
                            }
                            runOnUiThread(() -> callback.onError(jsonObject));
                        }
                    }catch (JSONException exception){
                        exception.printStackTrace();
                        return;
                    }
                }
            }
        });
    }

    interface APIResponse{

        void onResponse(@NotNull JSONObject jsonObject);

        void onError(@NotNull JSONObject jsonObject);

    }

}