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

public class RegisterFragment extends Fragment {

    RegInterface am;

    Button sub_btn;
    TextView reg_cancel;
    EditText txt_fname, txt_lname, txt_email, txt_pass;

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        if(context instanceof RegInterface){
            am = (RegInterface) context;
        }else{
            throw new RuntimeException(context.toString());
        }
    }

    interface RegInterface{

        void sendLoginFragment();

        void sendTriviaFragment();

        void sendRegRequest(MainActivity.APIResponse response, String... data);

        void showAlertDialog(String error);

        void setUser(@Nullable User user);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_register, container, false);
        getActivity().setTitle(R.string.register_account);
        txt_fname = view.findViewById(R.id.reg_fname);
        txt_lname = view.findViewById(R.id.reg_lname);
        txt_email = view.findViewById(R.id.reg_email);
        txt_pass = view.findViewById(R.id.reg_password);
        sub_btn = view.findViewById(R.id.button_submit);
        reg_cancel = view.findViewById(R.id.reg_cancel);
        sub_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(txt_fname.getText().toString().isEmpty() || txt_lname.getText().toString().isEmpty() || txt_email.getText().toString().isEmpty() || txt_pass.getText().toString().isEmpty()){
                    am.showAlertDialog(getString(R.string.empty_fields));
                    return;
                }
                am.sendRegRequest(new MainActivity.APIResponse() {
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
                }, txt_fname.getText().toString(), txt_lname.getText().toString(), txt_email.getText().toString(), txt_pass.getText().toString());
            }
        });

        reg_cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                am.sendLoginFragment();
            }
        });

        return view;
    }
}