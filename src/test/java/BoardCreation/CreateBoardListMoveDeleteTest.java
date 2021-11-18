package BoardCreation;

import BaseTestData.BaseDataTest;
import io.restassured.path.json.JsonPath;
import io.restassured.response.Response;
import org.apache.http.HttpStatus;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;

import static io.restassured.RestAssured.given;

public class CreateBoardListMoveDeleteTest extends BaseDataTest {

    @Test
    //create empty board
    public void createNewBoard() {
        Response response = given()
                .spec(reqSpec)
                .queryParam("name", "testBoard")
                .queryParam("defaultLists", false)
                .when()
                .post(BASE_URL + BOARD)
                .then()
                .statusCode(HttpStatus.SC_OK)
                .extract()
                .response();
        JsonPath json = response.jsonPath();
        String boardId = json.get("id");
        String boardName = json.getString("name");
        Assertions.assertThat(boardName).isEqualTo("testBoard");

        //create first list
        Response responseList01 = given()
                .spec(reqSpec)
                .queryParam("name", "first list - inProgress")
                .queryParam("idBoard", boardId)
                .when()
                .post(BASE_URL + LISTS)
                .then()
                .statusCode(HttpStatus.SC_OK)
                .extract()
                .response();
        JsonPath jsonFirstList = responseList01.jsonPath();
        String firstListId = jsonFirstList.get("id");
        String firstListName = jsonFirstList.getString("name");
        Assertions.assertThat(firstListName).isEqualTo("first list - inProgress");

        //create second list
        Response responseList02 = given()
                .spec(reqSpec)
                .queryParam("name", "second list - done")
                .queryParam("idBoard", boardId)
                .when()
                .post(BASE_URL + LISTS)
                .then()
                .statusCode(HttpStatus.SC_OK)
                .extract()
                .response();
        JsonPath jsonSecondList = responseList02.jsonPath();
        String secondListId = jsonSecondList.get("id");
        String secondListName = jsonSecondList.getString("name");
        Assertions.assertThat(secondListName).isEqualTo("second list - done");

        //create a card on the first list
        Response responseCard = given()
                .spec(reqSpec)
                .queryParam("name", "card from the first list")
                .queryParam("idList", firstListId)
                .when()
                .post(BASE_URL + CARD)
                .then()
                .statusCode(HttpStatus.SC_OK)
                .extract()
                .response();
        JsonPath jsonCard = responseCard.jsonPath();
        String cardId = jsonCard.get("id");
        String cardName = jsonCard.getString("name");
        Assertions.assertThat(cardName).isEqualTo("card from the first list");

        //move card on the second list
        Response responseCardMoved = given()
                .spec(reqSpec)
                .queryParam("idBoard", boardId)
                .queryParam("idList", secondListId)
                .when()
                .put(BASE_URL + CARD + "/" + cardId)
                .then()
                .statusCode(HttpStatus.SC_OK)
                .extract()
                .response();
        JsonPath jsonCardMoved = responseCardMoved.jsonPath();
        Assertions.assertThat(jsonCardMoved.getString("idList")).isEqualTo(secondListId);

        //delete board
        given()
                .spec(reqSpec)
                .queryParam("idBoard", boardId)
                .when()
                .delete(BASE_URL + BOARD + "/" + boardId)
                .then()
                .statusCode(HttpStatus.SC_OK);
    }
}
