package Entities;
import oracle.jdbc.driver.OracleDriver;
import Entities.Client;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.io.*;
public class PremiumClient extends Client implements Serializable {
    static final String DB_URL = "jdbc:oracle:thin:@193.226.51.37:1521:o11g";
    static final String USER = "grupa35";
    static final String PASS = "bazededate";
    private ArrayList <PremiumTransaction> MyPremiumTransactions;
    public PremiumClient(String myName, int day, LocalDateTime local) {
        super(myName, day, local);
        //Inserting data in our database
        try {
            Connection conn = DriverManager.getConnection(DB_URL, USER, PASS);
            PreparedStatement preparedStatement = conn.prepareStatement("SELECT * FROM PREMIUMCLIENT WHERE CLIENT_ID=?");
            preparedStatement.setInt(1,super.ClientID);
            int aux = preparedStatement.executeUpdate();
            if(aux==0) {
                preparedStatement = conn
                        .prepareStatement("INSERT INTO PREMIUMCLIENT(CLIENT_ID,NUME,ZI,TIMP) VALUES(?,?,?,?)");
                preparedStatement.setInt(1, super.ClientID);
                preparedStatement.setString(2, myName);
                preparedStatement.setInt(3, day);
                Timestamp timestamp = Timestamp.valueOf(local);
                preparedStatement.setTimestamp(4, timestamp);
                preparedStatement.executeUpdate();
            }
            conn.close();
        }catch(Exception e){
            e.printStackTrace();
        }

        MyPremiumTransactions = new ArrayList <PremiumTransaction> ();
    }
    ArrayList <PremiumTransaction> getMyPremiumTransactions() {
        return MyPremiumTransactions;
    }
    @Override
    void addTransaction(Transaction x, int a, double b, double c, double d, LocalDateTime l, int f) {
        PremiumTransaction y = new PremiumTransaction(x.EntrySum, x.EntryCurrency, x.ExitCurrency, a, b, c, d, l, f);
        this.MyPremiumTransactions.add(y);
    }
}