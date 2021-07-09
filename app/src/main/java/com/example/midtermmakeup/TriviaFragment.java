package com.example.midtermmakeup;

import android.content.Context;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class TriviaFragment extends Fragment {

    ITrivia am;

    Button logout;

    RecyclerView recyclerView;

    ArrayList<Trivia> trivias = new ArrayList<>();

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        if(context instanceof ITrivia){
            am = (ITrivia) context;
        }else{
            throw new RuntimeException(context.toString());
        }
    }

    interface ITrivia{

        User getUser();

        void sendLoginFragment();

        void sendTriviaIdRequest(MainActivity.APIResponse response, String id, String token);

        void setUser(@Nullable User user);

        void sendTriviaInfoFragment(Trivia trivia);

        void sendTriviaRequest(MainActivity.APIResponse response, String token);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_trivia, container, false);
        getActivity().setTitle(R.string.trivia);
        logout = view.findViewById(R.id.button);
        logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                am.setUser(null);
                am.sendLoginFragment();
            }
        });
        recyclerView = view.findViewById(R.id.trivia_list);
        recyclerView.setHasFixedSize(true);
        LinearLayoutManager llm = new LinearLayoutManager(getContext());
        recyclerView.setLayoutManager(llm);
        DividerItemDecoration dividerItemDecoration = new DividerItemDecoration(recyclerView.getContext(),
                llm.getOrientation());
        recyclerView.addItemDecoration(dividerItemDecoration);
        am.sendTriviaRequest(new MainActivity.APIResponse() {
            @Override
            public void onResponse(@NotNull JSONObject jsonObject) {
                try{
                    trivias.clear();
                    JSONArray jsonArray = jsonObject.getJSONArray("trivias");
                    for (int i = 0; i < jsonArray.length(); i++) {
                        trivias.add(new Trivia(jsonArray.getJSONObject(i)));
                    }

                }catch (JSONException e){
                    e.printStackTrace();
                }
                recyclerView.setAdapter(new RecyclerView.Adapter() {
                    @NonNull
                    @Override
                    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
                        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.simple_trivia_list, parent, false);
                        return new RecyclerView.ViewHolder(view){};
                    }

                    @Override
                    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
                        TextView title = holder.itemView.findViewById(R.id.text1);
                        TextView desc = holder.itemView.findViewById(R.id.text3);
                        Trivia trivia = trivias.get(position);
                        title.setText(trivia.getTitle());
                        desc.setText(trivia.getDescription());
                        holder.itemView.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                am.sendTriviaIdRequest(new MainActivity.APIResponse() {
                                    @Override
                                    public void onResponse(@NotNull JSONObject jsonObject) {
                                        try{
                                            trivia.setQuestions(jsonObject);
                                            am.sendTriviaInfoFragment(trivia);
                                        }catch (JSONException e){
                                            e.printStackTrace();
                                        }

                                    }
                                    @Override
                                    public void onError(@NotNull JSONObject jsonObject) {
                                    }
                                }, trivia.getTrivia_id(), am.getUser().getToken());

                            }
                        });
                    }

                    @Override
                    public int getItemCount() {
                        return trivias.size();
                    }
                });
            }

            @Override
            public void onError(@NotNull JSONObject jsonObject) {
            }
        }, am.getUser().getToken());

        return view;
    }
}