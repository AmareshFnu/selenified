package com.coveros.selenified.selenium;

import java.io.File;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Date;

import org.apache.commons.io.FileUtils;
import org.openqa.selenium.Alert;
import org.openqa.selenium.By;
import org.openqa.selenium.Cookie;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.Keys;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.remote.Augmenter;
import org.openqa.selenium.remote.DesiredCapabilities;
import org.openqa.selenium.remote.RemoteWebDriver;
import org.testng.log4testng.Logger;

import com.coveros.selenified.exceptions.InvalidBrowserException;
import com.coveros.selenified.output.OutputFile;
import com.coveros.selenified.output.OutputFile.Result;
import com.coveros.selenified.selenium.Selenium.Browser;
import com.coveros.selenified.selenium.Selenium.Locator;
import com.coveros.selenified.selenium.element.Element;
import com.coveros.selenified.tools.General;
import com.coveros.selenified.tools.TestSetup;

public class App {

    private static final Logger log = Logger.getLogger(General.class);

    // this will be the name of the file we write all commands out to
    private OutputFile file;

    // what locator actions are available in webdriver
    // this is the driver that will be used for all selenium actions
    private WebDriver driver;

    // what browsers are we interested in implementing
    // this is the browser that we are using
    private Browser browser;
    private DesiredCapabilities capabilities;

    // keeps track of the initial window open
    private String parentWindow;

    // the is class to determine if something exists
    private Is is;
    // the wait class to determine if we need to wait for something
    private WaitFor waitFor;
    // the get class to retrieve information about the app
    private Get get;
    // the assert class to verify information about the app
    private Assert azzert;

    // constants
    private static final String WAITED = "Waited ";
    private static final String SECONDS = " seconds";
    private static final String AVAILABLE = "</b> is available and selected";
    private static final String NOTSELECTED = "</b> was unable to be selected";
    private static final String FRAME = "Frame <b>";

    /**
     * the constructor, determining which browser use and how to run the
     * browser: either grid or standalone
     *
     * @param browser
     *            - the Browser we are running the test on
     * @param capabilities
     *            - what browser capabilities are desired
     * @param file
     *            - the TestOutput file. This is provided by the
     *            SeleniumTestBase functionality
     * @throws InvalidBrowserException
     *             If a browser that is not one specified in the
     *             Selenium.Browser class is used, this exception will be thrown
     * @throws MalformedURLException
     *             If the provided hub address isn't a URL, this exception will
     *             be thrown
     */
    public App(Browser browser, DesiredCapabilities capabilities, OutputFile file)
            throws InvalidBrowserException, MalformedURLException {
        this.browser = browser;
        this.capabilities = capabilities;
        this.file = file;

        // if we want to test remotely
        if (System.getProperty("hub") != null) {
            driver = new RemoteWebDriver(new URL(System.getProperty("hub") + "/wd/hub"), capabilities);
        } else {
            capabilities.setJavascriptEnabled(true);
            driver = TestSetup.setupDriver(browser, capabilities);
        }

        is = new Is(driver, file);
        waitFor = new WaitFor(driver, file);
        get = new Get(driver, file);
        azzert = new Assert(this, file);
    }

    /**
     * setups a new element which is located on the page
     * 
     * @param type
     *            - the locator type e.g. Locator.id, Locator.xpath
     * @param locator
     *            - the locator string e.g. login, //input[@id='login']
     * @return Element: a page element to interact with
     */
    public Element newElement(Locator type, String locator) {
        return new Element(driver, file, type, locator);
    }

    /**
     * setups a new element which is located on the page
     * 
     * @param type
     *            - the locator type e.g. Locator.id, Locator.xpath
     * @param locator
     *            - the locator string e.g. login, //input[@id='login']
     * @param match
     *            - if there are multiple matches of the selector, this is which
     *            match (starting at 0) to interact with
     * @return Element: a page element to interact with
     */
    public Element newElement(Locator type, String locator, int match) {
        return new Element(driver, file, type, locator, match);
    }

    ///////////////////////////////////////////////////////
    // instantiating our additional action classes for further use
    ///////////////////////////////////////////////////////

    public Is is() {
        return is;
    }

