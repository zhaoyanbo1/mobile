package com.kuafu.llm.debug;

import org.springframework.boot.CommandLineRunner;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;

@Component
public class SqliteCheckRunner implements CommandLineRunner {
    private final JdbcTemplate jdbc;

    public SqliteCheckRunner(JdbcTemplate jdbc) { this.jdbc = jdbc; }

    @Override
    public void run(String... args) {
        System.out.println(">>> PRAGMA database_list = " +
                jdbc.queryForList("PRAGMA database_list;"));
        System.out.println(">>> ai_conversation = " +
                jdbc.queryForList("SELECT name FROM sqlite_master " +
                        "WHERE type='table' AND name='ai_conversation';"));
        System.out.println(">>> all tables = " +
                jdbc.queryForList("SELECT name FROM sqlite_master WHERE type='table' ORDER BY name;"));
    }
}
