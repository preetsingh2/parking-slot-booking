package com.mastek.parking.common;

import lombok.NoArgsConstructor;

import static lombok.AccessLevel.PRIVATE;

public class Constants {

    @NoArgsConstructor(access = PRIVATE)
    public static final class Paths {
        public static final String BASE_PATH = "barcode";

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

                public static final String SCANNED_BARCODE = "Scanned barcode";
                public static final String STORE_NUMBER = "Store number under given store ip";
                public static final String STORE_UUID = "Store UUID under given store ip";
                public static final String PRICE_FILTER = "Set 'true' to get price details";
                public static final String PRODUCT_LOCATION_FILTER = "Set 'true' to get product location details";
                public static final String STOCK_DETAILS_FILTER = "Set 'true' to get stock details";
                public static final String COUNTRY_CODE = "Country code for barcode";
                public static final String BARCODE_LIST = "List of barcodes. Provide 'gtins' in price v4";
                public static final String EFFECTIVE_DATE_TIME = "Effective date time (of today's or tomorrow) in ISO 8601 date-time format. e.g. 2020-10-14T23:00:00Z";
                public static final String TRACE_ID = "TraceId to trace request";
                public static final String ALL_PRODUCTS = "Set 'true' to get All Product details";
                public static final String FILTERS = "List of ALL Filter - stock-level,stock-availability,deliveries,merch-groups,actual-range,planned-locations,traded-unit,all-products,price,stock,product-location,store-order,no-tus";

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
            public static final String REDIS_UNEXPECTED_ERROR = "REDIS_ERROR";
            public static final String STOCK_API_ERROR = "STOCK_API_ERROR ";
            public static final String RANGE_API_ERROR = "RANGE_API_ERROR ";
            public static final String PRODUCT_LOCATION_API_ERROR = "PRODUCT_LOCATION_API_ERROR ";
            public static final String PRODUCT_STOCK_API_ERROR = "PRODUCT_STOCK_API_ERROR ";
            public static final String TRADING_PARTNER_API_ERROR = "TRADING_PARTNER_API_ERROR ";
            public static final String PRODUCT_SERVICE_API_ERROR = "PRODUCT_SERVICE_API_ERROR ";
            public static final String INTERNAL_SERVER_ERROR = "Internal Server Error";
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
