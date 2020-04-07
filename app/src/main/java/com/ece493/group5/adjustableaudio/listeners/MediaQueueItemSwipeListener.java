package com.ece493.group5.adjustableaudio.listeners;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.RecyclerView;

/**
 * The MediaQueueItemSwipeListener class helps implement the following requirement:
 *
 * #SRS: Media Player
 *
 * In particular, the MediaQueueItemSwipeListener helps implement the media queue.
 */

public abstract class MediaQueueItemSwipeListener extends ItemTouchHelper.SimpleCallback {
    public MediaQueueItemSwipeListener()
    {
        super(0, ItemTouchHelper.LEFT | ItemTouchHelper.RIGHT);
    }

    @Override
    public boolean onMove(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder, @NonNull RecyclerView.ViewHolder target)
    {
        return false;
    }

    @Override
    public void onSwiped(RecyclerView.ViewHolder viewHolder, int direction)
    {
        onSwiped(viewHolder.getAdapterPosition());
    }

    public abstract void onSwiped(int position);
}