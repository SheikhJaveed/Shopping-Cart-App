import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Map;

public class ShoppingCartApp extends JFrame {
    private HashMap<String, Double> items = new HashMap<>();
    private HashMap<String, Integer> cart = new HashMap<>();
    private ArrayList<Integer> quantity = new ArrayList<>();
    private JLabel totalLabel;
    private double totalAmount = 0.0;

    public ShoppingCartApp() {
        setTitle("Shopping Cart App");
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setLayout(new BorderLayout());
        setPreferredSize(new Dimension(800, 500));
        getContentPane().setBackground(new Color(240, 240, 240));

        // Adding sample items
        items.put("SOAP  $5", 5.0);
        items.put("BISCUITS  $5", 5.0);
        items.put("APPLE  $40", 40.0);
        items.put("CHIPS  $8", 8.0);
        items.put("MILK  $15", 15.0);
        items.put("DETERGENT  $20", 20.0);
        items.put("MANGO JUICE  $30", 30.0);
        items.put("PINEAPPLE  $40", 40.0);
        items.put("NOODLES  $25", 25.0);
        items.put("SPICES  $12", 12.0);


        // Components
        JPanel itemListPanel = new JPanel(new GridLayout(5, 6));
    
        for (String item : items.keySet()) {
            JButton addButton = new JButton(item);
            addButton.setBackground(new Color(255, 215, 0));
            addButton.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    String itemName = e.getActionCommand();
                    if (cart.containsKey(itemName)) {
                        cart.put(itemName, cart.get(itemName) + 1);
                    } else {
                        cart.put(itemName, 1);
                    }
                    totalAmount += items.get(itemName);
                    updateTotalLabel();
                }
            });
            itemListPanel.add(addButton);
        }

        totalLabel = new JLabel("Total: $0.0");
        totalLabel.setFont(new Font("Arial", Font.BOLD, 20));

        JButton removeButton = new JButton("Remove Item");
        removeButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String itemName = JOptionPane.showInputDialog("Enter item to remove:");
                if (cart.containsKey(itemName)) {
                    int quantity = cart.get(itemName);
                    if (quantity == 1) {
                        cart.remove(itemName);
                        JOptionPane.showMessageDialog(null, "Removed "+itemName +"\n"+ "Quantity left:"+(quantity-1));
                    } else {
                        cart.put(itemName, quantity - 1);
                        JOptionPane.showMessageDialog(null, "Removed "+itemName +"\n"+ "Quantity left:"+(quantity-1));
                    }
                    totalAmount -= items.get(itemName);
                    updateTotalLabel();
                } else {
                    JOptionPane.showMessageDialog(null, "Item not found in the cart!");
                }
            }
        });

        JButton clearButton = new JButton("Clear Cart");
        clearButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                cart.clear();
                totalAmount = 0.0;
                updateTotalLabel();
                JOptionPane.showMessageDialog(null, "Cleared cart\n Total: $0.0\n Items: 0\n Quantity: 0\n");
            }
        });
        clearButton.setBackground(Color.GRAY);
        clearButton.setForeground(Color.BLACK);
        clearButton.setFont(new Font("Arial",Font.BOLD,16));


        JButton placeOrderButton = new JButton("Place Order");
        placeOrderButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if(cart.isEmpty()){
                    JOptionPane.showMessageDialog(null, "No items in Cart");
                    
                }else{
                    generateBill();
                    
                }
            }
        });
        placeOrderButton.setBackground(Color.GREEN);
        placeOrderButton.setForeground(Color.BLACK);
        placeOrderButton.setFocusPainted(false);
        placeOrderButton.setFont(new Font("Arial", Font.BOLD, 16));

     
        add(itemListPanel, BorderLayout.CENTER);
        JPanel controlPanel = new JPanel(new GridLayout(1, 4));
        controlPanel.add(removeButton);
        controlPanel.add(totalLabel);
        controlPanel.add(clearButton);
        controlPanel.add(placeOrderButton);
        add(controlPanel, BorderLayout.SOUTH);

        pack();
        setLocationRelativeTo(null);
    }

    private void updateTotalLabel() {
        totalLabel.setText("Total: $" + totalAmount);
    }
    
    private void generateBill() {
        StringBuilder bill = new StringBuilder();
        int totalItems = 0;
        
        for (Map.Entry<String, Integer> entry : cart.entrySet()) {
            String itemName = entry.getKey();
            int quantity = entry.getValue();
            double itemPrice = items.get(itemName);
            double subtotal = itemPrice * quantity;
            bill.append(itemName).append("\n - Quantity: ").append(quantity).append(", Price: $").append(itemPrice).append(", Subtotal: $").append(subtotal).append("\n");
            bill.append("\n");
            totalItems += quantity;
        }

        double totalPrice = totalAmount;
        bill.append("\nTotal Items: ").append(totalItems).append("\n Total Price: $").append(totalPrice);

        JTextArea billTextArea = new JTextArea(bill.toString());
        billTextArea.setEditable(false);
        JScrollPane scrollPane = new JScrollPane(billTextArea);
        scrollPane.setPreferredSize(new Dimension(400, 300));

        int result = JOptionPane.showConfirmDialog(this, scrollPane, "Bill", JOptionPane.OK_CANCEL_OPTION, JOptionPane.INFORMATION_MESSAGE);
        
        //if the user clicks ok button, go to the payment process
        if (result == JOptionPane.OK_OPTION) {
            JOptionPane.showMessageDialog(null, "Please proceed with the payment");
            payment();
        }
    }


    private void payment(){
        JFrame frame = new JFrame("Payment options");
        JPanel panel = new JPanel(new FlowLayout());
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
        // Create a button group
        ButtonGroup buttonGroup = new ButtonGroup();

        // Create radio buttons for multiple choices
        JRadioButton option1 = new JRadioButton("Google Pay");
        JRadioButton option2 = new JRadioButton("Phonepe");
        JRadioButton option3 = new JRadioButton("Paytm");
        JRadioButton option4 = new JRadioButton("Net Banking");

        // Add radio buttons to the button group
        buttonGroup.add(option1);
        buttonGroup.add(option2);
        buttonGroup.add(option3);
        buttonGroup.add(option4);

        // Add radio buttons to the panel
        panel.add(option1);
        panel.add(option2);
        panel.add(option3);
        panel.add(option4);

        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER)); // Horizontal arrangement for buttons
        // Create OK and Cancel buttons
        JButton okButton = new JButton("OK");
        JButton cancelButton = new JButton("Cancel");

        // Add action listener to the OK button
        okButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                // Get the selected option
                String selectedOption = getSelectedOption(buttonGroup);
                if (selectedOption != null) {
                    paymentProcess();
                    frame.dispose();
                    
                    // Perform OK action here
                } else {
                    JOptionPane.showMessageDialog(frame, "Please select an option!", "Error", JOptionPane.ERROR_MESSAGE);
                }
            }
        });

        // Add action listener to the Cancel button
        cancelButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                // Perform Cancel action here
                cart.clear();
                totalAmount=0.0;
                updateTotalLabel();
                frame.dispose();
                
            }
        });

        buttonPanel.add(okButton);
        buttonPanel.add(cancelButton);
        // Add the panel to the frame
        frame.add(panel, BorderLayout.CENTER);
        frame.add(buttonPanel, BorderLayout.SOUTH);

        // Set frame properties
        frame.setSize(400, 200);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setVisible(true);
    }
    private static String getSelectedOption(ButtonGroup buttonGroup) {
        for (Enumeration<AbstractButton> buttons = buttonGroup.getElements(); buttons.hasMoreElements();) {
            AbstractButton button = buttons.nextElement();
            if (button.isSelected()) {
                return button.getText();
            }
        }
        return null;
    }

    private void paymentProcess(){
        JFrame frame = new JFrame("Payment Window");
        JPanel panel = new JPanel(new FlowLayout());
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));

        JTextField accountNumField = new JTextField();
        JPasswordField pinField = new JPasswordField();
        JLabel accountNumLabel = new JLabel("Account Number:");
        JLabel pinLabel = new JLabel("PIN:");
        

        panel.add(accountNumLabel);
        panel.add(accountNumField);
        panel.add(pinLabel);
        panel.add(pinField);
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER)); // Horizontal arrangement for buttons
        // Create OK and Cancel buttons
        JButton okButton = new JButton("OK");
        JButton cancelButton = new JButton("Cancel");

        // Add action listener to the OK button
        okButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                String enteredAccountNumber = accountNumField.getText();
                String enteredPin = new String(pinField.getPassword());    
            
                // Check if the entered account number and PIN are correct (e.g., by comparing with a predefined value)
                // For simplicity, assume a predefined account number and PIN
                String correctAccountNumber = "123456";
                String correctPin = "7890";
            
                if (enteredAccountNumber.equals(correctAccountNumber) && enteredPin.equals(correctPin)) {
                    JOptionPane.showMessageDialog(frame, "Payment Successful!", "Success", JOptionPane.INFORMATION_MESSAGE);
                    // Reset cart and total amount
                    AddressInfo(); 
                    frame.dispose();
                } else {
                    JOptionPane.showMessageDialog(frame, "Incorrect Account Number or PIN!", "Error", JOptionPane.ERROR_MESSAGE);
                }
            }
        });

        // Add action listener to the Cancel button
        cancelButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                // Perform Cancel action here
                frame.dispose();
                
            }
        });

        buttonPanel.add(okButton);
        buttonPanel.add(cancelButton);

        // Add the panel to the frame
        frame.add(panel, BorderLayout.CENTER);
        frame.add(buttonPanel, BorderLayout.SOUTH);

        // Set frame properties
        frame.setSize(400, 200);
        frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        frame.setVisible(true);

    }
    private void AddressInfo(){
        JFrame frame = new JFrame("Address Window");
        JPanel panel = new JPanel(new FlowLayout());
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));

        JTextField addressField = new JTextField();
        JLabel addressLabel = new JLabel("Enter your address:");
        

        panel.add(addressLabel);
        panel.add(addressField);
        
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER)); // Horizontal arrangement for buttons
        // Create OK and Cancel buttons
        JButton okButton = new JButton("OK");
        JButton cancelButton = new JButton("Cancel");

        // Add action listener to the OK button
        okButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                JOptionPane.showMessageDialog(null,"Your product will be delivered to your address");
                cart.clear();
                totalAmount = 0.0;
                updateTotalLabel(); 
                frame.dispose();
            }
        });

        // Add action listener to the Cancel button
        cancelButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                // Perform Cancel action here
                frame.dispose();
                
            }
        });

        buttonPanel.add(okButton);
        buttonPanel.add(cancelButton);

        // Add the panel to the frame
        frame.add(panel, BorderLayout.CENTER);
        frame.add(buttonPanel, BorderLayout.SOUTH);

        // Set frame properties
        frame.setSize(400, 200);
        frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        frame.setVisible(true);

    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(new Runnable() {
            public void run() {
                new ShoppingCartApp().setVisible(true);
            }
        });
    }
}
