package gui;

import model.*;
import db.DatabaseConnectionException;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.sql.SQLException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

public class ExpenseTrackerGUI extends JFrame {
    private ExpenseManager manager;
    private JTextField amountField;
    private JTextField descriptionField;
    private JTextField dateField;
    private JComboBox<String> expenseTypeCombo;
    private JTextField restaurantField;
    private JTextField destinationField;
    private JTextField transportField;
    private JTextField utilityTypeField;
    private JButton addExpenseButton;
    private JTable expensesTable;
    private DefaultTableModel tableModel;
    private JLabel totalLabel;

    public ExpenseTrackerGUI() {
        try {
            manager = new ExpenseManager();
        } catch (DatabaseConnectionException ex) {
            JOptionPane.showMessageDialog(this,
                    "Unable to connect to database: " + ex.getMessage(),
                    "Database Error", JOptionPane.ERROR_MESSAGE);
            System.exit(1);
        }
        setTitle("Expense Tracker");
        setSize(800, 600);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        initComponents();
        layoutComponents();
        registerListeners();
        refreshTable(manager.getExpenses());
        updateTotalLabel();
    }

    private void initComponents() {
        amountField = new JTextField(10);
        descriptionField = new JTextField(15);
        dateField = new JTextField(10);
        dateField.setText(new SimpleDateFormat("dd-MM-yyyy").format(new Date()));
        expenseTypeCombo = new JComboBox<>(new String[] { "Food", "Travel", "Utility" });
        restaurantField = new JTextField(15);
        destinationField = new JTextField(15);
        transportField = new JTextField(15);
        utilityTypeField = new JTextField(15);
        addExpenseButton = new JButton("Add Expense");
        tableModel = new DefaultTableModel(new String[] { "Type", "Amount", "Date", "Description", "Details" }, 0);
        expensesTable = new JTable(tableModel);
        totalLabel = new JLabel("Total Expenses: $0.00");
    }

