package piggybit.piggybitmongodb;

import java.util.List;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Document(collection = "Users")
public class User {
    @Id
    private String id;
    private String userName;
    private String password;
    private String firstName;
    private String lastName;
    private String email;
    private String bankingApiAccount;
    private String coinbaseAccount;
    private String cryptocurrency;
    private int priceMargin;
    private int intesmentPeriod;
    private List<eventLog> eventLogs;

    protected User() {
    }

    public User(String userName, String password, String firstName, String lastName, String email, String bankingApiAccount, String coinbaseAccount, String cryptocurrency, int priceMargin, int intesmentPeriod, List<eventLog> eventLogs) {
        this.userName = userName;
        this.password = password;
        this.firstName = firstName;
        this.lastName = lastName;
        this.email = email;
        this.bankingApiAccount = bankingApiAccount;
        this.coinbaseAccount = coinbaseAccount;
        this.cryptocurrency = cryptocurrency;
        this.priceMargin = priceMargin;
        this.intesmentPeriod = intesmentPeriod;
        this.eventLogs = eventLogs;
    }

    public User(String userName, String password, String firstName, String lastName, String email) {
        this.userName = userName;
        this.password = password;
        this.firstName = firstName;
        this.lastName = lastName;
        this.email = email;
    }

    public User(String userName, String password) {
        this.userName = userName;
        this.password = password;
    }

    public String getId() {
        return id;
    }

    public String getUserName() {
        return userName;
    }

    public String getPassword() {
        return password;
    }

    public String getFirstName() {
        return firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public String getEmail() {
        return email;
    }

    public String getBankingApiAccount() {
        return bankingApiAccount;
    }

    public String getCoinbaseAccount() {
        return coinbaseAccount;
    }

    public String getCryptocurrency() {
        return cryptocurrency;
    }

    public int getPriceMargin() {
        return priceMargin;
    }

    public int getIntesmentPeriod() {
        return intesmentPeriod;
    }

    public List<eventLog> getEventLogs() {
        return eventLogs;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public void setBankingApiAccount(String bankingApiAccount) {
        this.bankingApiAccount = bankingApiAccount;
    }

    public void setCoinbaseAccount(String coinbaseAccount) {
        this.coinbaseAccount = coinbaseAccount;
    }

    public void setCryptocurrency(String cryptocurrency) {
        this.cryptocurrency = cryptocurrency;
    }

    public void setPriceMargin(int priceMargin) {
        this.priceMargin = priceMargin;
    }

    public void setIntesmentPeriod(int intesmentPeriod) {
        this.intesmentPeriod = intesmentPeriod;
    }

    public void addEventLogs(eventLog eventLogs) {
        this.eventLogs.add(eventLogs);
    }
}

