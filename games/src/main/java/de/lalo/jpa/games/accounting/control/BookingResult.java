package de.lalo.jpa.games.accounting.control;

/**
 * @author llorenzen
 * @since 12.01.18
 */
public class BookingResult {

    private long balance;

    // success of something else
    private String state;

    public BookingResult(long balance, String state) {
        this.balance = balance;
        this.state = state;
    }

    public boolean isSuccess() {
        return "success".equals(state);
    }

    public long getBalance() {
        return balance;
    }

    public static BookingResult failed(long balance) {
        return new BookingResult(balance, "failed");
    }

    public static BookingResult sucess(long balance) {
        return new BookingResult(balance, "success");
    }
}
