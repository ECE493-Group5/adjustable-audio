package com.ece493.group5.adjustableaudio.ui.media_player;

import android.Manifest;
import android.app.Activity;
import android.content.ComponentName;
import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.drawable.Drawable;
import android.media.MediaDescription;
import android.media.MediaMetadata;
import android.media.browse.MediaBrowser;
import android.media.session.MediaController;
import android.media.session.MediaSession;
import android.media.session.PlaybackState;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.ece493.group5.adjustableaudio.R;
import com.ece493.group5.adjustableaudio.adapters.MediaQueueAdapter;
import com.ece493.group5.adjustableaudio.listeners.MediaFragmentListener;
import com.ece493.group5.adjustableaudio.models.Song;
import com.ece493.group5.adjustableaudio.services.MusicService;

import java.io.File;
import java.util.List;

import static android.app.Activity.RESULT_OK;

public class MediaPlayerFragment extends Fragment
{
    private static final String TAG = MediaPlayerFragment.class.getSimpleName();
    private static final int REQUEST_CODE_AUDIO_FILE = 0;
    private static final int REQUEST_CODE_PERMISSIONS = 1;
    private static final String ARG_MEDIA_ID = "media_id";

    private MediaPlayerViewModel mediaPlayerViewModel;
    private MediaQueueAdapter mediaQueueAdapter;
    private MediaBrowser mediaBrowser;
    private MediaController mediaController;
    private MediaFragmentListener mediaFragmentListener;

    private TextView songTitle;
    private TextView artistTitle;
    private ImageButton skipPreviousButton;
    private ImageButton rewindButton;
    private ImageButton playPauseButton;
    private ImageButton fastForwardButton;
    private ImageButton skipNextButton;
    private TextView songTitleLabel;
    private TextView songArtistLabel;
    private TextView mediaTimeLabel;
    private RecyclerView recyclerView;
    private ImageButton addMediaButton;

    private String mediaId;

    public int songIndex = -1;

    private final MediaController.Callback controllerCallback = new MediaController.Callback()
    {
        @Override
        public void onPlaybackStateChanged(@Nullable PlaybackState state)
        {
            super.onPlaybackStateChanged(state);

            if (state.getState() == PlaybackState.STATE_SKIPPING_TO_NEXT)
            {
                songIndex = (songIndex + 1) % mediaPlayerViewModel.getQueue().getValue().size();
            }

            mediaPlayerViewModel.setState(state);
        }

        @Override
        public void onMetadataChanged(@Nullable MediaMetadata metadata)
        {
            super.onMetadataChanged(metadata);
            mediaPlayerViewModel.setMetadata(metadata);
        }
    };


