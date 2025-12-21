package org.player.controller.qa.dto;

import java.util.Objects;

public class PlayerUpdateResponseDto {

    private Long id;
    private Integer age;
    private String gender;
    private String login;
    private String role;
    private String screenName;

    public PlayerUpdateResponseDto() {
    }

    public Integer getAge() {
        return age;
    }

    public void setAge(Integer age) {
        this.age = age;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getLogin() {
        return login;
    }

    public void setLogin(String login) {
        this.login = login;
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
    public String toString() {
        return "PlayerUpdateResponseDto{" +
                "age=" + age +
                ", gender='" + gender + '\'' +
                ", id=" + id +
                ", login='" + login + '\'' +
                ", role='" + role + '\'' +
                ", screenName='" + screenName + '\'' +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        PlayerUpdateResponseDto that = (PlayerUpdateResponseDto) o;
        return Objects.equals(id, that.id) && Objects.equals(age, that.age) && Objects.equals(gender, that.gender) && Objects.equals(login, that.login) && Objects.equals(role, that.role) && Objects.equals(screenName, that.screenName);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, age, gender, login, role, screenName);
    }
}
