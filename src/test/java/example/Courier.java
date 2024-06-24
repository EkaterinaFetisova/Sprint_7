package example;
public class Courier {
    private String login;
    private String password;
    private String firstName;
    boolean isInApp;

    public Courier() {
        this.login = "Petrov123";
        this.password = "111111";
        this.firstName = "Petr";
        this.isInApp = false;
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

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public boolean isInApp() {
        return isInApp;
    }

    public void setInApp(boolean inApp) {
        isInApp = inApp;
    }
}
