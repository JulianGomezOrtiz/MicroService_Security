package com.ucaldas.mssecurity.Models;

public class Permission {
    private String _id;
    private String url;
    private String method;

    public Permission(String _id, String url, String method) {
        this._id = _id;
        this.url = url;
        this.method = method;
    }

    public String get_id() {
        return _id;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getMethod() {
        return method;
    }

    public void setMethod(String method) {
        this.method = method;
    }
}
