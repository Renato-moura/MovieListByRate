package com.moura.movelistbyrate;

import java.util.Locale;

public class Move {
    public final String title;
    public final String overview;
    public final String iconUrl;

    public Move(String iconUrl, String title, String overview){
        this.title = title;
        this.overview = overview;
        this.iconUrl = String.format(
                Locale.getDefault(),
                "https://image.tmdb.org/t/p/w500/%s",iconUrl
        );
    }
    @Override
    public String toString(){
        return "result{" +
                "title='" + title + '\'' +
                ", overview='" + overview + '\'' +
                ", post_path='" + iconUrl + '\'' +'}';

    }
}
