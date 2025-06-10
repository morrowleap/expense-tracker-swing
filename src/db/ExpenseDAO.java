package db;

import model.*;
import java.sql.*;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

public class ExpenseDAO {
    private Connection connection;

    public ExpenseDAO() throws DatabaseConnectionException {
        connection = DatabaseConnection.getConnection();
    }

    public void insertExpense(Expense exp) throws SQLException {
        String sql = "INSERT INTO expenses (amount, date, description, type, details) VALUES (?, ?, ?, ?, ?)";
        try (PreparedStatement pstmt = connection.prepareStatement(sql)) {
            pstmt.setDouble(1, exp.getAmount());
            String dateStr = new SimpleDateFormat("yyyy-MM-dd").format(exp.getDate());
            pstmt.setString(2, dateStr);
            pstmt.setString(3, exp.getDescription());
            pstmt.setString(4, exp.getType());
            String details = "";
            if (exp instanceof FoodExpense) {
                details = ((FoodExpense) exp).getRestaurant();
            } else if (exp instanceof TravelExpense) {
                TravelExpense t = (TravelExpense) exp;
                details = t.getDestination() + ", " + t.getTransportMode();
            } else if (exp instanceof UtilityExpense) {
                details = ((UtilityExpense) exp).getUtilityType();
            }
            pstmt.setString(5, details);
            pstmt.executeUpdate();
        } catch (SQLException e) {
            System.err.println("Error inserting expense: " + e.getMessage());
            throw e;
        }
    }

    public ArrayList<Expense> getAllExpenses() {
        ArrayList<Expense> expenses = new ArrayList<>();
        String sql = "SELECT * FROM expenses";
        try (Statement stmt = connection.createStatement();
                ResultSet rs = stmt.executeQuery(sql)) {
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
            while (rs.next()) {
                double amount = rs.getDouble("amount");
                String dateStr = rs.getString("date");
                Date date = sdf.parse(dateStr);
                String description = rs.getString("description");
                String type = rs.getString("type");
                String details = rs.getString("details");
                switch (type) {
                    case "Food":
                        expenses.add(new FoodExpense(amount, date, description, details));
                        break;
                    case "Travel":
                        String[] td = details.split(", ");
                        expenses.add(new TravelExpense(amount, date, description, td[0], td[1]));
                        break;
                    case "Utility":
                        expenses.add(new UtilityExpense(amount, date, description, details));
                        break;
                }
            }
        } catch (SQLException e) {
            System.err.println("Error retrieving expenses: " + e.getMessage());
        } catch (ParseException e) {
            System.err.println("Error parsing date: " + e.getMessage());
        }
        return expenses;
    }

    public double getTotalAmount() {
        String sql = "SELECT SUM(amount) FROM expenses";
        try (Statement stmt = connection.createStatement();
                ResultSet rs = stmt.executeQuery(sql)) {
            if (rs.next()) {
                return rs.getDouble(1);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return 0.0;
    }
}
