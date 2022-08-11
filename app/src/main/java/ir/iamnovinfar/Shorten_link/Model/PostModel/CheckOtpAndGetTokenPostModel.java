package ir.iamnovinfar.Shorten_link.Model.PostModel;

public class CheckOtpAndGetTokenPostModel {
   private String phone_number;
   private String token;


    public CheckOtpAndGetTokenPostModel(String phone_number, String token) {
        this.phone_number = phone_number;
        this.token = token;
    }
}
