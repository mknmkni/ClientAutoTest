package selenide.page;

import static com.codeborne.selenide.Condition.*;
import static com.codeborne.selenide.Selenide.*;

import org.openqa.selenium.By;
import org.openqa.selenium.support.FindBy;

import com.codeborne.selenide.ElementsCollection;
import com.codeborne.selenide.SelenideElement;

/**
 * 共通の要素をまとめたクラス
 */
public class PageObject {

    /**
     * 画面全体
     */
    @FindBy (xpath = "/html")
    public SelenideElement full;

    /**
     * 添付ファイルのダウンロードボタンが画面上に多数存在する場合（例: 検索結果テーブルなど）、その中から enabled
     * の状態のボタンが表示されるのを待ちます。
     *
     * <p>
     * ダウンロードボタンは初期状態は disabled ですが、このボタン（部品）が内部的で非同期に REST API
     * (/api/AttachFileTable)を呼び出し、
     * その結果をダウンロード可能なファイルが存在すればenabledになります(ダウンロード可能なファイルがなければdisabledのまま)<br>
     * 画面上のすべてのダウンロードボタンが REST API を呼び出し、 結果 ボタンの状態が enabled または
     * disabledのどちらかに確定する前にスクリーンショットを取得してしまうと 毎回異なる差分が発生してしまう場合があります。<br>
     * 上記事象が発生する場合、このスクリーンショットを取得する前にこのメソッドを呼び出してください。
     *
     * @param xpath ダウンロードボタンのある列のボタンのXPATH 例：/table/tbody/tr/td[5]/button
     *
     */
    public void ensureActiveダウンロードボタン (String xpath) {
        ElementsCollection result = $$ (By.xpath (xpath));
        result.findBy (enabled).shouldBe (enabled);
    }


    public void set検索ボックス (String value) {
        $ (By.xpath ("/html/body/div/div/header/section[1]/div/form/fieldset/span/input")).setValue (value);
    }

    public void click検索 () {
        $ (By.xpath ("/html/body/div/div/header/section[1]/div/form/fieldset/span/button")).click ();
    }

    public void exists検索ページ (String value) {
        $ (By.xpath ("/html/body/div/header/div[1]/div[2]/nav/div[1]/div[1]")).shouldHave (text(value));
    }

}
