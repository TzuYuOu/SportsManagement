package com.sports;

import com.sports.model.ComboItem;

import javax.swing.*;
import java.awt.event.*;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class Registration extends JFrame {
    private JLabel jLabel1;
    private JPanel registrationPanel;
    private JComboBox schoolBox;
    private JComboBox playerBox;
    private JComboBox sportBox;
    private JButton saveButton;
    private JButton backButton;
    private JLabel groupLabel;
    private JLabel genderLabel;
    private JLabel positionLabel;
    private String gender;

    public Registration(){
        add(registrationPanel);
        setSize(1000,600);

        // initialize sportBox
        initSportBox();

        schoolBox.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {

                // clear items in playerBox
                playerBox.removeAllItems();
                PreparedStatement ps;
                ResultSet rs;
                String searchPlayerQuery = "SELECT id, name FROM `player` WHERE scid = ?";
                try {
                    ps = ConnectionProvider.getConnection().prepareStatement(searchPlayerQuery);
                    ps.setString(1, String.valueOf(schoolBox.getSelectedIndex()+1));
                    rs = ps.executeQuery();
                    int idx = 0;
                    while(rs.next()){
                        String playerInfo = rs.getString("id")+"-" + rs.getString("name");
                        playerBox.addItem(new ComboItem(String.valueOf(idx),playerInfo));
                    }
                } catch (SQLException throwables) {
                    throwables.printStackTrace();
                }
            }


        });
        
        saveButton.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                String playerInfo = playerBox.getSelectedItem().toString();
                String playerId = playerInfo.split("-")[0];
                int sportId = sportBox.getSelectedIndex()+1;
                int eventId=0;

                PreparedStatement ps, ps2;
                ResultSet rs;

                String getEventIdQuery = "SELECT id FROM `event` WHERE egender=? AND egroup =? AND spid=?";
                String registrationQuery = "INSERT INTO `registration`(`pid`, `eid`, `valid`) VALUES (?,?,?)";
                try {
                    ps = ConnectionProvider.getConnection().prepareStatement(getEventIdQuery);
                    ps.setString(1, genderLabel.getText());
                    ps.setString(2, groupLabel.getText());
                    ps.setInt(3, sportId);

                    rs = ps.executeQuery();
                    while (rs.next()){
                        eventId = rs.getInt(1);
                    }

                    if(!checkRegistrationExist(eventId)) {
                        try{
                            ps2 = ConnectionProvider.getConnection().prepareStatement(registrationQuery);
                            ps2.setInt(1,Integer.parseInt(playerId));
                            ps2.setInt(2, eventId);
                            ps2.setInt(3, 1);
                            if(ps2.executeUpdate() != 0){
                                JOptionPane.showMessageDialog(null, "This registration is successful");
                            }
                            else{
                                JOptionPane.showMessageDialog(null, "Error: check your information");
                            }

                            ps2.close();
                        } catch (SQLException throwables) {
                            throwables.printStackTrace();
                        }
                    }

                    else{
                        JOptionPane.showMessageDialog(null, "Error: Player has already signed up");
                    }
                    ps.close();
                    rs.close();
                } catch (SQLException throwables) {
                    throwables.printStackTrace();
                }



            }
        });

        playerBox.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                String player_info = playerBox.getSelectedItem().toString();
                String[] cutText = player_info.split("-");

                PreparedStatement ps;
                ResultSet rs;
                String selectGroupQuery = "SELECT position, gender, pgroup FROM `player` WHERE id=?";
                try {
                    ps = ConnectionProvider.getConnection().prepareStatement(selectGroupQuery);
                    ps.setInt(1,Integer.parseInt(cutText[0]));
                    rs = ps.executeQuery();
                    while (rs.next()){
                        positionLabel.setText(rs.getString("position"));
                        groupLabel.setText(rs.getString("pgroup"));
                        genderLabel.setText(rs.getString("gender")+"生組");

                    }
                } catch (SQLException throwables) {
                    throwables.printStackTrace();
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
    }

    public void initSportBox(){
        PreparedStatement ps;
        ResultSet rs;
        String sportQuery = "SELECT * FROM `sports`";
        try {
            ps = ConnectionProvider.getConnection().prepareStatement(sportQuery);
            rs = ps.executeQuery();
            int idx = 0;
            while(rs.next()){
                String sport_name = rs.getString("category") + "--" + rs.getString("details");
                sportBox.addItem(new ComboItem(String.valueOf(idx),sport_name));
            }
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
    }

    public boolean checkRegistrationExist(int eventId){

        String playerInfo = playerBox.getSelectedItem().toString();
        int playerId = Integer.parseInt(playerInfo.split("-")[0]);

        PreparedStatement ps;
        ResultSet rs;
        String searchUserEventQuery = "SELECT pid, eid FROM `registration`";

        try {
            ps = ConnectionProvider.getConnection().prepareStatement(searchUserEventQuery);
            rs = ps.executeQuery();
            while(rs.next()){
                if((playerId == rs.getInt("pid")) && (eventId == rs.getInt("eid"))){

                    return true;
                }
            }
            return false;
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }

        return true;
    }
}
