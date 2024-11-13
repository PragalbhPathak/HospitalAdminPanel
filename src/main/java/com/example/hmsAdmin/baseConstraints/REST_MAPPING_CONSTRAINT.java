package com.example.hmsAdmin.baseConstraints;

public interface REST_MAPPING_CONSTRAINT {
    String BASE_URL = "/api/admin";

    interface MESSAGE_NAMES{
        String COMMON_MESSAGE_CREATION = "Data Saved Successfully";
        String COMMON_MESSAGE_UPDATION = "Data Updated Successfully";
        String COMMON_MESSAGE_DEACTIVATION = "Data is not currently active";
        String COMMON_MESSAGE_INVALID = "Invalid Credentials";
        String COMMON_MESSAGE_FORMAT = "Invalid Format";
        String COMMON_ERROR = "Something went wrong";
        String FIELD_REQUIRED_MESSAGE="Field is required";
        String COMMON_MESSAGE_DATA_FETCHED="Data fetched  successfully";
        String NOT_PRESENT="Data not found";
        String PARTIAL_DATA="Some data were not processed";
    }

    interface DEFINE_API{
        String CREATE_OR_UPDATE_ADMIN = "/createAdmin";
        String FETCH_ADMINS = "/getAdmin";

        String CREATE_OR_UPDATE_USERS = "/createUser";
        String FETCH_USERS = "/getUser";

        String LOGIN_ADMIN = "/loginAdmin";
    }
    interface SUCCESS_CODES{
       // String COMMON_MESSAGE = "Data Save SuccessFully";
        int SUCCESS = 1;
        int FAILURE = 0;
    }
    interface STATUS_CODES{
        String INTERNAL_SERVER_ERROR="500";
        String SUCCESS_OK="200";
        String  PARTIAL_STATUS="207";
        String BAD_REQUEST= "400";
        String UNAUTHORIZED = "401";
        String FORBIDDEN = "403";
        String NOT_FOUND ="404";
    }
}
