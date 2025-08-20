package com.melut.combatmaster.database;

import com.melut.combatmaster.CombatMaster;
import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;

import java.io.File;
import java.sql.*;
import java.util.*;
import java.util.concurrent.CompletableFuture;

public class DatabaseManager {

    private final CombatMaster plugin;
    private HikariDataSource dataSource;

    public DatabaseManager(CombatMaster plugin) {
        this.plugin = plugin;
    }

    public boolean initialize() {
        try {
            setupDataSource();
            createTables();
            return true;
        } catch (SQLException e) {
            plugin.getLogger().severe("Database başlatılırken hata oluştu: " + e.getMessage());
            e.printStackTrace();
            return false;
        }
    }

    private void setupDataSource() {
        HikariConfig config = new HikariConfig();

        String databaseType = plugin.getConfigManager().getDatabaseType().toLowerCase();

        if (databaseType.equals("mysql")) {
            config.setJdbcUrl("jdbc:mysql://" +
                    plugin.getConfigManager().getDatabaseHost() + ":" +
                    plugin.getConfigManager().getDatabasePort() + "/" +
                    plugin.getConfigManager().getDatabaseName() + "?useSSL=false&autoReconnect=true");
            config.setUsername(plugin.getConfigManager().getDatabaseUsername());
            config.setPassword(plugin.getConfigManager().getDatabasePassword());
            config.setDriverClassName("com.mysql.cj.jdbc.Driver");
        } else {
            // SQLite
            File dataFolder = plugin.getDataFolder();
            if (!dataFolder.exists()) {
                dataFolder.mkdirs();
            }
            config.setJdbcUrl("jdbc:sqlite:" + dataFolder.getAbsolutePath() + "/" + plugin.getConfigManager().getSqliteFileName());
            config.setDriverClassName("org.sqlite.JDBC");
        }

        config.setMaximumPoolSize(10);
        config.setMinimumIdle(2);
        config.setConnectionTimeout(30000);
        config.setIdleTimeout(600000);
        config.setMaxLifetime(1800000);

        dataSource = new HikariDataSource(config);
    }

    private void createTables() throws SQLException {
        String createPlayersTable = """
            CREATE TABLE IF NOT EXISTS combat_players (
                uuid VARCHAR(36) PRIMARY KEY,
                player_name VARCHAR(16) NOT NULL,
                best_combo INTEGER DEFAULT 0,
                total_hits INTEGER DEFAULT 0,
                last_updated BIGINT DEFAULT 0
            )
            """;

        try (Connection conn = getConnection();
             PreparedStatement stmt = conn.prepareStatement(createPlayersTable)) {
            stmt.executeUpdate();
        }

        // Index oluştur - MySQL uyumluluğu için DESC kaldırıldı
        String createIndex = "CREATE INDEX IF NOT EXISTS idx_best_combo ON combat_players(best_combo)";
        try (Connection conn = getConnection();
             PreparedStatement stmt = conn.prepareStatement(createIndex)) {
            stmt.executeUpdate();
        }
    }

    public Connection getConnection() throws SQLException {
        return dataSource.getConnection();
    }

