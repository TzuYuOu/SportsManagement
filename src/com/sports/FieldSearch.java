package com.sports;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class FieldSearch extends JFrame{
    private JPanel fieldSearchPanel;
    private JTable fieldEventTable;
    private JButton closeBtn;

    public FieldSearch(){
        add(fieldSearchPanel);
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
        String [] columns = new String[] {"運動種類", "比賽項目", "比賽地點"};
        DefaultTableModel model = (DefaultTableModel) fieldEventTable.getModel();
        model.setColumnIdentifiers(columns);

        PreparedStatement ps;
        ResultSet rs;
        String query = "SELECT event.egender, event.egroup, sports.category, sports.details, field.name FROM `competition`, `event`, `field`, `sports` WHERE competition.eid = event.id AND competition.fid = field.id AND sports.id = event.spid";
        try {
            ps = ConnectionProvider.getConnection().prepareStatement(query);
            rs = ps.executeQuery();
            while(rs.next()){
                model.addRow(new Object[]{rs.getString("sports.category"),
                        rs.getString("event.egender") + rs.getString("event.egroup") + rs.getString("sports.details"),
                        rs.getString("field.name")});
            }

            ps.close();
            rs.close();
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
    }
}
