package com.sports;

import com.sports.model.ComboItem;

import javax.swing.*;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class AddNewGrade extends JFrame{
    private JPanel addNewGradePanel;
    private JComboBox playerBox;
    private JTextField gradeField;
    private JComboBox eventBox;
    private JComboBox fieldBox;
    private JButton saveButton;
    private JButton resetButton;
    private JButton backButton;

    public AddNewGrade(){
        add(addNewGradePanel);
        setSize(1000,600);

        // initialize playerBox
        initPlayerBox();
        // based on playerBox Item, initialize eventBox
        changeEventBox();

        playerBox.addItemListener(new ItemListener() {
            @Override
            public void itemStateChanged(ItemEvent e) {
                if(e.getStateChange()== ItemEvent.SELECTED) {
                    changeEventBox();
                }
            }
        });

        saveButton.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                if(verifyFields()){
                    ComboItem playerItem = (ComboItem)playerBox.getSelectedItem();
                    ComboItem eventItem = (ComboItem) eventBox.getSelectedItem();
                    int playerId = Integer.parseInt(playerItem.getValue());
                    int eventId = Integer.parseInt(eventItem.getValue());
                    int fieldId = fieldBox.getSelectedIndex()+1;
                    String grade = gradeField.getText();

                    PreparedStatement ps;
                    String query = "INSERT INTO `competition`(`pid`, `eid`, `fid`, `grade`) VALUES (?,?,?,?)";
                    try {
                        ps = ConnectionProvider.getConnection().prepareStatement(query);
                        ps.setInt(1,playerId);
                        ps.setInt(2, eventId);
                        ps.setInt(3, fieldId);
                        ps.setString(4,grade);
                        if(ps.executeUpdate() != 0){
                            JOptionPane.showMessageDialog(null, "The grade has been added");
                        }
                        else{
                            JOptionPane.showMessageDialog(null, "Check your information","Problem", JOptionPane.ERROR_MESSAGE);
                        }
                    } catch (SQLException throwables) {
                        throwables.printStackTrace();
                    }

                }
            }
        });

        backButton.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                Home home = new Home();
                home.setVisible(true);
                home.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
                dispose();
            }
        });

        resetButton.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                gradeField.setText("");
            }
        });
    }

    public void initPlayerBox(){
        PreparedStatement ps;
        ResultSet rs;
        String query = "SELECT DISTINCT(player.name), player.id FROM `registration`, `player` WHERE registration.pid = player.id";
        try {
            ps = ConnectionProvider.getConnection().prepareStatement(query);
            rs = ps.executeQuery();
            while(rs.next()){
                playerBox.addItem(new ComboItem(String.valueOf(rs.getInt("player.id")), rs.getString("player.name")));
            }
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }


    }

    public void changeEventBox(){
        eventBox.removeAllItems();
        String playerName = playerBox.getSelectedItem().toString();
        String query = "SELECT event.id, event.egender, event.egroup, sports.category, sports.details FROM `registration`, `player`, `event`, `sports` WHERE registration.pid = player.id AND registration.eid = event.id AND event.spid = sports.id AND player.name = ?";

        PreparedStatement ps;
        ResultSet rs;

        try {
            ps = ConnectionProvider.getConnection().prepareStatement(query);
            ps.setString(1, playerName);
            rs = ps.executeQuery();
            while(rs.next()){
                eventBox.addItem(new ComboItem(rs.getString("event.id"),
                        rs.getString("sports.category") + rs.getString("sports.details")));
            }
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }

    }

    public boolean verifyFields(){
        if(gradeField.getText().trim().equals("")){
            return false;
        }


        return true;
    }
}
