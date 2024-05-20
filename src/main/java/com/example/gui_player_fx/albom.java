package com.example.gui_player_fx;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;

import java.io.File;
import java.io.IOException;
import java.nio.file.*;
import java.util.ArrayList;

public class albom {
    private ArrayList<song> songs;
    private int cur_song;
    private final String name;
    // create file with paths to songs
    public albom (String name_playlist) {
        name = name_playlist;
        songs = new ArrayList<>();
        cur_song = 0;
    }
    public void loadAlbum(TextField songTextField, Button loadSongButton) throws IOException {
        String currentPath = "";
        String name_of_directory = "/" + get_name() + "/";
        try {
            currentPath = new File(".").getCanonicalPath();
        }
        catch (IOException e) {
            System.out.println(e.getMessage() + ": error in " + e.getClass());
        }
        Path theDir = Paths.get(currentPath + "/music/" + name);
        try {
            Files.createDirectories(theDir);
        }
        catch (IOException e) {
            System.out.println(e.getMessage() + ": error create directory in " + e.getClass());
        }
        songs.clear();
        File[] listOfFiles = theDir.toFile().listFiles();
        try {
            for (File file : listOfFiles) {

                if (file.isFile()) {
                    songTextField.setText(file.getName());
                    System.out.println(file.getName());
                    loadSongButton.fire();
                }
            }
        }
        catch (NullPointerException e) {
            System.out.println(e.getMessage() + ": error in " + e.getClass());
        }
    }
    // надо бы сделать (load)
    /*
    public albom (String name_playlist, String Path) {
        ;
    }
    */
    public song set_cur_song(int i) {
        cur_song = i;
        if (i >= songs.size()) {
            System.out.println("Wrong num");
            return null;
        }
        else return songs.get(i);
    }

    public song get_cur_song() {
        if (songs.isEmpty()) return null;
        return songs.get(cur_song);
    }

    public song next_song() {
        if (cur_song + 1 < songs.size()) cur_song++;
        else cur_song = 0;
        if (songs.isEmpty()) return null;
        return songs.get(cur_song);
    }
    public song previous_song() {
        if (cur_song - 1 >= 0) cur_song--;
        else cur_song = songs.size()-1;
        if (songs.isEmpty()) return null;
        System.out.println(songs.size());
        return songs.get(cur_song);
    }
    public String get_name() {return name;}

    public void out() {
        System.out.println(name);
        for (int i=0;i<songs.size();i++) {
            songs.get(i).out();
        }
    }

    public void remove_song(int index) {
        index--;
        if (index >= 0 && index < songs.size() ) {
            songs.remove(index);
            if (cur_song == songs.size()) cur_song--;
        }
        else {
            System.out.println("can't remove unreal song");
        }
    }
    public void remove_song(String name) {
        for (int i=0;i<songs.size();i++) {
            if (songs.get(i).get_name().equals(name)) {
                songs.remove(i);
                if (cur_song == songs.size()) cur_song--;
                break;
            }
        }
    }
    public void remove_song(song song_) {
        for (int i=0;i<songs.size();i++) {
            if (songs.get(i).equals(song_)) {
                songs.remove(i);
                if (cur_song == songs.size()) cur_song--;
                break;
            }
        }
    }
    public void add_song(String path, int index) throws IOException{
        if (index > songs.size()) index = songs.size();
        songs.add(index, new song(path));
    }

    public void add_song(String path) throws IOException{
        songs.add(new song(path));
    }
    public void add_song(song _song) {
        if (_song != null && _song.get_name() != null)  songs.add(_song);
        else System.out.println("song is unreal");
    }
    public song[] get_songs() {
        song[] _songs = new song[songs.size()];
        songs.toArray(_songs);
        return _songs;

    }
    // didn't work
    public void save_albom() throws IOException{
        String currentPath = "";
        String name_of_directory = "/" + get_name() + "/";
        try {
            currentPath = new File(".").getCanonicalPath();
        }
        catch (IOException e) {
            System.out.println(e.getMessage() + ": error in " + e.getClass());
        }
        Path theDir = Paths.get(currentPath + "/music/" + name);
        try {
            Files.createDirectories(theDir);
        }
        catch (IOException e) {
            System.out.println(e.getMessage() + ": error create directory in " + e.getClass());
        }

        for (int i=0;i<songs.size();i++) {
            songs.get(i).save_song(name_of_directory);
        }
    }
    public int get_count_songs() {
        return songs.size();
    }
}
// equals, hashcode