    public WaitFor waitFor() {
        return waitFor;
    }

    public Get get() {
        return get;
    }

    public Assert azzert() {
        return azzert;
    }

    ////////////////////////////////////////////
    // Ability to interact with important elements
    ////////////////////////////////////////////

    /**
     * a method to allow retrieving the driver instance
     *
     * @return WebDriver: access to the driver controlling the browser via
     *         webdriver
     */
    public WebDriver getDriver() {
        return driver;
    }

    /**
     * a method to allow retrieving the set browser
     *
     * @return Browser: the enum of the browser
     */
    public Browser getBrowser() {
        return browser;
    }

    /**
     * a method to allow retrieving the set output file
     *
     * @return OutputFile: the file recording all actions
     */
    public OutputFile getOutputFile() {
        return file;
    }

    /**
     * a method to allow retreiving the set capabilities
     *
     * @return capabilities: the listing of set capabilities
     */
    public DesiredCapabilities getCapabilities() {
        return capabilities;
    }

    /**
     * a method to end the driver instance
     */
    public void killDriver() {
        if (driver != null) {
            try {
                driver.quit();
            } catch (Exception e) {
                log.error(e);
            }
        }
    }

    ////////////////////////////////////////////
    // perform actions on the page itself, not the elements
    ////////////////////////////////////////////

    /**
     * a method for allowing Selenium to pause for a set amount of time
     *
     * @param seconds
     *            - the number of seconds to wait
     * @return Integer: the number of errors encountered while executing these
     *         steps
     */
    public int wait(double seconds) {
        String action = "Wait " + seconds + SECONDS;
        String expected = WAITED + seconds + SECONDS;
        try {
            Thread.sleep((long) (seconds * 1000));
        } catch (InterruptedException e) {
            log.error(e);
            file.recordAction(action, expected, "Failed to wait " + seconds + SECONDS + ". " + e.getMessage(),
                    Result.FAILURE);
            file.addError();
            Thread.currentThread().interrupt();
            return 1;
        }
        file.recordAction(action, expected, WAITED + seconds + SECONDS, Result.SUCCESS);
        return 0;
    }

    /**
     * a method for navigating to a new url
     *
     * @param url
     *            - the URL to navigate to
     * @return Integer: the number of errors encountered while executing these
     *         steps
     */
    public int goToURL(String url) {
        String action = "Loading " + url;
        String expected = "Loaded " + url;
        double start = System.currentTimeMillis();
        try {
            driver.get(url);
        } catch (Exception e) {
            log.error(e);
            file.recordAction(action, expected, "Fail to Load " + url + ". " + e.getMessage(), Result.FAILURE);
            file.addError();
            return 1;
        }
        double timetook = System.currentTimeMillis() - start;
        timetook = timetook / 1000;
        file.recordAction(action, expected, "Loaded " + url + " in " + timetook + SECONDS, Result.SUCCESS);
        return 0;
    }

    /**
     * a method to obtain screenshots
     *
     * @param imageName
     *            - the name of the image typically generated via functions from
     *            TestOutput.generateImageName
     */
    public void takeScreenshot(String imageName) {
        if (browser == null || browser == Browser.HTMLUNIT || browser == Browser.NONE) {
            return;
        }
        try {
            // take a screenshot
            File srcFile;
            if (System.getProperty("hubAddress") != "LOCAL") {
                WebDriver augemented = new Augmenter().augment(driver);
                srcFile = ((TakesScreenshot) augemented).getScreenshotAs(OutputType.FILE);
            } else {
                srcFile = ((TakesScreenshot) driver).getScreenshotAs(OutputType.FILE);
            }
            // now we need to save the file
            FileUtils.copyFile(srcFile, new File(imageName));
        } catch (Exception e) {
            log.error("Error taking screenshot: " + e);
        }
    }

