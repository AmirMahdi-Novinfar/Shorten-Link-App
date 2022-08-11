package ir.iamnovinfar.Shorten_link.Model.GsonModel;

import com.google.gson.annotations.SerializedName;

import java.util.List;


public class LoginErrorGsonModel {


    @SerializedName("phone_number")
    private List<String> phoneNumber;

    public LoginErrorGsonModel(List<String> phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public List<String> getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(List<String> phoneNumber) {
        this.phoneNumber = phoneNumber;
    }
}
