package com.digital.epharmacy.controller.Order;

import com.digital.epharmacy.entity.Order.OrderHistory;
import com.digital.epharmacy.entity.User.UserProfile;
import com.digital.epharmacy.factory.Order.OrderHistoryFactory;
import com.digital.epharmacy.factory.User.UserProfileFactory;
import org.junit.FixMethodOrder;
import org.junit.Test;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.TestMethodOrder;
import org.junit.runner.RunWith;
import org.junit.runners.MethodSorters;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.junit4.SpringRunner;

import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@RunWith(SpringRunner.class)
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class OrderHistoryControllerTest {

    private static final String USERNAME = "UserProfile";
    private static final String USER_PASSWORD = "54321";
    private static final String ADMIN_USERNAME = "Admin";
    private static final String ADMIN_PASSWORD = "12345";

    private static UserProfile user = UserProfileFactory
            .createUserProfile("Ayabulela","Mahlathini", "male");

    private static OrderHistory orderHistory = OrderHistoryFactory
            .createOrderHistory(user, 500, new BigDecimal(6000.00));


    @Autowired
    private TestRestTemplate restTemplate;
    private String baseURL = "http://localhost:8080/orderHistory/";

    @Order(1)
    @Test
    public void a_create() {

        String url = baseURL + "create";
        System.out.println("URL:" + url);
        System.out.println("POST data: " + orderHistory);
        ResponseEntity<OrderHistory> postResponse = restTemplate
                .withBasicAuth(ADMIN_USERNAME, ADMIN_PASSWORD)
                .postForEntity(url, orderHistory, OrderHistory.class);
        assertNotNull(postResponse);
        assertNotNull(postResponse.getBody());
        orderHistory = postResponse.getBody();
        System.out.println("Saved data:" + orderHistory);
        assertEquals(orderHistory.getTotalNumberOfOrders(), postResponse.getBody().getTotalNumberOfOrders());

    }

    @Order(2)
    @Test
    public void b_read() {
        String url = baseURL + "read/" + orderHistory.getUser().getUserId();
        System.out.println("URL: " + url);
        ResponseEntity<OrderHistory> response = restTemplate
                .withBasicAuth(USERNAME, USER_PASSWORD)
                .getForEntity(url, OrderHistory.class);
        assertEquals(orderHistory.getTotalOrderValue().doubleValue(), response.getBody().getTotalOrderValue().doubleValue());
    }

    @Order(3)
    @Test
    public void c_update() {
        OrderHistory updatedOrderHistory = new OrderHistory.Builder().copy(orderHistory).setTotalNumberOfOrders(7000).build();
        String url = baseURL + "update";
        System.out.println("URL: " + url);
        System.out.println("POST data: " + updatedOrderHistory);
        ResponseEntity<OrderHistory> response = restTemplate
                .withBasicAuth(ADMIN_USERNAME, ADMIN_PASSWORD)
                .postForEntity(url, updatedOrderHistory, OrderHistory.class);
        System.out.println("Response: " + response.getBody());
        assertEquals(orderHistory.getUser().getUserId(), response.getBody().getUser().getUserId());
    }

    @Order(5)
    @Test
    public void e_delete() {
        String url = baseURL + "delete/" + orderHistory.getUser().getUserId();
        System.out.println("URL: " + url);
        restTemplate.withBasicAuth(ADMIN_USERNAME, ADMIN_PASSWORD).delete(url);
    }

    @Order(4)
    @Test
    public void d_getAll() {
        String url = baseURL + "all";
        System.out.println("URL: " + url);
        HttpHeaders headers = new HttpHeaders();
        HttpEntity<String> entity = new HttpEntity<>(null,headers);
        ResponseEntity<String> response = restTemplate
                .withBasicAuth(ADMIN_USERNAME, ADMIN_PASSWORD)
                .exchange(url, HttpMethod.GET,entity,String.class);
        System.out.println(response);
        System.out.println(response.getBody());
    }
}