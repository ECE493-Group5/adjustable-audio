package com.ece493.group5.adjustableaudio.adapters;

import android.media.MediaMetadata;
import android.media.session.MediaSession;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.RecyclerView;

import com.ece493.group5.adjustableaudio.R;
import com.ece493.group5.adjustableaudio.models.Song;

import java.util.List;

public class MediaQueueAdapter extends RecyclerView.Adapter<MediaQueueAdapter.ViewHolder>
{
    private List<MediaSession.QueueItem> queue;
    private int selectedPosition;
    private OnSelectedListener onSelectedListener;

    public interface OnSelectedListener
    {
        void onSelected(int position);
    }

    public static class ViewHolder extends RecyclerView.ViewHolder
    {
        public View parent;
        public TextView title;
        public TextView artist;
        public TextView duration;
        public TextView rank;

        public ViewHolder(View view)
        {
            super(view);

            parent = view;
            title = view.findViewById(R.id.mediaMetaDataTitle);
            artist = view.findViewById(R.id.mediaMetaDataArtist);
            duration = view.findViewById(R.id.mediaMetaDataDuration);
            rank = view.findViewById(R.id.mediaMetaDataRank);
        }
    }

    public MediaQueueAdapter(List<MediaSession.QueueItem> queue)
    {
        this.queue = queue;
        this.selectedPosition = -1;

        notifyDataSetChanged();
    }

    public void setQueue(List<MediaSession.QueueItem> queue)
    {
        if (this.queue != queue)
        {
            this.queue = queue;
            notifyDataSetChanged();
        }
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
    public void onBindViewHolder(ViewHolder holder, final int position)
    {
        if (position == selectedPosition)
            holder.parent.setSelected(true);
        else
            holder.parent.setSelected(false);

        holder.parent.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setSelectedPosition(position);
            }
        });

        Song song = Song.fromQueueItem(queue.get(position));

        holder.title.setText(song.getTitle());
        holder.artist.setText(song.getArtist());
        holder.duration.setText(song.getDurationAsString());
        holder.rank.setText(String.valueOf(position + 1));
    }

    @Override
    public int getItemCount()
    {
        return queue.size();
    }

    public int getSelectedPosition()
    {
        return selectedPosition;
    }

    public void setOnSelectedListener(OnSelectedListener onSelectedListener)
    {
        this.onSelectedListener = onSelectedListener;
    }

    public void setSelectedPosition(int newPosition)
    {
        int oldPosition = selectedPosition;

        if (oldPosition == newPosition)
            return;
        else
            selectedPosition = newPosition;

        if (0 <= oldPosition && oldPosition < queue.size())
            notifyItemChanged(oldPosition);

        if (0 <= newPosition && newPosition < queue.size())
            notifyItemChanged(selectedPosition);

        if (onSelectedListener != null)
            onSelectedListener.onSelected(newPosition);
    }
}
