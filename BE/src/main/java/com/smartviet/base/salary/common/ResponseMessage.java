package com.smartviet.base.salary.common;

public class ResponseMessage {

    private ResponseMessage() {
    }

    public static final class Common {
        public static final String SUCCESS = "success";
        public static final String SYSTEM_ERROR = "system.error";
        public static final String NOT_FOUND = "not.found";
        public static final String INVALID_REQUEST = "invalid.request";
        public static final String METHOD_NOT_SUPPORTED = "method.not.supported";

        private Common() {
        }
    }

    public static final class Authentication {
        public static final String PERMISSION_DENIED = "authentication.permission.denied";
        public static final String FAILED = "authentication.failed";
        public static final String REFRESH_TOKEN_FAILED = "authentication.refresh.token.failed";
        public static final String MISSING_USERNAME = "authentication.missing.username";
        public static final String MISSING_PASSWORD = "authentication.missing.password";
        public static final String MISSING_REFRESH_TOKEN = "authentication.missing.refresh.token";
        public static final String INVALID_REFRESH_TOKEN = "authentication.invalid.refresh.token";
        public static final String UNAUTHORIZED = "authentication.unauthorized";
        public static final String INVALID_USERNAME_OR_PASSWORD = "authentication.invalid.username.or.password";
        public static final String INVALID_LENGTH_USERNAME = "authentication.invalid.length.username";
        public static final String INVALID_USERNAME_FORMAT = "authentication.invalid.username.format";
        public static final String INVALID_LENGTH_PASSWORD = "authentication.invalid.length.password";

        private Authentication() {
        }
    }

    public static final class User {
        public static final String NOT_FOUND = "user.not.found";

        private User() {
        }
    }

    public static final class Conversation {
        public static final String NOT_FOUND = "conversation.not.found";
        public static final String MISSING_ID = "conversation.missing.id";
        public static final String MISSING_CONTENT = "conversation.missing.prompt";
        public static final String MISSING_REQUEST_TIME = "conversation.missing.request.time";
        public static final String MISSING_RATING = "conversation.missing.rating";
        public static final String INVALID_CONTENT_LENGTH = "conversation.invalid.content.length";
        public static final String INVALID_REQUEST_TIME = "conversation.invalid.request.time";
        public static final String INVALID_RATING = "conversation.invalid.rating";

        private Conversation() {
        }
    }

    public static final class OptionSet {
        public static final String NOT_FOUND = "option.set.not.found";
        public static final String MISSING_ID = "option.set.missing.id";
        public static final String MISSING_CODE = "option.set.missing.code";

        private OptionSet() {
        }
    }

}
