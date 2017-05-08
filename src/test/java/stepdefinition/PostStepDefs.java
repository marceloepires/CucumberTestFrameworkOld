package stepdefinition;

import cucumber.api.java.Before;
import cucumber.api.java.en.Given;
import cucumber.api.java.en.When;
import files.payLoad;
import files.resources;
import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import io.restassured.path.json.JsonPath;
import io.restassured.response.Response;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.Properties;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.equalTo;


public class PostStepDefs {
    Properties prop = new Properties();
    String placeId;

    @Before
    public void readConfig() throws IOException {
        FileInputStream fileInputStream = new FileInputStream("src/properties/env.properties");
        prop.load(fileInputStream);
    }

    @Given("^I create the google place$")
    public void createThePlace() {

        RestAssured.baseURI = prop.getProperty("HOST");

        Response resp = given().
                queryParam("key", prop.getProperty("KEY")).
                body(payLoad.getPostCreateGooglePlaceData()).
                when().
                post(resources.getJsonPostResourceCreate()).
                then().assertThat().statusCode(200).and().contentType(ContentType.JSON).and().
                body("status", equalTo("OK")).
                extract().response();

        String bodyResponse = resp.asString();

        JsonPath js = new JsonPath(bodyResponse);

        placeId = js.get("place_id");

        System.out.println(placeId);


    }

    @When("^I delete the google place$")
    public void deleteThePlace(){

        given().
                queryParam("key", prop.getProperty("KEY")).
                body(payLoad.getPostDeleteGooglePlaceData(placeId)).
                when().
                post(resources.getJsonPostResourceDelete()).
                then().assertThat().statusCode(200).and().contentType(ContentType.JSON).and().
                body("status", equalTo("OK"));

    }
}
