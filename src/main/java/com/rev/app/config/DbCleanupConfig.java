package com.rev.app.config;

import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.jdbc.core.JdbcTemplate;
import lombok.extern.slf4j.Slf4j;

@Configuration
@Slf4j
public class DbCleanupConfig {

    @Bean
    public CommandLineRunner cleanupDatabase(JdbcTemplate jdbcTemplate) {
        return args -> {
            log.info("Starting database schema cleanup...");

            // Drop the old 'status' column from 'orders' table if it exists
            // This column was replaced by 'order_status' to bypass a stale constraint
            try {
                jdbcTemplate.execute("ALTER TABLE orders DROP COLUMN status");
                log.info("Successfully dropped ghost column 'status' from 'orders' table.");
            } catch (Exception e) {
                log.debug("Column 'status' already dropped or does not exist in 'orders' table.");
            }

            // Initialize the new 'order_status' column for any existing rows that might be
            // NULL
            try {
                jdbcTemplate.execute("UPDATE orders SET order_status = 'PENDING' WHERE order_status IS NULL");
                log.info("Initialized NULL values in 'order_status' to 'PENDING'.");
            } catch (Exception e) {
                log.error("Failed to initialize NULL values in 'order_status': {}", e.getMessage());
            }

            // Also check for 'orders' table constraints if needed
            try {
                jdbcTemplate.execute("ALTER TABLE orders DROP CONSTRAINT CHK_ORDERS_STATUS");
                log.info("Successfully dropped stale constraint 'CHK_ORDERS_STATUS'.");
            } catch (Exception e) {
                log.debug("Constraint 'CHK_ORDERS_STATUS' already dropped or does not exist.");
            }

            log.info("Database schema cleanup completed.");
        };
    }
}
