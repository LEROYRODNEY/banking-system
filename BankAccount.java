import java.io.Serializable;

class BankAccount implements Serializable {
    private String accountHolderName;
    private double balance;

    public BankAccount(String name) {
        this.accountHolderName = name;
        this.balance = 0.0;
    }

    public void deposit(double amount) {
        balance += amount;
    }

    public void withdraw(double amount) {
        if (amount <= balance) {
            balance -= amount;
        } else {
            throw new IllegalArgumentException("Insufficient funds");
        }
    }

    public double getBalance() {
        return balance;
    }
}

