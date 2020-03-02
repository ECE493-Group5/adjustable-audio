package com.ece493.group5.adjustableaudio.adapters;

import android.media.MediaMetadata;
import android.media.session.MediaSession;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.ece493.group5.adjustableaudio.R;
import com.ece493.group5.adjustableaudio.models.Song;

import java.util.List;

public class MediaQueueAdapter extends RecyclerView.Adapter<MediaQueueAdapter.ViewHolder>
{
    private List<Song> queue;

    public static class ViewHolder extends RecyclerView.ViewHolder
    {
        public TextView title;

        public ViewHolder(View view)
        {
            super(view);
            title = view.findViewById(R.id.mediaMetaDataTitle);
        }
    }

    public MediaQueueAdapter(List<Song> queue)
    {
        this.queue = queue;
    }

    @Override
    public MediaQueueAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType)
    {
        // create a new view
        View view = LayoutInflater
                .from(parent.getContext())
                .inflate(R.layout.view_media_metadata, parent, false);

        ViewHolder viewHolder = new ViewHolder(view);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position)
    {
        Song song = queue.get(position);

        if (song.getTitle() != null)
            holder.title.setText(song.getTitle());
        else
            holder.title.setText(song.getMediaId());
    }

    @Override
    public int getItemCount()
    {
        return queue.size();
    }
}
