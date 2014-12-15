package com.worldpay.sdk.util;

/**
 * Created by Sudhakar on 14/11/2014.
 */
public final class WorldpayLibraryConstants {

    private WorldpayLibraryConstants(){
        // this prevents from creating instances of this class
    }
    /**
     * Connection timeout in milliseconds.
     */
    public static final int CONNECTION_TIMEOUT = 10000;
    /**
     * Socket timeout in milliseconds.
     */
    public static final int SOCKET_TIMEOUT = 10000;

    public static final java.lang.String ACCEPT = "Accept";

    public static final java.lang.String AUTHORIZATION = "Authorization";

    public static final java.lang.String CONTENT_TYPE = "Content-Type";

    public static final java.lang.String WP_CLIENT_USER_AGENT = "X-wp-client-user-agent";
    /**
     * JSON header value.
     */
    public static final String APPLICATION_JSON = "application/json";

    public static final String COMMA = ",";

    public static final String GET = "GET";

    public static final String POST = "POST";

    public static final String PUT = "PUT";

    public static final String DELETE = "DELETE";

    public static final int HTTP_ERROR_CODE_300 = 300;

    public static final String OS_NAME_PROP = "os.name=" + System.getProperty("os.name");

    public static final String OS_VERSION_PROP = "os.version=" + System.getProperty("os.version");

    public static final String OS_ARCH_PROP = "os.arch=" + System.getProperty("os.arch");

    public static final String LANG_VERSION_PROP = "lang.version=" + System.getProperty("java.vm.specification.version");

    public static final String JAVA_VENDOR_PROP = "java.vendor=" + System.getProperty("java.vendor");

    public static final String JVM_VENDOR_PROP = "jvm.vendor=" + System.getProperty("java.vm.vendor");

    public static final String LIB_VERSION_PROP = "lib.version=1.2";

    public static final String API_VERSION_PROP = "api.version=V1";

    public static final String OWNER_PROP = "owner=Worldpay";

    public static final String EOF = "\\u001a";
}
