package com.softdesign.devintensive.pojo;

/**
 * Created by savos on 08.07.2016.
 */

public class UserInfo {

    public UserInfo(){};
    public UserInfo(boolean success, Data data) {
        this.success = success;
        this.data = data;
    }
    private boolean success;

    public boolean isSuccess() {
        return success;
    }

    public Data getData() {
        return data;
    }

    private Data data;

    public class Data{
        public Data(){};

        public Data(User user, String token) {
            this.user = user;
            this.token = token;
        }

        private User user;

        public User getUser() {
            return user;
        }

        public String getToken() {
            return token;
        }

        private String token;
    }

    public class User{
        public User(){};

        public User(String _id, String first_name, String second_name, String __v,
                    Repositories repositories, Contact contacts, ProfileValues profileValues,
                    PublicInfo publicInfo, String specialization, String role, String updated) {
            this._id = _id;
            this.first_name = first_name;
            this.second_name = second_name;
            this.__v = __v;
            this.repositories = repositories;
            this.contacts = contacts;
            this.profileValues = profileValues;
            this.publicInfo = publicInfo;
            this.specialization = specialization;
            this.role = role;
            this.updated = updated;
        }

        public String get_id() {
            return _id;
        }

        public String getFirst_name() {
            return first_name;
        }

        public String getSecond_name() {
            return second_name;
        }

        public String get__v() {
            return __v;
        }

        public Repositories getRepositories() {
            return repositories;
        }

        public Contact getContacts() {
            return contacts;
        }

        public ProfileValues getProfileValues() {
            return profileValues;
        }

        public PublicInfo getPublicInfo() {
            return publicInfo;
        }

        private String _id;
        private String first_name;
        private String second_name;
        private String __v;
        private Repositories repositories;
        private Contact contacts;
        private ProfileValues profileValues;
        private PublicInfo publicInfo;
        private String specialization;
        private String role;
        private String updated;

    }

    public class Repositories{

        public Repositories(){};
        public Repositories(Repo[] repo, String updated) {
            this.repo = repo;
            this.updated = updated;
        }

        public Repo[] getRepo() {
            return repo;
        }

        public String getUpdated() {
            return updated;
        }

        private Repo repo[];
        private String updated;
    }

    public class Repo{

        public Repo(){};
        public Repo(String _id, String git, String title) {
            this._id = _id;
            this.git = git;
            this.title = title;
        }

        public void set_id(String _id) {
            this._id = _id;
        }

        public void setGit(String git) {
            this.git = git;
        }

        public void setTitle(String title) {
            this.title = title;
        }

        private String _id;
        private String git;
        private String title;
    }

    public class Contact{
        public Contact(){};

        public Contact(String vk, String phone, String email, String updated) {
            this.vk = vk;
            this.phone = phone;
            this.email = email;
            this.updated = updated;
        }

        public String getVk() {
            return vk;
        }

        public String getPhone() {
            return phone;
        }

        public String getEmail() {
            return email;
        }

        private String vk;
        private String phone;
        private String email;
        private String updated;

    }

    public class ProfileValues{
        public ProfileValues(){};

        public ProfileValues(int homeTask, int projects, int linesCode, int rait, String updated) {
            this.homeTask = homeTask;
            this.projects = projects;
            this.linesCode = linesCode;
            this.rait = rait;
            this.updated = updated;
        }

        public int getHomeTask() {
            return homeTask;
        }

        public int getProjects() {
            return projects;
        }

        public int getLinesCode() {
            return linesCode;
        }

        public int getRait() {
            return rait;
        }

        private int homeTask;
        private int projects;
        private int linesCode;
        private int rait;
        private String updated;
    }

    public class PublicInfo{
        public PublicInfo(){};
        public PublicInfo(String avatar, String bio, String photo, String updated) {
            this.avatar = avatar;
            this.bio = bio;
            this.photo = photo;
            this.updated = updated;
        }

        private String bio;

        public String getBio() {
            return bio;
        }

        public String getPhoto() {
            return photo;
        }

        public String getAvatar() {
            return avatar;
        }

        private String avatar;
        private String photo;
        private String updated;
    }




}
