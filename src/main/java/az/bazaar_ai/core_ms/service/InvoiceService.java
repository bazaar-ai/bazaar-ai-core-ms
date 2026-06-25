package az.bazaar_ai.core_ms.service;

import static az.bazaar_ai.core_ms.util.constants.ErrorConstants.USER_NOT_FOUND;

import az.bazaar_ai.core_ms.handler.exception.ResourceNotFoundException;
import az.bazaar_ai.core_ms.model.dto.invoice.InvoiceResponse;
import az.bazaar_ai.core_ms.model.dto.shared.SuccessResponse;
import az.bazaar_ai.core_ms.model.entity.Invoice;
import az.bazaar_ai.core_ms.model.entity.User;
import az.bazaar_ai.core_ms.repository.InvoiceRepository;
import az.bazaar_ai.core_ms.repository.UserRepository;
import az.bazaar_ai.core_ms.util.enums.InvoiceStatus;
import java.math.BigDecimal;
import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.Optional;
import java.util.Random;
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

    private final Random random = new Random();

    private final UserRepository userRepository;
    private final InvoiceRepository invoiceRepository;

    public SuccessResponse<Page<InvoiceResponse>> getAllInvoices(String email, int page, int size) {
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new ResourceNotFoundException(USER_NOT_FOUND));

        Pageable pageable = PageRequest.of(page, size, Sort.by(SORT_FIELD).descending());
        Page<Invoice> invoices = invoiceRepository.findByMerchantId(user.getId(), pageable);

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

    public SuccessResponse<Void> uploadInvoice(String email, MultipartFile file) {
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new ResourceNotFoundException(USER_NOT_FOUND));

        Optional<String> content = readText(file);

        Invoice invoice;

        if (content.isPresent() && isInvoiceText(content.get())) {
            invoice = parseInvoice(content.get());
        } else {
            invoice = randomInvoice();
        }

        invoice.setMerchantId(user.getId());
        invoice.setInvoiceCode(generateInvoiceCode());

        invoiceRepository.save(invoice);

        return SuccessResponse.of("invoice uploaded successfully!");
    }

    private boolean isInvoiceText(String text) {
        return text.contains("Buyer:") && text.contains("Amount:") && text.contains("Status:");
    }

    private Optional<String> readText(MultipartFile file) {

        try {
            return Optional.of(new String(file.getBytes(), StandardCharsets.UTF_8));
        } catch (Exception e) {
            return Optional.empty();
        }
    }

    private String generateInvoiceCode() {
        return invoiceRepository.findTopByOrderByCreatedAtDesc()
                .map(Invoice::getInvoiceCode)
                .map(this::nextInvoiceCode)
                .orElse("INV-0001");
    }

    private String nextInvoiceCode(String current) {
        int number = Integer.parseInt(current.replace("INV-", ""));
        return "INV-" + String.format("%04d", number + 1);
    }

    private Invoice parseInvoice(String text) {
        Invoice invoice = new Invoice();

        for (String line : text.split("\\R")) {
            if (line.startsWith("Buyer:")) {
                invoice.setBuyer(line.substring(6).trim());
            } else if (line.startsWith("Amount:")) {
                invoice.setAmount(new BigDecimal(line.substring(7).trim()));
            } else if (line.startsWith("Status:")) {
                invoice.setInvoiceStatus(
                        InvoiceStatus.valueOf(line.substring(7).trim().toUpperCase())
                );
            }
        }
        return invoice;
    }

    private Invoice randomInvoice() {
        return Invoice.builder()
                .buyer(randomBuyer())
                .amount(randomAmount())
                .invoiceStatus(randomStatus())
                .build();
    }

    private String randomBuyer() {
        List<String> buyers = List.of(
                "Azersu LLC",
                "SOCAR",
                "Kapital Bank",
                "PASHA Holding",
                "Bravo Market",
                "AzerGold"
        );

        return buyers.get(random.nextInt(buyers.size()));
    }

    private BigDecimal randomAmount() {
        return BigDecimal.valueOf(100 + random.nextInt(9900));
    }

    private InvoiceStatus randomStatus() {
        InvoiceStatus[] values = InvoiceStatus.values();
        return values[random.nextInt(values.length)];
    }
}
