package database;

import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;

public class CreateUsersTable implements Migration {
    @Override
    public int getVersion() {
        return 1;
    }

    @Override
    public String getDescription() {
        return "Create users table";
    }

    @Override
    public void up(Connection connection) throws SQLException {
        String sql = """
        CREATE TABLE IF NOT EXISTS users (
            id INTEGER PRIMARY KEY AUTO_INCREMENT,
            email TEXT NOT NULL UNIQUE,
            password_hash TEXT NOT NULL,
            full_name TEXT NOT NULL,
            phone_number TEXT NOT NULL,
            blood_group TEXT,
            role TEXT NOT NULL,
            is_verified BOOLEAN DEFAULT FALSE,
            created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
        )
    """;

        try (Statement stmt = connection.createStatement()) {
            stmt.execute(sql);
        }
    }


    @Override
    public void down(Connection connection) throws SQLException {
        try (Statement stmt = connection.createStatement()) {
            stmt.execute("DROP TABLE IF EXISTS users");
        }
    }
}