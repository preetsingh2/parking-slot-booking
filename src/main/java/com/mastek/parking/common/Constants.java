package com.mastek.parking.common;

import lombok.NoArgsConstructor;

import static lombok.AccessLevel.PRIVATE;

public class Constants {

    @NoArgsConstructor(access = PRIVATE)
    public static final class Paths {
        public static final String BASE_PATH = "api";

        @NoArgsConstructor(access = PRIVATE)
        public static final class ApiPaths {
            public static final String PARKING_AVAILABILITY_V1 = "v1/parkingavailability";

        }
    }

    @NoArgsConstructor(access = PRIVATE)
    public static final class Swagger {


        public static final String PROJECT_DESCRIPTION = "Api to get details of a Parking";
        public static final String TEAM_NAME = "Parking Team";
        public static final String PROJECT_NAME = "Parking API";

        @NoArgsConstructor(access = PRIVATE)
        public static final class ApiTags {
            public static final String PARKING_AVAILABILITY = "Parking Availability";
        }


        @NoArgsConstructor(access = PRIVATE)
        public static final class ApiOperations {
            public static final String GET_PARKING_AVAILABILITY = "Get product details with a barcode";

        }

        @NoArgsConstructor(access = PRIVATE)
        public static final class ApiParams {

            @NoArgsConstructor(access = PRIVATE)
            public static final class Values {

            }
        }
    }

    @NoArgsConstructor(access = PRIVATE)
    public static final class ApiResponses {

        @NoArgsConstructor(access = PRIVATE)
        public static final class Message {
            public static final String SUCCESS = "Successful call";

        }

        @NoArgsConstructor(access = PRIVATE)
        public static final class Code {
            public static final int SUCCESS = 200;

        }

        @NoArgsConstructor(access = PRIVATE)
        public static class ErrorTags {
        }
    }
    @NoArgsConstructor(access = PRIVATE)
    public static class StatusCodes {
        public static final int SUCCESS = 200;
        public static final int ERROR = 400;
    }
    @NoArgsConstructor(access = PRIVATE)
    public static class StatusMessages {
        public static final String SUCCESS = "Successful";
        public static final String ERROR = "Error";
    }
}
