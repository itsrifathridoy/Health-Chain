package database;

import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;

public class DatabaseMigration {
    private static final Logger logger = Logger.getLogger(DatabaseMigration.class.getName());
    private static final List<Migration> migrations = new ArrayList<>();

    static {
        // Register all migrations here in order
        migrations.add(new CreateUsersTable());
        migrations.add(new CreateHospitalsTable()); // Added hospital table migration
    }

    public static void migrate(Connection connection) {
        try {
            createMigrationTable(connection);

            for (Migration migration : migrations) {
                if (!isMigrationApplied(connection, migration.getVersion())) {
                    logger.info("Applying migration: " + migration.getDescription());
                    migration.up(connection);
                    recordMigration(connection, migration);
                }
            }
        } catch (SQLException e) {
            logger.severe("Migration failed: " + e.getMessage());
            throw new RuntimeException("Database migration failed", e);
        }
    }

    private static void createMigrationTable(Connection connection) throws SQLException {
        String sql = """
            CREATE TABLE IF NOT EXISTS migrations (
                version INTEGER PRIMARY KEY,
                description TEXT NOT NULL,
                applied_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
            )
        """;

        try (Statement stmt = connection.createStatement()) {
            stmt.execute(sql);
        }
    }

    private static boolean isMigrationApplied(Connection connection, int version) throws SQLException {
        var sql = "SELECT COUNT(*) FROM migrations WHERE version = ?";
        try (var stmt = connection.prepareStatement(sql)) {
            stmt.setInt(1, version);
            var rs = stmt.executeQuery();
            return rs.next() && rs.getInt(1) > 0;
        }
    }

    private static void recordMigration(Connection connection, Migration migration) throws SQLException {
        var sql = "INSERT INTO migrations (version, description) VALUES (?, ?)";
        try (var stmt = connection.prepareStatement(sql)) {
            stmt.setInt(1, migration.getVersion());
            stmt.setString(2, migration.getDescription());
            stmt.executeUpdate();
        }
    }
}

