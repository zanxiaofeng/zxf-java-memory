package zxf.java.memory.jdbc;

import java.io.PrintWriter;
import java.sql.Driver;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Collections;
import java.util.concurrent.atomic.AtomicReference;

public class JdbcDriverTests {
    public static void main(String[] args) {
        DriverManager.setLogWriter(new PrintWriter(System.out));

        AtomicReference<Driver> oracleDriver = new AtomicReference();
        //classpath:/META-INF/services/java.sql.Driver
        Collections.list(DriverManager.getDrivers()).forEach(driver -> {
            System.out.println("Driver: " + driver.getClass().getName());
            try {
                if (driver.acceptsURL("jdbc:oracle:thin:@")) {
                    oracleDriver.set(driver);
                }
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
        });

        if (oracleDriver.get() == null) {
            System.out.println("Can not find the oracle driver.");
            return;
        }

        System.out.println("Find the oracle driver: " + oracleDriver.get().getClass().getName());
    }
}
