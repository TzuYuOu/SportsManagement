package com.sports;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class CompetitionResult extends JFrame {

    private JPanel cmpResPanel;
    private JLabel reLabel;
    private JTable gradeTable;
    private JButton button1;

    public CompetitionResult(){
        add(cmpResPanel);
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
        String [] columns = new String[] {"選手編號", "學校", "名字", "比賽項目", "成績"};
        DefaultTableModel model = (DefaultTableModel) gradeTable.getModel();
        model.setColumnIdentifiers(columns);

        PreparedStatement ps;
        ResultSet rs;
        String query = "SELECT player.id, school.name, player.name, event.egroup, event.egender, sports.category, sports.details, grade FROM `player`, `school`, `event`, `competition`, `sports` WHERE `player`.id = `competition`.`pid` AND `competition`.`eid` = `event`.`id` AND `player`.`scid` = `school`.`id` AND `sports`.`id` = `event`.`spid`";
        try {
            ps = ConnectionProvider.getConnection().prepareStatement(query);
            rs = ps.executeQuery();
            while(rs.next()){
                model.addRow(new Object[]{rs.getString("player.id"),
                        rs.getString("school.name"),
                        rs.getString("player.name"),
                        rs.getString("event.egroup")+rs.getString("event.egender")
                                +rs.getString("sports.category")+rs.getString("sports.details"),
                        rs.getString("grade")});
            }

            ps.close();
            rs.close();
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }


    }
}
