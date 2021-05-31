package com.sports;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.util.Locale;
import java.util.Vector;

public class Home extends JFrame {

    private JPanel homePanel;
    private JLabel allPlayers;
    private JLabel gradeLabel;
    private JLabel reLabel;
    private JLabel addNewPlayerLabel;
    private JLabel registrationLabel;
    private JLabel hisPlayerLabel;
    private JLabel fieldSearchLabel;
    private JLabel addGradeLabel;
    private JLabel otherLabel;
    private JButton resetButton;
    private JButton sendButton;
    private JTextField sqlField;
    private JLabel modifyGradeLabel;

    public Home(){
        add(homePanel);
        setSize(1000,600);


        allPlayers.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
               AllPlayers allPlayers = new AllPlayers();
               allPlayers.setVisible(true);
               allPlayers.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
               dispose();
            }
        });

        reLabel.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                RegistrationResult registrationResult = new RegistrationResult();
                registrationResult.setVisible(true);
                registrationResult.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
                dispose();
            }
        });

        addNewPlayerLabel.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
               AddNewPlayer addNewPlayer = new AddNewPlayer();
               addNewPlayer.setVisible(true);
               addNewPlayer.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
               dispose();
            }
        });
        registrationLabel.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                Registration registration = new Registration();
                registration.setVisible(true);
                registration.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
                dispose();
            }
        });
        gradeLabel.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                CompetitionResult competitionResult = new CompetitionResult();
                competitionResult.setVisible(true);
                competitionResult.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
                dispose();
            }
        });
        hisPlayerLabel.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                HistoricalPlayer historicalPlayer = new HistoricalPlayer();
                historicalPlayer.setVisible(true);
                historicalPlayer.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
                dispose();
            }
        });
        fieldSearchLabel.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                FieldSearch fieldSearch = new FieldSearch();
                fieldSearch.setVisible(true);
                fieldSearch.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
                dispose();
            }
        });

        resetButton.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                sqlField.setText("");
            }
        });

        sendButton.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                String query = sqlField.getText();
                if(checkQuery(query)){
                    String queryType = query.split(" ")[0];
                    PreparedStatement ps;
                    ResultSet rs;

                    try {
                        ps = ConnectionProvider.getConnection().prepareStatement(query);

                        if(queryType.toLowerCase(Locale.ROOT).equals("select")){
                            rs = ps.executeQuery();
                            JTable table = new JTable(buildTableModel(rs));
                            JOptionPane.showMessageDialog(null, new JScrollPane(table));

                        }
                        else{
                            if(ps.executeUpdate() != 0){
                                JOptionPane.showMessageDialog(null, "This operation is successful");
                            }
                            else{
                                JOptionPane.showMessageDialog(null,"Check your query","Query Error", JOptionPane.ERROR_MESSAGE );
                            }
                        }


                    } catch (SQLException throwables) {
                        throwables.printStackTrace();
                    }
                }
                else{
                    JOptionPane.showMessageDialog(null,"Check your query","Typing Error", JOptionPane.ERROR_MESSAGE );
                }
            }
        });

        addGradeLabel.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                AddNewGrade addNewGrade = new AddNewGrade();
                addNewGrade.setVisible(true);
                addNewGrade.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
                dispose();
            }
        });

    }

    public boolean checkQuery(String query){
        if(query.trim().equals("")){
            return false;
        }
        else{
            if(query.split(" ")[0].toLowerCase(Locale.ROOT).equals("select") ||
                    query.split(" ")[0].toLowerCase(Locale.ROOT).equals("delete") ||
                    query.split(" ")[0].toLowerCase(Locale.ROOT).equals("update") ||
                    query.split(" ")[0].toLowerCase(Locale.ROOT).equals("insert")){
                return true;
            }
        }
        return false;
    }

    public static DefaultTableModel buildTableModel(ResultSet rs)
            throws SQLException {

        ResultSetMetaData metaData = rs.getMetaData();

        // names of columns
        Vector<String> columnNames = new Vector<String>();
        int columnCount = metaData.getColumnCount();
        for (int column = 1; column <= columnCount; column++) {
            columnNames.add(metaData.getColumnName(column));
        }

        // data of the table
        Vector<Vector<Object>> data = new Vector<Vector<Object>>();
        while (rs.next()) {
            Vector<Object> vector = new Vector<Object>();
            for (int columnIndex = 1; columnIndex <= columnCount; columnIndex++) {
                vector.add(rs.getObject(columnIndex));
            }
            data.add(vector);
        }

        return new DefaultTableModel(data, columnNames);

    }
}
