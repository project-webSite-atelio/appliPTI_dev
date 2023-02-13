package com.ateliopti.lapplicationpti;

import android.app.Application;
import android.media.MediaPlayer;

// Unique MediaPlayer pour Fonctions
public final class MediaPlayerFonctions extends Application {
        static MediaPlayer instance;
        public static MediaPlayer getInstance() {

            if (instance == null) {
                instance = new MediaPlayer();
            }

            return instance;
        }
    }