package ivi.tests;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;

import java.util.concurrent.TimeUnit;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.core.Is.is;
public class PromoTest {
    public static void main(String[] args) throws InterruptedException {
        String BaseUrl = "https://www.ivi.ru";
        String watchLatterButtonActiveState = "huge action-link bright js-favourite-button favorite active";
        String watchLatterButtonXPathLocator = "//li[6]/div/a[3]";
        String arrowNextButtonCssLocator = "div.promo span.control.next";
        String watchLatterButtonTextCssLocator = "#favourites-title > a:nth-child(1)";
        String watchLatterMovieContainerCssLocator = "#favourites > li:nth-child(1)";

        ChromeOptions options = new ChromeOptions();
        // Иногда кнопки не нажимаются, поэтому сделал запуск браузера в полном экране.
        options.addArguments("--start-maximized");
        WebDriver driver = new ChromeDriver(options);
        driver.manage().timeouts().pageLoadTimeout(10, TimeUnit.SECONDS);

        driver.get(BaseUrl);
        Thread.sleep(2000);
        System.out.println("Страница загружена");

        WebElement watchLatter = driver.findElement(By.xpath(watchLatterButtonXPathLocator));  // Кнопка "Смотреть позже"
        WebElement arrowNext = driver.findElement(By.cssSelector(arrowNextButtonCssLocator));  //  Кнопка прокрутки промо-слайдера вправо
        System.out.println(arrowNext.getText());
        Thread.sleep(500);

        for(int i = 0; i < 5; i++) {
            arrowNext.click();
            Thread.sleep(1000);
        }
        String clickedMovieID = watchLatter.getAttribute("data-object-id");  //  Получение ID фильма добавленного в "Смотреть позже"
        watchLatter.click();
        Thread.sleep(2000);
        System.out.println(clickedMovieID);

        // Не нашёл как проверить что звезда стала "красной", поэтому проверяю, что класс кнопки изменился на *active
        assertThat(watchLatter.getAttribute("class"), is(watchLatterButtonActiveState));


        // Почему-то в "чистом" браузере кнопка "Смотреть позже" динамически не меняется
        // и не появляется блок с фильмом добавленным в "Смотерть позже" поэтому пришлось обновлять страницу
        driver.navigate().refresh();
        Thread.sleep(3000);


        // Получение текста  и проверка заголовка появившегося блока "Смотреть позже"
        String buttonText = driver.findElement(By.cssSelector(watchLatterButtonTextCssLocator)).getText();
        assertThat(buttonText,is("См. позже"));
        System.out.println(buttonText);

        // Проверка того, что совпадают ID фильмов нажатой кнопки "Смотреть позже" и добавленного в блок под слайдером
        String selectedMovieID = driver.findElement(By.cssSelector(watchLatterMovieContainerCssLocator)).getAttribute("data-id");
        System.out.println(clickedMovieID);

        assertThat(selectedMovieID,is(clickedMovieID));
        System.out.println("Всё верно, в раздел \"Смотреть позже\" добавлен правильный фильм.");
        driver.quit();
    }
}
