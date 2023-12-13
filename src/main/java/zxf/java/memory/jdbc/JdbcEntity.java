package zxf.java.memory.jdbc;

import java.sql.Clob;

public class JdbcEntity {
    private String id;
    private Clob data;

    public JdbcEntity(String id, Clob data) {
        this.id = id;
        this.data = data;
    }

    public String getId() {
        return id;
    }

    public Clob getData() {
        return data;
    }

    public void clearData() {
        this.data = null;
    }
}
