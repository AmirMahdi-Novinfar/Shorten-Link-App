package com.example.myapplication.Model.PostModel;

public class ShortePostModel {

    String urlToShorten;


    public ShortePostModel(String urlToShorten) {
        this.urlToShorten = urlToShorten;
    }


    public String getUrlToShorten() {
        return urlToShorten;
    }

    public void setUrlToShorten(String urlToShorten) {
        this.urlToShorten = urlToShorten;
    }
}
