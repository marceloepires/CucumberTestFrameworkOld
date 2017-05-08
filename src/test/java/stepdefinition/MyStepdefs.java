package stepdefinition;

import cucumber.api.java.Before;
import cucumber.api.java.en.Given;
import cucumber.api.java.en.When;
import io.restassured.RestAssured;
import io.restassured.http.ContentType;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.Properties;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.equalTo;


public class MyStepdefs {

    Properties prop = new Properties();

    @Before
    public void readConfig() throws IOException {
        FileInputStream fileInputStream = new FileInputStream("src/properties/env.properties");
        prop.load(fileInputStream);
    }

    @Given("^The rest request$")
    public void the_rest_request(){
        RestAssured.baseURI="https://maps.googleapis.com";

        given().
                param("location","-33.8670522,151.1957362").
                param("radius","500").
                param("key",prop.getProperty("KEY")).
                when().
                get("/maps/api/place/nearbysearch/xml").
                then().assertThat().statusCode(200).and().contentType(ContentType.XML);

    }

    @When("^We run the rest request$")
    public void we_run_the_rest_request(){
        given().
                param("location", "-33.8670522,151.1957362").
                param("radius", "500").
                param("key", prop.getProperty("KEY")).
                when().
                get("/maps/api/place/nearbysearch/json").
                then().assertThat().statusCode(200).and().contentType(ContentType.JSON).and().
                body("results[0].name", equalTo("Sydney")).and().
                body("results[0].place_id", equalTo("ChIJP3Sa8ziYEmsRUKgyFmh9AQM")).and().
                header("Server", "pablo");

    }
}
