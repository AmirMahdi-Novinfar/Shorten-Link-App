package com.example.myapplication;

public class ShortlinkDataModel {



    String domain,long_url;

    public ShortlinkDataModel(String domain, String long_url) {
        this.domain = domain;
        this.long_url = long_url;
    }

    public String getDomain() {
        return domain;
    }

    public void setDomain(String domain) {
        this.domain = domain;
    }

    public String getLong_url() {
        return long_url;
    }

    public void setLong_url(String long_url) {
        this.long_url = long_url;
    }
}
