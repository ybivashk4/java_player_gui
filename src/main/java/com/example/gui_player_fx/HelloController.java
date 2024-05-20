package com.example.gui_player_fx;

import javafx.beans.binding.Bindings;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.media.MediaPlayer;
import javafx.scene.media.Media;

import java.io.IOException;


public class HelloController{

    String currentPath = new java.io.File(".").getCanonicalPath();
    Media media;
    MediaPlayer player_;
    albom cur_album;
    String albom_name;
    player alboms;
    protected int count_song;
    protected int count_album;
    @FXML
    private Label welcomeText;
    @FXML
    private TextField songTextField;
    @FXML
    private TextField albomTextField;
    @FXML
    private ToolBar songs_collection;
    @FXML
    private ToolBar albums_collection;
    @FXML
    private ProgressBar myProgressBar;
    @FXML
    protected Button loadSongButton;
    @FXML
    protected Button deleteSongButton;

    public HelloController() throws IOException {
        cur_album = new albom("all_songs");
        alboms = new player(cur_album);
        albom_name = "//";
        count_song = 0;
        count_album = 0;
        Button button = new Button("all_songs");
        button.setId("" + count_album);
        count_album++;
    }

    // code for PlayButton
    @FXML
    protected void PlayButton(){
        if (count_song != 0) {
            player_.play();
            cur_album.get_cur_song().out("Play", welcomeText);
        }


    }
    @FXML
    protected void StopButton() {
        if (count_song != 0) {
            player_.pause();
            cur_album.get_cur_song().out("Pause", welcomeText);
        }
    }
    @FXML
    protected void NextSongButton(){
        if (count_song != 0) {
            player_.stop();
            song next = cur_album.next_song();

            media = new Media("file://" + currentPath + "/music/" + albom_name + "/" + next.get_path());
            player_ = new MediaPlayer(media);
            bind_progress_bar();
            player_.play();
            cur_album.get_cur_song().out("Play", welcomeText);
        }
    }
    @FXML
    protected void PrevSongButton() {
        if (count_song != 0) {
            player_.stop();
            song next = cur_album.previous_song();

            media = new Media("file://" + currentPath + "/music/" + albom_name + "/" + next.get_path());
            player_ = new MediaPlayer(media);
            bind_progress_bar();
            player_.play();
            cur_album.get_cur_song().out("Play", welcomeText);
        }
    }