    /**
     * A private method to send a key combination both as control and command
     * (PC and Mac compatibile)
     * 
     * @param action
     *            - the action occurring
     * @param expected
     *            - the expected result
     * @param fail
     *            - the failed result
     * @param key
     *            - what key to send along with control and/or command
     * @return Integer: the number of errors encountered while executing these
     *         steps
     */
    private int sendControlAndCommand(String action, String expected, String fail, String key) {
        try {
            driver.findElement(By.cssSelector("body")).sendKeys(Keys.CONTROL + key);
            driver.findElement(By.cssSelector("body")).sendKeys(Keys.COMMAND + key);
        } catch (Exception e) {
            file.recordAction(action, expected, fail + e.getMessage(), Result.FAILURE);
            file.addError();
            log.error(e);
            return 1;
        }
        file.recordAction(action, expected, expected, Result.SUCCESS);
        return 0;
    }

    /**
     * a way to open a new tab, and have it selected. Note, no content will be
     * present on this new tab, use the goToURL method to open load some content
     * 
     * @return Integer: the number of errors encountered while executing these
     *         steps
     */
    public int openTab() {
        return sendControlAndCommand("Opening new tab", "New tab is opened", "New tab was unable to be opened. ", "t");
    }

    /**
     * a way to open a new tab, and have it selected. The page provided will be
     * loaded
     * 
     * @param url
     *            - the url to load once the new tab is opened and selected
     * @return Integer: the number of errors encountered while executing these
     *         steps
     */
    public int openTab(String url) {
        if (openTab() != 0) {
            return 1;
        }
        return goToURL(url);
    }

    /**
     * a function to switch tabs. this method will switch to the next available
     * tab. if the last tab is already selected, it will loop back to the first
     * tab
     * 
     * @return Integer: the number of errors encountered while executing these
     *         steps
     */
    public int switchNextTab() {
        String action = "Switching to next tab ";
        String expected = "Next tab <b>" + AVAILABLE;
        try {
            driver.findElement(By.cssSelector("body")).sendKeys(Keys.chord(Keys.CONTROL, Keys.PAGE_DOWN));
            driver.findElement(By.cssSelector("body")).sendKeys(Keys.chord(Keys.COMMAND, Keys.PAGE_DOWN));
        } catch (Exception e) {
            file.recordAction(action, expected, "Next tab <b>" + NOTSELECTED + ". " + e.getMessage(), Result.FAILURE);
            file.addError();
            log.error(e);
            return 1;
        }
        file.recordAction(action, expected, expected, Result.SUCCESS);
        return 0;
    }

    /**
     * a function to switch tabs. this method will switch to the previous
     * available tab. if the first tab is already selected, it will loop back to
     * the last tab
     * 
     * @return Integer: the number of errors encountered while executing these
     *         steps
     */
    public int switchPreviousTab() {
        String action = "Switching to previous tab ";
        String expected = "Previous tab <b>" + AVAILABLE;
        try {
            driver.findElement(By.cssSelector("body")).sendKeys(Keys.chord(Keys.CONTROL, Keys.PAGE_UP));
            driver.findElement(By.cssSelector("body")).sendKeys(Keys.chord(Keys.COMMAND, Keys.PAGE_UP));
        } catch (Exception e) {
            file.recordAction(action, expected, "Previous tab <b>" + NOTSELECTED + ". " + e.getMessage(),
                    Result.FAILURE);
            file.addError();
            log.error(e);
            return 1;
        }
        file.recordAction(action, expected, expected, Result.SUCCESS);
        return 0;
    }

    /**
     * a function to close tabs. note that if this is the only tab open, you
     * will end the session
     * 
     * @return Integer: the number of errors encountered while executing these
     *         steps
     */
    public int closeTab() {
        return sendControlAndCommand("Closing currently open tab", "Tab is closed", "Tab was unable to be closed. ",
                "w");
    }

    /**
     * some functionality to go back one page in browser history
     * 
     * @return Integer: the number of errors encountered while executing these
     *         steps
     */
    public int goBack() {
        String action = "Going back one page";
        String expected = "Previous page from browser history is loaded";
        try {
            driver.navigate().back();
        } catch (Exception e) {
            file.recordAction(action, expected, "Browser was unable to go back one page. " + e.getMessage(),
                    Result.FAILURE);
            file.addError();
            log.error(e);
            return 1;
        }
        file.recordAction(action, expected, expected, Result.SUCCESS);
        return 0;
    }

