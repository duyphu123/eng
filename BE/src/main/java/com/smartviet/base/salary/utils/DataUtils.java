package com.smartviet.base.salary.utils;

import com.smartviet.base.salary.common.Constants;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.data.domain.Page;

import java.math.BigDecimal;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.sql.Timestamp;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.*;
import java.util.concurrent.ThreadLocalRandom;

@Slf4j
public class DataUtils {

    private static final String CHARACTERS = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789";
    private static final String DIGITS = "0123456789";
    private static final String LETTERS  = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz";
    private static final char[] VIETNAMESE_CHARACTERS = {'À', 'Á', 'Â', 'Ã', 'È', 'É', 'Ê', 'Ì', 'Í', 'Ò', 'Ó', 'Ô', 'Õ', 'Ù', 'Ú', 'Ý', 'à', 'á', 'â', 'ã', 'è', 'é', 'ê', 'ì', 'í', 'ò', 'ó', 'ô', 'õ', 'ù', 'ú', 'ý', 'Ă', 'ă', 'Đ', 'đ', 'Ĩ', 'ĩ', 'Ũ', 'ũ', 'Ơ', 'ơ', 'Ư', 'ư', 'Ạ', 'ạ', 'Ả', 'ả', 'Ấ', 'ấ', 'Ầ', 'ầ', 'Ẩ', 'ẩ', 'Ẫ', 'ẫ', 'Ậ', 'ậ', 'Ắ', 'ắ', 'Ằ', 'ằ', 'Ẳ', 'ẳ', 'Ẵ', 'ẵ', 'Ặ', 'ặ', 'Ẹ', 'ẹ', 'Ẻ', 'ẻ', 'Ẽ', 'ẽ', 'Ế', 'ế', 'Ề', 'ề', 'Ể', 'ể', 'Ễ', 'ễ', 'Ệ', 'ệ', 'Ỉ', 'ỉ', 'Ị', 'ị', 'Ọ', 'ọ', 'Ỏ', 'ỏ', 'Ố', 'ố', 'Ồ', 'ồ', 'Ổ', 'ổ', 'Ỗ', 'ỗ', 'Ộ', 'ộ', 'Ớ', 'ớ', 'Ờ', 'ờ', 'Ở', 'ở', 'Ỡ', 'ỡ', 'Ợ', 'ợ', 'Ụ', 'ụ', 'Ủ', 'ủ', 'Ứ', 'ứ', 'Ừ', 'ừ', 'Ử', 'ử', 'Ữ', 'ữ', 'Ự', 'ự'};
    private static final char[] ENGLISH_CHARACTERS = {'A', 'A', 'A', 'A', 'E', 'E', 'E', 'I', 'I', 'O', 'O', 'O', 'O', 'U', 'U', 'Y', 'a', 'a', 'a', 'a', 'e', 'e', 'e', 'i', 'i', 'o', 'o', 'o', 'o', 'u', 'u', 'y', 'A', 'a', 'D', 'd', 'I', 'i', 'U', 'u', 'O', 'o', 'U', 'u', 'A', 'a', 'A', 'a', 'A', 'a', 'A', 'a', 'A', 'a', 'A', 'a', 'A', 'a', 'A', 'a', 'A', 'a', 'A', 'a', 'A', 'a', 'A', 'a', 'E', 'e', 'E', 'e', 'E', 'e', 'E', 'e', 'E', 'e', 'E', 'e', 'E', 'e', 'E', 'e', 'I', 'i', 'I', 'i', 'O', 'o', 'O', 'o', 'O', 'o', 'O', 'o', 'O', 'o', 'O', 'o', 'O', 'o', 'O', 'o', 'O', 'o', 'O', 'o', 'O', 'o', 'O', 'o', 'U', 'u', 'U', 'u', 'U', 'u', 'U', 'u', 'U', 'u', 'U', 'u', 'U', 'u'};

    private DataUtils() {
    }

    public static String objectToJson(Object object) {
        try {
            ObjectMapper objectMapper = new ObjectMapper();
            return objectMapper.writeValueAsString(object);
        } catch (Exception e) {
            return null;
        }
    }

    public static String generateRandomString(int length) {
        StringBuilder sb = new StringBuilder(length);
        for (int i = 0; i < length; i++) {
            int idx = ThreadLocalRandom.current().nextInt(CHARACTERS.length());
            sb.append(CHARACTERS.charAt(idx));
        }
        return sb.toString();
    }

    public static String generateRandomNumber(int length) {
        StringBuilder sb = new StringBuilder(length);
        for (int i = 0; i < length; i++) {
            sb.append(ThreadLocalRandom.current().nextInt(10));
        }
        return sb.toString();
    }

    public static String generateRandomNumber(int min, int max) {
        int r = ThreadLocalRandom.current().nextInt(min, max + 1);
        return String.valueOf(r);
    }

    public static String uppercaseFirstLetter(String input) {
        if (isNullOrBlank(input)) {
            return input;
        }
        char firstChar = Character.toUpperCase(input.charAt(0));
        return firstChar + input.substring(1);
    }

