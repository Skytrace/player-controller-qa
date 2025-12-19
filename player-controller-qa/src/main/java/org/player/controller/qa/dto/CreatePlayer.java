package org.player.controller.qa.dto;

import java.util.Objects;

public class CreatePlayer {
    private Long id;
    private String gender;
    private Integer age;
    private String login;
    private String password;
    private String role;
    private String screenName;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public Integer getAge() {
        return age;
    }

    public void setAge(Integer age) {
        this.age = age;
    }

    public String getLogin() {
        return login;
    }

    public void setLogin(String login) {
        this.login = login;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }

    public String getScreenName() {
        return screenName;
    }

    public void setScreenName(String screenName) {
        this.screenName = screenName;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        CreatePlayer userItem = (CreatePlayer) o;
        return Objects.equals(id, userItem.id) &&
                Objects.equals(gender, userItem.gender) &&
                Objects.equals(age, userItem.age) &&
                Objects.equals(login, userItem.login) &&
                Objects.equals(password, userItem.password) &&
                Objects.equals(role, userItem.role) &&
                Objects.equals(screenName, userItem.screenName);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, gender, age, login, password, role, screenName);
    }

    @Override
    public String toString() {
        return "UserItem{" +
                "id=" + id +
                ", gender='" + gender + '\'' +
                ", age=" + age +
                ", login='" + login + '\'' +
                ", password='" + password + '\'' +
                ", role='" + role + '\'' +
                ", screenName='" + screenName + '\'' +
                '}';
    }
}
