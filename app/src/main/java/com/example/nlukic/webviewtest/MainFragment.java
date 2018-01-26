package com.example.nlukic.webviewtest;

import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

public class MainFragment extends Fragment {

    private String textContent;

    public MainFragment() {
    }

    public MainFragment(String text) {
        this.textContent = text;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.content_main, container, false);


        TextView textView = (TextView) view.findViewById(R.id.textina);

        textView.setText(this.textContent);

        return view;
    }
}