    public static String uppercaseAllFirstLetters(String input) {
        if (isNullOrBlank(input)) {
            return input;
        }
        String[] words = input.split("\\s");
        StringBuilder result = new StringBuilder();
        for (String word : words) {
            if (!word.isEmpty()) {
                result.append(Character.toUpperCase(word.charAt(0))).append(word.substring(1)).append(" ");
            }
        }
        return result.toString().trim();
    }

    public static <T> boolean isNullOrEmpty(T[] array) {
        return Objects.isNull(array) || Objects.equals(array.length, 0);
    }

    public static <T> boolean isNullOrEmpty(Iterable<T> iterable) {
        return Objects.isNull(iterable) || !iterable.iterator().hasNext();
    }

    public static <T> boolean isNullOrEmpty(Map<?, T> map) {
        return Objects.isNull(map) || map.isEmpty();
    }

    public static boolean isNullOrEmpty(CharSequence cs) {
        return Objects.isNull(cs) || StringUtils.isEmpty(cs);
    }

    public static boolean isNullOrBlank(CharSequence cs) {
        return Objects.isNull(cs) || StringUtils.isBlank(cs);
    }

    public static boolean isNullOrZero(Integer number) {
        return Objects.isNull(number) || Objects.equals(number, 0);
    }

    public static boolean isNullOrZero(Long number) {
        return Objects.isNull(number) || Objects.equals(number, 0L);
    }

    public static boolean isNullOrZero(Double number) {
        return Objects.isNull(number) || Objects.equals(number, 0D);
    }

    public static boolean isNullOrZero(Float number) {
        return Objects.isNull(number) || Objects.equals(number, 0F);
    }

    public static boolean isNullOrZero(String number) {
        return isNullOrBlank(number) || number.equals("0");
    }

    public static boolean isNullOrZero(Object number) {
        return Objects.isNull(number) || isNullOrZero(number.toString());
    }

    public static boolean isNullOrZero(Object[] number) {
        return Objects.isNull(number) || Objects.equals(number.length, 0);
    }

    public static boolean isNullOrZero(Iterable<?> number) {
        return Objects.isNull(number) || !number.iterator().hasNext();
    }

    public static String getIP() {
        InetAddress ip;
        try {
            ip = InetAddress.getLocalHost();
            return ip.getHostAddress();
        } catch (UnknownHostException e) {
            return "UNKNOWN";
        }
    }

    public static boolean isDate(String dateTimeStr, String formatPattern) {
        try {
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern(formatPattern);
            formatter.parse(dateTimeStr);
            return true;
        } catch (DateTimeParseException e) {
            log.error("Error when checking date", e);
            return false;
        }
    }

    public static LocalDate parseLocalDate(String dateStr) {
        try {
            if (isNullOrBlank(dateStr)) {
                return null;
            }
            if (isDate(dateStr, Constants.FormatPattern.LOCAL_DATE)) {
                DateTimeFormatter formatter = DateTimeFormatter.ofPattern(Constants.FormatPattern.LOCAL_DATE);
                return LocalDate.parse(dateStr, formatter);
            }
            return null;
        } catch (DateTimeParseException e) {
            log.error("Error when parsing local date", e);
            return null;
        }
    }

    public static LocalDateTime parseLocalDateTime(String dateTimeStr) {
        try {
            if (isNullOrBlank(dateTimeStr)) {
                return null;
            }
            if (isDate(dateTimeStr, Constants.FormatPattern.LOCAL_DATETIME)) {
                DateTimeFormatter formatter = DateTimeFormatter.ofPattern(Constants.FormatPattern.LOCAL_DATETIME);
                return LocalDateTime.parse(dateTimeStr, formatter);
            }
            return null;
        } catch (DateTimeParseException e) {
            log.error("Error when parsing local date time", e);
            return null;
        }
    }

    public static LocalDateTime parseLocalDateTime(String dateTimeStr, String formatPattern) {
        try {
            if (isNullOrBlank(dateTimeStr)) {
                return null;
            }
            if (isDate(dateTimeStr, formatPattern)) {
                DateTimeFormatter formatter = DateTimeFormatter.ofPattern(formatPattern);
                return LocalDateTime.parse(dateTimeStr, formatter);
            }
            return null;
        } catch (DateTimeParseException e) {
            log.error("Error when parsing local date time with format", e);
            return null;
        }
    }

    public static Timestamp parseTimestamp(String dateTimeStr) {
        try {
            if (isNullOrBlank(dateTimeStr)) {
                return null;
            }
            return Timestamp.valueOf(dateTimeStr);
        } catch (DateTimeParseException e) {
            log.error("Error when parsing timestamp", e);
            return null;
        }
    }

    public static String parseString(Object obj) {
        return Objects.isNull(obj) ? null : obj.toString();
    }

    public static Short parseShort(Object obj) {
        return Objects.isNull(obj) ? null : Short.parseShort(obj.toString());
    }

    public static Integer parseInteger(Object obj) {
        return Objects.isNull(obj) ? null : Integer.parseInt(obj.toString());
    }

