package controller;

import Setup.Setup;
import io.restassured.RestAssured;
import io.restassured.path.json.JsonPath;
import io.restassured.response.Response;
import model.UserModel;
import org.apache.commons.configuration.ConfigurationException;
import utils.Utils;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Properties;

import static io.restassured.RestAssured.given;

public class User extends Setup {
    public User() throws IOException {
        initConfig();
    }
    private String message;

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public void callingLoginAPI(String email, String password) throws ConfigurationException, IOException {
        RestAssured.baseURI = prop.getProperty("BASE_URL");
        UserModel userModel = new UserModel(email, password);
        Response res =
                given()
                        .contentType("application/json")
                        .body(userModel)
                        .when()
                        .post("/user/login")
                        .then()
                        .assertThat().statusCode(200).extract().response();

        JsonPath jsonpath = res.jsonPath();
        String token = jsonpath.get("token");
        String message=jsonpath.get("message");
        setMessage(message);
        Utils.setEnvVariable("TOKEN", token);
    }

    public String callingUserListAPI() throws IOException {
        RestAssured.baseURI = prop.getProperty("BASE_URL");
        Response res =
                given()
                        .contentType("application/json")
                        .header("Authorization", prop.getProperty("TOKEN"))
                        .when()
                        .get("/user/list")
                        .then()
                        .assertThat().statusCode(200).extract().response();

        JsonPath response = res.jsonPath();
        String id = response.get("users[0].id").toString();
        return id;
    }

    public Integer nid;
    public String name;
    public String email;
    public String email2;
    public String password;
    public String phone_number;
    public String role;
    public void AddCustomer() throws ConfigurationException, IOException {

        //prop.load(file);
        RestAssured.baseURI = "http://dmoney.professionaltrainingbd.com";
        Response res =

                given()
                        .contentType("application/json")
                        .header("Authorization", prop.getProperty("TOKEN"))
                        .header("X-AUTH-SECRET-KEY","ROADTOSDET")
                        .when()
                        .post("/user/create");
                        //.then()
                        //.assertThat().statusCode(200).extract().response();

        JsonPath ath = res.jsonPath();
        nid=(int)Math.floor(Math.random()*(999999-100000)+1);
        name = ath.get("name");
        email = ath.get("email_u");
        email2 = email + "@test.com";
        phone_number = ath.get("phone_w");
        Utils.setEnvVariable("nid",nid.toString());
        Utils.setEnvVariable("name", name);
        Utils.setEnvVariable("email", email2);
        Utils.setEnvVariable("phone_number", phone_number);
        Utils.setEnvVariable("password", password);
        System.out.println(res.asString());
    }

    public void createCustomer() throws IOException {
        //prop.load(file);
        RestAssured.baseURI = prop.getProperty("BASE_URL");
        Response res =
                given()
                        .contentType("application/json").header("Authorization", prop.getProperty("token"))
                        .body("{\"id\":"+prop.getProperty("nid")+",\n" +
                                "    \"name\":\""+prop.getProperty("name")+"\", \n" +
                                "    \"email\":\""+prop.getProperty("email")+"\",\n" +
                                "    \"password\":\"1234\",\n" +
                                "    \"role\":\"Customer\",\n" +
                                "    \"phone_number\":\""+prop.getProperty("phone_number")+"\"}")
                        .when()
                        .post("/user/create").
                        then()
                        .assertThat().statusCode(201).extract().response();


        System.out.println(res.asString());
    }

    public void updateCustomer() throws IOException {
        //prop.load(file);
        RestAssured.baseURI = prop.getProperty("BASE_URL");
        Response res =
                (Response) given()
                        .contentType("application/json").header("Authorization", prop.getProperty("TOKEN")).
                        when()
                        .put("/user/update/101").
                        then()
                        .assertThat().statusCode(200).extract().response();

//        JsonPath jsonPath = res.jsonPath();
//        Assert.assertEquals(jsonPath.get("name").toString(), "Mr. Kamal");
        System.out.println(res.asString());
    }





}
