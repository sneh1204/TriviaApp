package com.example.midtermmakeup;

import android.content.Context;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.json.JSONException;
import org.json.JSONObject;

public class LoginFragment extends Fragment {

    EditText txt_email, txt_pass;
    Button log_btn;
    TextView new_view;
    LoginInterface am;

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        if(context instanceof LoginInterface){
            am = (LoginInterface)context;
        }else{
            throw new RuntimeException(context.toString());
        }
    }

    interface LoginInterface{

        void sendRegisterFragment();

        void sendTriviaFragment();

        void showAlertDialog(String error);

        void setUser(@Nullable User user);

        void sendLoginRequest(MainActivity.APIResponse response, String... data);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_login, container, false);
        getActivity().setTitle(R.string.login);
        txt_email = view.findViewById(R.id.enter_email);
        txt_pass = view.findViewById(R.id.enter_password);
        txt_email.setText("");
        txt_pass.setText("");
        log_btn = view.findViewById(R.id.button_login);
        new_view = view.findViewById(R.id.create_new);
        new_view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                am.sendRegisterFragment();
            }
        });
        log_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(txt_email.getText().toString().isEmpty() || txt_pass.getText().toString().isEmpty()){
                    am.showAlertDialog(getString(R.string.empty_fields));
                    return;
                }
                am.sendLoginRequest(new MainActivity.APIResponse() {
                    @Override
                    public void onResponse(@NotNull JSONObject jsonObject) {
                        try {
                            am.setUser(new User(jsonObject));
                            am.sendTriviaFragment();
                        }catch (JSONException exception){
                            exception.printStackTrace();
                            return;
                        }
                    }

                    @Override
                    public void onError(@NotNull JSONObject jsonObject) {
                    }
                }, txt_email.getText().toString(), txt_pass.getText().toString());
            }
        });
        return view;
    }
}