    /**
     * some functionality to go forward one page in browser history
     * 
     * @return Integer: the number of errors encountered while executing these
     *         steps
     */
    public int goForward() {
        String action = "Going forward one page";
        String expected = "Next page from browser history is loaded";
        try {
            driver.navigate().forward();
        } catch (Exception e) {
            file.recordAction(action, expected, "Browser was unable to go forward one page. " + e.getMessage(),
                    Result.FAILURE);
            file.addError();
            log.error(e);
            return 1;
        }
        file.recordAction(action, expected, expected, Result.SUCCESS);
        return 0;
    }

    /**
     * some functionality to reload the current page
     * 
     * @return Integer: the number of errors encountered while executing these
     *         steps
     */
    public int refresh() {
        String action = "Reloading current page";
        String expected = "Page is refreshed";
        try {
            driver.navigate().refresh();
        } catch (Exception e) {
            file.recordAction(action, expected, "Browser was unable to be refreshed. " + e.getMessage(),
                    Result.FAILURE);
            file.addError();
            log.error(e);
            return 1;
        }
        file.recordAction(action, expected, expected, Result.SUCCESS);
        return 0;
    }

    /**
     * some functionality to reload the current page while clearing the cache.
     * This only works for certain browsers, as the command/control and F5 key
     * combination is used to perform the action
     * 
     * @return Integer: the number of errors encountered while executing these
     *         steps
     */
    public int refreshHard() {
        String action = "Reloading current page while clearing the cache";
        String expected = "Cache is cleared, and the page is refreshed";
        try {
            driver.findElement(By.cssSelector("body")).sendKeys(Keys.chord(Keys.CONTROL, Keys.F5));
            driver.findElement(By.cssSelector("body")).sendKeys(Keys.chord(Keys.COMMAND, Keys.F5));
        } catch (Exception e) {
            file.recordAction(action, expected,
                    "There was a problem clearing the cache and reloading the page. " + e.getMessage(), Result.FAILURE);
            file.addError();
            log.error(e);
            return 1;
        }
        file.recordAction(action, expected, expected, Result.SUCCESS);
        return 0;
    }

    /**
     * some functionality to add a specific cookie to the page
     * 
     * @param cookie
     *            - the details of the cookie to set
     * 
     * @return Integer: the number of errors encountered while executing these
     *         steps
     */
    public int setCookie(Cookie cookie) {
        String domain = cookie.getDomain();
        Date expiry = cookie.getExpiry();
        String name = cookie.getName();
        String path = cookie.getPath();
        String value = cookie.getValue();

        String action = "Setting up cookie with attributes:<div><table><tbody><tr><td>Domain</td><td>" + domain
                + "</tr><tr><td>Expiration</td><td>" + expiry.toString() + "</tr><tr><td>Name</td><td>" + name
                + "</tr><tr><td>Path</td><td>" + path + "</tr><tr><td>Value</td><td>" + value
                + "</tr></tbody></table></div><br/>";
        String expected = "Cookie is added";
        try {
            driver.manage().addCookie(cookie);
        } catch (Exception e) {
            file.recordAction(action, expected, "Unable to add cookie. " + e.getMessage(), Result.FAILURE);
            file.addError();
            log.error(e);
            return 1;
        }
        file.recordAction(action, expected, expected, Result.SUCCESS);
        return 0;
    }

    /**
     * some functionality to delete a specifically stored cookie
     * 
     * @param cookieName
     *            - the name of the cookie to delete
     * 
     * @return Integer: the number of errors encountered while executing these
     *         steps
     */
    public int deleteCookie(String cookieName) {
        String action = "Deleting cookie <i>" + cookieName + "</i>";
        String expected = "Cookie <i>" + cookieName + "</i> is removed";
        Cookie cookie = driver.manage().getCookieNamed(cookieName);
        if (cookie == null) {
            file.recordAction(action, expected,
                    "Unable to remove cookie <i>" + cookieName + "</i> as it doesn't exist.", Result.FAILURE);
            file.addError();
            return 1;
        }
        try {
            driver.manage().deleteCookieNamed(cookieName);
        } catch (Exception e) {
            file.recordAction(action, expected, "Unable to remove cookie <i>" + cookieName + "</i>. " + e.getMessage(),
                    Result.FAILURE);
            file.addError();
            log.error(e);
            return 1;
        }
        file.recordAction(action, expected, expected, Result.SUCCESS);
        return 0;
    }

