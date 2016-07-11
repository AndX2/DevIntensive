package com.softdesign.devintensive.pojo;

/**
 * Created by savos on 08.07.2016.
 */

public class Repo {
    public Repo(String id, String git, String title) {
        this.id = id;
        this.git = git;
        this.title = title;
    }

    private String id;
    private String git;
    private String title;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getGit() {
        return git;
    }

    public void setGit(String git) {
        this.git = git;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }
}
