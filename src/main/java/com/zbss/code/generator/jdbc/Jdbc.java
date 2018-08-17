package com.zbss.code.generator.jdbc;

import com.alibaba.fastjson.JSONObject;
import com.zbss.code.generator.config.Config;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Iterator;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicInteger;

public class Jdbc {

    private static volatile Jdbc instance = null;

    private String driver;
    private String url;
    private String user;
    private String pwd;
    private Map<AtomicInteger, Connection> connMap = new ConcurrentHashMap<>();

    private Jdbc(String driver, String url, String user, String pwd) {
        this.driver = driver;
        this.url = url;
        this.user = user;
        this.pwd = pwd;
        init();
    }

    private void init() {
        try {
            Class.forName(driver);
            initPool(10);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void initPool(Integer cap) throws SQLException {
        int c = (cap != null && cap > 0) ? cap : 10;
        for (int i = 0; i < c; i++) {
            connMap.put(new AtomicInteger(0), DriverManager.getConnection(url, user, pwd));
        }
    }

    private Connection getConnectionInternal() {
        Connection conn = null;
        Iterator<Map.Entry<AtomicInteger, Connection>> iter = connMap.entrySet().iterator();
        while (iter.hasNext()) {
            Map.Entry<AtomicInteger, Connection> entry = iter.next();
            if (entry.getKey().get() == 0) {
                if (0 == entry.getKey().getAndSet(1)) {
                    conn = entry.getValue();
                    break;
                }
            }
        }

        if (conn == null) {
            try {
                conn = DriverManager.getConnection(url, user, pwd);
                connMap.put(new AtomicInteger(0), conn);
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }

        return conn;
    }

    public static Jdbc getInstance(String driver, String url, String user, String pwd) {
        if (instance == null) {
            synchronized (Jdbc.class) {
                if (instance == null) {
                    return new Jdbc(driver, url, user, pwd);
                }
            }
        }
        return instance;
    }

    public static Jdbc getInstance(Config config) {
        JSONObject conf = config.getConfig();
        JSONObject jdbcObj = conf.getJSONObject("jdbc");
        String driver = jdbcObj.getString("driver");
        String url = jdbcObj.getString("url");
        String user = jdbcObj.getString("user");
        String pwd = jdbcObj.getString("pwd");
        return getInstance(driver, url, user, pwd);
    }

    public Connection getConnection() {
        Connection conn = null;
        while (conn == null) {
            conn = getConnectionInternal();
        }
        return conn;
    }

    public void returnConnection(Connection connection) {
        Iterator<Map.Entry<AtomicInteger, Connection>> iter = connMap.entrySet().iterator();
        while (iter.hasNext()) {
            Map.Entry<AtomicInteger, Connection> entry = iter.next();
            if (entry.getValue().equals(connection)) {
                entry.getKey().getAndSet(0);
                break;
            }
        }
    }

}
