package com.example.gui_player_fx;

import java.io.File;
import java.io.IOException;
import java.nio.file.DirectoryStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

public class player{
    private ArrayList<albom> alboms;
    private int cur_albom;
    private ArrayList<song> songs;
    public player(albom all_songs) {
        alboms = new ArrayList<>();
        alboms.add(all_songs);
        cur_albom = 0;
    }
    public void set_cur_albom(int i) { 
        if (i >= alboms.size())
            System.out.println("Wrong num");
        else 
            cur_albom = i;
        
    }
    public void set_cur_albom(albom _albom) { 
        cur_albom = alboms.indexOf(_albom);
        if (cur_albom == -1) {
            throw new IllegalArgumentException("unexpected error in set_cur_albom(albom _albom) in class" + this.getClass().getName());
        }
    }
    public int get_size () {return alboms.size();}
    public albom get_cur_albom() {
        if (alboms.size() == 0) return null;
        return alboms.get(cur_albom);
    }

    public void remove_albom(int index) { 
        if (index >= 0 && index < alboms.size() ) {
            alboms.remove(index);
            if (cur_albom == alboms.size()) cur_albom--;
        }
        else {
            System.out.println("can't remove unreal album");
        }
    }
    public void remove_albom(String name) {
        for (int i=0;i<alboms.size();i++) {
            if (alboms.get(i).get_name().equals(name)) {
                alboms.remove(i);
                if (cur_albom == alboms.size()) cur_albom--;
                break;
            }
        }
    }
    public void add_albom(albom _albom) {
        alboms.add(_albom);
        song [] _songs = _albom.get_songs();
        for (int i=0;i<_songs.length;i++) {
            songs.add(_songs[i]);
        }
    }
    public void add_albom(albom _albom, int index) {
        if (index >= alboms.size() || index < 0) index = alboms.size()-1;
        alboms.add(index, _albom);
        song [] _songs = _albom.get_songs();
        for (int i=0;i<_songs.length;i++) {
            songs.add(_songs[i]);
        }
    }
    public void add_albom(String name) {
        alboms.add(new albom(name));
        song [] _songs = alboms.get(alboms.size()-1).get_songs();
        for (int i=0;i<_songs.length;i++) {
            songs.add(_songs[i]);
        }
    }
    
    public void out_alboms() {
        for (int i=0;i<alboms.size();i++) {
            alboms.get(i).out();
        }
    }
    public void out_songs() {
        song[] songs;
        for (int i=0;i<alboms.size();i++) {
            songs = alboms.get(i).get_songs();
            for (int j=0;j<songs.length;j++) {
                songs[j].out();
            }
        }
    }
    public albom[] get_alboms() {
        albom[] _alboms = new albom[alboms.size()];
        alboms.toArray(_alboms);
        return _alboms;

    }
    public song[] get_songs() {
        song[] _songs = new song[songs.size()];
        songs.toArray(_songs);
        return _songs;
    }

    public song get_song(String name) {
        for (int i=0;i<songs.size();i++) {
            if (songs.get(i).get_name().equals(name)) {
                return songs.get(i);
            }
        }
        return null;
    }

    public song get_song(int ind) {
        if (ind > songs.size() || ind <= 0) { return null; }
        ind--;
        return songs.get(ind);
    }

    public song[] get_songs(String artist) {
        ArrayList<song> _songs = new ArrayList<>();
        song[] songs_res;
        for (int i=0;i<songs.size();i++) {
            if (songs.get(i).get_artist() == artist) {
                _songs.add(songs.get(i));
            }
        }
        if (_songs.size() == 0) return null;
        songs_res = new song[_songs.size()];
        _songs.toArray(songs_res);
        return songs_res;
    }
    public albom get_albom(String name) {
        for (int i=0;i<alboms.size();i++) {
            if (alboms.get(i).get_name().equals(name)) {
                return alboms.get(i);
            }
        }
        return null;
    }
    public albom get_albom(int ind) {
        if (ind > alboms.size() || ind <= 0) { return null; }
        ind--;
        return alboms.get(ind);
    }
    public void play() {
        if (alboms.size() > 0)
            alboms.get(cur_albom).get_cur_song().out();
        else 
            System.out.println("no songs in albom");
    }
    // не работает
    public void load_albom(String path) throws IOException{
        String currentPath = "";
        try {
            currentPath = new File(".").getCanonicalPath();
        }
        catch (IOException e) {
            System.out.println(e.getMessage() + ": error in " + e.getClass());
        }
        String absPath = currentPath + "/" + path + "/";
        Path newFilePath = Paths.get(absPath);
        if (!Files.isDirectory(newFilePath)) {
            System.out.println("Wrong path: " + absPath + " in " + this.getClass());
            return;
        }
        String temp [] = newFilePath.normalize().toString().replace("\\", "/").split("/");
        alboms.add(new albom(temp[temp.length-1]));
        cur_albom = alboms.size()-1;

        List<Path> files = new ArrayList<>();
        DirectoryStream<Path> stream;
        try {
            stream = Files.newDirectoryStream(newFilePath);
        }
        catch(IOException e) {
            System.out.println("Unexcepted error1 in: " + this.getClass() + "\n" + e.getMessage());
            return;
        }

        for (Path entry : stream) {
          files.add(entry);
        }

        try {
            stream.close();
        }
        catch(IOException e) {
            System.out.println("Unexcepted error2 in: " + this.getClass() + "\n" + e.getMessage());
            return;
        }
        for (Path file : files) {
            alboms.get(alboms.size()-1).add_song(new song(file.toString()));
        }
   }
}
