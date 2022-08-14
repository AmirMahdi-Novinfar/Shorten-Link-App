package ir.iamnovinfar.Shorten_link.Model.GsonModel;


import com.google.gson.annotations.SerializedName;



public class ShortenGsonModel {


    @SerializedName("status")
    private String status;
    @SerializedName("link")
    private LinkDTO link;


    public static class LinkDTO {
        @SerializedName("id")
        private Integer id;
        @SerializedName("user_id")
        private Integer userId;
        @SerializedName("original_url")
        private String originalUrl;
        @SerializedName("short_url")
        private String shortUrl;
        @SerializedName("view_count")
        private Integer viewCount;
        @SerializedName("approved")
        private Integer approved;
        @SerializedName("created_at")
        private String createdAt;
        @SerializedName("updated_at")
        private String updatedAt;

        public Integer getId() {
            return id;
        }

        public void setId(Integer id) {
            this.id = id;
        }

        public Integer getUserId() {
            return userId;
        }

        public void setUserId(Integer userId) {
            this.userId = userId;
        }

        public String getOriginalUrl() {
            return originalUrl;
        }

        public void setOriginalUrl(String originalUrl) {
            this.originalUrl = originalUrl;
        }

        public String getShortUrl() {
            return shortUrl;
        }

        public void setShortUrl(String shortUrl) {
            this.shortUrl = shortUrl;
        }

        public Integer getViewCount() {
            return viewCount;
        }

        public void setViewCount(Integer viewCount) {
            this.viewCount = viewCount;
        }

        public Integer getApproved() {
            return approved;
        }

        public void setApproved(Integer approved) {
            this.approved = approved;
        }

        public String getCreatedAt() {
            return createdAt;
        }

        public void setCreatedAt(String createdAt) {
            this.createdAt = createdAt;
        }

        public String getUpdatedAt() {
            return updatedAt;
        }

        public void setUpdatedAt(String updatedAt) {
            this.updatedAt = updatedAt;
        }
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public LinkDTO getLink() {
        return link;
    }

    public void setLink(LinkDTO link) {
        this.link = link;
    }
}


