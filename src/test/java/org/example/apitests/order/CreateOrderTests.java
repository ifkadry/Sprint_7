package org.example.apitests.order;

import io.qameta.allure.*;
import io.qameta.allure.junit4.DisplayName;
import io.qameta.allure.junit4.Tag;
import io.restassured.response.Response;
import org.example.resthandlers.apihandlers.OrdersAPIHandler;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;

import java.util.List;

@RunWith(Parameterized.class)
@Link(url = "https://qa-scooter.praktikum-services.ru/docs/#api-Orders-CreateOrder", name = "#api-Orders-CreateOrder")
@Tag("create-order")
@Epic("Sprint 7. Project")
@Feature("Группа тестов для API создания заказа")
@DisplayName("3. Создание заказа")
public class CreateOrderTests {
    private final OrdersAPIHandler ordersAPIHandler;
    private String firstName;
    private String lastName;
    private String address;
    private String phone;
    private String rentTime;
    private String deliveryDate;
    private String comment;
    private final List<String> scooterColor;
    private Integer trackId;

    public CreateOrderTests(List<String> scooterColor) {
        this.ordersAPIHandler = new OrdersAPIHandler();
        this.scooterColor = scooterColor;
    }

    @Parameterized.Parameters(name = "Цвет самоката: {0}")
    public static Object[][] initParamsForTest() {
        return new Object[][] {
                {List.of()},
                {List.of("BLACK")},
                {List.of("GREY")},
                {List.of("BLACK", "GREY")},
        };
    }

    @Before
    @Step("Подготовка тестовых данных")
    public void prepareTestData() {
        this.firstName = "Тестимя";
        this.lastName = "Тестфамилия";
        this.address = "Москва, Беговая ул., д. 16";
        this.phone = "+7 (916) 138-38-38";
        this.rentTime = "3";
        this.deliveryDate = "2024-09-15";
        this.comment = "Тестовый";
    }

    @After
    @Step("Очистка данных после теста")
    public void clearAfterTests() {
        if (trackId == null) return;

        ordersAPIHandler.deleteOrder(trackId);
    }

    @Test
    @DisplayName("Создание заказа")
    @Description("Тест проверяет API создания заказа. Ожидаемый результат - заказ создан, возвращается его track-номер")
    public void createOrderIsSuccess() {
        Allure.parameter("Цвет самоката", scooterColor);

        Response response = ordersAPIHandler.createOrder(firstName, lastName, address, phone, rentTime, deliveryDate, comment, scooterColor);
        ordersAPIHandler.checkStatusCode(response, 201);
        ordersAPIHandler.checkResponseParamNotNull(response, "track");

        this.trackId = ordersAPIHandler.getTrack(response);
    }

}
