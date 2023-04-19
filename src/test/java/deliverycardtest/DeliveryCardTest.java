package deliverycardtest;

import com.codeborne.selenide.Condition;
import com.codeborne.selenide.ElementsCollection;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.openqa.selenium.Keys;

import java.time.Duration;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

import static com.codeborne.selenide.Condition.exactText;
import static com.codeborne.selenide.Selectors.*;
import static com.codeborne.selenide.Selenide.*;

public class DeliveryCardTest {
    public String generateDate(int days, String pattern) {
        return LocalDate.now().plusDays(days).format(DateTimeFormatter.ofPattern(pattern));
    }

    @BeforeEach
    public void setUp() {
        open("http://localhost:9999");
    }

    @Test
    public void deliveryCardTest() {
        String date = generateDate(3, "dd.MM.yyyy");
        $("[data-test-id='city'] input").setValue("Москва");
        $("[data-test-id='date'] input").sendKeys(Keys.chord(Keys.SHIFT, Keys.HOME), Keys.BACK_SPACE);
        $("[data-test-id='date'] input").setValue(date);
        $("[data-test-id='name'] input").setValue("Геворгян Милена");
        $("[data-test-id='phone'] input").setValue("+79856958251");
        $("[data-test-id='agreement']").click();
        $(byXpath(".//*[contains(@class,'button button_view_extra')]")).click();
        $(byXpath(".//*[@class='notification__content']")).shouldBe(Condition.visible, Duration.ofSeconds(15))
                .shouldHave(exactText("Встреча успешно забронирована на " + date));
    }

    @Test
    public void selectDateAndCityInTerminalTest() {
        String date = generateDate(7, "dd");
        String deliveryDate = generateDate(7, "dd.MM.yyyy");
        String city = ("Пе");
        $("[data-test-id='city'] input").setValue(city);
        $(byXpath(".//*[contains(text(),"+city+')'+']')).click();
        $("[data-test-id='date'] input").sendKeys(Keys.chord(Keys.SHIFT, Keys.HOME), Keys.BACK_SPACE);
        $(byXpath(".//*[@class='input__icon']")).click();
        ElementsCollection block = $$(byXpath(".//*[@class='calendar__day']"));
        if (block.contains($(byXpath(".//*[text()=" + date + ']')))) {
            $(byXpath(".//*[text()=" + date + ']')).click();
        } else {
            $(byXpath(".//*[@data-step='1']")).click();
            $(byXpath(".//*[text()=" + date + ']')).click();
        }
        $("[data-test-id='name'] input").setValue("Геворгян Милена");
        $("[data-test-id='phone'] input").setValue("+79856958251");
        $("[data-test-id='agreement']").click();
        $(byXpath(".//*[contains(@class,'button button_view_extra')]")).click();
        $(byXpath(".//*[@class='notification__content']")).shouldBe(Condition.visible, Duration.ofSeconds(15))
                .shouldHave(Condition.exactText("Встреча успешно забронирована на " + deliveryDate));
    }
}
