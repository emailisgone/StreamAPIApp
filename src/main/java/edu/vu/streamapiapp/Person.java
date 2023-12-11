package edu.vu.streamapiapp;

public class Person {
    private int id;
    private String firstName;
    private String lastName;
    private String email;
    private String gender;
    private String country;
    private String domain;
    private String birthDate;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public String getDomain() {
        return domain;
    }

    public void setDomain(String domain) {
        this.domain = domain;
    }

    public String getBirthDate() {
        return birthDate;
    }

    public void setBirthDate(String birthDate) {
        this.birthDate = birthDate;
    }

    Person(int id, String firstName, String lastName, String email, String gender, String country, String domain, String birthDate){
        setId(id);
        setFirstName(firstName);
        setLastName(lastName);
        setEmail(email);
        setGender(gender);
        setCountry(country);
        setDomain(domain);
        setBirthDate(birthDate);
    }
}
