package BaseTestData;

import io.restassured.builder.RequestSpecBuilder;
import io.restassured.http.ContentType;
import io.restassured.specification.RequestSpecification;
import org.junit.jupiter.api.BeforeAll;

public class BaseDataTest {
    protected static final String KEY = "<Your key>";
    protected static final String TOKEN = "<Your token>";

    protected static final String BASE_URL = "https://api.trello.com/1/";
    protected static final String BOARD = "boards";
    protected static final String LISTS = "lists";
    protected static final String CARD = "cards";
    protected static final String ORGANIZATION = "organizations";

    protected static RequestSpecification reqSpec;
    protected static RequestSpecBuilder reqBuilder;

    @BeforeAll
    public static void beforeAll() {
        reqBuilder = new RequestSpecBuilder();
        reqBuilder.addQueryParam("key", KEY);
        reqBuilder.addQueryParam("token", TOKEN);
        reqBuilder.setContentType(ContentType.JSON);


        reqSpec = reqBuilder.build();
    }
}