    /**
     * some functionality to delete all currently stored cookies
     * 
     * @return Integer: the number of errors encountered while executing these
     *         steps
     */
    public int deleteAllCookies() {
        String action = "Deleting all cookies";
        String expected = "All cookies are removed";
        try {
            driver.manage().deleteAllCookies();
        } catch (Exception e) {
            file.recordAction(action, expected, "Unable to remove all cookies. " + e.getMessage(), Result.FAILURE);
            file.addError();
            log.error(e);
            return 1;
        }
        file.recordAction(action, expected, expected, Result.SUCCESS);
        return 0;
    }

    /**
     * some functionality to maximize the current window
     * 
     * @return Integer: the number of errors encountered while executing these
     *         steps
     */
    public int maximize() {
        String action = "Maximizing browser";
        String expected = "Browser is maximized";
        try {
            driver.manage().window().maximize();
        } catch (Exception e) {
            file.recordAction(action, expected, "Browser was unable to be maximized. " + e.getMessage(),
                    Result.FAILURE);
            file.addError();
            log.error(e);
            return 1;
        }
        file.recordAction(action, expected, expected, Result.SUCCESS);
        return 0;
    }

    /**
     * An custom script to scroll to a given position on the page
     *
     * @param desiredPosition
     *            - the position on the page to scroll to
     * @return Integer: the number of errors encountered while executing these
     *         steps
     */
    public int scroll(int desiredPosition) {
        JavascriptExecutor jse = (JavascriptExecutor) driver;
        Long initialPosition = (Long) jse.executeScript("return window.scrollY;");

        String action = "Scrolling page from " + initialPosition + " to " + desiredPosition;
        String expected = "Page is now set at position " + desiredPosition;

        jse.executeScript("window.scrollBy(0, " + desiredPosition + ")");

        Long newPosition = (Long) jse.executeScript("return window.scrollY;");

        if (newPosition != desiredPosition) {
            file.recordAction(action, expected, "Page is set at position " + newPosition, Result.FAILURE);
            file.addError();
            return 1; // indicates page didn't scroll properly
        }
        file.recordAction(action, expected, "Page is now set at position " + newPosition, Result.SUCCESS);
        return 0; // indicates page scrolled properly
    }

    /**
     * a function to switch windows.
     * 
     * @return Integer: the number of errors encountered while executing these
     *         steps
     */
    public int switchToNewWindow() {
        String action = "Switching to the new window";
        String expected = "New window is available and selected";
        try {
            parentWindow = driver.getWindowHandle();
            for (String winHandle : driver.getWindowHandles()) {
                driver.switchTo().window(winHandle);
            }
        } catch (Exception e) {
            file.recordAction(action, expected, "New window was unable to be selected. " + e.getMessage(),
                    Result.FAILURE);
            file.addError();
            log.error(e);
            return 1;
        }
        file.recordAction(action, expected, expected, Result.SUCCESS);
        return 0;
    }

    /**
     * a function to switch back to the parent window.
     * 
     * @return Integer: the number of errors encountered while executing these
     *         steps
     */
    public int switchToParentWindow() {
        String action = "Switching back to parent window";
        String expected = "Parent window is available and selected";
        try {
            driver.switchTo().window(parentWindow);
        } catch (Exception e) {
            file.recordAction(action, expected, "Parent window was unable to be selected. " + e.getMessage(),
                    Result.FAILURE);
            file.addError();
            log.error(e);
            return 1;
        }
        file.recordAction(action, expected, expected, Result.SUCCESS);
        return 0;
    }

    /**
     * a function to close the currently selected window. After this window is
     * closed, ensure that focus is shifted to the next window using
     * switchToXxxWindow methods
     * 
     * @return Integer: the number of errors encountered while executing these
     *         steps
     */
    public int closeCurrentWindow() {
        String action = "Closing currently selected window";
        String expected = "Current window is closed";
        try {
            driver.close();
        } catch (Exception e) {
            file.recordAction(action, expected, "Current window was unable to be closed. " + e.getMessage(),
                    Result.FAILURE);
            file.addError();
            log.error(e);
            return 1;
        }
        file.recordAction(action, expected, expected, Result.SUCCESS);
        return 0;
    }

