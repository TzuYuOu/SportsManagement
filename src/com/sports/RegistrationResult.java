package com.sports;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class RegistrationResult extends JFrame {
    private JPanel registrationResultPanel;
    private JTable table1;
    private JLabel reLabel;
    private JButton button1;

    public RegistrationResult(){
        add(registrationResultPanel);
        setSize(1000,600);
        tableDetails();
        button1.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                Home home = new Home();
                home.setVisible(true);
                home.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
                dispose();
            }
        });
    }

    public void tableDetails(){
        String [] columns = new String[] {"選手編號", "學校", "名字", "性別", "組別", "運動種類", "項目", "審核"};
        DefaultTableModel model = (DefaultTableModel) table1.getModel();
        model.setColumnIdentifiers(columns);

        PreparedStatement ps;
        ResultSet rs;
        String query = "SELECT player.id, school.name, player.name, player.gender, player.pgroup, sports.category, sports.details, registration.valid  FROM `player`, `school`, `event`, `registration`, `sports` WHERE `player`.id = `registration`.`pid` AND `registration`.`eid` = `event`.`id` AND `player`.`scid` = `school`.`id` AND `sports`.`id` = `event`.`spid`";

        try {
            ps = ConnectionProvider.getConnection().prepareStatement(query);
            rs = ps.executeQuery();
            while(rs.next()){
                model.addRow(new Object[]{rs.getString("player.id"), rs.getString("school.name"),
                        rs.getString("player.name"), rs.getString("player.gender"),
                        rs.getString("player.pgroup"), rs.getString("sports.category"),
                        rs.getString("sports.details"), rs.getString("registration.valid")});
            }

            ps.close();
            rs.close();
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }


    }
}
