package unit;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.openqa.selenium.Proxy;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.remote.CapabilityType;
import org.openqa.selenium.remote.DesiredCapabilities;
import org.testng.Assert;
import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;
import selenified.exceptions.InvalidBrowserException;
import tools.selenium.SeleniumHelper.Browsers;
import tools.selenium.SeleniumSetup;

public class SeleniumSetupTest {

	private DesiredCapabilities capabilities = new DesiredCapabilities();
	private String setBrowser = null;
	private String setHub = null;
	private String setProxy = null;

	@BeforeClass
	public void saveBrowser() {
		if (System.getProperty("browser") != null) {
			setBrowser = System.getProperty("browser");
		}
		if (System.getProperty("hub") != null) {
			setHub = System.getProperty("hub");
		}
		if (System.getProperty("proxy") != null) {
			setProxy = System.getProperty("proxy");
		}
	}

	@AfterClass
	public void restoreBrowser() {
		if (setBrowser != null) {
			System.setProperty("browser", setBrowser);
		}
		if (setHub != null) {
			System.setProperty("hub", setHub);
		}
		if (setProxy != null) {
			System.setProperty("proxy", setProxy);
		}
	}

	@BeforeMethod
	public void clearBrowser() {
		System.clearProperty("browser");
		System.clearProperty("hub");
		System.clearProperty("proxy");
	}

	@Test
	public void setupProxyTest() {
		DesiredCapabilities capability = SeleniumSetup.setupProxy(capabilities);
		Assert.assertFalse(capability.is(CapabilityType.PROXY));

		System.setProperty("proxy", "localhost");
		capability = SeleniumSetup.setupProxy(capabilities);
		Proxy export = (Proxy) capability.getCapability(CapabilityType.PROXY);
		Assert.assertEquals(export.getHttpProxy(), "localhost");
	}

	@Test
	public void areBrowserDetailsSetNoBrowserTest() {
		Assert.assertFalse(SeleniumSetup.areBrowserDetailsSet());
	}

	@Test
	public void areBrowserDetailsSetSingleBrowserTest() {
		Assert.assertFalse(SeleniumSetup.areBrowserDetailsSet());

		System.setProperty("browser", "Chrome");
		Assert.assertFalse(SeleniumSetup.areBrowserDetailsSet());

		System.setProperty("browser", "browserName=Chrome");
		Assert.assertTrue(SeleniumSetup.areBrowserDetailsSet());
	}

	@Test
	public void areBrowserDetailsSetMultipleBrowserTest() {
		System.setProperty("browser", "Chrome,Firefox");
		Assert.assertFalse(SeleniumSetup.areBrowserDetailsSet());

		System.setProperty("browser", "browserName=Chrome,browserName=Firefox");
		Assert.assertTrue(SeleniumSetup.areBrowserDetailsSet());
	}

	@Test
	public void setBrowserNoBrowserTest() {
		Assert.assertTrue(SeleniumSetup.setBrowser().isEmpty());
		Assert.assertEquals(SeleniumSetup.setBrowser(), new ArrayList<Browsers>());
	}

	@Test
	public void setBrowserSingleBrowserTest() {
		System.setProperty("browser", "Chrome");
		List<Browsers> browsers = SeleniumSetup.setBrowser();
		Assert.assertEquals(browsers.size(), 1);
		Assert.assertTrue(browsers.contains(Browsers.Chrome));

		System.setProperty("browser", "browserName=Chrome");
		browsers = SeleniumSetup.setBrowser();
		Assert.assertEquals(browsers.size(), 1);
		Assert.assertTrue(browsers.contains(Browsers.Chrome));
	}

	@Test
	public void setBrowserMultipleBrowserTest() {
		System.setProperty("browser", "Chrome,Firefox");
		List<Browsers> browsers = SeleniumSetup.setBrowser();
		Assert.assertEquals(browsers.size(), 2);
		Assert.assertTrue(browsers.contains(Browsers.Chrome));
		Assert.assertTrue(browsers.contains(Browsers.Firefox));

		System.setProperty("browser", "browserName=Chrome,browserName=Firefox");
		browsers = SeleniumSetup.setBrowser();
		Assert.assertEquals(browsers.size(), 2);
		Assert.assertTrue(browsers.contains(Browsers.Chrome));
		Assert.assertTrue(browsers.contains(Browsers.Firefox));
	}

