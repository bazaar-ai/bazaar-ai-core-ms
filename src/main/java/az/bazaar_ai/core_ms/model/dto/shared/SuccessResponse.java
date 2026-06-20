package az.bazaar_ai.core_ms.model.dto.shared;

import com.fasterxml.jackson.annotation.JsonInclude;
import java.time.LocalDateTime;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.http.HttpStatus;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
@FieldDefaults(level = AccessLevel.PRIVATE)
public class SuccessResponse<T> {

    @Builder.Default
    HttpStatus status = HttpStatus.OK;

    @Builder.Default
    int statusCode = HttpStatus.OK.value();

    @Builder.Default
    LocalDateTime timestamp = LocalDateTime.now();

    String message;

    T data;

    public static SuccessResponse<Void> of(String message) {
        return SuccessResponse.<Void>builder()
                .message(message)
                .build();
    }

    public static <T> SuccessResponse<T> of(T data, String message) {
        return SuccessResponse.<T>builder()
                .message(message)
                .data(data)
                .build();
    }
}
