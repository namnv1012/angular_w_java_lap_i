package com.laptop.shopping.config;

import com.laptop.shopping.common.ValueUtil;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.AuditorAware;

import javax.servlet.http.HttpServletRequest;
import java.util.Objects;
import java.util.Optional;

public class AuditorProvider implements AuditorAware<String> {
    @Autowired
    private HttpServletRequest httpServletRequest;

    @Override
    public Optional<String> getCurrentAuditor() {
        String sub = httpServletRequest.getHeader(ValueUtil.subValueDefault);
        if (Objects.nonNull(sub) && StringUtils.isNotBlank(sub)) {
            return Optional.of(sub);
        }
        return Optional.empty();
    }
}
