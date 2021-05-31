package com.sports;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class AllPlayers extends JFrame {
    private JTable table1;
    private JPanel playerPanel;
    private JButton closeBtn;


    public AllPlayers(){
        add(playerPanel);
        setSize(1000,600);

        tableDetails();

        closeBtn.addMouseListener(new MouseAdapter() {
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
        String [] columns = new String[] {"選手編號", "名字", "性別", "組別", "職稱", "學校"};
        DefaultTableModel model = (DefaultTableModel) table1.getModel();
        model.setColumnIdentifiers(columns);

        PreparedStatement ps;
        ResultSet rs;
        String query = "SELECT player.id, player.name, player.gender, player.pgroup, player.position, school.name FROM `player`, `school` WHERE player.scid = school.id";

        try {
            ps = ConnectionProvider.getConnection().prepareStatement(query);
            rs = ps.executeQuery();
            while(rs.next()){
                model.addRow(new Object[]{rs.getString("id"), rs.getString("name"),
                        rs.getString("gender"), rs.getString("pgroup"),
                        rs.getString("position"), rs.getString("school.name")});
            }

            ps.close();
            rs.close();
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }


    }
}
