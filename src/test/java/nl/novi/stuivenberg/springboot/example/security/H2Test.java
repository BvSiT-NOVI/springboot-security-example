package nl.novi.stuivenberg.springboot.example.security;

import org.junit.jupiter.api.Test;

import java.sql.*;

public class H2Test {

    //Error in path
    //@Test
    public void testDatabaseNoMem() throws SQLException {
        testDatabase("jdbc:h2:test");
    }

    //OK
    //@Test
    public void testDatabaseMem() throws SQLException {
        testDatabase("jdbc:h2:mem:test");
    }

    private void testDatabase(String url) throws SQLException {
        Connection connection= DriverManager.getConnection(url);
        Statement s=connection.createStatement();
        try {
            s.execute("DROP TABLE USER_ROLE");
        } catch(SQLException sqle) {
            System.out.println("Table not found, not dropping");
        }
        s.execute("CREATE TABLE PERSON (ID INT PRIMARY KEY, FIRSTNAME VARCHAR(64), LASTNAME VARCHAR(64))");
        s.execute("INSERT INTO PERSON(ID,FIRSTNAME,LASTNAME) VALUES(1,'jjj','jjj')");

        PreparedStatement ps=connection.prepareStatement("select * from PERSON");
        ResultSet r=ps.executeQuery();
        if(r.next()) {
            String firstName = r.getString("FIRSTNAME");
            System.out.println(firstName);
        }
        r.close();
        ps.close();
        s.close();
        connection.close();
    }
}
