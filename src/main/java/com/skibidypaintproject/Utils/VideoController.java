package com.skibidypaintproject.Utils;

import javafx.fxml.FXML;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.scene.media.MediaView;

public class VideoController {

    @FXML
    private MediaView mediaView;

    public void initialize() {

        String videoPath = getClass().getClassLoader().getResource("com/skibidypaintproject/resources/cat2.mp4")
                .toExternalForm();

        if (videoPath == null) {
            System.out.println("No se pudo encontrar el archivo de video en la ruta especificada.");
            return;
        }

        Media media = new Media(videoPath);
        MediaPlayer mediaPlayer = new MediaPlayer(media);

        mediaView.setMediaPlayer(mediaPlayer);
        mediaPlayer.setAutoPlay(true);
    }
}
