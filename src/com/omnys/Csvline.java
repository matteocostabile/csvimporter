package com.omnys;

class Csvline{
    private String name;
    private String surname;
    private String email;

    public Csvline(String name, String surname, String email) {
        this.name = name;
        this.surname =  surname;
        this.email = email;
    }

    @Override
    public String toString() {
        return name + "," + surname + "," + email ;
    }
}
