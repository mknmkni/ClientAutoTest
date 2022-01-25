package selenide;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;

import ru.yandex.qatools.ashot.AShot;
import ru.yandex.qatools.ashot.Screenshot;
import ru.yandex.qatools.ashot.comparison.ImageDiff;
import ru.yandex.qatools.ashot.comparison.ImageDiffer;
import ru.yandex.qatools.ashot.coordinates.WebDriverCoordsProvider;
import ru.yandex.qatools.ashot.shooting.ShootingStrategies;

public class FileCompare {

    /** スクリーンショットを取得する際のスクロールのタイムアウト秒数 */
    static final int SCROLL_TIMEOUT = 1 * 1000;

    // 指定の要素を無視した画像比較 (参考： https://github.com/pazone/ashot/issues/138
    public static boolean compareIgnore(WebDriver driver, WebElement element, String actualFilePath, String expected, String resultFilePath) throws IOException {
        Screenshot actualScreen = new AShot().shootingStrategy(ShootingStrategies.viewportPasting(SCROLL_TIMEOUT))
                // 処理時間（ない機能もある）
                .addIgnoredElement(By.xpath("//*[@id=\"app_aria\"]/div[1]/span"))
                // 機能名
                .addIgnoredElement(By.xpath("//*[@id=\"app_aria\"]/div[1]/h3"))
                // 登録後のメッセージ
                .addIgnoredElement(By.xpath("//*[@id=\"collapse-biz-err\"]/div/div/span"))
                // 登録後の注文番号
                .addIgnoredElement(By.xpath("//*[@id=\"__BVID__50\"]")).coordsProvider(new WebDriverCoordsProvider()).takeScreenshot(driver, element);

        // 比較先ファイルの保存
        BufferedImage actualImage = actualScreen.getImage();
        ImageIO.write(actualImage, "png", new File(actualFilePath));

        // 比較元ファイルのロード
        BufferedImage original = ImageIO.read(new File(expected));
        Screenshot originalScreen = new Screenshot(original);

        // 比較元ファイルで無視する箇所を設定
        originalScreen.setIgnoredAreas(actualScreen.getIgnoredAreas());
        originalScreen.setCoordsToCompare(actualScreen.getCoordsToCompare());

        // 画像比較
        ImageDiff diff = new ImageDiffer().makeDiff(originalScreen, actualScreen);
        // 差分があれば差分比較画像を保存し true, 差分がなければ false を返す
        if (diff.hasDiff()) {
            BufferedImage resultImage = diff.getMarkedImage();
            ImageIO.write(resultImage, "png", new File(resultFilePath));
            return true;
        } else {
            return false;
        }

    }

    // 指定の要素のキャプチャを撮り、画像を返す
    public static BufferedImage partScreenshot(WebDriver driver, WebElement element) {
        BufferedImage partImage = new AShot().shootingStrategy(ShootingStrategies.viewportPasting(SCROLL_TIMEOUT)).coordsProvider(new WebDriverCoordsProvider()).takeScreenshot(driver, element).getImage();
        return partImage;
    }

}
