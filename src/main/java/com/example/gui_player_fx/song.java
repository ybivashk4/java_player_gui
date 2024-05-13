package com.example.gui_player_fx;

import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Scanner;

import static java.nio.file.StandardCopyOption.REPLACE_EXISTING;

public class song {
    private String name;
    private String artist;
    private String path;
    // path - path from music e.g. path = name_of_album/name_of_song/, path = name_of_song
    public song (String path) throws IOException {
        String currentPath = new java.io.File(".").getCanonicalPath();
        File file = new File(currentPath + "/music/"+ path);
        File file2 = new File(currentPath + "/music/"+ path.replace(" ", "-"));
        if (!file2.exists()) {
            boolean success = file.renameTo(file2);
            path = path.replace(" ", "-");
        }
        this.path = path;
        if (path.contains("_")) {
            artist = path.split("_")[0];
            name = path.split("_")[1].replace(".mp3", "").replace(".wav", "");
        }
        else {
            artist = "Неизвестен";
            name = "Без названия";
        }
    }
    public String get_path() {return path;}
    public String get_name() {return name;}
    public String get_artist() {return artist;}
    public void out(String action, Label welcomeText) {
        System.out.println(name + " " + artist);
        welcomeText.setText(action + " : " + artist + " - " + name);
    }
    public void out() {
        System.out.println(name + " " + artist + " " + path);
    }

    // copy song into /name_of_album/
    public void save_song(String path) throws IOException {
        System.out.println(path);
        String currentPath = new java.io.File(".").getCanonicalPath();
        Path targetFile = Paths.get(currentPath + "/music/" + this.path);
        Path newFilePath = Paths.get(currentPath + "/music/" + path + "/" + this.path);
        try {
            Files.copy(targetFile, newFilePath, REPLACE_EXISTING);
        }
        catch (IOException e) {
            System.out.println("Wrong path: " + targetFile + ", " + newFilePath + " in class" + this.getClass());
        }
    }
}
