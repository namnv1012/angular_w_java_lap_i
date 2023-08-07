package com.laptop.shopping.service;

import com.laptop.shopping.common.ValueUtil;
import com.laptop.shopping.domain.CreatePayURLRequest;
import com.laptop.shopping.response.GlobalResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;


import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.*;

@Slf4j
@Service
@RequiredArgsConstructor
public class PaymentService {

    private final GenericService genericService;

    private final com.laptop.shopping.repository.iRepository.ITransactionRepository ITransactionRepository;

    public GlobalResponse<Object> createPaymentUrl(String servicePackageCode, CreatePayURLRequest createPayURLRequest)
        throws IOException {
        String urlPayment = genericService.createPayURL(createPayURLRequest.getVnp_Version(),
            createPayURLRequest.getVnp_Command(), createPayURLRequest.getVnp_Amount(),
            createPayURLRequest.getVnp_Locale(), servicePackageCode);
        return GlobalResponse.builder()
            .status(HttpStatus.OK.value())
            .message("Thành công!")
            .data(urlPayment)
            .build();
    }

    public GlobalResponse<Object> verifyPaymentResult(HttpServletRequest request) throws Exception {
        GlobalResponse<Object> globalResponse;
        Map<String, String> fields = new HashMap<>();
        for (Enumeration<String> params = request.getParameterNames(); params.hasMoreElements(); ) {
            String fieldName = URLEncoder.encode(params.nextElement(), StandardCharsets.US_ASCII.toString());
            String fieldValue = URLEncoder.encode(request.getParameter(fieldName), StandardCharsets.US_ASCII.toString());
            if (Objects.nonNull(fieldValue) && fieldValue.length() > ValueUtil.intDefault) {
                fields.put(fieldName, fieldValue);
            }
        }
        String vnp_SecureHash = request.getParameter(ValueUtil.vnp_SecureHash);
        fields.remove(ValueUtil.vnp_SecureHashType);
        fields.remove(ValueUtil.vnp_SecureHash);
        String signValue = this.hashAllFields(fields);
        if (signValue.equals(vnp_SecureHash)) {
            if (request.getParameter(ValueUtil.vnp_ResponseCode).equals(ValueUtil.vnp_SuccessCode) &&
                request.getParameter(ValueUtil.vnp_TransactionStatus).equals(ValueUtil.vnp_SuccessCode)) {
                globalResponse = GlobalResponse.builder()
                    .status(HttpStatus.OK.value())
                    .message("Giao dịch thanh toán thành công!")
                    .data(null)
                    .build();
            } else {
                globalResponse = GlobalResponse.builder()
                    .status(HttpStatus.INTERNAL_SERVER_ERROR.value())
                    .message("Giao dịch thanh toán không thành công!")
                    .data(null)
                    .build();
            }
        } else {
            globalResponse = GlobalResponse.builder()
                .status(HttpStatus.INTERNAL_SERVER_ERROR.value())
                .message("Dữ liệu không hợp lệ!")
                .data(null)
                .build();
        }
        return globalResponse;
    }

    public String hashAllFields(Map<String, String> fields) {
        List<String> fieldNames = new ArrayList<>(fields.keySet());
        Collections.sort(fieldNames);
        StringBuilder sb = new StringBuilder();
        Iterator<String> itr = fieldNames.iterator();
        while (itr.hasNext()) {
            String fieldName = itr.next();
            String fieldValue = fields.get(fieldName);
            if (Objects.nonNull(fieldValue) && fieldValue.length() > ValueUtil.intDefault) {
                sb.append(fieldName);
                sb.append(ValueUtil.equal);
                sb.append(fieldValue);
            }
            if (itr.hasNext()) {
                sb.append(ValueUtil.and);
            }
        }
        return genericService.hmacSHA512("DQFPURNPMPJSYPHGDYQNRHBJVUXTESCH", sb.toString());
    }
}
