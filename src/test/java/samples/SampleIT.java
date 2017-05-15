package samples;

import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertNotEquals;
import static org.testng.Assert.assertTrue;
import java.util.Arrays;

import org.testng.annotations.BeforeClass;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

import tools.TestBase;
import tools.output.Action;
import tools.output.Assert;
import tools.output.Selenium.Locators;

public class SampleIT extends TestBase {

	@BeforeClass(alwaysRun = true)
	public void beforeClass() throws Exception {
		// set the base URL for the tests here
		testSite = "http://172.31.2.65/";
		// set the author of the tests here
		author = "Max Saperstone\n<br/>max.saperstone@coveros.com";
		// set the version of the tests or of the software, possibly with a
		// dynamic check
		version = "0.0.1";
	}

	@DataProvider(name = "car list items", parallel = true)
	public Object[][] DataSetOptions() {
		return new Object[][] { new Object[] { "volvo" }, new Object[] { "saab" }, new Object[] { "mercedes" } };
	}

	@Test(groups = { "sample", "virtual" }, description = "A sample test to check a title")
	public void sampleTest() throws Exception {
		// use this object to verify our page looks as expected
		Assert asserts = this.asserts.get();
		// perform our verification
		asserts.compareTitle("Selenified Test Page");
		// perform our verification
		finish();
	}

	@Test(groups = { "sample", "virtual" }, description = "A sample negative test to check a title")
	public void sampleNegativeTest() throws Exception {
		// use this object to verify our page looks as expected
		Assert asserts = this.asserts.get();
		// perform some actions
		asserts.compareTitle("Yahoo");
		// perform our verification
		finish(1);
	}

	@Test(dataProvider = "car list items", groups = { "sample",
			"virtual" }, description = "A sample test using a data provider to perform searches")
	public void sampleTestWDataProvider(String listItem) throws Exception {
		// use this object to manipulate our page
		Action actions = this.actions.get();
		// perform some actions
		actions.select(Locators.ID, "car_list", listItem);
		// close out the test
		finish();
	}

	@Test(groups = { "sample", "virtual" }, description = "A sample test to check the goToURL method")
	public void sampleTestGoToURL() throws Exception {
		// use this object to manipulate our page
		Action actions = this.actions.get();
		// use this object to verify our page looks as expected
		Assert asserts = this.asserts.get();
		// perform some actions
		actions.goToURL("https://www.google.com/");
		asserts.compareURL("https://www.google.com/");
		// verify no issues
		finish();
	}

	@Test(groups = { "sample", "virtual" }, description = "A sample negative test to check the goToURL method")
	public void sampleTestNegativeGoToURL() throws Exception {
		// use this object to manipulate our page
		Action actions = this.actions.get();
		// use this object to verify our page looks as expected
		Assert asserts = this.asserts.get();
		// perform some actions
		actions.goToURL("https://www.yahoo.com/");
		asserts.compareURL("https://www.google.com/");
		// verify no issues
		finish(1);
	}

	@Test(groups = { "sample", "virtual" }, description = "A sample test to check the waitForElementPresent method")
	public void sampleTestWaitForElementPresent() throws Exception {
		// use this object to manipulate our page
		Action actions = this.actions.get();
		// perform some actions
		actions.waitForElementPresent(Locators.NAME, "car_list");
		// verify no issues
		finish();
	}

	@Test(groups = { "sample",
			"virtual" }, description = "A sample negative test to check the waitForElementPresent method")
	public void sampleTestNegativeWaitForElementPresent() throws Exception {
		// use this object to manipulate our page
		Action actions = this.actions.get();
		// perform some actions
		actions.waitForElementPresent(Locators.NAME, "non-existent-name");
		// verify no issues
		finish(1);
	}

	@Test(groups = { "sample", "virtual" }, description = "A sample test to check the waitForElementNotPresent method")
	public void sampleTestWaitForElementNotPresent() throws Exception {
		// use this object to manipulate our page
		Action actions = this.actions.get();
		// perform some actions
		actions.waitForElementNotPresent(Locators.NAME, "non-existent-name");
		// verify no issues
		finish();
	}

