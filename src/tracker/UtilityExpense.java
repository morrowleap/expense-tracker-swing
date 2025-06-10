package tracker;

import java.util.Date;

public class UtilityExpense extends Expense {
    private String utilityType;

    public UtilityExpense(double amount, Date date, String description, String utilityType) {
        super(amount, date, description);
        this.utilityType = utilityType;
    }

    @Override
    public String getType() {
        return "Utility";
    }

    public String getUtilityType() {
        return utilityType;
    }
}
