package ir.iamnovinfar.Shorten_link.Model.PostModel;

public class ShortLinkPostModel {
   private String original_url;
   private String user_id;


    public ShortLinkPostModel(String original_url, String user_id) {
        this.original_url = original_url;
        this.user_id = user_id;
    }
}
