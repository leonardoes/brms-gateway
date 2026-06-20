package gateway.web.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
@Schema(description = "SQL Query Request for database analysis")
public class SqlQueryRequest {

    @Schema(description = "SQL query to execute",
            example = "SELECT * FROM parametro LIMIT 10")
    private String sql;

    @Schema(description = "Maximum number of rows to return (default: 1000, max: 10000)",
            example = "100")
    private Integer maxRows = 1000;
}
