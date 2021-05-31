package com.sports;

import com.sports.model.ComboItem;

import javax.swing.*;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class ModifyGrade extends JFrame{
    private JPanel modifyGradePanel;
    private JComboBox playerBox;
    private JComboBox eventBox;
    private JTextField gradeField;
    private JButton updateButton;
    private JButton deleteButton;
    private JButton backButton;

    public ModifyGrade(){
        add(modifyGradePanel);
        setSize(1000,600);

        initPlayerBox();
        changeEventBox();

    }

    public void initPlayerBox(){
        PreparedStatement ps;
        ResultSet rs;
        String query="SELECT DISTINCT(player.name), player.id FROM `competition`, `player` WHERE competition.pid = player.id";

        try {
            ps = ConnectionProvider.getConnection().prepareStatement(query);
            rs = ps.executeQuery();
            while(rs.next()){
                playerBox.addItem(new ComboItem(String.valueOf(rs.getInt("player.id")),
                        rs.getString("player.name")));

            }
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
    }

    public void changeEventBox(){
        ComboItem playerBoxItem = (ComboItem) playerBox.getSelectedItem();
        int playerId = Integer.parseInt(playerBoxItem.getValue());

        PreparedStatement ps;
        ResultSet rs;
        String query="SELECT event.id, sports.category, sports.details FROM competition, event, sports WHERE competition.pid = ? AND competition.eid = event.id AND event.spid = sports.id";
        try {
            ps = ConnectionProvider.getConnection().prepareStatement(query);
            ps.setInt(1, playerId);
            rs = ps.executeQuery();
            while(rs.next()){
                eventBox.addItem(new ComboItem(String.valueOf(rs.getInt("event.id")),
                        rs.getString("sports.category")+rs.getString("sports.details")));
            }
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }


    }
}
