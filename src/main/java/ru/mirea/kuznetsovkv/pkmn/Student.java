package ru.mirea.kuznetsovkv.pkmn;

import java.io.Serializable;

public class Student implements Serializable {
    @Override
    public String toString() {
        return lastName + " / " + firstName + " / " + middleName + " / " + group;
    }
    private String lastName;
    private String firstName;
    private String middleName;
    private String group;
    public static final long serialVersionUID = 1L;

    public Student(String lastName, String firstName, String middleName, String group) {
        this.lastName = lastName;
        this.firstName = firstName;
        this.middleName = middleName;
        this.group = group;
    }

    public String getLastName() {
        return lastName;
    }
    public String getFirstName() {
        return firstName;
    }
    public String getMiddleName() {
        return middleName;
    }
    public String getGroup() {
        return group;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }
    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }
    public void setMiddleName(String middleName) {
        this.middleName = middleName;
    }
    public void setGroup(String group) {
        this.group = group;
    }
}
