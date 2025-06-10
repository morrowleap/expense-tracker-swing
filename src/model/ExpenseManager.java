package model;

import db.ExpenseDAO;
import db.DatabaseConnectionException;
import java.sql.SQLException;
import java.util.ArrayList;

public class ExpenseManager {
    private ExpenseDAO expenseDAO;

    public ExpenseManager() throws DatabaseConnectionException {
        this.expenseDAO = new ExpenseDAO();
    }

    public void addExpense(Expense exp) throws SQLException {
        expenseDAO.insertExpense(exp);
    }

    public ArrayList<Expense> getExpenses() {
        return expenseDAO.getAllExpenses();
    }

    public double getTotalExpenses() {
        return expenseDAO.getTotalAmount();
    }
}
