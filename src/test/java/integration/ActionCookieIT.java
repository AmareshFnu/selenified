package integration;

import com.coveros.selenified.DriverSetup;
import com.coveros.selenified.Selenified;
import com.coveros.selenified.application.App;
import org.openqa.selenium.Cookie;
import org.testng.Assert;
import org.testng.ITestContext;
import org.testng.ITestResult;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import java.io.IOException;
import java.lang.reflect.Method;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.util.Date;
import java.util.TimeZone;

public class ActionCookieIT extends Selenified {

    @BeforeClass(alwaysRun = true)
    public void beforeClass(ITestContext test) {
        // set the base URL for the tests here
        setTestSite(this, test, "https://www.coveros.com/");
        // set the author of the tests here
        setAuthor(this, test, "Max Saperstone\n<br/>max.saperstone@coveros.com");
        // set the version of the tests or of the software, possibly with a
        // dynamic check
        setVersion(this, test, "0.0.1");
    }

    @Test(groups = {"integration", "actions", "go", "cookie"},
            description = "An integration test to check the " + "setCookie " + "method")
    public void setCookieTest(ITestContext context) throws IOException, ParseException {
        String dateval = "2011-11-17T09:52:13";
        DateFormat df = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss");
        df.setTimeZone(TimeZone.getTimeZone("UTC"));
        Cookie cookie = new Cookie("new_cookie", "this_cookie",
                getTestSite(this.getClass().getName(), context).split("/")[2].split(":")[0], "/", df.parse(dateval));
        // use this object to manipulate the app
        App app = this.apps.get();
        // perform some actions
        app.setCookie(cookie);
        // verify no issues
        finish();
    }

    @Test(groups = {"integration", "actions", "get", "cookie"},
            description = "An integration test to check the getCookieDomain method")
    public void getCookieDomainTest(ITestContext context) throws ParseException {
        // use this object to manipulate the app
        App app = this.apps.get();
        // perform some actions
        String cookieDomain = app.get().cookieDomain("PHPSESSID");
        Assert.assertEquals(cookieDomain, getTestSite(this.getClass().getName(), context).split("/")[2].split(":")[0]);
        // verify no issues
        finish();
    }

    @Test(groups = {"integration", "actions", "go", "cookie"},
            description = "An integration test to check the setCookie method")
    public void setCookieErrorTest(ITestContext context) throws IOException, ParseException {
        String dateval = "2011-11-17T09:52:13";
        DateFormat df = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss");
        df.setTimeZone(TimeZone.getTimeZone("UTC"));
        Cookie cookie = new Cookie("new_cookie", "this_cookie",
                getTestSite(this.getClass().getName(), context).split("/")[2].split(":")[0], "/", df.parse(dateval));
        // use this object to manipulate the app
        App app = this.apps.get();
        // perform some actions
        app.killDriver();
        app.setCookie(cookie);
        // verify 1 issue
        finish(1);
    }

    @Test(groups = {"integration", "actions", "go", "cookie"},
            description = "An integration test to check the deleteCookie method")
    public void deleteCookieTest() {
        // use this object to manipulate the app
        App app = this.apps.get();
        // perform some actions
        app.deleteCookie("PHPSESSID");
        Cookie cookie = app.get().cookie("PHPSESSID");
        org.testng.Assert.assertNull(cookie);
        // verify no issues
        finish();
    }

    @Test(groups = {"integration", "actions", "go", "cookie"},
            description = "An integration test to check the deleteCookie method")
    public void deleteNonExistentCookieTest() {
        // use this object to manipulate the app
        App app = this.apps.get();
        // perform some actions
        app.deleteCookie("new_cookie");
        // verify 1 issue
        finish(1);
    }

    @Test(groups = {"integration", "actions", "go", "cookie"},
            description = "An integration test to check the deleteAllCookies method")
    public void deleteAllCookiesTest() {
        // use this object to manipulate the app
        App app = this.apps.get();
        // perform some actions
        app.deleteAllCookies();
        Cookie cookie = app.get().cookie("cookie");
        org.testng.Assert.assertNull(cookie);
        // verify no issues
        finish();
    }

    @Test(groups = {"integration", "actions", "go", "cookie"},
            description = "An integration test to check the deleteAllCookies method")
    public void deleteAllCookiesTwiceTest() {
        // use this object to manipulate the app
        App app = this.apps.get();
        // perform some actions
        app.deleteAllCookies();
        app.deleteAllCookies();
        Cookie cookie = app.get().cookie("cookie");
        org.testng.Assert.assertNull(cookie);
        // verify no issues
        finish();
    }

