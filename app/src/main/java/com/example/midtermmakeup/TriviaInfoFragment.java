package com.example.midtermmakeup;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import com.squareup.picasso.Picasso;

import org.jetbrains.annotations.NotNull;
import org.json.JSONException;
import org.json.JSONObject;


public class TriviaInfoFragment extends Fragment {

    private static final String ARG_ID = "trivia";

    private Trivia trivia;

    ITriviaInfo am;

    ImageView image;

    TextView question_info, question;

    ListView answers;

    int cur_ques = 0;

    interface ITriviaInfo{

        User getUser();

        void goBack();

        void showAlertDialog(String error);

        void checkTriviaAnswer(MainActivity.APIResponse response, String token, String... data);

    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        if(context instanceof ITriviaInfo){
            am = (ITriviaInfo) context;
        }else{
            throw new RuntimeException(context.toString());
        }
    }

    public static TriviaInfoFragment newInstance(Trivia trivia) {
        TriviaInfoFragment fragment = new TriviaInfoFragment();
        Bundle args = new Bundle();
        args.putSerializable(ARG_ID, trivia);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            trivia = (Trivia) getArguments().getSerializable(ARG_ID);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_trivia_info, container, false);
        getActivity().setTitle(trivia.getTitle());
        question_info = view.findViewById(R.id.textView);
        question = view.findViewById(R.id.textView2);
        image = view.findViewById(R.id.imageView);
        answers = view.findViewById(R.id.answers);
        updateFrag();
        return view;
    }

    public void updateFrag(){
        TriviaQuestion triviaQuestion = trivia.getTriviaQuestions().get(cur_ques);
        int cur = cur_ques + 1;
        if(trivia.getTriviaQuestions().size() == cur){
            am.showAlertDialog("You have completed this trivia");
            am.goBack();
            return;
        }
        question_info.setText("Question " + cur + " of " + trivia.getTriviaQuestions().size());
        question.setText(triviaQuestion.getText());
        if(triviaQuestion.getUrl() != null){
            Picasso.get().load(triviaQuestion.getUrl()).into(image);
        }else{
            image.setVisibility(View.GONE);
        }
        ArrayAdapter<TriviaAnswer> adapter = new ArrayAdapter<>(getContext(), android.R.layout.simple_list_item_1, android.R.id.text1, triviaQuestion.getTriviaAnswers());
        answers.setAdapter(adapter);
        answers.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                am.checkTriviaAnswer(new MainActivity.APIResponse() {
                    @Override
                    public void onResponse(@NotNull JSONObject jsonObject) {
                        try{
                            am.showAlertDialog(jsonObject.getString("message"));
                            if(jsonObject.getString("isCorrectAnswer").equals("true")){
                                cur_ques = cur_ques + 1;
                                updateFrag();
                            }
                        }catch (JSONException e){
                            e.printStackTrace();
                        }
                    }

                    @Override
                    public void onError(@NotNull JSONObject jsonObject) {
                    }
                }, am.getUser().getToken(), triviaQuestion.getId() + "", triviaQuestion.getTriviaAnswers().get(i).getId());
            }
        });
    }
}