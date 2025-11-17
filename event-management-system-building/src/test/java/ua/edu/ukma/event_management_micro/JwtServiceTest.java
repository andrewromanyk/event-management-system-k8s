package ua.edu.ukma.event_management_micro;

import io.restassured.RestAssured;
import org.junit.jupiter.api.*;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import ua.edu.ukma.event_management_micro.jwt.JwtService;

import static io.restassured.RestAssured.with;


@ExtendWith(SpringExtension.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
@TestPropertySource(
        properties = {
                "grpc.server.mtls.enabled=false",
                "grpc.server.port=-1",
                "grpc.start.enabled=false"
        }
)
class JwtServiceTest {

    @LocalServerPort
    private int port;

    String name = "api";
    String token;

    @Autowired
    JwtService jwtService;

    @BeforeEach
    void setUp() {
        RestAssured.port = port;
        token = jwtService.generateToken(name);
    }

    @Test
    @Order(1)
    void testJwtAccessPost() {
        with()
                .body("""
                        {
                            "address": "test1",
                            "hourlyRate": 12,
                            "areaM2": 13,
                            "capacity": 50,
                            "description": "desc"
                        }
                        """)
                .header("Authorization", "Bearer " + token)
                .contentType("application/json")
                .post("/api/building")
                .then()
                .statusCode(201);
    }

    @Test
    @Order(2)
    void testJwtAccessSuccess() {
        with()
                .header("Authorization", "Bearer " + token)
                .contentType("application/json")
                .get("/api/building/1")
                .then()
                .statusCode(200);
    }

//    @Test
//    @Order(3)
//    void testJwtAccessFail() {
//        token = token.substring(0, token.length() - 1) + "X";
//        with()
//                .header("Authorization", "Bearer " + token)
//                .contentType("application/json")
//                .get("/api/building/1")
//                .then()
//                .statusCode(403);
//    }
}