    private MediaBrowser.SubscriptionCallback subscriptionCallback = new MediaBrowser.SubscriptionCallback() {
        @Override
        public void onChildrenLoaded(@NonNull String parentId, @NonNull List<MediaBrowser.MediaItem> children) {
            super.onChildrenLoaded(parentId, children);

        }

        @Override
        public void onError(@NonNull String parentId) {
            super.onError(parentId);
        }
    };


    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState)
    {
        View root = inflater.inflate(R.layout.fragment_media_player, container, false);

        mediaPlayerViewModel =
                ViewModelProviders.of(this).get(MediaPlayerViewModel.class);

        skipPreviousButton = root.findViewById(R.id.skipPrevButton);
        rewindButton = root.findViewById(R.id.fastRewindButton);
        playPauseButton = root.findViewById(R.id.playButton);
        fastForwardButton = root.findViewById(R.id.fastForwardButton);
        skipNextButton = root.findViewById(R.id.skipForwardButton);
        songTitleLabel = root.findViewById(R.id.labelSongTitle);
        songArtistLabel = root.findViewById(R.id.labelArtist);
        mediaTimeLabel = root.findViewById(R.id.mediaTime);
        addMediaButton = root.findViewById(R.id.addMediaButton);

        recyclerView = root.findViewById(R.id.mediaQueueRecyclerView);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

        mediaPlayerViewModel.getQueue().observe(this, new Observer<List<Song>>()
        {
            @Override
            public void onChanged(@Nullable List<Song> queue)
            {
                Log.d("MediaPlayerFragment", "Queue Size Changed to: " + queue.size());
                if (recyclerView.getAdapter() == null)
                {
                    mediaQueueAdapter = new MediaQueueAdapter(queue);
                    recyclerView.setAdapter(mediaQueueAdapter);
                    return;
                }

                mediaQueueAdapter.notifyDataSetChanged();
            }
        });

        mediaPlayerViewModel.getState().observe(this, new Observer<PlaybackState>()
        {
            @Override
            public void onChanged(@Nullable PlaybackState state)
            {
                Log.d("MediaPlayerFragment", "PlaybackState changed.");
                Log.d(TAG,  Integer.toString(state.getState()));

                if (state != null && state.getState() == PlaybackState.STATE_PLAYING)
                    showPauseButton();
                else
                    showPlayButton();
            }
        });

        mediaPlayerViewModel.getMetadata().observe(this, new Observer<MediaMetadata>()
        {
            @Override
            public void onChanged(@Nullable MediaMetadata metadata)
            {
                if (metadata == null)
                    return;

                MediaDescription mediaDescription = metadata.getDescription();
                songArtistLabel.setText(mediaDescription.getTitle());
                songTitleLabel.setText(mediaDescription.getSubtitle());

                Log.d("MediaPlayer", "" + mediaDescription.getSubtitle());
                Log.d("MediaPlayer", "" + mediaDescription.getDescription());
            }
        });

        addMediaButton.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                Log.d(TAG,"ADD BUTTON IS PRESSED");
                Intent requestAudioIntent = new Intent(Intent.ACTION_PICK,
                        android.provider.MediaStore.Audio.Media.EXTERNAL_CONTENT_URI);
                startActivityForResult(requestAudioIntent, REQUEST_CODE_AUDIO_FILE);
            }
        });

        checkAndRequestPermissions();

        return root;
    }


    @Override
    public void onStart()
    {
        super.onStart();

        Log.d(TAG, "on start");
        if(!mediaPlayerViewModel.getQueue().getValue().isEmpty())
        {
            enableMediaControls();
        }

        MediaBrowser mediaBrowser = this.mediaFragmentListener.getMediaBrowser();

        if (mediaBrowser.isConnected())
        {
            onConnected();
        }
    }


    @Override
    public void onStop()
    {
        super.onStop();

        MediaBrowser mediaBrowser = this.mediaFragmentListener.getMediaBrowser();

        if(mediaBrowser != null && mediaBrowser.isConnected() && mediaId != null)
        {
            mediaBrowser.unsubscribe(mediaId);
        }

        if(getActivity().getMediaController() != null)
        {
            getActivity().getMediaController().unregisterCallback(controllerCallback);
        }
    }


    @Override
    public void onAttach(Context context)
    {
        super.onAttach(context);
        this.mediaFragmentListener = (MediaFragmentListener) context;
    }


    @Override
    public void onDetach() {
        super.onDetach();
        this.mediaFragmentListener = null;
    }


    private boolean checkAndRequestPermissions()
    {
        if (!hasPermission(Manifest.permission.READ_EXTERNAL_STORAGE) || !hasPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE))
        {
            String[] permissionsToRequest = {
                    Manifest.permission.READ_EXTERNAL_STORAGE,
                    Manifest.permission.WRITE_EXTERNAL_STORAGE
            };
            requestPermissions(permissionsToRequest, REQUEST_CODE_PERMISSIONS);

            return false;
        }

        return true;
    }


    protected boolean hasPermission(String permission)
    {
        return ContextCompat.checkSelfPermission(this.getContext(), permission) == PackageManager.PERMISSION_GRANTED;
    }


    @Override
    public void onRequestPermissionsResult(int requestCode, String permissions[], int[] grantResults)
    {
        /* TODO: Handle when request is not granted */
    }


    public void onConnected()
    {
        if (isDetached())
        {
            return;
        }

        mediaId = this.mediaFragmentListener.getMediaBrowser().getRoot();;

        this.mediaFragmentListener.getMediaBrowser().unsubscribe(mediaId);

        this.mediaFragmentListener.getMediaBrowser().subscribe(mediaId, subscriptionCallback);

        enableMediaControls();

        if(getActivity().getMediaController() != null)
        {
            getActivity().getMediaController().registerCallback(controllerCallback);
        }
    }


    public void showPauseButton()
    {
        Drawable drawable
                = getResources().getDrawable(R.drawable.ic_pause_grey_24dp, null);

        playPauseButton.setImageDrawable(drawable);
    }


    public void showPlayButton()
    {
        Drawable drawable
                = getResources().getDrawable(R.drawable.ic_play_arrow_grey_24dp, null);

        playPauseButton.setImageDrawable(drawable);
    }


    public void enableMediaControls()
    {
        skipPreviousButton.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                Log.d("MediaPlayerFragment", "SkipPrevButton is pressed");
                songIndex = (songIndex - 1) % mediaPlayerViewModel.getQueue().getValue().size();
                MediaController mediaController = getActivity().getMediaController();
                mediaController.getTransportControls().skipToPrevious();
            }
        });

        rewindButton.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                Log.d("MediaPlayerFragment", "RewindButton is pressed");
                MediaController mediaController = getActivity().getMediaController();
                mediaController.getTransportControls().rewind();
            }
        });

        playPauseButton.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                Log.d("MediaPlayerFragment", "PlayButton is pressed");
                MediaController mediaController = getActivity().getMediaController();
                PlaybackState state = mediaController.getPlaybackState();

                if (state == null || mediaPlayerViewModel.getQueue().getValue().isEmpty())
                    return;

                if (state.getState() == PlaybackState.STATE_PLAYING)
                {
                    mediaController.getTransportControls().pause();
                }
                else {
                    Log.d(TAG, "Current song playing");
//                    mediaController.getTransportControls().playFromMediaId(mediaPlayerViewModel.getQueue().getValue().get(songIndex).getFilename(), null);
//                    mediaController.getTransportControls().playFromMediaId(Integer.toString(songIndex), null);
                    mediaController.getTransportControls().play();
                }
            }
        });

        fastForwardButton.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                Log.d("MediaPlayerFragment", "FastForwardButton is pressed");
                MediaController mediaController = getActivity().getMediaController();
                mediaController.getTransportControls().fastForward();
            }
        });

        skipNextButton.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                Log.d("MediaPlayerFragment", "Skip Next Button is pressed");
                songIndex = (songIndex + 1) % mediaPlayerViewModel.getQueue().getValue().size();
