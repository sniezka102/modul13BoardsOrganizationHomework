package OrganizatioinRequests;

import BaseTestData.BaseDataTest;
import io.restassured.path.json.JsonPath;
import io.restassured.response.Response;
import org.apache.http.HttpStatus;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;

import static io.restassured.RestAssured.given;

@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class OrganizationTest extends BaseDataTest {

    private static String companyId;

    @Test
    @Order(1)
    public void createOrganizationTests() {
        Response responseCreate = given()
                .spec(reqSpec)
                .queryParam("displayName", "Test Company")
                .queryParam("website", "https://companyTest.com")
                .queryParam("name", "new_123")
                .queryParam("desc", "company description should be longer, but dont have any ideas on")
                .when()
                .post(BASE_URL + ORGANIZATION)
                .then()
                .statusCode(HttpStatus.SC_OK)
                .extract()
                .response();
        JsonPath jsonCreate = responseCreate.jsonPath();
        companyId = jsonCreate.get("id");
        String orgName = jsonCreate.getString("displayName");

        //verification of company display name
        Assertions.assertThat(orgName).containsAnyOf("Test");
        Assertions.assertThat(orgName).isEqualTo("Test Company");

        //check if website starts with https, doesnt contains pl at the end
        Assertions.assertThat(jsonCreate.getString("website")).startsWith("https");
        Assertions.assertThat(jsonCreate.getString("website")).doesNotContain(".pl");

        //check if name is unique
        String compName = jsonCreate.get("name");
        Assertions.assertThat(compName).hasSizeGreaterThanOrEqualTo(3);
        Assertions.assertThat((compName)).containsAnyOf("_", "123");
    }

    @Test
    @Order(2)
    public void getCompanyIdAndCheckTheName() {
        Response responseGet = given()
                .spec(reqSpec)
                .when()
                .get(BASE_URL + ORGANIZATION + "/" + companyId)
                .then()
                .statusCode(HttpStatus.SC_OK)
                .extract()
                .response();
        JsonPath jsonGetId = responseGet.jsonPath();
        String orgName2 = jsonGetId.getString("displayName");
        Assertions.assertThat(orgName2).isEqualTo("Test Company");
    }

    @Test
    @Order(3)
    public void updateCompanyWebsite() {
        Response responsePut = given()
                .spec(reqSpec)
                .queryParam("website", "http://companyNotSecured.pl")
                .when()
                .put(BASE_URL + ORGANIZATION + "/" + companyId)
                .then()
                .statusCode(HttpStatus.SC_OK)
                .extract()
                .response();
        JsonPath jsonPut = responsePut.jsonPath();
        String newWeb = jsonPut.getString("website");
        Assertions.assertThat(newWeb).startsWith("http").endsWith("pl").contains("Secured");
    }

    @Test
    @Order(4)
    public void deleteCompany() {
        given()
                .spec(reqSpec)
                .when()
                .delete(BASE_URL + ORGANIZATION + "/" + companyId)
                .then()
                .statusCode(HttpStatus.SC_OK);
    }
}