	@Test(groups = { "sample",
			"virtual" }, description = "A sample negative test to check the waitForElementNotPresent method")
	public void sampleTestNegativeWaitForElementNotPresent() throws Exception {
		// use this object to manipulate our page
		Action actions = this.actions.get();
		// perform some actions
		actions.waitForElementNotPresent(Locators.NAME, "car_list");
		// verify no issues
		finish(1);
	}

	@Test(groups = { "sample", "virtual" }, description = "A sample test to check the waitForElementDisplayed method")
	public void sampleTestWaitForElementDisplayed() throws Exception {
		// use this object to manipulate our page
		Action actions = this.actions.get();
		// perform some actions
		actions.waitForElementDisplayed(Locators.NAME, "car_list");
		// verify no issues
		finish();
	}

	@Test(groups = { "sample",
			"virtual" }, description = "A sample negative test to check the waitForElementDisplayed method")
	public void sampleTestNegativeWaitForElementDisplayed() throws Exception {
		// use this object to manipulate our page
		Action actions = this.actions.get();
		// perform some actions
		actions.waitForElementDisplayed(Locators.NAME, "non-existent-name");
		// verify no issues
		finish(1);
	}

	@Test(groups = { "sample",
			"virtual" }, description = "A sample test to check the WaitForElementNotDisplayed method")
	public void sampleTestWaitForElementNotDisplayed() throws Exception {
		// use this object to manipulate our page
		Action actions = this.actions.get();
		// perform some actions
		actions.waitForElementNotDisplayed(Locators.NAME, "hidden_div");
		// verify no issues
		finish();
	}

	@Test(groups = { "sample",
			"virtual" }, description = "A sample negative test to check the WaitForElementNotDisplayed method")
	public void sampleTestNegativeWaitForElementNotDisplayed() throws Exception {
		// use this object to manipulate our page
		Action actions = this.actions.get();
		// use this object to verify our page looks as expected
		actions.waitForElementNotDisplayed(Locators.NAME, "car_list");
		// verify no issues
		finish(1);
	}

	@Test(groups = { "sample", "virtual" }, description = "A sample test to check the WaitForElementEnabled method")
	public void sampleTestWaitForElementEnabled() throws Exception {
		// use this object to manipulate our page
		Action actions = this.actions.get();
		// perform some actions
		actions.waitForElementEnabled(Locators.NAME, "car_list");
		// verify no issues
		finish();
	}

	@Test(groups = { "sample" }, description = "A sample negative test to check the WaitForElementEnabled method")
	public void sampleTestNegativeWaitForElementEnabled() throws Exception {
		// use this object to manipulate our page
		Action actions = this.actions.get();
		// perform some actions
		actions.waitForElementEnabled(Locators.NAME, "enable_button");
		// verify no issues
		finish(1);
	}

	@Test(groups = { "sample" }, description = "A sample test to check the WaitForElementNotEnabled method")
	public void sampleTestWaitForElementNotEnabled() throws Exception {
		// use this object to manipulate our page
		Action actions = this.actions.get();
		// perform some actions
		actions.waitForElementNotEnabled(Locators.NAME, "enable_button");
		// verify no issues
		finish();
	}

	@Test(groups = { "sample",
			"virtual" }, description = "A sample negative test to check the WaitForElementNotEnabled method")
	public void sampleTestNegativeWaitForNotElementEnabled() throws Exception {
		// use this object to manipulate our page
		Action actions = this.actions.get();
		// perform some actions
		actions.waitForElementNotEnabled(Locators.NAME, "car_list");
		// verify no issues
		finish(1);
	}

	@Test(groups = { "sample", "virtual" }, description = "A sample test to check the getNumOfSelectOptions method")
	public void sampleGetNumOfSelectOptionsTest() throws Exception {
		// use this object to manipulate our page
		Action actions = this.actions.get();
		// perform some actions
		assertEquals(actions.getNumOfSelectOptions(Locators.NAME, "car_list"), 4);
		// verify no issues
		finish();
	}

	@Test(groups = { "sample",
			"virtual" }, description = "A sample negative test to check the getNumOfSelectOptions method")
	public void sampleNegativeGetNumOfSelectOptionsTest() throws Exception {
		// use this object to manipulate our page
		Action actions = this.actions.get();
		// perform some actions
		assertNotEquals(actions.getNumOfSelectOptions(Locators.NAME, "car_list"), 0);
		// verify no issues
		finish();
	}

