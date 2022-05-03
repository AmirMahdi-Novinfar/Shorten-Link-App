package ir.iamnovinfar.Shorten_link.Model.GsonModel;


import com.google.gson.annotations.SerializedName;


public class ShortenGsonModel {


    @SerializedName("status")
    private String status;
    @SerializedName("link")
    private String link;
    @SerializedName("short_url")
    private String shortUrl;
    @SerializedName("created_at")
    private String createdAt;

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getLink() {
        return link;
    }

    public void setLink(String link) {
        this.link = link;
    }

    public String getShortUrl() {
        return shortUrl;
    }

    public void setShortUrl(String shortUrl) {
        this.shortUrl = shortUrl;
    }

    public String getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(String createdAt) {
        this.createdAt = createdAt;
    }
}


