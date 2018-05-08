package integration;

import com.coveros.selenified.Locator;
import com.coveros.selenified.Selenified;
import com.coveros.selenified.application.App;
import org.testng.ITestContext;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

public class ActionGoIT extends Selenified {

    @BeforeClass(alwaysRun = true)
    public void beforeClass(ITestContext test) {
        // set the base URL for the tests here
        setTestSite(this, test, "file:" + System.getProperty("user.dir") + "/public/index.html");
        // set the author of the tests here
        setAuthor(this, test, "Max Saperstone\n<br/>max.saperstone@coveros.com");
        // set the version of the tests or of the software, possibly with a
        // dynamic check
        setVersion(this, test, "0.0.1");
    }

    @Test(groups = {"integration", "actions", "go", "browser"},
            description = "An integration test to check the goBackOnePage method")
    public void goBackOnePageTest() {
        // use this object to manipulate the app
        App app = this.apps.get();
        // perform some actions
        app.newElement(Locator.ID, "submit_button").submit();
        app.azzert().textPresent("You're on the next page");
        app.goBack();
        app.azzert().textNotPresent("You're on the next page");
        // verify no issues
        finish();
    }

    @Test(groups = {"integration", "actions", "go"},
            description = "An integration test to check the goBackOnePage method")
    public void goBackOnePageNoBackTest() {
        // use this object to manipulate the app
        App app = this.apps.get();
        // perform some actions
        app.goBack();
        // verify no issues
        finish();
    }

    @Test(groups = {"integration", "actions", "go"},
            description = "An integration test to check the goBackOnePage method")
    public void goBackOnePageErrorTest() {
        // use this object to manipulate the app
        App app = this.apps.get();
        // perform some actions
        app.killDriver();
        app.goBack();
        // verify 1 issue
        finish(1);
    }

    @Test(groups = {"integration", "actions", "go", "browser"},
            description = "An integration test to check the goForwardOnePage method")
    public void goForwardOnePageTest() {
        // use this object to manipulate the app
        App app = this.apps.get();
        // perform some actions
        app.newElement(Locator.ID, "submit_button").submit();
        app.azzert().textPresent("You're on the next page");
        app.goBack();
        app.azzert().textNotPresent("You're on the next page");
        app.goForward();
        app.azzert().textPresent("You're on the next page");
        // verify no issues
        finish();
    }

    @Test(groups = {"integration", "actions", "go"},
            description = "An integration test to check the goForwardOnePage method")
    public void goForwardOnePageNoForwardTest() {
        // use this object to manipulate the app
        App app = this.apps.get();
        // perform some actions
        app.goForward();
        // verify no issues
        finish();
    }

    @Test(groups = {"integration", "actions", "go"},
            description = "An integration test to check the goForwardOnePage method")
    public void goForwardOnePageErrorTest() {
        // use this object to manipulate the app
        App app = this.apps.get();
        // perform some actions
        app.killDriver();
        app.goForward();
        // verify 1 issue
        finish(1);
    }

    @Test(groups = {"integration", "actions", "go"},
            description = "An integration test to check the refreshPage method")
    public void refreshPageTest() {
        // use this object to manipulate the app
        App app = this.apps.get();
        // perform some actions
        app.refresh();
        // verify no issues
        finish();
    }

    @Test(groups = {"integration", "actions", "go"},
            description = "An integration test to check the refreshPage method")
    public void refreshPageErrorTest() {
        // use this object to manipulate the app
        App app = this.apps.get();
        // perform some actions
        app.killDriver();
        app.refresh();
        // verify 1 issue
        finish(1);
    }

    @Test(groups = {"integration", "actions", "go"},
            description = "An integration test to check the refreshPageHard method")
    public void refreshPageHardTest() {
        // use this object to manipulate the app
        App app = this.apps.get();
        // perform some actions
        app.refreshHard();
        // verify no issues
        finish();
    }

    @Test(groups = {"integration", "actions", "go"},
            description = "An integration test to check the refreshPageHard method")
    public void refreshPageHardErrorTest() {
        // use this object to manipulate the app
        App app = this.apps.get();
        // perform some actions
        app.killDriver();
        app.refreshHard();
        // verify 1 issue
        finish(1);
    }

    @Test(groups = {"integration", "actions", "go"},
            description = "An integration test to check the maximizeScreen method")
    public void maximizeScreenTest() {
        // use this object to manipulate the app
        App app = this.apps.get();
        // perform some actions
        app.maximize();
        // verify no issues
        finish();
    }

    @Test(groups = {"integration", "actions", "go"},
            description = "An integration test to check the maximizeScreen method")
    public void maximizeScreenErrorTest() {
        // use this object to manipulate the app
        App app = this.apps.get();
        // perform some actions
        app.killDriver();
        app.maximize();
        // verify 1 issue
        finish(1);
    }
}