package gateway.web.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;
import java.util.Map;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "SQL Query Response")
public class SqlQueryResponse {

    @Schema(description = "List of column names")
    private List<String> columns;

    @Schema(description = "Query result rows (each row is a map of column name to value)")
    private List<Map<String, Object>> rows;

    @Schema(description = "Number of rows returned")
    private Integer rowCount;

    @Schema(description = "Execution time in milliseconds")
    private Long executionTimeMs;

    @Schema(description = "Error message if query failed")
    private String error;

    @Schema(description = "Whether the query was successful")
    private Boolean success;

    public static SqlQueryResponse success(List<String> columns, List<Map<String, Object>> rows, long executionTimeMs) {
        SqlQueryResponse response = new SqlQueryResponse();
        response.setColumns(columns);
        response.setRows(rows);
        response.setRowCount(rows.size());
        response.setExecutionTimeMs(executionTimeMs);
        response.setSuccess(true);
        return response;
    }

    public static SqlQueryResponse error(String errorMessage, long executionTimeMs) {
        SqlQueryResponse response = new SqlQueryResponse();
        response.setError(errorMessage);
        response.setExecutionTimeMs(executionTimeMs);
        response.setSuccess(false);
        response.setRowCount(0);
        return response;
    }
}