    public static Long parseLong(Object obj) {
        return Objects.isNull(obj) ? null : Long.parseLong(obj.toString());
    }

    public static Double parseDouble(Object obj) {
        return Objects.isNull(obj) ? null : Double.parseDouble(obj.toString());
    }

    public static Boolean parseBoolean(Object obj) {
        return Objects.isNull(obj) ? null : Boolean.parseBoolean(obj.toString());
    }

    public static LocalDate parseLocalDate(Object obj) {
        return Objects.isNull(obj) ? null : parseLocalDate(obj.toString());
    }

    public static LocalDateTime parseLocalDateTime(Object obj) {
        return Objects.isNull(obj) ? null : parseLocalDateTime(obj.toString());
    }

    public static Timestamp parseTimestamp(Object obj) {
        return Objects.isNull(obj) ? null : parseTimestamp(obj.toString());
    }

    public static String parseString(Object obj, String defaultValue) {
        return Objects.isNull(obj) ? defaultValue : obj.toString();
    }

    public static Short parseShort(Object obj, Short defaultValue) {
        return Objects.isNull(obj) ? defaultValue : Short.parseShort(obj.toString());
    }

    public static Integer parseInteger(Object obj, Integer defaultValue) {
        return Objects.isNull(obj) ? defaultValue : Integer.parseInt(obj.toString());
    }

    public static Long parseLong(Object obj, Long defaultValue) {
        return Objects.isNull(obj) ? defaultValue : Long.parseLong(obj.toString());
    }

    public static Double parseDouble(Object obj, Double defaultValue) {
        return Objects.isNull(obj) ? defaultValue : Double.parseDouble(obj.toString());
    }

    public static Boolean parseBoolean(Object obj, Boolean defaultValue) {
        return Objects.isNull(obj) ? defaultValue : Boolean.parseBoolean(obj.toString());
    }

    public static LocalDate parseLocalDate(Object obj, LocalDate defaultValue) {
        return Objects.isNull(obj) ? defaultValue : parseLocalDate(obj.toString());
    }

    public static LocalDateTime parseLocalDateTime(Object obj, LocalDateTime defaultValue) {
        return Objects.isNull(obj) ? defaultValue : parseLocalDateTime(obj.toString());
    }

    public static Timestamp parseTimestamp(Object obj, Timestamp defaultValue) {
        return Objects.isNull(obj) ? defaultValue : parseTimestamp(obj.toString());
    }

    public static BigDecimal parseBigDecimal(Object obj) {
        return Objects.isNull(obj) ? null : new BigDecimal(obj.toString());
    }

    public static BigDecimal parseBigDecimal(Object obj, BigDecimal defaultValue) {
        return Objects.isNull(obj) ? defaultValue : new BigDecimal(obj.toString());
    }

    public static Map<String, Long> getRowBounds(Page<?> data, Boolean fullData) {
        long startRow;
        long endRow;
        if (Boolean.TRUE.equals(fullData)) {
            startRow = data.getContent().isEmpty() ? 0L : 1L;
            endRow = data.getContent().isEmpty() ? 0L : data.getTotalElements();
        } else {
            startRow = data.getContent().isEmpty() ? 0L : DataUtils.parseLong(data.getNumber() * data.getSize(), 0L) + 1L;
            endRow = data.getContent().isEmpty() ? 0L : DataUtils.parseLong(data.getNumber() * data.getSize(), 0L) + data.getNumberOfElements();
        }
        return Map.of(Constants.Pagination.START_ROW_KEY, startRow, Constants.Pagination.END_ROW_KEY, endRow);
    }

    public static int getSize(List<?> list) {
        return DataUtils.isNullOrEmpty(list) ? 0 : list.size();
    }

    public static List<UUID> parseStringListToUUIDList(List<String> ids) {
        if (isNullOrEmpty(ids)) {
            return Collections.emptyList();
        }
        List<UUID> uuidList = new ArrayList<>();
        for (String id : ids) {
            if (isNullOrBlank(id)) {
                continue;
            }
            try {
                uuidList.add(UUID.fromString(id));
            } catch (IllegalArgumentException e) {
                log.error("Invalid UUID format: {}", id, e);
            }
        }
        return uuidList;
    }

    public static UUID parseStringToUUID(String id) {
        if (isNullOrBlank(id)) {
            return null;
        }
         try {
            return UUID.fromString(id);
        } catch (IllegalArgumentException e) {
            log.error("Invalid UUID format: {}", id, e);
            return null;
        }
    }

    public static UUID parseStringToUUID(String id, String messageWhenError) {
        if (isNullOrBlank(id)) {
            return null;
        }
        try {
            return UUID.fromString(id);
        } catch (IllegalArgumentException e) {
            throw new IllegalArgumentException(messageWhenError);
        }
    }

    public static <T> List<T> safeCastList(Object obj, Class<T> type) {
        if (obj instanceof List<?> rawList) {
            List<T> result = new ArrayList<>();
            for (Object element : rawList) {
                if (type.isInstance(element)) {
                    result.add(type.cast(element));
                }
            }
            return result;
        }
        return Collections.emptyList();
    }

}