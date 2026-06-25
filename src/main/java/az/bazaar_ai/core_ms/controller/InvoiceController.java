package az.bazaar_ai.core_ms.controller;

import az.bazaar_ai.core_ms.model.dto.invoice.InvoiceResponse;
import az.bazaar_ai.core_ms.model.dto.shared.SuccessResponse;
import az.bazaar_ai.core_ms.service.InvoiceService;
import java.security.Principal;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/invoices")
public class InvoiceController {

    private final InvoiceService invoiceService;

    @GetMapping
    public ResponseEntity<SuccessResponse<Page<InvoiceResponse>>> getAllInvoices(@RequestParam(defaultValue = "0") int page, @RequestParam(defaultValue = "10") int size) {
        return ResponseEntity.ok(invoiceService.getAllInvoices(page, size));
    }

    @PostMapping(value = "/upload-invoice", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<SuccessResponse<Void>> uploadInvoice(Principal principal, @RequestParam(value = "file", required = false) MultipartFile multipartFile) {
        return ResponseEntity.ok(invoiceService.uploadInvoice(principal.getName(), multipartFile));
    }
}
