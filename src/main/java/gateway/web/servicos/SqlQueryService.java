/*
package gateway.web.servicos;

import gateway.web.dto.SqlQueryRequest;
import gateway.web.dto.SqlQueryResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;

import java.sql.ResultSetMetaData;
import java.util.*;

@Service
public class SqlQueryService {

    private static final Logger logger = LoggerFactory.getLogger(SqlQueryService.class);
    private static final int MAX_ROWS_LIMIT = 10000;
    private static final Set<String> FORBIDDEN_KEYWORDS = Set.of(
            "DROP", "DELETE", "TRUNCATE", "INSERT", "UPDATE",
            "CREATE", "ALTER", "GRANT", "REVOKE"
    );

    private final JdbcTemplate jdbcTemplate;

    public SqlQueryService(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    */
/**
     * Executes a SQL query and returns the results
     *
     * @param request SQL query request
     * @return SQL query response with results or error
     *//*

    public SqlQueryResponse executeQuery(SqlQueryRequest request) {
        long startTime = System.currentTimeMillis();

        try {
            // Validate request
            validateRequest(request);

            // Sanitize and validate SQL
            String sql = request.getSql().trim();
            validateSql(sql);

            // Apply row limit
            int maxRows = Math.min(
                    Optional.ofNullable(request.getMaxRows()).orElse(1000),
                    MAX_ROWS_LIMIT
            );

            // Execute query
            List<Map<String, Object>> rows = new ArrayList<>();
            List<String> columns = new ArrayList<>();

            jdbcTemplate.query(sql, rs -> {
                // Get column names from metadata (only once)
                if (columns.isEmpty()) {
                    ResultSetMetaData metaData = rs.getMetaData();
                    int columnCount = metaData.getColumnCount();
                    for (int i = 1; i <= columnCount; i++) {
                        columns.add(metaData.getColumnName(i));
                    }
                }

                // Check row limit
                if (rows.size() >= maxRows) {
                    return;
                }

                // Build row map
                Map<String, Object> row = new LinkedHashMap<>();
                for (String column : columns) {
                    row.put(column, rs.getObject(column));
                }
                rows.add(row);
            });

            long executionTime = System.currentTimeMillis() - startTime;
            logger.info("Query executed successfully in {}ms, returned {} rows", executionTime, rows.size());

            return SqlQueryResponse.success(columns, rows, executionTime);

        } catch (Exception e) {
            long executionTime = System.currentTimeMillis() - startTime;
            logger.error("Error executing query: {}", e.getMessage(), e);
            return SqlQueryResponse.error(e.getMessage(), executionTime);
        }
    }

    */
/**
     * Validates the SQL query request
     *//*

    private void validateRequest(SqlQueryRequest request) {
        if (request == null) {
            throw new IllegalArgumentException("Request cannot be null");
        }
        if (request.getSql() == null || request.getSql().trim().isEmpty()) {
            throw new IllegalArgumentException("SQL query cannot be empty");
        }
    }

    */
/**
     * Validates SQL to prevent dangerous operations
     * WARNING: This is basic validation and NOT a complete SQL injection prevention
     *//*

    private void validateSql(String sql) {
        String upperSql = sql.toUpperCase();

        // Check for forbidden keywords
        for (String keyword : FORBIDDEN_KEYWORDS) {
            if (upperSql.contains(keyword)) {
                throw new IllegalArgumentException(
                        "Query contains forbidden keyword: " + keyword + ". Only SELECT queries are allowed."
                );
            }
        }

        // Must start with SELECT (after whitespace)
        if (!upperSql.trim().startsWith("SELECT") && !upperSql.trim().startsWith("WITH")) {
            throw new IllegalArgumentException("Only SELECT or WITH (CTE) queries are allowed");
        }
    }

    */
/**
     * Get database table names for reference
     *//*

    public List<String> getTableNames() {
        String sql = """
            SELECT table_name
            FROM information_schema.tables
            WHERE table_schema = 'public'
            ORDER BY table_name
            """;

        return jdbcTemplate.queryForList(sql, String.class);
    }

    */
/**
     * Get columns for a specific table
     *//*

    public Map<String, Object> getTableInfo(String tableName) {
        String sql = """
            SELECT
                column_name,
                data_type,
                is_nullable,
                column_default
            FROM information_schema.columns
            WHERE table_schema = 'public'
            AND table_name = ?
            ORDER BY ordinal_position
            """;

        List<Map<String, Object>> columns = jdbcTemplate.queryForList(sql, tableName);

        Map<String, Object> result = new HashMap<>();
        result.put("tableName", tableName);
        result.put("columns", columns);
        return result;
    }
}
*/