    private void layoutComponents() {
        JPanel inputPanel = new JPanel(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        gbc.gridx = 0;
        gbc.gridy = 0;
        inputPanel.add(new JLabel("Expense Type:"), gbc);
        gbc.gridx = 1;
        inputPanel.add(expenseTypeCombo, gbc);
        gbc.gridx = 2;
        inputPanel.add(new JLabel("Amount:"), gbc);
        gbc.gridx = 3;
        inputPanel.add(amountField, gbc);
        gbc.gridx = 4;
        inputPanel.add(new JLabel("Date (dd-MM-yyyy):"), gbc);
        gbc.gridx = 5;
        inputPanel.add(dateField, gbc);
        gbc.gridx = 0;
        gbc.gridy = 1;
        inputPanel.add(new JLabel("Description:"), gbc);
        gbc.gridx = 1;
        gbc.gridwidth = 2;
        inputPanel.add(descriptionField, gbc);
        gbc.gridwidth = 1;

        gbc.gridx = 0;
        gbc.gridy = 2;
        gbc.gridwidth = 6;
        inputPanel.add(createDynamicFieldsPanel(), gbc);
        gbc.gridwidth = 1;

        gbc.gridx = 0;
        gbc.gridy = 3;
        gbc.gridwidth = 6;
        inputPanel.add(addExpenseButton, gbc);
        gbc.gridwidth = 1;

        JPanel tablePanel = new JPanel(new BorderLayout());
        tablePanel.setBorder(BorderFactory.createTitledBorder("Expenses"));
        tablePanel.add(new JScrollPane(expensesTable), BorderLayout.CENTER);

        JPanel bottomPanel = new JPanel(new BorderLayout());
        bottomPanel.add(totalLabel, BorderLayout.WEST);

        Container cp = getContentPane();
        cp.setLayout(new BorderLayout());
        cp.add(inputPanel, BorderLayout.NORTH);
        cp.add(tablePanel, BorderLayout.CENTER);
        cp.add(bottomPanel, BorderLayout.PAGE_END);
    }

    private JPanel createDynamicFieldsPanel() {
        JPanel dynamicPanel = new JPanel(new CardLayout());
        JPanel foodPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        foodPanel.add(new JLabel("Restaurant:"));
        foodPanel.add(restaurantField);
        JPanel travelPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        travelPanel.add(new JLabel("Destination:"));
        travelPanel.add(destinationField);
        travelPanel.add(new JLabel("Transport Mode:"));
        travelPanel.add(transportField);
        JPanel utilityPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        utilityPanel.add(new JLabel("Utility Type:"));
        utilityPanel.add(utilityTypeField);
        dynamicPanel.add(foodPanel, "Food");
        dynamicPanel.add(travelPanel, "Travel");
        dynamicPanel.add(utilityPanel, "Utility");

        CardLayout cl = (CardLayout) dynamicPanel.getLayout();
        cl.show(dynamicPanel, "Food");

        expenseTypeCombo.addActionListener(e -> {
            String selected = (String) expenseTypeCombo.getSelectedItem();
            cl.show(dynamicPanel, selected);
        });

        return dynamicPanel;
    }

    private void registerListeners() {
        addExpenseButton.addActionListener(e -> addExpense());
    }

    private void addExpense() {
        String type = (String) expenseTypeCombo.getSelectedItem();
        String amountStr = amountField.getText().trim();
        String desc = descriptionField.getText().trim();
        String dateStr = dateField.getText().trim();

        if (amountStr.isEmpty() || desc.isEmpty() || dateStr.isEmpty()) {
            JOptionPane.showMessageDialog(this,
                    "Amount, description and date are required.",
                    "Input Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        double amount;
        try {
            amount = Double.parseDouble(amountStr);
        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(this, "Invalid amount.",
                    "Input Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        if (amount <= 0) {
            JOptionPane.showMessageDialog(this, "Amount must be positive.",
                    "Input Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        Date date;
        try {
            date = new SimpleDateFormat("dd-MM-yyyy").parse(dateStr);
        } catch (ParseException ex) {
            JOptionPane.showMessageDialog(this, "Invalid date: " + ex.getMessage(),
                    "Input Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        Expense exp = null;
        switch (type) {
            case "Food":
                if (restaurantField.getText().trim().isEmpty()) {
                    JOptionPane.showMessageDialog(this, "Restaurant is required.",
                            "Input Error", JOptionPane.ERROR_MESSAGE);
                    return;
                }
                exp = new FoodExpense(amount, date, desc, restaurantField.getText().trim());
                break;
            case "Travel":
                if (destinationField.getText().trim().isEmpty()
                        || transportField.getText().trim().isEmpty()) {
                    JOptionPane.showMessageDialog(this, "Destination and transport mode are required.",
                            "Input Error", JOptionPane.ERROR_MESSAGE);
                    return;
                }
                exp = new TravelExpense(amount, date, desc, destinationField.getText().trim(),
                        transportField.getText().trim());
                break;
            case "Utility":
                if (utilityTypeField.getText().trim().isEmpty()) {
                    JOptionPane.showMessageDialog(this, "Utility type is required.",
                            "Input Error", JOptionPane.ERROR_MESSAGE);
                    return;
                }
                exp = new UtilityExpense(amount, date, desc, utilityTypeField.getText().trim());
                break;
        }

        try {
            manager.addExpense(exp);
            refreshTable(manager.getExpenses());
            updateTotalLabel();
            clearInputFields();
            JOptionPane.showMessageDialog(this, "Expense added successfully!",
                    "Success", JOptionPane.INFORMATION_MESSAGE);
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(this, "Database error: " + ex.getMessage(),
                    "Database Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void refreshTable(ArrayList<Expense> expenses) {
        tableModel.setRowCount(0);
        for (Expense exp : expenses) {
            String type = exp.getType();
            String amount = String.format("%.2f", exp.getAmount());
            String date = new SimpleDateFormat("dd-MM-yyyy").format(exp.getDate());
            String desc = exp.getDescription();
            String details = "";
            if (exp instanceof FoodExpense) {
                details = ((FoodExpense) exp).getRestaurant();
            } else if (exp instanceof TravelExpense) {
                details = ((TravelExpense) exp).getDestination() + ", " + ((TravelExpense) exp).getTransportMode();
            } else if (exp instanceof UtilityExpense) {
                details = ((UtilityExpense) exp).getUtilityType();
            }
            tableModel.addRow(new Object[] { type, amount, date, desc, details });
        }
    }

    private void updateTotalLabel() {
        totalLabel.setText("Total Expenses: $" + String.format("%.2f", manager.getTotalExpenses()));
    }

    private void clearInputFields() {
        amountField.setText("");
        descriptionField.setText("");
        dateField.setText(new SimpleDateFormat("dd-MM-yyyy").format(new Date()));
        restaurantField.setText("");
        destinationField.setText("");
        transportField.setText("");
        utilityTypeField.setText("");
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            ExpenseTrackerGUI gui = new ExpenseTrackerGUI();
            gui.setVisible(true);
        });
    }
}
