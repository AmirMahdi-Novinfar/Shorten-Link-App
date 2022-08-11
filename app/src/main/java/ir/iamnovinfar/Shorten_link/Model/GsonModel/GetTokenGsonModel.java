package ir.iamnovinfar.Shorten_link.Model.GsonModel;

import com.google.gson.annotations.SerializedName;


public class GetTokenGsonModel {


    @SerializedName("api_key")
    private String apiKey;
    @SerializedName("user")
    private UserDTO user;


    public static class UserDTO {
        @SerializedName("id")
        private Integer id;
        @SerializedName("phone_number")
        private String phoneNumber;
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

        public String getPhoneNumber() {
            return phoneNumber;
        }

        public void setPhoneNumber(String phoneNumber) {
            this.phoneNumber = phoneNumber;
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

    public String getApiKey() {
        return apiKey;
    }

    public void setApiKey(String apiKey) {
        this.apiKey = apiKey;
    }

    public UserDTO getUser() {
        return user;
    }

    public void setUser(UserDTO user) {
        this.user = user;
    }
}