	@Test(groups = { "sample", "virtual" }, description = "A sample test to check the getSelectOptions method")
	public void sampleGetSelectOptionsTest() throws Exception {
		// use this object to manipulate our page
		Action actions = this.actions.get();
		// perform some actions
		assertTrue(Arrays.equals(actions.getSelectOptions(Locators.NAME, "car_list"),
				new String[] { "volvo", "saab", "mercedes", "audi" }));
		// verify no issues
		finish();
	}

	@Test(groups = { "sample", "virtual" }, description = "A sample test to check the getTableRows method")
	public void sampleGetTableRows() throws Exception {
		// use this object to manipulate our page
		Action actions = this.actions.get();
		// perform some actions
		assertEquals(actions.getTableRows(Locators.ID, "table").size(), 7);
		// verify no issues
		finish();
	}

	@Test(groups = { "sample", "virtual" }, description = "A sample negative test to check the getTableRows method")
	public void sampleNegativeGetTableRows() throws Exception {
		// use this object to manipulate our page
		Action actions = this.actions.get();
		// perform some actions
		assertNotEquals(actions.getTableRows(Locators.ID, "table").size(), 0);
		// verify no issues
		finish();
	}

	@Test(groups = { "sample", "virtual" }, description = "A sample test to check the getNumOfTableRows method")
	public void sampleGetNumOfTableRows() throws Exception {
		// use this object to manipulate our page
		Action actions = this.actions.get();
		// perform some actions
		assertEquals(actions.getNumOfTableRows(Locators.ID, "table"), 7);
		// verify no issues
		finish();
	}

	@Test(groups = { "sample",
			"virtual" }, description = "A sample negative test to check the getNumOfTableRows method")
	public void sampleNegativeGetNumOfTableRows() throws Exception {
		// use this object to manipulate our page
		Action actions = this.actions.get();
		// perform some actions
		assertNotEquals(actions.getNumOfTableRows(Locators.ID, "table"), 0);
		// verify no issues
		finish();
	}

	@Test(groups = { "sample", "virtual" }, description = "A sample test to check the getTableColumns method")
	public void sampleGetTableColumns() throws Exception {
		// use this object to manipulate our page
		Action actions = this.actions.get();
		// perform some actions
		assertEquals(actions.getTableColumns(Locators.ID, "table").size(), 3);
		// verify no issues
		finish();
	}

	@Test(groups = { "sample", "virtual" }, description = "A sample negative test to check the getTableColumns method")
	public void sampleNegativeGetTableColumns() throws Exception {
		// use this object to manipulate our page
		Action actions = this.actions.get();
		// perform some actions
		assertNotEquals(actions.getTableColumns(Locators.ID, "table").size(), 0);
		// verify no issues
		finish();
	}

	@Test(groups = { "sample", "virtual" }, description = "A sample test to check the getNumOfTableColumns method")
	public void sampleGetNumOfTableColumns() throws Exception {
		// use this object to manipulate our page
		Action actions = this.actions.get();
		// perform some actions
		assertEquals(actions.getNumOfTableColumns(Locators.ID, "table"), 3);
		// verify no issues
		finish();
	}

	@Test(groups = { "sample",
			"virtual" }, description = "A sample negative test to check the getNumOfTableColumns method")
	public void sampleNegativeGetNumOfTableColumns() throws Exception {
		// use this object to manipulate our page
		Action actions = this.actions.get();
		// perform some actions
		assertNotEquals(actions.getNumOfTableColumns(Locators.ID, "table"), 0);
		// verify no issues
		finish();
	}

	@Test(groups = { "sample" }, description = "A sample test to check the scroll method")
	public void sampleScrollTest() throws Exception {
		// use this object to manipulate our page
		Action actions = this.actions.get();
		// perform some actions
		actions.click(Locators.NAME, "scroll_button");
		actions.scroll(50);
		// verify no issues
		finish();
	}

	@Test(groups = { "sample" }, description = "A sample negative test to check the scroll method")
	public void sampleNegativeScrollTest() throws Exception {
		// use this object to manipulate our page
		Action actions = this.actions.get();
		// perform some actions
		actions.scroll(50);
		// verify no issues
		finish(1);
	}
}