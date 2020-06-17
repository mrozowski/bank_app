package com.bank.DAO;

import com.bank.AccountDetails.ClientDetails;
import com.bank.InformationWindow.BoxWindow;
import com.bank.Menu.Client;
import com.bank.Registration.NewClient;

import java.math.BigDecimal;
import java.sql.*;


public class ClientDAO_DB implements ClientDAO {
    private final String JDBC_DRIVER = "com.mysql.cj.jdbc.Driver";
    private final String DB_URL = "jdbc:mysql://localhost/bank_db?useUnicode=true&characterEncoding=utf-8";
    private final String USER = "root";
    private final String PASS = "";
//    private final String DB_URL = "jdbc:mysql://db4free.net:3306/smks_bank_db";
//    private final String USER = "smks_2020";
//    private final String PASS = "00522Bank2323";

    private final Connection connection;

    public ClientDAO_DB() {
        try {
            connection = DriverManager.getConnection(DB_URL, USER, PASS);
        } catch (SQLException ex) {
            throw new RuntimeException("Could not connect to the database", ex);
        }
    }

    public boolean connection(){
        Statement p;
        try{
            String check_conn_sql = "Select 1 from clients";
            p = connection.createStatement();
            p.execute(check_conn_sql);
            return true;
        }catch (SQLException e) {
            e.printStackTrace();

            return false;
        }
    }

    @Override
    public Client getClient(String login, char[] password) {
        Client client;
        PreparedStatement pstmt;
        try{
            //Check if there is connection with server
            if(!connection()){
                throw new SQLException("Brak połączenia z serwerem");
            }

            String sql = "Select id, nick, first_name, last_name, email, phone, money, account_number from clients" +
                    " where nick = ? and password = ? limit 1";
            pstmt = connection.prepareStatement(sql);
            pstmt.setString(1, login);
            pstmt.setString(2, String.valueOf(password));

            ResultSet rs = pstmt.executeQuery(); //Fetch the data from database

            // Jeżeli nie uda się przesunąć wskaźnika na pierwszy wiersz, to znaczy, że zapytanie nie zwróciło wyników
            if(!rs.next()) {
                throw new SQLException("Nie odnaleziono użytkownika");
            }
            else{
                int id = rs.getInt("id");
                String nick = rs.getString("nick");
                String first_name = rs.getString("first_name");
                String last_name = rs.getString("last_name");
                String email = rs.getString("email");
                String phone = rs.getString("phone");
                BigDecimal money = rs.getBigDecimal("money");
                String account_number = rs.getString("account_number");

                client = new Client(
                        id,
                        nick,
                        first_name,
                        last_name,
                        email,
                        phone,
                        money,
                        account_number
                );
                return client;
            }
        }
        catch(Exception e){
            //e.printStackTrace();
            BoxWindow.infoBox(e.getMessage(),"Błąd");
            return null;
        }
    }

    @Override
    public boolean addClient(NewClient newClient) {
        PreparedStatement pstmt;
        try{
            String sql = "Insert into Clients VALUES(null, ?, ?, ?, ?, ?, ?, ?, ?)";

            pstmt = connection.prepareStatement(sql);
            pstmt.setString(1, newClient.getNick());
            pstmt.setString(2, newClient.getName());
            pstmt.setString(3, newClient.getLast_name());
            pstmt.setString(4, String.valueOf(newClient.getPassword1()));
            pstmt.setString(5, newClient.getEmail());
            pstmt.setString(6, newClient.getPhone());
            pstmt.setBigDecimal(7, BigDecimal.valueOf(0));
            pstmt.setString(8, newClient.getAccountNumber());

            pstmt.executeUpdate();

            return true;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    @Override
    public boolean updateClient(ClientDetails client, char[] password) {
        //For changing account details
        PreparedStatement pstmt;
        try {
            String sql = "Update Clients set first_name = ?, last_name = ?, email = ?, phone = ?" +
                    " where nick = ? and password = ? ;";

            pstmt = connection.prepareStatement(sql);
            pstmt.setString(1, client.getName());
            pstmt.setString(2, client.getLast_name());
            pstmt.setString(3, client.getEmail());
            pstmt.setString(4, client.getPhone());
            pstmt.setString(5, client.getNick());
            pstmt.setString(6, String.valueOf(password));
            int execute = pstmt.executeUpdate();
            if(execute < 1){
                throw new SQLException("Couldn't find an account");
            }
            return true;

        } catch (SQLException e) {
            if(e.getMessage().equals("Couldn't find an account")){
                BoxWindow.infoBox("Podano nie poprawne hasło", "Błąd dostępu");
                return false;
            }
            else BoxWindow.infoBox("Coś poszło nie tak", "Błąd");
            return false;
        }
    }

    @Override
    public boolean transferMoney(Client client, char[] password, String ac_number, BigDecimal money) {
        //transfer money need authorization with password
        String transfer_from = "Update Clients set money = money - ? where nick = ? and password = ? ;";
        String transfer_to = "Update Clients set money = money + ? where account_number = ?;";
        try {
            //update money column in client who sends money
            //Maybe latter try to make database transaction for that

            PreparedStatement pstmt = connection.prepareStatement(transfer_from);
            pstmt.setBigDecimal(1, money);
            pstmt.setString(2, client.getNick());
            pstmt.setString(3, String.valueOf(password));

            int execute = pstmt.executeUpdate();
            if(execute < 1){
                //0 when nothing was changed, -1 when got error
                throw new AuthorizationFailureException();
            }

            PreparedStatement pstmt2 = connection.prepareStatement(transfer_to);
            pstmt2.setBigDecimal(1, money);
            pstmt2.setString(2, ac_number);

            int execute_two = pstmt2.executeUpdate();
            System.out.println(execute_two);
            if(execute_two < 1){
                throw new SQLException("Couldn't found account number");
            }
            return true;
        } catch (SQLException | AuthorizationFailureException e) {
            if(e instanceof AuthorizationFailureException){
                BoxWindow.infoBox(e.getMessage(),"Błąd dostępu");
                return false;
            }
            else if(e.getMessage().equals("Couldn't found account number")){
                BoxWindow.infoBox("Podany numer konta jest nie poprawny", "Błąd");
                return false;
            }
            else{
                BoxWindow.infoBox("Coś poszło nie tak", "Błąd");
                return false;
            }


        }
    }

    @Override
    public boolean checkUserName(String user) {
        return checkIfExist(user, "nick");
    }

    @Override
    public boolean checkEmail(String email) {
        return  checkIfExist(email, "email");
    }


    @Override
    public boolean checkAccountNumber(String number) {
        return checkIfExist(number, "account_number");
    }

    private boolean checkIfExist(String name, String column){
        PreparedStatement pstmt;
        try{
            String sql = "Select count(*) from clients where "+ column +" = ? ;";
            pstmt = connection.prepareStatement(sql);
            pstmt.setString(1, name);

            ResultSet rs = pstmt.executeQuery();
            if(rs.next()){
                return rs.getInt(1) != 0; //Zwraca false jeżeli podana nazwa się nie powtarza
            }
        } catch (SQLException e) {
            e.printStackTrace();

        }
        return true;
    }
}

