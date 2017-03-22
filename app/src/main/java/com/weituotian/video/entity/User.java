package com.weituotian.video.entity;

import java.io.Serializable;
import java.util.List;

/**
 * 系统用户
 * Created by ange on 2017/1/28.
 */
public class User implements Serializable {

    private int id;
    private String loginname;
    private String password;
    private String email;
    private String name;
    private String avatar;
    private Integer usertype;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getLoginname() {
        return loginname;
    }

    public void setLoginname(String loginname) {
        this.loginname = loginname;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getAvatar() {
        return avatar;
    }

    public void setAvatar(String avatar) {
        this.avatar = avatar;
    }

    public Integer getUsertype() {
        return usertype;
    }

    public void setUsertype(Integer usertype) {
        this.usertype = usertype;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        User user = (User) o;

        if (id != user.id) return false;
        if (loginname != null ? !loginname.equals(user.loginname) : user.loginname != null) return false;
        if (password != null ? !password.equals(user.password) : user.password != null) return false;
        if (email != null ? !email.equals(user.email) : user.email != null) return false;
        if (name != null ? !name.equals(user.name) : user.name != null) return false;
        if (usertype != null ? !usertype.equals(user.usertype) : user.usertype != null) return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = id;
        result = 31 * result + (loginname != null ? loginname.hashCode() : 0);
        result = 31 * result + (password != null ? password.hashCode() : 0);
        result = 31 * result + (email != null ? email.hashCode() : 0);
        result = 31 * result + (name != null ? name.hashCode() : 0);
        result = 31 * result + (usertype != null ? usertype.hashCode() : 0);
        return result;
    }

    @Override
    public String toString() {
        return "User{" +
                "id=" + id +
                ", loginname='" + loginname + '\'' +
                ", password='" + password + '\'' +
                ", email='" + email + '\'' +
                ", name='" + name + '\'' +
                ", usertype=" + usertype +
                '}';
    }
}
