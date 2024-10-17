package ru.mirea.pkmn;

import java.io.Serializable;

public class Student implements Serializable {
    @Override
    public String toString() {
        return firstName + " / " + surName + " / " + familyName + " / " + group;
    }
    private String firstName;
    private String surName;
    private String familyName;
    private String group;
    public static final long serialVersionUID = 1L;

    public Student(String firstName, String surName, String familyName, String group) {
        this.firstName = firstName;
        this.surName = surName;
        this.familyName = familyName;
        this.group = group;
    }

    public String getFirstName() {
        return firstName;
    }
    public String getSurName() {
        return surName;
    }
    public String getFamilyName() {
        return familyName;
    }
    public String getGroup() {
        return group;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }
    public void setSurName(String surName) {
        this.surName = surName;
    }
    public void setFamilyName(String familyName) {
        this.familyName = familyName;
    }
    public void setGroup(String group) {
        this.group = group;
    }
}
