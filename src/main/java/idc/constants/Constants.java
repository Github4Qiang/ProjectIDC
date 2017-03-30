package idc.constants;

/**
 * Created by sky on 2017/3/11.
 */
public class Constants {

    public static final String JDBC_DRIVER = "com.mysql.jdbc.Driver";

    public static final int DBC_DATASOURCE_SIZE = 20;
    public static final Boolean SPARK_LOCAL = true;

    public static final String JDBC_URL = "jdbc:mysql://192.168.2.35:3306/idcproject?serverTimezone=GMT&userSSL=true";
    public static final String JDBC_USER = "root";
    public static final String JDBC_PASSWORD = "hadoop";
    public static final String JDBC_URL_PROD = "";
    public static final String JDBC_USER_PROD = "";
    public static final String JDBC_PASSWORD_PROD = "";

    // Task bean attribution
    public static final String TASK_ID = "taskId";
    public static final String TASK_LOWER_AGE = "lowerAge";
    public static final String TASK_HIGHER_AGE = "higherAge";
    public static final String TASK_PROFESSIONAL = "professional";
    public static final String TASK_CITY = "city";
    public static final String TASK_SEARCH_WORD = "searchWord";
    public static final String DFRAME_SEARCH_WORD = "search_keyword";
    public static final String TASK_CATEGORY = "category";
    public static final String TASK_START_SESSION_TIME = "startSessionTime";
    public static final String TASK_END_SESSION_TIME = "endSessionTime";

    public static final int NULLABLE_AGE = -1;
    public static final String STRING_NULLABLE = "null";
    public static final String STRING_ZERO_LENGTH = "";

    public static String LOCAL_APP_NAME = "Session Analysis";
    public static String LOCAL_MATER_NUM = "local[3]";

    public static String TABLE_USER_VISIT_ACTION = "clickAction";
    public static String TABLE_USER_INFO = "userInfo";
    public static String TABLE_PRODUCT_INFO = "productInfo";

    public static String LOCAL_USER_DATA_PATH = "hdfs://192.168.79.101:9000/project/idc/input/user.txt";

    public static String LOCAL_SESSION_DATA_PATH = "hdfs://192.168.79.101:9000/project/idc/input/click.log";

    public static final String DATE_TIME_FORMAT = "yyyy-MM-dd HH:mm:ss";

}