	@Test(expectedExceptions = IllegalArgumentException.class)
	public void setBrowserIllegalBrowserTest() {
		System.setProperty("browser", "BadBrowser");
		SeleniumSetup.setBrowser();
	}

	@Test
	public void setupBrowserDetailsBrowserNameTest() {
		DesiredCapabilities capability = SeleniumSetup.setupBrowserDetails(capabilities, null);
		Assert.assertFalse(capability.is(CapabilityType.BROWSER_NAME));
		Assert.assertNull(capability.getCapability(CapabilityType.BROWSER_NAME));

		capability = SeleniumSetup.setupBrowserDetails(capabilities, new HashMap<>());
		Assert.assertFalse(capability.is(CapabilityType.BROWSER_NAME));
		Assert.assertNull(capability.getCapability(CapabilityType.BROWSER_NAME));

		Map<String, String> browserDetails = new HashMap<>();
		browserDetails.put("browserName", "Chrome");
		capability = SeleniumSetup.setupBrowserDetails(capabilities, browserDetails);
		Assert.assertEquals(capability.getBrowserName(), "Chrome");
		Assert.assertEquals(capability.getCapability(CapabilityType.BROWSER_NAME), "Chrome");
	}

	@Test(expectedExceptions = IllegalArgumentException.class)
	public void setupBrowserDetailsBrowserNameIllegalBrowserTest() {
		Map<String, String> browserDetails = new HashMap<>();
		browserDetails.put("browserName", "BadBrowser");
		SeleniumSetup.setupBrowserDetails(capabilities, browserDetails);
	}

	@Test
	public void setupBrowserDetailsBrowserVersionTest() {
		DesiredCapabilities capability = SeleniumSetup.setupBrowserDetails(capabilities, null);
		Assert.assertFalse(capability.is(CapabilityType.VERSION));
		Assert.assertNull(capability.getCapability(CapabilityType.VERSION));

		capability = SeleniumSetup.setupBrowserDetails(capabilities, new HashMap<>());
		Assert.assertFalse(capability.is(CapabilityType.VERSION));
		Assert.assertNull(capability.getCapability(CapabilityType.VERSION));

		Map<String, String> browserDetails = new HashMap<>();
		browserDetails.put("browserName", "Chrome");
		capability = SeleniumSetup.setupBrowserDetails(capabilities, browserDetails);
		Assert.assertFalse(capability.is(CapabilityType.VERSION));
		Assert.assertNull(capability.getCapability(CapabilityType.VERSION));

		browserDetails.put("browserVersion", "50");
		capability = SeleniumSetup.setupBrowserDetails(capabilities, browserDetails);
		Assert.assertEquals(capability.getVersion(), "50");
		Assert.assertEquals(capability.getCapability(CapabilityType.VERSION), "50");

		browserDetails.put("browserVersion", "49.1.1");
		capability = SeleniumSetup.setupBrowserDetails(capabilities, browserDetails);
		Assert.assertEquals(capability.getVersion(), "49.1.1");
		Assert.assertEquals(capability.getCapability(CapabilityType.VERSION), "49.1.1");

		browserDetails.put("browserVersion", "latest");
		capability = SeleniumSetup.setupBrowserDetails(capabilities, browserDetails);
		Assert.assertEquals(capability.getVersion(), "latest");
		Assert.assertEquals(capability.getCapability(CapabilityType.VERSION), "latest");
	}

	@Test
	public void setupBrowserDetailsDeviceNameTest() {
		DesiredCapabilities capability = SeleniumSetup.setupBrowserDetails(capabilities, null);
		Assert.assertFalse(capability.is("deviceName"));
		Assert.assertNull(capability.getCapability("deviceName"));

		capability = SeleniumSetup.setupBrowserDetails(capabilities, new HashMap<>());
		Assert.assertFalse(capability.is("deviceName"));
		Assert.assertNull(capability.getCapability("deviceName"));

		Map<String, String> browserDetails = new HashMap<>();
		browserDetails.put("browserName", "Chrome");
		capability = SeleniumSetup.setupBrowserDetails(capabilities, browserDetails);
		Assert.assertFalse(capability.is("deviceName"));
		Assert.assertNull(capability.getCapability("deviceName"));

		browserDetails.put("deviceName", "Android");
		capability = SeleniumSetup.setupBrowserDetails(capabilities, browserDetails);
		Assert.assertEquals(capability.getCapability("deviceName"), "Android");
	}

