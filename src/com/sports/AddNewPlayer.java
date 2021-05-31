package com.sports;

import javax.swing.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class AddNewPlayer extends JFrame {
    private JPanel addNewPlayerPanel;
    private JComboBox genderBox;
    private JComboBox positionBox;
    private JComboBox schoolBox;
    private JComboBox groupBox;
    private JButton saveButton;
    private JButton resetButton;
    private JButton closeBtn;
    private JTextField nameTextField;
    private JTextField idTextField;



    public AddNewPlayer(){
        add(addNewPlayerPanel);
        setSize(1000,600);

        int id = 1;
        idTextField.setText(String.valueOf(id));
        idTextField.setEditable(false);


        PreparedStatement ps;
        ResultSet rs;

        try {
            ps = ConnectionProvider.getConnection().prepareStatement("SELECT max(`id`) FROM `player`");
            rs = ps.executeQuery();

            while(rs.next()){
                id = rs.getInt(1);
                id += 1;
            }
            idTextField.setText(String.valueOf(id));
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }

        closeBtn.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                Home home = new Home();
                home.setVisible(true);
                home.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
                dispose();
            }
        });
        saveButton.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                String name = nameTextField.getText();
                String gender = genderBox.getSelectedItem().toString();
                String group = groupBox.getSelectedItem().toString();
                String position = positionBox.getSelectedItem().toString();
                int schoolId = schoolBox.getSelectedIndex()+1;

                if(verifyFields()){
                    PreparedStatement ps;
                    String addPlayerQuery = "INSERT INTO `player`(`name`, `gender`, `pgroup`, `position`, `scid`) VALUES (?,?,?,?,?)";
                    try {
                        ps = ConnectionProvider.getConnection().prepareStatement(addPlayerQuery);
                        ps.setString(1, name);
                        ps.setString(2, gender);
                        ps.setString(3, group);
                        ps.setString(4, position);
                        ps.setInt(5, schoolId);

                        if(ps.executeUpdate() != 0){
                            JOptionPane.showMessageDialog(null, "This player has been added");
                            int newId = Integer.parseInt(idTextField.getText())+1;
                            idTextField.setText(String.valueOf(newId));
                        }
                        else{
                            JOptionPane.showMessageDialog(null, "Error: check your information");
                        }

                        ps.close();
                    } catch (SQLException throwables) {
                        throwables.printStackTrace();
                    }
                }
            }
        });
        resetButton.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                nameTextField.setText("");
            }
        });
    }

    public boolean verifyFields(){
        String name = nameTextField.getText();
        String gender = genderBox.getSelectedItem().toString();
        String group = groupBox.getSelectedItem().toString();
        String position = positionBox.getSelectedItem().toString();
        int schoolId = schoolBox.getSelectedIndex()+1;

        if(name.trim().equals("") || gender.trim().equals("")
                ||  group.trim().equals("") || position.trim().equals("")){
            return false;
        }
        else{
            return  true;
        }
    }

}
