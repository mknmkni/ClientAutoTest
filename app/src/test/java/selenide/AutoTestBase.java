package selenide;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Objects;

import javax.imageio.ImageIO;

import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.TestInfo;

import com.codeborne.selenide.Configuration;
import com.codeborne.selenide.Selenide;
import com.codeborne.selenide.WebDriverRunner;

import selenide.page.PageObject;

/**
 * クライアント自動テストを行うために必要な設定や共通の処理をまとめたクラス
 */
public class AutoTestBase {

    /** Chrome をヘッドレスモードで起動するかどうか */
    static final boolean headlessMode = false;
    /** ファイルアップロード用のファイルパス */
    protected static final String UPLOAD_FILE = "test.txt";
    /** 画面ログインURL */
    protected static final String BASE_URL = "https://www.yahoo.co.jp/";

    /**
     * テストクラスが実行される前に1度実行。 WebDriver, ブラウザなどテストを実施する上での設定を行う。
     */
    @BeforeAll
    public static void beforeAll () {

        Configuration.browser = "chrome";
        // 要素に対するチェックを行う際に待機する時間（ミリ秒）
        Configuration.timeout = 20 * 1000;

        Configuration.baseUrl = BASE_URL;

        Configuration.reportsFolder = "report";

        Configuration.holdBrowserOpen = false;

        // HTMLファイルを保存するか
        Configuration.savePageSource = false;

        Configuration.fastSetValue = false;

        Configuration.browserSize = "1400x1200";

        Configuration.headless = headlessMode;

    }

    /** テストメソッド情報 */
    protected TestInfo testInfo;

    /**
     * テストメソッド実行の直前に実行される。 現在のテストの情報を取得。
     *
     * @param testInfo
     */
    @BeforeEach
    public void beforeEach (TestInfo testInfo) {
        this.testInfo = testInfo;
    };

    /**
     * テストクラスが実行されたあとに実行される。 WebDriver を Close する。
     */
    @AfterAll
    public static void afterAll () {
        Selenide.closeWebDriver ();
    }

    /**
     * キャプチャした画像の保存、画像の比較を行う
     *
     * @param  pageObject  ページオブジェクトクラスのインスタンス
     * @return             画像の比較結果 差分がある場合は true を返し、差分を示したファイルを保存する
     * @throws IOException
     */
    public boolean captureAndCompare (PageObject pageObject) throws IOException {

        String[] testPackageName = testInfo.getTestClass ().get ().getPackage ().getName ().split ("\\.");
        String testClassName = testInfo.getTestClass ().get ().getSimpleName ();
        String testMethodName = testInfo.getTestMethod ().get ().getName ();
        String displayName = testInfo.getDisplayName ();

        // 比較元ファイルパス、差分があった場合に出力するファイルパス
        Path originalFilePath = Paths.get ("pages/" + testPackageName[testPackageName.length - 1] + "/" + testClassName
                + "/" + testClassName + "-" + testMethodName + "-" + displayName + ".png");
        Path actualFilePath = Paths.get ("pages/temp/" + testPackageName[testPackageName.length - 1] + "/"
                + testClassName + "/" + testClassName + "-" + testMethodName + "-" + displayName + ".png");
        Path diffFilePath = Paths
                .get ("report/" + testClassName + "-" + testMethodName + "-" + displayName + "-diff.png");

        // originalFilePath のディレクトリが存在しない場合作成する
        if (!Files.exists (originalFilePath.getParent ())) {
            Files.createDirectories (originalFilePath.getParent ());
        }

        // actualFilePath のディレクトリが存在しない場合作成する
        if (!Files.exists (actualFilePath.getParent ())) {
            Files.createDirectories (actualFilePath.getParent ());
        }

        // 環境変数 template_refresh が true の場合のみキャプチャを撮る（これが比較元になる）
        if (Objects.equals (System.getenv ("template_refresh"), "true")) {
            BufferedImage contentScreenshot = FileCompare.partScreenshot (WebDriverRunner.getWebDriver (),
                    pageObject.full);
            ImageIO.write (contentScreenshot, "png", originalFilePath.toFile ());
        }

        // 比較したい画面のキャプチャを取得し、比較元と画像比較を行う
        return FileCompare.compareIgnore (WebDriverRunner.getWebDriver (), pageObject.full, actualFilePath.toString (),
                originalFilePath.toString (), diffFilePath.toString ());
    }

}