    /**
     * a way to select a frame within a page. Select a frame by its (zero-based)
     * index. That is, if a page has three frames, the first frame would be at
     * index 0, the second at index 1 and the third at index 2. Once the frame
     * has been selected, all subsequent calls on the WebDriver interface are
     * made to that frame.
     * 
     * @param frameNumber
     *            - the frame number, starts at 0
     * @return Integer: the number of errors encountered while executing these
     *         steps
     */
    public int selectFrame(int frameNumber) {
        String action = "Switching to frame <b>" + frameNumber + "</b>";
        String expected = FRAME + frameNumber + AVAILABLE;
        try {
            driver.switchTo().frame(frameNumber);
        } catch (Exception e) {
            file.recordAction(action, expected, FRAME + frameNumber + NOTSELECTED + ". " + e.getMessage(),
                    Result.FAILURE);
            file.addError();
            log.error(e);
            return 1;
        }
        file.recordAction(action, expected, expected, Result.SUCCESS);
        return 0;
    }

    /**
     * a way to select a frame within a page. Select a frame by its name or ID.
     * Frames located by matching name attributes are always given precedence
     * over those matched by ID.
     * 
     * @param frameIdentifier
     *            - the frame name or ID
     * @return Integer: the number of errors encountered while executing these
     *         steps
     */
    public int selectFrame(String frameIdentifier) {
        String action = "Switching to frame <b>" + frameIdentifier + "</b>";
        String expected = FRAME + frameIdentifier + AVAILABLE;
        try {
            driver.switchTo().frame(frameIdentifier);
        } catch (Exception e) {
            file.recordAction(action, expected, FRAME + frameIdentifier + NOTSELECTED + ". " + e.getMessage(),
                    Result.FAILURE);
            file.addError();
            log.error(e);
            return 1;
        }
        file.recordAction(action, expected, expected, Result.SUCCESS);
        return 0;
    }

    //////////////////////////
    // pop-up interactions
    /////////////////////////

    /**
     * A private method to accept (click 'OK' on) whatever popup is present on
     * the page
     * 
     * @param action
     *            - the action occurring
     * @param expected
     *            - the expected result
     * @param popup
     *            - the element we are interacting with
     * @return Integer: the number of errors encountered while executing these
     *         steps
     */
    private int accept(String action, String expected, String popup) {
        try {
            Alert alert = driver.switchTo().alert();
            alert.accept();
        } catch (Exception e) {
            log.error(e);
            file.recordAction(action, expected, "Unable to click 'OK' on the " + popup + ". " + e.getMessage(),
                    Result.FAILURE);
            file.addError();
            return 1;
        }
        file.recordAction(action, expected, "Clicked 'OK' on the " + popup, Result.SUCCESS);
        return 0;
    }

    /**
     * A private method to dismiss (click 'Cancel' on) whatever popup is present
     * on the page
     * 
     * @param action
     *            - the action occurring
     * @param expected
     *            - the expected result
     * @param popup
     *            - the element we are interacting with
     * @return Integer: the number of errors encountered while executing these
     *         steps
     */
    private int dismiss(String action, String expected, String popup) {
        try {
            Alert alert = driver.switchTo().alert();
            alert.dismiss();
        } catch (Exception e) {
            log.error(e);
            file.recordAction(action, expected, "Unable to click 'Cancel' on the " + popup + ". " + e.getMessage(),
                    Result.FAILURE);
            file.addError();
            return 1;
        }
        file.recordAction(action, expected, "Clicked 'Cancel' on the " + popup, Result.SUCCESS);
        return 0;
    }

    /**
     * A private method to determine if a confirmation is present or not, and
     * can be interacted with
     * 
     * @param action
     *            - the action occurring
     * @param expected
     *            - the expected result
     * @return Integer: the number of errors encountered while executing these
     *         steps
     */
    private int isConfirmation(String action, String expected) {
        // wait for element to be present
        if (!is.confirmationPresent()) {
            waitFor.confirmationPresent();
        }
        if (!is.confirmationPresent()) {
            file.recordAction(action, expected, "Unable to click confirmation as it is not present", Result.FAILURE);
            return 1; // indicates element not present
        }
        return 0;
    }

