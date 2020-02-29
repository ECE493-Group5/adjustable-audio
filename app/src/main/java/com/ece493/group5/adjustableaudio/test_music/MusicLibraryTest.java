package com.ece493.group5.adjustableaudio.test_music;

import android.content.ContentResolver;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.MediaMetadata;
import android.media.browse.MediaBrowser;
import android.provider.MediaStore;

import com.ece493.group5.adjustableaudio.BuildConfig;
import com.ece493.group5.adjustableaudio.R;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.TreeMap;
import java.util.concurrent.TimeUnit;

public class MusicLibraryTest {

    private static final TreeMap<String, MediaMetadata> music = new TreeMap<>();
    private static final HashMap<String, Integer> albumRes = new HashMap<>();
    private static final HashMap<String, String> musicFilenames = new HashMap<>();

    static {
        createMediaMetadata ("Blinding_Lights", "Blinding Lights", "The Weeknd",
                "Blinding Lights - Single", "R&B/Soul", 202, TimeUnit.SECONDS,
                "01 Blinding Lights.mp3", R.drawable.ic_pause_grey_24dp, "blindingLights");
    }

    public static String getMusicFilename(String mediaId)
    {
        return musicFilenames.containsKey(mediaId) ? musicFilenames.get(mediaId) : null;
    }

    private static int getAlbumRes(String mediaId)
    {
        return albumRes.containsKey(mediaId) ? albumRes.get(mediaId) : 0;
    }

    public static Bitmap getAlbumBitmap(Context context, String mediaId)
    {
        return BitmapFactory.decodeResource(context.getResources(),
                MusicLibraryTest.getAlbumRes(mediaId));
    }

    public static List<MediaBrowser.MediaItem> getMediaItems()
    {
        List<MediaBrowser.MediaItem> result = new ArrayList<>();
        for (MediaMetadata metadata : music.values()) {
            result.add(
                    new MediaBrowser.MediaItem(
                            metadata.getDescription(), MediaBrowser.MediaItem.FLAG_PLAYABLE));
        }
        return result;
    }

    private static String getAlbumArtUri(String albumArtResName)
    {
        return ContentResolver.SCHEME_ANDROID_RESOURCE + "://" +
                BuildConfig.APPLICATION_ID + "/drawable/" + albumArtResName;
    }


    public static MediaMetadata getMetadata(Context context, String mediaId)
    {
        MediaMetadata metadataWithoutBitmap = music.get(mediaId);
        Bitmap albumArt = getAlbumBitmap(context, mediaId);

        // Since MediaMetadataCompat is immutable, we need to create a copy to set the album art.
        // We don't set it initially on all items so that they don't take unnecessary memory.
        MediaMetadata.Builder builder = new MediaMetadata.Builder();
        for (String key :
                new String[]{
                        MediaMetadata.METADATA_KEY_MEDIA_ID,
                        MediaMetadata.METADATA_KEY_ALBUM,
                        MediaMetadata.METADATA_KEY_ARTIST,
                        MediaMetadata.METADATA_KEY_GENRE,
                        MediaMetadata.METADATA_KEY_TITLE
                }) {
            builder.putString(key, metadataWithoutBitmap.getString(key));
        }
        builder.putLong(
                MediaMetadata.METADATA_KEY_DURATION,
                metadataWithoutBitmap.getLong(MediaMetadata.METADATA_KEY_DURATION));
        builder.putBitmap(MediaMetadata.METADATA_KEY_ALBUM_ART, albumArt);
        return builder.build();
    }


    private static void createMediaMetadata (String mediaId, String title, String artist,
                                             String album, String genre, long duration,
                                             TimeUnit durationUnit, String musicFilename, int albumArtId,
                                             String albumArtResName)
    {
        music.put(
                mediaId,
                new MediaMetadata.Builder()
                        .putString(MediaMetadata.METADATA_KEY_MEDIA_ID, mediaId)
                        .putString(MediaMetadata.METADATA_KEY_ALBUM, album)
                        .putString(MediaMetadata.METADATA_KEY_ARTIST, artist)
                        .putLong(MediaMetadata.METADATA_KEY_DURATION,
                                TimeUnit.MILLISECONDS.convert(duration, durationUnit))
                        .putString(MediaMetadata.METADATA_KEY_GENRE, genre)
                        .putString(
                                MediaMetadata.METADATA_KEY_ALBUM_ART_URI,
                                getAlbumArtUri(albumArtResName))
                        .putString(
                                MediaMetadata.METADATA_KEY_DISPLAY_ICON_URI,
                                getAlbumArtUri(albumArtResName))
                        .putString(MediaMetadata.METADATA_KEY_TITLE, title)
                        .build());

        musicFilenames.put(mediaId, musicFilename);
    }
}
