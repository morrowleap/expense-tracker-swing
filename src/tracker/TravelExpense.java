package tracker;

import java.util.Date;

public class TravelExpense extends Expense {
    private String destination;
    private String transportMode;

    public TravelExpense(double amount, Date date, String description, String destination, String transportMode) {
        super(amount, date, description);
        this.destination = destination;
        this.transportMode = transportMode;
    }

    @Override
    public String getType() {
        return "Travel";
    }

    public String getDestination() {
        return destination;
    }

    public String getTransportMode() {
        return transportMode;
    }
}
