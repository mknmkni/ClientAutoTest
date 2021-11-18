package selenide;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;

import com.codeborne.selenide.Configuration;
import com.codeborne.selenide.Selenide;
import com.codeborne.selenide.WebDriverRunner;

public class Screenshot {

    /**
     * テストで使用するWebページ
     */
    static final String BASE_URL = "https://www.yahoo.co.jp/";
    /**
     * ヘッドレスモードを使用するかどうか
     */
    static final Boolean HEADLESS_MODE = false;

    @BeforeAll
    public static void beforeAll () {
        Configuration.timeout = 30000;

        Configuration.baseUrl = "https://xxx";

        Configuration.reportsFolder = "report";

        Configuration.holdBrowserOpen = true;

        Configuration.browser = "chrome";
        // テスト環境がローカルかサーバかによって使用するChromeのドライバを変更する
        if (System.getProperty ("os.name").toLowerCase ().equals ("windows 10")) {
            System.setProperty ("webdriver.chrome.driver", "driver/chromedriver-96.exe");
        } else {
            System.setProperty ("webdriver.chrome.driver", "driver/chromedriver-90");
        }

        ChromeOptions chromeOptions = new ChromeOptions ()
                .setHeadless (HEADLESS_MODE)
                .addArguments ("--disable-extensions")
                .addArguments ("--no-sandbox")
                .addArguments ("--disable-gpu")
                .addArguments ("--ignore-certificate-errors")
                // ブラウザのサイズ
                .addArguments ("-window-size=1280,1024");

        WebDriverRunner.setWebDriver (new ChromeDriver (chromeOptions));
        Configuration.fastSetValue = true;

    }

//    @BeforeEach
//    TestInfo = testInfo;

    @AfterEach
    public void afterEach() {
        Selenide.closeWindow ();
        Selenide.closeWebDriver ();
    }

    /**
     * アサーションチェック
     */
    @Test
    @DisplayName("アサーションチェック")
    public void test01 () {
        System.out.println ("まだ実装されていません");
    }

}
