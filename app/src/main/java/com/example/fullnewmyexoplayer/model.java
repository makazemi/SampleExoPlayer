package com.example.fullnewmyexoplayer;

public class model {

    public  String uri;
    public  String mediaId;
    public  String title;
    private int index_dataSource;

    public model(String  uri, String mediaId, String title) {
        this.uri = uri;
        this.mediaId = mediaId;
        this.title = title;
    }

    public String getUri() {
        return uri;
    }

    public void setUri(String uri) {
        this.uri = uri;
    }

    public String getMediaId() {
        return mediaId;
    }

    public void setMediaId(String mediaId) {
        this.mediaId = mediaId;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public int getIndex_dataSource() {
        return index_dataSource;
    }

    public void setIndex_dataSource(int index_dataSource) {
        this.index_dataSource = index_dataSource;
    }
}
