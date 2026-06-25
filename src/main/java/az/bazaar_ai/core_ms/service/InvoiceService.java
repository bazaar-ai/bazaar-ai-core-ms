package az.bazaar_ai.core_ms.service;

import az.bazaar_ai.core_ms.model.dto.invoice.InvoiceResponse;
import az.bazaar_ai.core_ms.model.dto.shared.SuccessResponse;
import az.bazaar_ai.core_ms.model.entity.Invoice;
import az.bazaar_ai.core_ms.repository.InvoiceRepository;
import az.bazaar_ai.core_ms.util.enums.InvoiceStatus;
import java.math.BigDecimal;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

@Slf4j
@Service
@RequiredArgsConstructor
public class InvoiceService {

    private final static String SORT_FIELD = "createdAt";

    private final InvoiceRepository invoiceRepository;

    public SuccessResponse<Page<InvoiceResponse>> getAllInvoices(int page, int size) {
        Pageable pageable = PageRequest.of(page, size, Sort.by(SORT_FIELD).descending());
        Page<Invoice> invoices =  invoiceRepository.findAll(pageable);
        return SuccessResponse.of(invoices.map(this::toInvoiceResponse), "invoices retrieved successfully!");
    }

    private InvoiceResponse toInvoiceResponse(Invoice invoice) {
        return InvoiceResponse.builder()
                .id(invoice.getId())
                .invoiceCode(invoice.getInvoiceCode())
                .invoiceStatus(invoice.getInvoiceStatus())
                .amount(invoice.getAmount())
                .buyer(invoice.getBuyer())
                .updatedAt(invoice.getUpdatedAt())
                .createdAt(invoice.getCreatedAt())
                .build();
    }

    public SuccessResponse<Void> uploadInvoice(String email, MultipartFile multipartFile) {
        Invoice invoice = Invoice.builder()
                .invoiceCode("INV-000")
                .buyer("Azersu LLC")
                .amount(BigDecimal.valueOf(12.500))
                .invoiceStatus(InvoiceStatus.PENDING)
                .build();
        invoiceRepository.save(invoice);
        return SuccessResponse.of("invoice uploaded successfully!");
    }
}
