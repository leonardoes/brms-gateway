package gateway.web.config;

import contratos.globalvida.response.ResponseAceitacaoGlobalvida;
import contratos.globalvida.response.ResponseGlobalvida;
import contratos.globalvida.response.ResponseRegraAceitacao;
import drools.RetornoRegra;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.ArrayList;
import java.util.List;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ResponseGlobalvida> handleAllExceptions(Exception ex) {
        RetornoRegra error = new RetornoRegra();
        error.setCodigoRetorno(0L);
        error.setNomeAmigavel("Tratamento de erros de negócio.");

        // Captura informações completas da exceção incluindo stack trace
        StringBuilder devolutiva = new StringBuilder();
        devolutiva.append("Erro: ").append(ex.getClass().getSimpleName());
        if (ex.getMessage() != null) {
            devolutiva.append(" - Mensagem: ").append(ex.getMessage());
        }

        // Adiciona informações da causa raiz se existir
        Throwable cause = ex.getCause();
        if (cause != null && cause.getMessage() != null) {
            devolutiva.append(" - Causa: ").append(cause.getMessage());
        }

        // Para ambiente de desenvolvimento - incluir stack trace
        // REMOVER EM PRODUÇÃO por questões de segurança
        /*StringWriter sw = new StringWriter();
        PrintWriter pw = new PrintWriter(sw);
        ex.printStackTrace(pw);
        devolutiva.append(" - Stack Trace: ").append(sw.toString());*/

        error.setDevolutiva(devolutiva.toString());
        ResponseGlobalvida response = new ResponseGlobalvida();
        //TODO adicionar regras de funcionarios e socios
        response.setRegras(List.of(error));
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ResponseGlobalvida> handleValidationExceptions(MethodArgumentNotValidException ex) {
        ResponseGlobalvida response = new ResponseGlobalvida();
        List<RetornoRegra> regras = new ArrayList<>();
        for (FieldError fieldError : ex.getBindingResult().getFieldErrors()) {
            RetornoRegra error = new RetornoRegra();
            error.setCodigoRetorno(0L);
            error.setNomeAmigavel("Dados inválidos.");
            error.setDevolutiva(fieldError.getDefaultMessage());
            regras.add(error);
        }
        //TODO adicionar regras de funcionarios e socios
        response.setRegras(regras);
        return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
    }
}
