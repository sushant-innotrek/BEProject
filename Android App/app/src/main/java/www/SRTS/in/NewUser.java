package www.SRTS.in;

/**
 * Created by Harshit on 24-Feb-18.
 */

public class NewUser {
    private String Name;
    private String Mobile;
    private String Email;
    private double WalletBalance;


    public String getEmail() {
        return Email;
    }

    public void setEmail(String email) {
        Email = email;
    }


    public double getWalletBalance() {
        return WalletBalance;
    }

    public void setWalletBalance(double walletBalance) {
        WalletBalance = walletBalance;
    }

    public String getMobile() {
        return Mobile;
    }

    public void setMobile(String mobile) {
        Mobile = mobile;
    }

    public String getName() {

        return Name;
    }

    public void setName(String name) {
        Name = name;
    }
}
