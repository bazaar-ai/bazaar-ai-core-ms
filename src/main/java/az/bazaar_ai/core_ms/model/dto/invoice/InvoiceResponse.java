package az.bazaar_ai.core_ms.model.dto.invoice;

import az.bazaar_ai.core_ms.util.enums.InvoiceStatus;
import java.math.BigDecimal;
import java.time.Instant;
import java.util.UUID;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.FieldDefaults;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class InvoiceResponse {

    UUID id;

    String invoiceCode;

    String buyer;

    BigDecimal amount;

    InvoiceStatus invoiceStatus;

    Instant createdAt;
    Instant updatedAt;
}