    @FXML
    protected void LoadSong()  {

        try {
            song loaded_song = new song(songTextField.getText());
            media = new Media("file://" + currentPath + "/music/" + loaded_song.get_path());
            player_ = new MediaPlayer(media);
            Button button = new Button(loaded_song.get_name());
            button.setId("" + count_song); // button id - index in buttonList
            loaded_song.save_song(albom_name);
            button.setOnAction(e -> {
                cur_album.set_cur_song(Integer.parseInt(button.getId()));
                System.out.println(Integer.parseInt(button.getId()));
                player_.pause();
                player_.stop();
                media = new Media("file://" + currentPath + "/music/" + albom_name + "/" + cur_album.get_cur_song().get_path());
                player_ = new MediaPlayer(media);
                bind_progress_bar();
                player_.play();
                cur_album.get_cur_song().out("Play", welcomeText);
            });
            bind_progress_bar();
            count_song++;
            cur_album.add_song(loaded_song);
            songs_collection.getItems().add(button);

        }
        catch (Exception e) {

            System.out.println(e.getMessage());
        }
    }
    private void bind_progress_bar() {
        if (player_ != null) {
            var binding = Bindings.createDoubleBinding(
                    () -> {
                        if (player_ != null) {
                            var currentTime = player_.getCurrentTime();
                            var duration = player_.getMedia().getDuration();
                            return currentTime.toMillis() / duration.toMillis();
                        }
                        return new Double(0);
                    },
                    player_.currentTimeProperty(),
                    player_.getMedia().durationProperty());
            myProgressBar.progressProperty().bind(binding);
        }

    }
    @FXML
    private void saveAlbum() throws IOException {
        Button button = new Button(albomTextField.getText());
        albom loaded_albom = new albom(albomTextField.getText());
        button.setId("" + count_album);
        count_album++;
        alboms.add_albom(loaded_albom);

        albums_collection.getItems().add(button);
        albom_name = albomTextField.getText();
        System.out.println(albom_name + " saved");
        System.out.println(count_album + ", " + alboms.get_alboms().length);
        button.setOnAction(e -> {

            alboms.set_cur_albom(Integer.parseInt(button.getId()));

            System.out.println(Integer.parseInt(button.getId()));
            if (player_ != null) {
                player_.pause();
                player_.stop();
            }
            alboms.set_cur_albom(Integer.parseInt(button.getId()));
            bind_progress_bar();

            albom_name = "/" + alboms.get_cur_albom().get_name() + "/";
            cur_album = alboms.get_cur_albom();
            count_song = cur_album.get_count_songs();
            songs_collection.getItems().removeAll(songs_collection.getItems());
            songs_collection.getItems().add(new Label("songs"));
            for (int i=0;i<count_song;i++) {
                cur_album.set_cur_song(i);
                song loaded_song = cur_album.get_cur_song();
                Button button2 = new Button(loaded_song.get_name());
                button2.setId("" + i); // button id - index in buttonList
                button2.setOnAction(ev -> {
                    cur_album.set_cur_song(Integer.parseInt(button2.getId()));
                    if (player_ != null) {
                        player_.pause();
                        player_.stop();
                    }
                    media = new Media("file://" + currentPath + "/music/" + cur_album.get_cur_song().get_path());
                    player_ = new MediaPlayer(media);
                    bind_progress_bar();
                    player_.play();
                    cur_album.get_cur_song().out("Play", welcomeText);
                    try {
                        cur_album.get_cur_song().save_song(cur_album.get_name());
                    }
                    catch (IOException ioe) {
                        System.out.println("Error in " + getClass() + "\nError is " + ioe.getMessage());
                    }
                });

                bind_progress_bar();
            }
            try {
                alboms.get_cur_albom().loadAlbum(songTextField, loadSongButton);
                repair_song_buttons(cur_album);
            }
            catch (Exception except) {
                System.out.println(except.getMessage());
            }
        });
    }
    @FXML
    private void deleteSong() {
        if (cur_album!= null && cur_album.get_cur_song() != null) {
            if (player_ != null) {
                player_.pause();
                player_.stop();
                media = null;
                player_ = null;
            }
            for (Object button : songs_collection.getItems().toArray()) {
                if (button instanceof Button && ((Button) button).getText().equals(cur_album.get_cur_song().get_name())) {
                    songs_collection.getItems().remove((Button) button);
                }
            }
            cur_album.remove_song(cur_album.get_cur_song());
            repair_song_buttons(cur_album);
            count_song--;
            if (cur_album.get_cur_song() == null) return;
            media = new Media("file://" + currentPath + "/music/" + albom_name +"/"+ cur_album.get_cur_song().get_path());
            player_ = new MediaPlayer(media);
        }
    }

    private void repair_song_buttons(albom cur_albom) {
        if (cur_albom == null || songs_collection == null || songs_collection.getItems() == null) return;
        int i = 0;
        for (song song_ : cur_albom.get_songs()) {
            for (Object button : songs_collection.getItems().toArray()) {
                if (button instanceof Button && ((Button) button).getText().equals(song_.get_name())) {
                    ((Button) button).setId(String.valueOf(i));
                    break;
                }
            }
            i++;
        }
    }
    @FXML
    private void deleteAlbum() {
        if (alboms.get_cur_albom() != null && alboms.get_cur_albom().get_name() != "all_songs") {
            if (player_ != null) {
                player_.pause();
                player_.stop();
                media = null;
                player_ = null;
            }
            songs_collection.getItems().removeAll(songs_collection.getItems());

            for (Object button : albums_collection.getItems().toArray()) {
                if (button instanceof Button && ((Button) button).getText().equals(alboms.get_cur_albom().get_name())) {
                    albums_collection.getItems().remove((Button) button);
                    break;
                }
            }
            alboms.remove_albom(alboms.get_cur_albom().get_name());
            alboms.set_cur_albom(0);

            count_album--;
            cur_album = alboms.get_cur_albom();
            albom_name = "";
            count_song = 0;
            repair_album_buttons(alboms);
        }
    }
    private void repair_album_buttons(player my_player) {
        if (my_player == null || albums_collection == null || albums_collection.getItems() == null) return;
        int i = 0;
        for (albom album : my_player.get_alboms()) {
            if (i == 0) {
                i++;
                continue;
            }
            for (Object button : albums_collection.getItems().toArray()) {
                if (button instanceof Button && ((Button) button).getText().equals(album.get_name())) {
                    ((Button) button).setId(String.valueOf(i));
                    break;
                }
            }
            i++;
        }
    }
}