	@Test
	public void setupBrowserDetailsDeviceOrientationTest() {
		DesiredCapabilities capability = SeleniumSetup.setupBrowserDetails(capabilities, null);
		Assert.assertFalse(capability.is("device-orientation"));
		Assert.assertNull(capability.getCapability("device-orientation"));

		capability = SeleniumSetup.setupBrowserDetails(capabilities, new HashMap<>());
		Assert.assertFalse(capability.is("device-orientation"));
		Assert.assertNull(capability.getCapability("device-orientation"));

		Map<String, String> browserDetails = new HashMap<>();
		browserDetails.put("browserName", "Chrome");
		capability = SeleniumSetup.setupBrowserDetails(capabilities, browserDetails);
		Assert.assertFalse(capability.is("device-orientation"));
		Assert.assertNull(capability.getCapability("device-orientation"));

		browserDetails.put("deviceOrientation", "Landscape");
		capability = SeleniumSetup.setupBrowserDetails(capabilities, browserDetails);
		Assert.assertEquals(capability.getCapability("device-orientation"), "Landscape");
	}

	@Test
	public void setupBrowserDetailsDevicePlatformTest() {
		DesiredCapabilities capability = SeleniumSetup.setupBrowserDetails(capabilities, null);
		Assert.assertFalse(capability.is(CapabilityType.PLATFORM));
		Assert.assertNull(capability.getCapability(CapabilityType.PLATFORM));

		capability = SeleniumSetup.setupBrowserDetails(capabilities, new HashMap<>());
		Assert.assertFalse(capability.is(CapabilityType.PLATFORM));
		Assert.assertNull(capability.getCapability(CapabilityType.PLATFORM));

		Map<String, String> browserDetails = new HashMap<>();
		browserDetails.put("browserName", "Chrome");
		capability = SeleniumSetup.setupBrowserDetails(capabilities, browserDetails);
		Assert.assertFalse(capability.is(CapabilityType.PLATFORM));
		Assert.assertNull(capability.getCapability(CapabilityType.PLATFORM));

		browserDetails.put("devicePlatform" ,"Windows 10");
		capability = SeleniumSetup.setupBrowserDetails(capabilities, browserDetails);
		Assert.assertEquals(capability.getPlatform().toString(), "WIN10");
		Assert.assertEquals(capability.getCapability(CapabilityType.PLATFORM).toString(), "WIN10");

		browserDetails.put("devicePlatform","Linux");
		capability = SeleniumSetup.setupBrowserDetails(capabilities, browserDetails);
		Assert.assertEquals(capability.getPlatform().toString(), "LINUX");
		Assert.assertEquals(capability.getCapability(CapabilityType.PLATFORM).toString(), "LINUX");
	}

	@Test(expectedExceptions = IllegalArgumentException.class)
	public void setupBrowserDetailsIllegalDevicePlatformTest() {
		Map<String, String> browserDetails = new HashMap<>();
		browserDetails.put("devicePlatform" ,"Sun");
		SeleniumSetup.setupBrowserDetails(capabilities, browserDetails).getPlatform();
	}

	@Test
	public void setupBrowserCapabilityAndroidTest() throws InvalidBrowserException {
		Assert.assertEquals(SeleniumSetup.setupBrowserCapability(Browsers.Android).getBrowserName(), "android");
		Assert.assertEquals(
				SeleniumSetup.setupBrowserCapability(Browsers.Android).getCapability(CapabilityType.BROWSER_NAME),
				"android");
	}

	@Test
	public void setupBrowserCapabilityChromeTest() throws InvalidBrowserException {
		Assert.assertEquals(SeleniumSetup.setupBrowserCapability(Browsers.Chrome).getBrowserName(), "chrome");
		Assert.assertEquals(
				SeleniumSetup.setupBrowserCapability(Browsers.Chrome).getCapability(CapabilityType.BROWSER_NAME),
				"chrome");
	}

