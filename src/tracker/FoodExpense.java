package tracker;

import java.util.Date;

public class FoodExpense extends Expense {
    private String restaurant;

    public FoodExpense(double amount, Date date, String description, String restaurant) {
        super(amount, date, description);
        this.restaurant = restaurant;
    }

    @Override
    public String getType() {
        return "Food";
    }

    public String getRestaurant() {
        return restaurant;
    }
}
