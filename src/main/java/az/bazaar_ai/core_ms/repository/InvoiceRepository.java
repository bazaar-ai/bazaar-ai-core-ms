package az.bazaar_ai.core_ms.repository;

import az.bazaar_ai.core_ms.model.entity.Invoice;
import java.util.UUID;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface InvoiceRepository extends JpaRepository<Invoice, UUID> {
}