	@Test
	public void setupBrowserCapabilityEdgeTest() throws InvalidBrowserException {
		Assert.assertEquals(SeleniumSetup.setupBrowserCapability(Browsers.Edge).getBrowserName(), "MicrosoftEdge");
		Assert.assertEquals(
				SeleniumSetup.setupBrowserCapability(Browsers.Edge).getCapability(CapabilityType.BROWSER_NAME),
				"MicrosoftEdge");
	}

	@Test
	public void setupBrowserCapabilityFirefoxTest() throws InvalidBrowserException {
		Assert.assertEquals(SeleniumSetup.setupBrowserCapability(Browsers.Firefox).getBrowserName(), "firefox");
		Assert.assertEquals(
				SeleniumSetup.setupBrowserCapability(Browsers.Firefox).getCapability(CapabilityType.BROWSER_NAME),
				"firefox");
	}

	@Test
	public void setupBrowserCapabilityHtmlUnitTest() throws InvalidBrowserException {
		Assert.assertEquals(SeleniumSetup.setupBrowserCapability(Browsers.HtmlUnit).getBrowserName(), "htmlunit");
		Assert.assertEquals(
				SeleniumSetup.setupBrowserCapability(Browsers.HtmlUnit).getCapability(CapabilityType.BROWSER_NAME),
				"htmlunit");
	}

	@Test
	public void setupBrowserCapabilityInternetExplorerTest() throws InvalidBrowserException {
		Assert.assertEquals(SeleniumSetup.setupBrowserCapability(Browsers.InternetExplorer).getBrowserName(),
				"internet explorer");
		Assert.assertEquals(SeleniumSetup.setupBrowserCapability(Browsers.InternetExplorer)
				.getCapability(CapabilityType.BROWSER_NAME), "internet explorer");
	}

	@Test
	public void setupBrowserCapabilityIpadTest() throws InvalidBrowserException {
		Assert.assertEquals(SeleniumSetup.setupBrowserCapability(Browsers.Ipad).getBrowserName(), "iPad");
		Assert.assertEquals(
				SeleniumSetup.setupBrowserCapability(Browsers.Ipad).getCapability(CapabilityType.BROWSER_NAME), "iPad");
	}

	@Test
	public void setupBrowserCapabilityIphoneTest() throws InvalidBrowserException {
		Assert.assertEquals(SeleniumSetup.setupBrowserCapability(Browsers.Iphone).getBrowserName(), "iPhone");
		Assert.assertEquals(
				SeleniumSetup.setupBrowserCapability(Browsers.Iphone).getCapability(CapabilityType.BROWSER_NAME),
				"iPhone");
	}

	@Test
	public void setupBrowserCapabilityMaronetteTest() throws InvalidBrowserException {
		Assert.assertEquals(SeleniumSetup.setupBrowserCapability(Browsers.Marionette).getBrowserName(), "firefox");
		Assert.assertEquals(
				SeleniumSetup.setupBrowserCapability(Browsers.Marionette).getCapability(CapabilityType.BROWSER_NAME),
				"firefox");
		Assert.assertTrue(
				(Boolean) SeleniumSetup.setupBrowserCapability(Browsers.Marionette).getCapability("marionette"));
	}

	@Test
	public void setupBrowserCapabilityOperaTest() throws InvalidBrowserException {
		Assert.assertEquals(SeleniumSetup.setupBrowserCapability(Browsers.Opera).getBrowserName(), "operablink");
		Assert.assertEquals(
				SeleniumSetup.setupBrowserCapability(Browsers.Opera).getCapability(CapabilityType.BROWSER_NAME),
				"operablink");
	}

	@Test
	public void setupBrowserCapabilityPhantomJSTest() throws InvalidBrowserException {
		Assert.assertEquals(SeleniumSetup.setupBrowserCapability(Browsers.PhantomJS).getBrowserName(), "phantomjs");
		Assert.assertEquals(
				SeleniumSetup.setupBrowserCapability(Browsers.PhantomJS).getCapability(CapabilityType.BROWSER_NAME),
				"phantomjs");
	}

	@Test
	public void setupBrowserCapabilitySafariTest() throws InvalidBrowserException {
		Assert.assertEquals(SeleniumSetup.setupBrowserCapability(Browsers.Safari).getBrowserName(), "safari");
		Assert.assertEquals(
				SeleniumSetup.setupBrowserCapability(Browsers.Safari).getCapability(CapabilityType.BROWSER_NAME),
				"safari");
	}

