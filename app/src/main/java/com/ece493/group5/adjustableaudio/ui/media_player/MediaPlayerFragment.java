package com.ece493.group5.adjustableaudio.ui.media_player;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;

import com.ece493.group5.adjustableaudio.R;

public class MediaPlayerFragment extends Fragment {

    private MediaPlayerViewModel mediaPlayerViewModel;

    private ImageView albumArt;
    private ImageButton skipPreviousButton;
    private ImageButton rewindButton;
    private ImageButton playButton;
    private ImageButton fastForwardButton;
    private ImageButton skipNextButton;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        mediaPlayerViewModel =
                ViewModelProviders.of(this).get(MediaPlayerViewModel.class);
        View root = inflater.inflate(R.layout.fragment_media_player, container, false);

        albumArt = (ImageView) root.findViewById(R.id.albumArt);
        skipPreviousButton = (ImageButton) root.findViewById(R.id.skipPrevButton);
        rewindButton = (ImageButton) root.findViewById(R.id.fastRewindButton);
        playButton = (ImageButton) root.findViewById(R.id.playButton);
        fastForwardButton = (ImageButton) root.findViewById(R.id.fastForwardButton);
        skipNextButton = (ImageButton) root.findViewById(R.id.skipForwardButton);

        skipPreviousButton.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                Log.d("MediaPlayerFragment", "SkipPrevButton is pressed");
            }
        });

        rewindButton.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                Log.d("MediaPlayerFragment", "RewindButton is pressed");
            }
        });

        playButton.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                Log.d("MediaPlayerFragment", "PlayButton is pressed");
            }
        });

        fastForwardButton.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                Log.d("MediaPlayerFragment", "FastForwardButton is pressed");
            }
        });

        skipNextButton.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                Log.d("MediaPlayerFragment", "Skip Next Button is pressed");
            }
        });

//        final TextView textView = root.findViewById(R.id.text_media_player);
//        mediaPlayerViewModel.getText().observe(this, new Observer<String>() {
//            @Override
//            public void onChanged(@Nullable String s) {
//                textView.setText(s);
//            }
//        });
        return root;
    }


}