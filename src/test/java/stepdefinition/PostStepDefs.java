package stepdefinition;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.equalTo;

import java.io.FileInputStream;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Properties;

import cucumber.api.java.Before;
import cucumber.api.java.en.Given;
import cucumber.api.java.en.When;
import files.ExtractPlaceId;
import files.payLoad;
import files.resources;
import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import io.restassured.path.json.JsonPath;
import io.restassured.path.xml.XmlPath;
import io.restassured.response.Response;


public class PostStepDefs {
    Properties prop = new Properties();
    String placeId;

    @Before
    public void readConfig() throws IOException {
        FileInputStream fileInputStream = new FileInputStream("src/properties/env.properties");
        prop.load(fileInputStream);
    }

    @Given("^I create the google place in Json$")
    public void createThePlaceJson() {

        RestAssured.baseURI = prop.getProperty("HOST");

        Response resp = given().
                queryParam("key", prop.getProperty("KEY")).
                body(payLoad.getJsonPostCreateGooglePlaceData()).
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

    @When("^I delete the google place in Json$")
    public void deleteThePlaceJson(){

        given().
                queryParam("key", prop.getProperty("KEY")).
                body(payLoad.getJsonPostDeleteGooglePlaceData(placeId)).
                when().
                post(resources.getJsonPostResourceDelete()).
                then().assertThat().statusCode(200).and().contentType(ContentType.JSON).and().
                body("status", equalTo("OK"));

    }

    @Given("^I create the google place in XML")
    public void createThePlaceXml() throws IOException {

        RestAssured.baseURI = prop.getProperty("HOST");

        String postData = GenerateStringFromResource("src/XML/postData.xml");

        Response resp = given().
                queryParam("key", prop.getProperty("KEY")).
                body(postData).
                when().
                post(resources.getXmlPostResourceCreate()).
                then().assertThat().statusCode(200).and().contentType(ContentType.XML).and().
                extract().response();

        XmlPath x = ExtractPlaceId.extractPlaceIdXml(resp);
        placeId = x.get("PlaceAddResponce.place_id");
        System.out.println(placeId);

    }

    @When("^I delete the google place in XML")
    public void deleteThePlaceXml() {

    	given().
	        queryParam("key", prop.getProperty("KEY")).
	        body(payLoad.getXmlPostDeleteGooglePlaceData(placeId)).
	        when().
	        post(resources.getXmlPostResourceDelete()).
	        then().assertThat().statusCode(200).and().contentType(ContentType.XML);
    }

    public static String GenerateStringFromResource(String path) throws IOException{
        return new String(Files.readAllBytes(Paths.get(path)));
    }
}
