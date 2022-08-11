package ir.iamnovinfar.Shorten_link.Model.GsonModel;

import com.google.gson.annotations.SerializedName;


public class OtpErrorGsonModel {


    @SerializedName("status")
    private String status;

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}
