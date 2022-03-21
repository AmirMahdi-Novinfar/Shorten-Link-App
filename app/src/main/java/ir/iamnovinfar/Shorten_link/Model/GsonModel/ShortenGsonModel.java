package ir.iamnovinfar.Shorten_link.Model.GsonModel;

import com.google.gson.annotations.SerializedName;


public class ShortenGsonModel {


    @SerializedName("status")
    private String status;
    @SerializedName("shortenedUrl")
    private String shortenedUrl;


    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getShortenedUrl() {
        return shortenedUrl;
    }

    public void setShortenedUrl(String shortenedUrl) {
        this.shortenedUrl = shortenedUrl;
    }
}