    /**
     * A private method to determine if a prompt is present or not, and can be
     * interacted with
     * 
     * @param action
     *            - the action occurring
     * @param expected
     *            - the expected result
     * @param perform
     *            - the action occurring to the prompt
     * @return Integer: the number of errors encountered while executing these
     *         steps
     */
    private int isPrompt(String action, String expected, String perform) {
        // wait for element to be present
        if (!is.promptPresent()) {
            waitFor.promptPresent();
        }
        if (!is.promptPresent()) {
            file.recordAction(action, expected, "Unable to " + perform + " prompt as it is not present",
                    Result.FAILURE);
            return 1; // indicates element not present
        }
        return 0;
    }

    /**
     * Some basic functionality for clicking 'OK' on an alert box
     *
     * @return Integer: the number of errors encountered while executing these
     *         steps
     */
    public int acceptAlert() {
        String action = "Clicking 'OK' on an alert";
        String expected = "Alert is present to be clicked";
        // wait for element to be present
        if (!is.alertPresent()) {
            waitFor.alertPresent();
        }
        if (!is.alertPresent()) {
            file.recordAction(action, expected, "Unable to click alert as it is not present", Result.FAILURE);
            return 1; // indicates element not present
        }
        return accept(action, expected, "alert");
    }

    /**
     * Some basic functionality for clicking 'OK' on a confirmation box
     *
     * @return Integer: the number of errors encountered while executing these
     *         steps
     */
    public int acceptConfirmation() {
        String action = "Clicking 'OK' on a confirmation";
        String expected = "Confirmation is present to be clicked";
        if (isConfirmation(action, expected) != 0) {
            return 1;
        }
        return accept(action, expected, "confirmation");
    }

    /**
     * Some basic functionality for clicking 'Cancel' on a confirmation box
     *
     * @return Integer: the number of errors encountered while executing these
     *         steps
     */
    public int dismissConfirmation() {
        String action = "Clicking 'Cancel' on a confirmation";
        String expected = "Confirmation is present to be clicked";
        if (isConfirmation(action, expected) != 0) {
            return 1;
        }
        return dismiss(action, expected, "confirmation");
    }

    /**
     * Some basic functionality for clicking 'OK' on a prompt box
     *
     * @return Integer: the number of errors encountered while executing these
     *         steps
     */
    public int acceptPrompt() {
        String action = "Clicking 'OK' on a prompt";
        String expected = "Prompt is present to be clicked";
        if (isPrompt(action, expected, "click") != 0) {
            return 1;
        }
        return accept(action, expected, "prompt");
    }

    /**
     * Some basic functionality for clicking 'Cancel' on a prompt box
     *
     * @return Integer: the number of errors encountered while executing these
     *         steps
     */
    public int dismissPrompt() {
        String action = "Clicking 'Cancel' on a prompt";
        String expected = "Prompt is present to be clicked";
        if (isPrompt(action, expected, "click") != 0) {
            return 1;
        }
        return dismiss(action, expected, "prompt");
    }

    /**
     * Some basic functionality for typing text into a prompt box
     * 
     * @param text
     *            - the text to type into the prompt
     *
     * @return Integer: the number of errors encountered while executing these
     *         steps
     */
    public int typeIntoPrompt(String text) {
        String action = "Typing text '" + text + "' into prompt";
        String expected = "Prompt is present and enabled to have text " + text + " typed in";
        if (isPrompt(action, expected, "type into") != 0) {
            return 1;
        }
        try {
            Alert alert = driver.switchTo().alert();
            alert.sendKeys(text);
        } catch (Exception e) {
            log.error(e);
            file.recordAction(action, expected, "Unable to type into prompt. " + e.getMessage(), Result.FAILURE);
            file.addError();
            return 1;
        }
        file.recordAction(action, expected, "Typed text '" + text + "' into prompt", Result.SUCCESS);
        return 0;
    }
}