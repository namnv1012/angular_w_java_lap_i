package com.laptop.shopping.service;


import com.laptop.shopping.common.ValueUtil;
import com.laptop.shopping.response.GlobalResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.validation.BindingResult;
import org.springframework.web.util.UriComponentsBuilder;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;
import java.io.IOException;
import java.net.InetAddress;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

@Slf4j
@Service
@RequiredArgsConstructor
public class GenericService {
    @Value("${service.user-entity}")
    private String userEntityService;
    private final ValidateService validateService;
    private final RestTemplateService restTemplateService;

    public String generatorCode() {
        return String.valueOf(UUID.randomUUID()).replace(ValueUtil.subtraction, ValueUtil.stringDefault).toUpperCase();
    }

    public GlobalResponse<Object> errorValidates(BindingResult bindingResult) {
        Map<String, String> errorValidates = validateService.getErrorValidate(bindingResult);
        return GlobalResponse.builder()
            .status(HttpStatus.BAD_REQUEST.value())
            .message("Tham số không hợp lệ!")
            .data(errorValidates)
            .build();
    }

    public GlobalResponse<Object> returnError(Map<String, String> errors) {
        return GlobalResponse.builder()
            .status(HttpStatus.BAD_REQUEST.value())
            .message("Tham số không hợp lệ!")
            .data(errors)
            .build();
    }

    public String createPayURL(String vnp_Version, String vnp_Command, Integer vnp_Amount, String vnp_Locale,
                               String vnp_OrderInfo) throws IOException {
        InetAddress ip = InetAddress.getLocalHost();
        Map<String, String> vnp_Params = new HashMap<>();
        Integer amount = vnp_Amount * 100;
        vnp_Params.put(ValueUtil.vnp_Version, vnp_Version);
        vnp_Params.put(ValueUtil.vnp_Command, vnp_Command);
        vnp_Params.put(ValueUtil.vnp_TmnCode, "CYBER002");
        vnp_Params.put(ValueUtil.vnp_Amount, String.valueOf(amount));
        vnp_Params.put(ValueUtil.vnp_CurrCode, "VND");
        vnp_Params.put(ValueUtil.vnp_TxnRef, this.generatorCode());
        vnp_Params.put(ValueUtil.vnp_OrderInfo, vnp_OrderInfo);
        vnp_Params.put(ValueUtil.vnp_Locale, vnp_Locale);
        vnp_Params.put(ValueUtil.vnp_ReturnUrl, "http://14.225.255.178:8184/laptopshop/#/checkout");
        vnp_Params.put(ValueUtil.vnp_IpAddr, ip.getHostAddress());
        vnp_Params.put(ValueUtil.vnp_CreateDate, this.calCreateDate());
        List<String> fieldNames = new ArrayList<>(vnp_Params.keySet());
        Collections.sort(fieldNames);
        StringBuilder hashData = new StringBuilder();
        StringBuilder query = new StringBuilder();
        Iterator<String> itr = fieldNames.iterator();
        while (itr.hasNext()) {
            String fieldName = itr.next();
            String fieldValue = vnp_Params.get(fieldName);
            if (Objects.nonNull(fieldValue) && fieldValue.length() > ValueUtil.intDefault) {
                hashData.append(fieldName);
                hashData.append(ValueUtil.equal);
                hashData.append(URLEncoder.encode(fieldValue, StandardCharsets.US_ASCII.toString()));
                query.append(URLEncoder.encode(fieldName, StandardCharsets.US_ASCII.toString()));
                query.append(ValueUtil.equal);
                query.append(URLEncoder.encode(fieldValue, StandardCharsets.US_ASCII.toString()));
                if (itr.hasNext()) {
                    query.append(ValueUtil.and);
                    hashData.append(ValueUtil.and);
                }
            }
        }
        String queryUrl = query.toString();
        String vnp_SecureHash = this.hmacSHA512("DQFPURNPMPJSYPHGDYQNRHBJVUXTESCH", hashData.toString());
        queryUrl += ValueUtil.and + ValueUtil.vnp_SecureHash + ValueUtil.equal + vnp_SecureHash;
        return "https://sandbox.vnpayment.vn/paymentv2/vpcpay.html" + ValueUtil.questionMark + queryUrl;
    }

    public String calCreateDate() {
        Calendar calendar = Calendar.getInstance(TimeZone.getTimeZone(ValueUtil.timeZone));
        SimpleDateFormat formatter = new SimpleDateFormat(ValueUtil.vnPayFormatDate);
        return formatter.format(calendar.getTime());
    }

    public String hmacSHA512(final String key, final String data) {
        try {
            if (Objects.isNull(key) || Objects.isNull(data)) {
                throw new NullPointerException();
            }
            final Mac hmac512 = Mac.getInstance(ValueUtil.HMAC_SHA512);
            byte[] hmacKeyBytes = key.getBytes();
            final SecretKeySpec secretKey = new SecretKeySpec(hmacKeyBytes, ValueUtil.HMAC_SHA512);
            hmac512.init(secretKey);
            byte[] dataBytes = data.getBytes(StandardCharsets.UTF_8);
            byte[] result = hmac512.doFinal(dataBytes);
            int capacity = 2 * result.length;
            StringBuilder sb = new StringBuilder(capacity);
            for (byte b : result) {
                sb.append(String.format(ValueUtil.formatByte, b & 0xff));
            }
            return sb.toString();
        } catch (Exception ex) {
            return ValueUtil.stringDefault;
        }
    }

    public String findOrganizationIsActive(String sub) {
        HttpHeaders httpHeaders = new HttpHeaders();
        HttpEntity<?> httpEntity = new HttpEntity<>(httpHeaders);
        UriComponentsBuilder uriComponentsBuilder = UriComponentsBuilder.fromHttpUrl(userEntityService + "/" + sub +
            "/find-organization-is-active");
        ResponseEntity<String> responseEntity = restTemplateService.restTemplate().exchange(uriComponentsBuilder.build(
            false).toUriString(), HttpMethod.GET, httpEntity, String.class);
        return responseEntity.getBody();
    }

    public Date convertToDate(String date) throws ParseException {
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat(ValueUtil.vnPayFormatDate);
        return simpleDateFormat.parse(date);
    }
}
