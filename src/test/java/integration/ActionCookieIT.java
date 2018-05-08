package integration;

import com.coveros.selenified.Selenified;
import com.coveros.selenified.application.App;
import org.openqa.selenium.Cookie;
import org.testng.Assert;
import org.testng.ITestContext;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import java.io.IOException;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.TimeZone;

public class ActionCookieIT extends Selenified {

    @BeforeClass(alwaysRun = true)
    public void beforeClass(ITestContext test) {
        // set the base URL for the tests here
        setTestSite(this, test, "http://172.31.2.65/");
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
        app.azzert().cookieExists("new_cookie");
        // verify no issues
        finish();
    }

    @Test(groups = {"integration", "actions", "get", "cookie"},
            description = "An integration test to check the getCookieDomain method")
    public void getCookieDomainTest(ITestContext context) throws ParseException {
        String dateval = "2011-11-17T09:52:13";
        DateFormat df = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss");
        df.setTimeZone(TimeZone.getTimeZone("UTC"));
        Cookie cookie = new Cookie("new_cookie", "this_cookie",
                getTestSite(this.getClass().getName(), context).split("/")[2].split(":")[0], "/", df.parse(dateval));
        // use this object to manipulate the app
        App app = this.apps.get();
        // perform some actions
        app.setCookie(cookie);
        String cookieDomain = app.get().cookieDomain("cookie");
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
    public void deleteCookieTest(ITestContext context) throws ParseException {
        // use this object to manipulate the app
        App app = this.apps.get();
        // create the cookie to delete
        String dateval = "2011-11-17T09:52:13";
        DateFormat df = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss");
        df.setTimeZone(TimeZone.getTimeZone("UTC"));
        Cookie cookie = new Cookie("cookie", "this_cookie",
                getTestSite(this.getClass().getName(), context).split("/")[2].split(":")[0], "/", df.parse(dateval));
        app.setCookie(cookie);

        // perform some actions
        app.deleteCookie("cookie");
        cookie = app.get().cookie("cookie");
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
}