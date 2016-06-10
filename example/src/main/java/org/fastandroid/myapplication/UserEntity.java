package org.fastandroid.myapplication;

/**
 * Created by Dong on 2016/5/15.
 */
public class UserEntity {
    private String username;
    private int age;

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public int getAge() {
        return age;
    }

    public void setAge(int age) {
        this.age = age;
    }

    public UserEntity(String username, int age) {
        this.username = username;
        this.age = age;
    }
}