    // Oyuncu verilerini kaydet
    public CompletableFuture<Void> savePlayerData(UUID playerUuid, String playerName, int bestCombo, int totalHits) {
        return CompletableFuture.runAsync(() -> {
            String sql = """
                INSERT INTO combat_players (uuid, player_name, best_combo, total_hits, last_updated)
                VALUES (?, ?, ?, ?, ?)
                ON CONFLICT(uuid) DO UPDATE SET
                    player_name = excluded.player_name,
                    best_combo = CASE WHEN excluded.best_combo > best_combo THEN excluded.best_combo ELSE best_combo END,
                    total_hits = excluded.total_hits,
                    last_updated = excluded.last_updated
                """;

            // SQLite için uyumlu hale getir
            if (!plugin.getConfigManager().getDatabaseType().equals("mysql")) {
                sql = """
                    INSERT OR REPLACE INTO combat_players (uuid, player_name, best_combo, total_hits, last_updated)
                    VALUES (?, ?, 
                        CASE WHEN (SELECT best_combo FROM combat_players WHERE uuid = ?) > ? 
                             THEN (SELECT best_combo FROM combat_players WHERE uuid = ?) 
                             ELSE ? END,
                        ?, ?)
                    """;
            }

            try (Connection conn = getConnection()) {
                if (plugin.getConfigManager().getDatabaseType().equals("mysql")) {
                    try (PreparedStatement stmt = conn.prepareStatement(sql)) {
                        stmt.setString(1, playerUuid.toString());
                        stmt.setString(2, playerName);
                        stmt.setInt(3, bestCombo);
                        stmt.setInt(4, totalHits);
                        stmt.setLong(5, System.currentTimeMillis());
                        stmt.executeUpdate();
                    }
                } else {
                    // SQLite için
                    try (PreparedStatement stmt = conn.prepareStatement(sql)) {
                        stmt.setString(1, playerUuid.toString());
                        stmt.setString(2, playerName);
                        stmt.setString(3, playerUuid.toString());
                        stmt.setInt(4, bestCombo);
                        stmt.setString(5, playerUuid.toString());
                        stmt.setInt(6, bestCombo);
                        stmt.setInt(7, totalHits);
                        stmt.setLong(8, System.currentTimeMillis());
                        stmt.executeUpdate();
                    }
                }
            } catch (SQLException e) {
                plugin.getLogger().severe("Oyuncu verisi kaydedilirken hata: " + e.getMessage());
            }
        });
    }

    // Oyuncu verilerini yükle
    public CompletableFuture<PlayerData> loadPlayerData(UUID playerUuid) {
        return CompletableFuture.supplyAsync(() -> {
            String sql = "SELECT player_name, best_combo, total_hits FROM combat_players WHERE uuid = ?";

            try (Connection conn = getConnection();
                 PreparedStatement stmt = conn.prepareStatement(sql)) {

                stmt.setString(1, playerUuid.toString());
                ResultSet rs = stmt.executeQuery();

                if (rs.next()) {
                    return new PlayerData(
                            rs.getString("player_name"),
                            rs.getInt("best_combo"),
                            rs.getInt("total_hits")
                    );
                }
            } catch (SQLException e) {
                plugin.getLogger().severe("Oyuncu verisi yüklenirken hata: " + e.getMessage());
            }

            return new PlayerData("", 0, 0);
        });
    }

    // Leaderboard verilerini al
    public CompletableFuture<List<LeaderboardEntry>> getLeaderboard(int limit) {
        return CompletableFuture.supplyAsync(() -> {
            List<LeaderboardEntry> leaderboard = new ArrayList<>();

            String sql = "SELECT player_name, best_combo FROM combat_players ORDER BY best_combo DESC LIMIT ?";

            try (Connection conn = getConnection();
                 PreparedStatement stmt = conn.prepareStatement(sql)) {

                stmt.setInt(1, limit);
                ResultSet rs = stmt.executeQuery();

                int rank = 1;
                while (rs.next()) {
                    leaderboard.add(new LeaderboardEntry(
                            rank++,
                            rs.getString("player_name"),
                            rs.getInt("best_combo")
                    ));
                }
            } catch (SQLException e) {
                plugin.getLogger().severe("Leaderboard verisi alınırken hata: " + e.getMessage());
            }

            return leaderboard;
        });
    }

    public void close() {
        if (dataSource != null && !dataSource.isClosed()) {
            dataSource.close();
        }
    }

    // Veri sınıfları
    public static class PlayerData {
        private final String playerName;
        private final int bestCombo;
        private final int totalHits;

        public PlayerData(String playerName, int bestCombo, int totalHits) {
            this.playerName = playerName;
            this.bestCombo = bestCombo;
            this.totalHits = totalHits;
        }

        public String getPlayerName() { return playerName; }
        public int getBestCombo() { return bestCombo; }
        public int getTotalHits() { return totalHits; }
    }

    public static class LeaderboardEntry {
        private final int rank;
        private final String playerName;
        private final int bestCombo;

        public LeaderboardEntry(int rank, String playerName, int bestCombo) {
            this.rank = rank;
            this.playerName = playerName;
            this.bestCombo = bestCombo;
        }

        public int getRank() { return rank; }
        public String getPlayerName() { return playerName; }
        public int getBestCombo() { return bestCombo; }
    }
}