	@Test(expectedExceptions = InvalidBrowserException.class)
	public void setupBrowserCapabilityIllegalBrowserTest() throws InvalidBrowserException {
		SeleniumSetup.setupBrowserCapability(Browsers.None);
	}

	@Test(expectedExceptions = InvalidBrowserException.class)
	public void setupDriverAndroidTest() throws InvalidBrowserException {
		SeleniumSetup.setupDriver(Browsers.Android, capabilities);
	}

	@Test
	public void setupDriverChromeTest() throws InvalidBrowserException {
		WebDriver driver = SeleniumSetup.setupDriver(Browsers.Chrome, capabilities);
		Assert.assertEquals(driver.getClass().getSimpleName(), "ChromeDriver");
		driver.quit();
	}

	// @Test
	// public void setupDriverEdgeTest() throws InvalidBrowserException {
	// WebDriver driver = SeleniumSetup.setupDriver(Browsers.Edge,
	// capabilities);
	// Assert.assertEquals(driver.getClass().getSimpleName(),
	// "MicrosoftEdgeDriver");
	// driver.quit();
	// }

	// @Test
	// public void setupDriverFirefoxTest() throws InvalidBrowserException {
	// WebDriver driver = SeleniumSetup.setupDriver(Browsers.Firefox,
	// capabilities);
	// Assert.assertEquals(driver.getClass().getSimpleName(), "FirefoxDriver");
	// driver.quit();
	// }

	@Test
	public void setupDriverHtmlUnitTest() throws InvalidBrowserException {
		WebDriver driver = SeleniumSetup.setupDriver(Browsers.HtmlUnit, capabilities);
		Assert.assertEquals(driver.getClass().getSimpleName(), "HtmlUnitDriver");
		driver.quit();
	}

	// @Test
	// public void setupDriverInternetExplorerTest() throws
	// InvalidBrowserException {
	// WebDriver driver = SeleniumSetup.setupDriver(Browsers.InternetExplorer,
	// capabilities);
	// Assert.assertEquals(driver.getClass().getSimpleName(),
	// "InternetExplorerDriver");
	// driver.quit();
	// }

	@Test(expectedExceptions = InvalidBrowserException.class)
	public void setupDriverIpadTest() throws InvalidBrowserException {
		SeleniumSetup.setupDriver(Browsers.Ipad, capabilities);
	}

	@Test(expectedExceptions = InvalidBrowserException.class)
	public void setupDriverIphoneTest() throws InvalidBrowserException {
		SeleniumSetup.setupDriver(Browsers.Iphone, capabilities);
	}

	// @Test
	// public void setupDriverMarionetteTest() throws InvalidBrowserException {
	// WebDriver driver = SeleniumSetup.setupDriver(Browsers.Marionette,
	// capabilities);
	// Assert.assertEquals(driver.getClass().getSimpleName(), "FirefoxDriver");
	// driver.quit();
	// }

	// @Test
	// public void setupDriverOperaTest() throws InvalidBrowserException {
	// WebDriver driver = SeleniumSetup.setupDriver(Browsers.Opera,
	// capabilities);
	// Assert.assertEquals(driver.getClass().getSimpleName(), "OperaDriver");
	// driver.quit();
	// }

	@Test(expectedExceptions = InvalidBrowserException.class)
	public void setupDriverPhantomJSTest() throws InvalidBrowserException {
		WebDriver driver = SeleniumSetup.setupDriver(Browsers.PhantomJS, capabilities);
		Assert.assertEquals(driver.getClass().getSimpleName(), "PhantomDriver");
		driver.quit();
	}

	// @Test
	// public void setupDriverSafariTest() throws InvalidBrowserException {
	// WebDriver driver = SeleniumSetup.setupDriver(Browsers.Safari,
	// capabilities);
	// Assert.assertEquals(driver.getClass().getSimpleName(), "SafariDriver");
	// driver.quit();
	// }

	@Test(expectedExceptions = InvalidBrowserException.class)
	public void setupDriverIllegalBrowserTest() throws InvalidBrowserException {
		SeleniumSetup.setupDriver(Browsers.None, capabilities);
	}
}