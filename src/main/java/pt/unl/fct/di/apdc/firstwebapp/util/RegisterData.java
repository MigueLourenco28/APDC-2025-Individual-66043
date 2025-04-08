package pt.unl.fct.di.apdc.firstwebapp.util;

public class RegisterData {

    public String id;
    public String password;
    public String confirmation;
    public String email;
    public String full_name;
    public String phone;
    public String profile;


    public RegisterData() {

    }

    public RegisterData(String email, String id, String full_name, String phone, String password, String confirmation, String profile) {
        this.id = id;
        this.email = email;
        this.password = password;
        this.confirmation = confirmation;
        this.full_name = full_name;
        this.phone = phone;
        this.profile = profile;
    }

    private boolean nonEmptyOrBlankField(String field) {
        return field != null && !field.isBlank();
    }

    public boolean validRegistration() {


        return nonEmptyOrBlankField(email) &&
                nonEmptyOrBlankField(id) &&
                nonEmptyOrBlankField(full_name) &&
                nonEmptyOrBlankField(phone) &&
                nonEmptyOrBlankField(password) &&
                nonEmptyOrBlankField(profile) &&
                email.contains("@") &&
                password.equals(confirmation) &&
                (profile.equals("private") || profile.equals("public"));
    }
}