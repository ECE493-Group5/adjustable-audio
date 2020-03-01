package com.ece493.group5.adjustableaudio.adapters;

import android.media.MediaMetadata;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.ece493.group5.adjustableaudio.R;

import java.util.ArrayList;
import java.util.List;

public class MediaMetadataAdapter extends RecyclerView.Adapter<MediaMetadataAdapter.ViewHolder>
{
    private List<MediaMetadata> metadataList;

    public static class ViewHolder extends RecyclerView.ViewHolder
    {
        public TextView title;

        public ViewHolder(View view)
        {
            super(view);
            title = view.findViewById(R.id.mediaMetaDataTitle);
        }
    }

    public MediaMetadataAdapter(List<MediaMetadata> metadataList)
    {
        this.metadataList = metadataList;
    }

    @Override
    public MediaMetadataAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType)
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
        CharSequence title = metadataList.get(position).getDescription().getTitle();
        holder.title.setText(title);
    }

    @Override
    public int getItemCount()
    {
        return metadataList.size();
    }
}
