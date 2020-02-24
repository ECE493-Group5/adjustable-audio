package com.ece493.group5.adjustableaudio.ui.media_player;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;

import com.ece493.group5.adjustableaudio.R;

public class MediaPlayerFragment extends Fragment {

    private MediaPlayerViewModel mediaPlayerViewModel;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        mediaPlayerViewModel =
                ViewModelProviders.of(this).get(MediaPlayerViewModel.class);
        View root = inflater.inflate(R.layout.fragment_media_player, container, false);
        final TextView textView = root.findViewById(R.id.text_media_player);
        mediaPlayerViewModel.getText().observe(this, new Observer<String>() {
            @Override
            public void onChanged(@Nullable String s) {
                textView.setText(s);
            }
        });
        return root;
    }
}