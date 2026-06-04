package com.smartviet.base.salary.common;

import java.util.Arrays;
import java.util.List;
import java.util.Locale;
import java.util.Map;

public class Constants {

    private Constants() {
    }

    public static class Common {

        private Common() {
        }
    }

    public static class Search {
        private Search() {}

        public static final int MAX_LENGTH = 1000;
    }

    public static class Regex {
        public static final String EMAIL = "^[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\\.[a-zA-Z]{2,}$";
        public static final String PHONE = "^(\\+?\\d{1,3})?[-.\\s]?\\(?\\d{1,4}?\\)?[-.\\s]?\\d{1,4}[-.\\s]?\\d{1,4}[-.\\s]?\\d{1,9}$";
        public static final String URL = "^(https?|ftp)://[^ /$.?#].[^ ]*$";
        public static final String USERNAME = "^[a-zA-Z0-9_]{3,100}$";

        private Regex() {
        }
    }

    public static class Database {
        private Database() {
        }

        public static final class Primary {
            public static final String PACKAGE_REPO = "com.smartviet.base.salary.repositories";
            public static final String PACKAGE_ENTITY = "com.smartviet.base.salary.entities";
            public static final String UNIT = "primary_datasource";
            public static final String PROPERTY_PREFIX = "spring.datasource";
            public static final String BEAN_PRIMARY_DATASOURCE = "primaryDataSource";
            public static final String BEAN_ENTITY_MANAGER_FACTORY = "entityManagerFactory";
            public static final String BEAN_TRANSACTION_MANAGER = "transactionManager";

            private Primary() {
            }
        }
    }

    public static class ExecutionCode {
        public static final String SUCCESS = "0";
        public static final String ERROR = "1";

        private ExecutionCode() {
        }

        public static class Authentication {
            public static final String KEY = "ERROR";
            public static final String UNAUTHORIZED = "UNAUTHORIZED";
            public static final String PERMISSION_DENIED = "PERMISSION_DENIED";
            public static final String EXP_TOKEN = "EXP_TOKEN";

            private Authentication() {
            }
        }
    }

    public static final class Status {
        public static final Short ACTIVE = 1;
        public static final Short INACTIVE = 0;
        public static final String ACTIVE_STR = "1";
        public static final String INACTIVE_STR = "0";

        private Status() {
        }

        public static class User {
            public static final Short LOCK = 2;

            private User() {
            }
        }
    }

    public static class Error {
        public static final String PREFIX = "Error";
        public static final String PREFIX_STRING = "Error {} [{}]: {}";
        public static final String PREFIX_DETAIL = "Error {} [{}]: ";

        private Error() {
        }
    }

    public static class Security {
        public static final Long TOKEN_EXPIRATION_TIME = 60 * 60 * 1000L * 12;  // (đơn vị mls) 12 giờ
        public static final Long TOKEN_REFRESH_EXPIRATION_TIME = 3 * 24 * 60 * 60 * 1000L;  // (đơn vị mls) 3 ngày
        public static final String TOKEN_PREFIX = "Bearer ";
        public static final String REQUEST_HEADER_AUTH = "Authorization";

        private Security() {
        }

        public static class Claims {
            public static final String USER_ID = "userId";
            public static final String USERNAME = "username";
            public static final String FULL_NAME = "fullName";
            public static final String ROLES = "roles";
            public static final String SIGNATURE = "signature";

            private Claims() {
            }
        }

        public static class Role {
            public static final String PREFIX = "ROLE_";
            public static final String SUPER_ADMIN = "SUPER_ADMIN";
            public static final String ADMIN = "ADMIN";
            public static final String STAFF = "STAFF";
            public static final List<String> toList = Arrays.asList(SUPER_ADMIN, ADMIN, STAFF);
            public static final Map<String, String> toMap = Map.of(SUPER_ADMIN, SUPER_ADMIN, ADMIN, ADMIN, STAFF, STAFF);

            private Role() {
            }
        }
    }

    public static class FormatPattern {
        public static final String LOCAL_DATETIME = "yyyy-MM-dd'T'HH:mm:ss";
        public static final String LOCAL_DATETIME_WITHOUT_T = "yyyy-MM-dd HH:mm:ss";
        public static final String LOCAL_DATETIME_WITH_NANOSECONDS = "yyyy-MM-dd HH:mm:ss.SSSSSS";
        public static final String LOCAL_DATE = "yyyy-MM-dd";

        private FormatPattern() {
        }
    }

    public static class Language {
        public static final String LO = "lo";
        public static final String VI = "vi";
        public static final String EN = "en";
        public static final String ZH = "zh";
        public static final String LAO = "lao";
        public static final String VIE = "vie";
        public static final String ENG = "eng";
        public static final String ZHO = "zho";
        public static final String ALL = "all";
        public static final String FOLDER = "templates/";
        public static final String RS_VI = "vi/";
        public static final String RS_EN = "en/";
        public static final String RS_LO = "lo/";
        private static final String[] VALUES_ARRAY = {LO, VI, EN, ZH, LAO, VIE, ENG, ZHO};
        private static final List<Locale> LOCALES = Arrays.asList(new Locale(LO), new Locale(VI), new Locale(EN), new Locale(ZH));

        private Language() {
        }

        public static List<String> toList() {
            return Arrays.asList(VALUES_ARRAY);
        }

        public static List<Locale> getLocales() {
            return LOCALES;
        }
    }

    public static class Pagination {
        public static final String PAGE_KEY = "pagination";
        public static final String DEFAULT_PAGE_STR = "1";
        public static final String DEFAULT_SIZE_STR = "10";
        public static final String START_ROW_KEY = "startRow";
        public static final String END_ROW_KEY = "endRow";

        private Pagination() {
        }
    }

    public static class OptionSet {
        private OptionSet() {
        }

        public static final class OptionSetCode {
            public static final String PROJECT_PREFIX = "PROJECT_PREFIX";

            private OptionSetCode() {
            }
        }
    }

    public static class ChangeType {
        private ChangeType() {
        }

        public static final short CREATE = 1;
        public static final short UPDATE = 2;
        public static final short DELETE = 3;
    }

    public static class IconType {
        private IconType() {
        }

        public static final short NON = 0;
        public static final short FONT = 1;
        public static final short SVG = 2;
        public static final short IMAGE = 3;
    }

}
