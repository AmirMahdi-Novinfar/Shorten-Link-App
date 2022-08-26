package ir.iamnovinfar.Shorten_link.Model.GsonModel.GetLinks;

import com.google.gson.annotations.SerializedName;

import java.util.List;


public class GetLinksGsonModel {


    @SerializedName("userLinks")
    private List<UserLinksDTO> userLinks;

    public List<UserLinksDTO> getUserLinks() {
        return userLinks;
    }

    public void setUserLinks(List<UserLinksDTO> userLinks) {
        this.userLinks = userLinks;
    }

    public static class UserLinksDTO {
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
}
