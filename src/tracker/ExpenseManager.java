package tracker;

import java.util.ArrayList;

public class ExpenseManager {
    private ExpenseDAO expenseDAO;

    public ExpenseManager() {
        this.expenseDAO = new ExpenseDAO();
    }

    public void addExpense(Expense exp) {
        expenseDAO.insertExpense(exp);
    }

    public ArrayList<Expense> getExpenses() {
        return expenseDAO.getAllExpenses();
    }

    public double getTotalExpenses() {
        double total = 0;
        for (Expense exp : getExpenses()) {
            total += exp.getAmount();
        }
        return total;
    }
}
