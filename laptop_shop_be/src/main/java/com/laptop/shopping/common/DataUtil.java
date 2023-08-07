package com.laptop.shopping.common;

import com.google.common.collect.Lists;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.reflect.Method;
import java.math.BigDecimal;
import java.math.BigInteger;

import java.util.*;

public class DataUtil {
    private static final Logger logger = LoggerFactory.getLogger(DataUtil.class);
    private DataUtil() {
        throw new IllegalStateException("DataUtils class Not Need To Initial");
    }

    /**
     * Upper first character
     *
     * @param input
     * @return
     */
    public static String upperFirstChar(String input) {
        if (DataUtil.isNullOrEmpty(input)) {
            return input;
        }
        return input.substring(0, 1).toUpperCase() + input.substring(1);
    }

    /**
     * @param obj1 Object
     * @return String
     */
    public static String safeToString(Object obj1, String defaultValue) {
        if (obj1 == null || obj1.toString().isEmpty()) {
            return defaultValue;
        }

        return obj1.toString();
    }

    /**
     * @param obj1 Object
     * @return String
     */
    public static String safeToString(Object obj1) {
        return safeToString(obj1, "");
    }

    public static boolean isNullOrEmpty(CharSequence cs) {
        int strLen;
        if (cs == null || (strLen = cs.length()) == 0) {
            return true;
        }
        for (int i = 0; i < strLen; i++) {
            if (!Character.isWhitespace(cs.charAt(i))) {
                return false;
            }
        }
        return true;
    }

    public static boolean isNullOrEmpty(final Object[] collection) {
        return collection == null || collection.length == 0;
    }

    /**
     * @param obj1
     * @return
     * @deprecated Ham nay mac du nhan tham so truyen vao la object nhung gan nhu chi hoat dong cho doi tuong la string
     * Chuyen sang dung isNullOrEmpty thay the
     */
    @Deprecated
    public static boolean isStringNullOrEmpty(Object obj1) {
        return obj1 == null || "".equals(obj1.toString().trim());
    }

    public static BigInteger length(BigInteger from, BigInteger to) {
        return to.subtract(from).add(BigInteger.ONE);
    }

    public static BigDecimal add(BigDecimal number1, BigDecimal number2, BigDecimal... numbers) {
        List<BigDecimal> realNumbers = Lists.newArrayList(number1, number2);
        if (!DataUtil.isNullOrEmpty(numbers)) {
            Collections.addAll(realNumbers, numbers);
        }
        return realNumbers.stream().filter(Objects::nonNull).reduce(BigDecimal.ZERO, BigDecimal::add);
    }

    public static Long add(Long number1, Long number2, Long... numbers) {
        List<Long> realNumbers = Lists.newArrayList(number1, number2);
        if (!DataUtil.isNullOrEmpty(numbers)) {
            Collections.addAll(realNumbers, numbers);
        }
        return realNumbers.stream().filter(Objects::nonNull).reduce(0L, (x, y) -> x + y);
    }

    /**
     * add
     *
     * @param obj1 BigDecimal
     * @param obj2 BigDecimal
     * @return BigDecimal
     */
    public static BigInteger add(BigInteger obj1, BigInteger obj2) {
        if (obj1 == null) {
            return obj2;
        } else if (obj2 == null) {
            return obj1;
        }

        return obj1.add(obj2);
    }

    /**
     * @param source       object list
     * @param propertyName name of property
     * @param returnClass  class of property
     * @return value list of property
     * @deprecated Collect values of a property from an object list instead of doing a for:each then call a getter
     * Consider using stream -> map -> collect of java 8 instead
     */
    @Deprecated
    public static <T> List<T> collectProperty(Collection<?> source, String propertyName, Class<T> returnClass) {
        List<T> propertyValues = Lists.newArrayList();
        try {
            String getMethodName = "get" + upperFirstChar(propertyName);
            for (Object x : source) {
                Class<?> clazz = x.getClass();
                Method getMethod = clazz.getMethod(getMethodName);
                Object propertyValue = getMethod.invoke(x);
                if (propertyValue != null && returnClass.isAssignableFrom(propertyValue.getClass())) {
                    propertyValues.add(returnClass.cast(propertyValue));
                }
            }
            return propertyValues;
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
            return Lists.newArrayList();
        }
    }

    public static Date add(Date fromDate, int num, int type) {
        Calendar cal = Calendar.getInstance();
        cal.setTime(fromDate);
        cal.add(type, num);
        return cal.getTime();
    }
}