//                mediaController.getTransportControls().playFromMediaId(mediaPlayerViewModel.getQueue().getValue().get(songIndex).getFilename(), null);
                MediaController mediaController = getActivity().getMediaController();
                mediaController.getTransportControls().skipToNext();
            }
        });
    }

    public void disableMediaControls()
    {
        skipPreviousButton.setOnClickListener(null);
        rewindButton.setOnClickListener(null);
        playPauseButton.setOnClickListener(null);
        fastForwardButton.setOnClickListener(null);
        skipNextButton.setOnClickListener(null);
        addMediaButton.setOnClickListener(null);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data)
    {
        switch (requestCode)
        {
            case REQUEST_CODE_AUDIO_FILE:
                onAudioFileResult(resultCode, data);
                break;
        }

        super.onActivityResult(requestCode, resultCode, data);
    }

    protected void onAudioFileResult(int resultCode, Intent data)
    {
        if (resultCode != RESULT_OK)
            return;

        Uri uri = data.getData();

        Cursor cursor = getActivity()
                .getContentResolver()
                .query(uri, null, MusicService.SELECTION, null, null);
        cursor.moveToFirst();

        Song song = new Song();
        song.setTitle(cursor.getString(cursor.getColumnIndex(MediaStore.Audio.Media.TITLE)));
        song.setAlbum(cursor.getString(cursor.getColumnIndex(MediaStore.Audio.Media.ALBUM)));
        song.setArtist(cursor.getString(cursor.getColumnIndex(MediaStore.Audio.Media.ARTIST)));
        song.setFilename(cursor.getString(cursor.getColumnIndex(MediaStore.Audio.Media.DATA)));
        song.setMediaId(song.getFilename().replace(' ', '_'));

        if (songIndex < 0 && mediaPlayerViewModel.getQueue().getValue().size() == 0)
        {
            songIndex = 0;
        }
        mediaPlayerViewModel.enqueue(song);
        Bundle songBundle = new Bundle();
        songBundle.putString("MEDIA_FILE_NAME", song.getFilename());

        MediaController mediaController = getActivity().getMediaController();
        mediaController.getTransportControls().sendCustomAction("ADD", songBundle);
    }
}