    @Test(groups = {"integration", "actions", "go", "cookie"},
            description = "An integration test to check the deleteAllCookies method")
    public void deleteAllCookiesErrorTest() {
        // use this object to manipulate the app
        App app = this.apps.get();
        // perform some actions
        app.killDriver();
        app.deleteAllCookies();
        // verify 1 issue
        finish(1);
    }


    @Test(groups = {"integration", "actions", "get", "cookie"},
            description = "An integration test to check the getCookie method")
    public void getCookieTest() throws IOException, ParseException {
        // the cookie date
        String dateval = "2019-12-18T12:00:00";
        DateFormat df = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss");
        df.setTimeZone(TimeZone.getTimeZone("UTC"));
        // use this object to manipulate the app
        App app = this.apps.get();
        // perform some actions
        Cookie cookie = app.get().cookie("PHPSESSID");
        Assert.assertEquals(cookie, new Cookie("PHPSESSID", "cookietest", "/", df.parse(dateval)));
        // verify no issues
        finish();
    }

    @Test(groups = {"integration", "actions", "get", "cookie"},
            description = "An integration negative test to check the getCookie method")
    public void negativeGetCookieTest() {
        // use this object to manipulate the app
        App app = this.apps.get();
        // perform some actions
        Cookie cookie = app.get().cookie("badcookie");
        Assert.assertNull(cookie);
        // verify no issues
        finish();
    }

    @Test(groups = {"integration", "actions", "get", "cookie"},
            description = "An integration test to check the getCookieValue method")
    public void getCookieValueTest() throws IOException, ParseException {
        // use this object to manipulate the app
        App app = this.apps.get();
        // perform some actions
        String cookie = app.get().cookieValue("cookie");
        Assert.assertEquals(cookie, "cookietest");
        // verify no issues
        finish();
    }

    @Test(groups = {"integration", "actions", "get", "cookie"},
            description = "An integration negative test to check the getCookieValue method")
    public void negativeGetCookieValueTest() {
        // use this object to manipulate the app
        App app = this.apps.get();
        // perform some actions
        String cookie = app.get().cookieValue("badcookie");
        Assert.assertNull(cookie);
        // verify no issues
        finish();
    }

    @Test(groups = {"integration", "actions", "get", "cookie"},
            description = "An integration test to check the getCookiePath method")
    public void getCookiePathTest() {
        // use this object to manipulate the app
        App app = this.apps.get();
        // perform some actions
        String cookie = app.get().cookiePath("PHPSESSID");
        Assert.assertEquals(cookie, "/");
        // verify no issues
        finish();
    }

    @Test(groups = {"integration", "actions", "get", "cookie"},
            description = "An integration negative test to check the getCookiePath method")
    public void negativeGetCookiePathTest() {
        // use this object to manipulate the app
        App app = this.apps.get();
        // perform some actions
        String cookie = app.get().cookiePath("badcookie");
        Assert.assertNull(cookie);
        // verify no issues
        finish();
    }

    @Test(groups = {"integration", "actions", "get", "cookie"},
            description = "An integration negative test to check the getCookieDomain method")
    public void negativeGetCookieDomainTest() {
        // use this object to manipulate the app
        App app = this.apps.get();
        // perform some actions
        String cookie = app.get().cookieDomain("badcookie");
        Assert.assertNull(cookie);
        // verify no issues
        finish();
    }

    @Test(groups = {"integration", "actions", "get", "cookie"},
            description = "An integration test to check the getCookieExpiration method")
    public void getCookieExpirationTest() throws IOException, ParseException {
        // the cookie date
        Date tomorrow = new Date(new Date().getTime() + (1000 * 60 * 60 * 24));
        // use this object to manipulate the app
        App app = this.apps.get();
        // perform some actions
        Date cookie = app.get().cookieExpiration("_gid");
        Assert.assertEquals(cookie.toString(), tomorrow.toString());
        // verify no issues
        finish();
    }

    @Test(groups = {"integration", "actions", "get", "cookie"},
            description = "An integration negative test to check the getCookieExpiration method")
    public void negativeGetCookieExpirationTest() {
        // use this object to manipulate the app
        App app = this.apps.get();
        // perform some actions
        Date cookie = app.get().cookieExpiration("badcookie");
        Assert.assertNull(cookie);
        // verify no issues
        finish();
    }
}