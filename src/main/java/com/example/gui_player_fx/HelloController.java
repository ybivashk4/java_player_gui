package com.example.gui_player_fx;

import javafx.beans.binding.Bindings;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.media.MediaPlayer;
import javafx.scene.media.Media;
import javafx.util.Duration;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class HelloController{

    String currentPath = new java.io.File(".").getCanonicalPath();
    String path;
    Media media;
    MediaPlayer player;
    albom all_songs;
    protected int count_song;
    List<Button> buttonlist = new ArrayList<>();

    @FXML
    private Label welcomeText;
    @FXML
    private TextField myTextField;
    @FXML
    private ToolBar songs_collection;
    @FXML
    private ToolBar albums_collection;
    @FXML
    private ProgressBar myProgressBar;

    public HelloController() throws IOException {
        String currentPath = new java.io.File(".").getCanonicalPath();
        all_songs = new albom("all_songs");

        count_song = 0;
    }

    // code for PlayButton
    @FXML
    protected void PlayButton() throws IOException {
        if (count_song != 0) {
            player.play();
            all_songs.get_cur_song().out("Play", welcomeText);
        }


    }
    @FXML
    protected void StopButton() throws IOException {
        if (count_song != 0) {
            player.pause();
            all_songs.get_cur_song().out("Pause", welcomeText);
        }
    }
    @FXML
    protected void NextSongButton() throws IOException {
        if (count_song != 0) {
            player.stop();
            song next = all_songs.next_song();

            media = new Media("file://" + currentPath + "/music/" +  next.get_path());
            player = new MediaPlayer(media);
            bind_progress_bar();
            player.play();
            all_songs.get_cur_song().out("Play", welcomeText);
        }
    }
    @FXML
    protected void PrevSongButton() throws IOException {
        if (count_song != 0) {
            player.stop();
            song next = all_songs.previous_song();

            media = new Media("file://" + currentPath + "/music/" +  next.get_path());
            player = new MediaPlayer(media);
            bind_progress_bar();
            player.play();
            all_songs.get_cur_song().out("Play", welcomeText);
        }
    }

    @FXML
    protected void LoadSong() throws IOException {

        try {
            song loaded_song = new song(myTextField.getText());
            System.out.println(loaded_song.get_path());
            media = new Media("file://" + currentPath + "/music/" + loaded_song.get_path());
            player = new MediaPlayer(media);
            Button button = new Button(loaded_song.get_name());
            button.setId("" + count_song); // button id - index in buttonList
            button.setOnAction(e -> {
                all_songs.set_cur_song(Integer.parseInt(button.getId()));
                System.out.println(Integer.parseInt(button.getId()));
                player.pause();
                player.stop();
                media = new Media("file://" + currentPath + "/music/" + all_songs.get_cur_song().get_path());
                player = new MediaPlayer(media);
                bind_progress_bar();
                player.play();
                all_songs.get_cur_song().out("Play", welcomeText);
            });
            bind_progress_bar();
            count_song++;
            all_songs.add_song(loaded_song);
            songs_collection.getItems().add(button);

        }
        catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }
    private void bind_progress_bar() {
        var binding = Bindings.createDoubleBinding(
                () -> {
                    var currentTime = player.getCurrentTime();
                    var duration = player.getMedia().getDuration();
                    return currentTime.toMillis() / duration.toMillis();
                },
                player.currentTimeProperty(),
                player.getMedia().durationProperty());
        myProgressBar.progressProperty().bind(binding);
    }
}
