package com.sports;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class HistoricalPlayer extends JFrame{
    private JPanel historicalPlayerPanel;
    private JButton closeBtn;
    private JComboBox seasonNameBox;
    private JTable table1;
    private JLabel yearLabel;

    public HistoricalPlayer(){
        add(historicalPlayerPanel);
        setSize(1000,600);

        initTable();

        closeBtn.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                Home home = new Home();
                home.setVisible(true);
                home.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
                dispose();
            }
        });

        seasonNameBox.addItemListener(new ItemListener() {
            @Override
            public void itemStateChanged(ItemEvent e) {
                if(e.getStateChange()== ItemEvent.SELECTED) {
                    yearLabel.setText(String.valueOf(2011+seasonNameBox.getSelectedIndex()));
                    changeTable(seasonNameBox.getSelectedIndex()+1);

                }
            }
        });

    }

    public void changeTable(int seid){
        DefaultTableModel model = (DefaultTableModel) table1.getModel();
        model.setNumRows(0);
        PreparedStatement ps;
        ResultSet rs;
        String query = "SELECT player.name, participation.pbefore FROM `participation`, `player` WHERE participation.pid = player.id AND participation.seid = ?";
        try {
            ps = ConnectionProvider.getConnection().prepareStatement(query);
            ps.setInt(1, seid);
            rs = ps.executeQuery();
            while(rs.next()){
                String participated = "是";
                if(rs.getInt("participation.pbefore") == 1){
                    participated = "否";
                }

                model.addRow(new Object[]{rs.getString("player.name"), participated});
            }

            ps.close();
            rs.close();
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
    }

    public void initTable(){
        String [] columns = new String[] {"比賽選手", "第一次參賽"};
        DefaultTableModel model = (DefaultTableModel) table1.getModel();
        model.setColumnIdentifiers(columns);

        PreparedStatement ps;
        ResultSet rs;
        String query = "SELECT player.name, participation.pbefore FROM `participation`, `player` WHERE participation.pid = player.id AND participation.seid = 1";
        try {
            ps = ConnectionProvider.getConnection().prepareStatement(query);
            rs = ps.executeQuery();
            while(rs.next()){
                String participated = "是";
                if(rs.getInt("participation.pbefore") == 1){
                    participated = "否";
                }

                model.addRow(new Object[]{rs.getString("player.name"), participated});
            }

            ps.close();
            rs.close();
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
    }
}
