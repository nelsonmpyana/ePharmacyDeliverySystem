package com.digital.epharmacy.controller.pharmacy;

import com.digital.epharmacy.entity.Pharmacy.PharmacyBankAccountInformation;
import com.digital.epharmacy.factory.Pharmacy.PharmacyBankAccountInformationFactory;
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

import static org.junit.Assert.*;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@RunWith(SpringRunner.class)
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class PharmacyBankAccountInformationControllerTest {

    PharmacyBankAccountInformation bankInfo = PharmacyBankAccountInformationFactory
            .createPharmacyBankAccountInformation(
                    "605f5fd6-e329-4b95-b120-c39e5e250000",
                    "Standard Bank",
                    794241,
                    25001,
                    "KEL001"
            );

    @Autowired
    private TestRestTemplate restTemplate;

    private String baseURL = "http://localhost:8080/bankdetails";

    @Order(1)
    @Test
    public void a_create() {
        String url = baseURL + "/create";
        System.out.println("URL: " + url);
        System.out.println("POST Data: " + bankInfo);

        ResponseEntity<PharmacyBankAccountInformation> response = restTemplate.postForEntity(url, bankInfo, PharmacyBankAccountInformation.class);

        assertNotNull(response);
        assertNotNull(response.getBody());
        bankInfo = response.getBody();
        System.out.println("Saved Data: " + bankInfo);
        assertEquals(bankInfo.getPharmacyID(), response.getBody().getPharmacyID());
    }

    @Order(2)
    @Test
    public void b_read() {
        String url = baseURL + "/read/" + bankInfo.getBankName();
        System.out.println("URL: " + url);

        ResponseEntity<PharmacyBankAccountInformation> response = restTemplate.getForEntity(url, PharmacyBankAccountInformation.class);
        assertEquals(bankInfo.getBankName(), response.getBody().getBankName());
    }

    @Order(3)
    @Test
    public void c_update() {
        PharmacyBankAccountInformation bankUpdate = new PharmacyBankAccountInformation
                .Builder()
                .copy(bankInfo)
                .setBankName(
                        "FNB"
                )
                .build();

        String url = baseURL + "/update/" + bankInfo.getPharmacyID();

        System.out.println("URL: " + url);
        System.out.println("POST Data: " + bankUpdate);

        ResponseEntity<PharmacyBankAccountInformation> response = restTemplate.postForEntity(url, bankUpdate, PharmacyBankAccountInformation.class);
        assertEquals(bankInfo.getBankName(), response.getBody().getPharmacyID());
    }

    @Order(4)
    @Test
    public void getall() {
        String url = baseURL + "/all";
        System.out.println("URL: " + url);
        HttpHeaders headers = new HttpHeaders();
        HttpEntity<String> entity = new HttpEntity<>(null, headers);

        ResponseEntity<String> response = restTemplate.exchange(url, HttpMethod.GET, entity, String.class);
        System.out.println(response);
        System.out.println(response.getBody());
    }

    @Order(5)
    @Test
    public void delete() {
        String url = baseURL + "/delete/" + bankInfo.getPharmacyID();
        System.out.println("URL: " + url);
        restTemplate.delete(url);
    }
}