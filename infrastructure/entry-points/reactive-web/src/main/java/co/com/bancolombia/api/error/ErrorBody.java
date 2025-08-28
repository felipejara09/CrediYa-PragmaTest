package co.com.bancolombia.api.error;



import java.time.OffsetDateTime;
import java.util.List;

public record ErrorBody (
     String code,
     String message,
     List<String> details,
     int status,
     OffsetDateTime timestamp
){

    static ErrorBody of(String code, String msg, List<String> details, int status) {
        return new ErrorBody(code, msg, details, status, OffsetDateTime.now());
    }
}
