package selenide;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.MethodOrderer.OrderAnnotation;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;

import com.codeborne.selenide.Selenide;

import selenide.page.PageObject;

@TestMethodOrder(OrderAnnotation.class)
public class Screenshot extends AutoTestBase {

    /**
     * アサーションチェック
     */
    @Test
    @DisplayName("アサーションチェック")
    public void test01 () {
        System.out.println ("まだ実装されていません");
    }

    /**
     * 検索する
     */
    @Test
    @DisplayName("検索する")
    public void test02 () {
        PageObject pageObject = Selenide.open("/", PageObject.class);
        pageObject.set検索ボックス ("java");
        pageObject.click検索 ();
        pageObject.exists検索ページ ("知恵袋");
    }

//    /**
//     * スクショを撮る
//     */
//    public void screenShot () {
//
//    }

}
