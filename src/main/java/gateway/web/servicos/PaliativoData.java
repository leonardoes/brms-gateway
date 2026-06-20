package gateway.web.servicos;

import lombok.extern.slf4j.Slf4j;

import java.text.SimpleDateFormat;
import java.util.Date;

@Slf4j
public final class PaliativoData {
    private PaliativoData() {
    }

    public static Date parseMotor(String data) throws Exception {
        return new SimpleDateFormat(AbaccusDatePattern.DATE_PATTERN_MOTOR).parse(data);
    }

    public static Date parseDataHora(String data) throws Exception {
        return new SimpleDateFormat(AbaccusDatePattern.DATE_PATTERN_HORA).parse(data);
    }

    public static String parseToMotor(String data) throws Exception {
        try {
            parseMotor(data);
            return data;
        } catch (Exception e) {
            try {
                Date dataHora = parseDataHora(data);
                return new SimpleDateFormat(AbaccusDatePattern.DATE_PATTERN_MOTOR).format(dataHora);
            } catch (Exception exception) {
                log.error("Data: " + data);
                return data;
            }
        }
    }
}
