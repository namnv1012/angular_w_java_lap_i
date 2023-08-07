package com.laptop.shopping.restful;

import com.laptop.shopping.domain.CreatePayURLRequest;
import com.laptop.shopping.response.GlobalResponse;
import com.laptop.shopping.service.GenericService;
import com.laptop.shopping.service.PaymentService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import java.io.IOException;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api")
public class PaymentResource {

    private final PaymentService paymentService;

    private final GenericService genericService;

    @PostMapping("/create-payment-url/{servicePackageCode}")
    public ResponseEntity<GlobalResponse<Object>> createPaymentUrl(@RequestBody @Valid CreatePayURLRequest request,
                                                                   @PathVariable String servicePackageCode,
                                                                   BindingResult bindingResult) throws IOException {
        if (bindingResult.hasErrors()) {
            return ResponseEntity.ok(genericService.errorValidates(bindingResult));
        } else {
            return ResponseEntity.ok(paymentService.createPaymentUrl(servicePackageCode, request));
        }
    }

    @PostMapping("/verify-payment-result")
    public ResponseEntity<?> verifyPaymentResult(HttpServletRequest request) throws Exception {
        return ResponseEntity.ok(paymentService.verifyPaymentResult(request));
    }

}
