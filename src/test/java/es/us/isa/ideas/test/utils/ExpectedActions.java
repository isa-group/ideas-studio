package es.us.isa.ideas.test.utils;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

/**
 * Applied Software Engineering Research Group (ISA Group) University of
 * Sevilla, Spain
 *
 * @author Felipe Vieira da Cunha Serafim <fvieiradacunha@us.es>
 * @version 1.0
 */
public class ExpectedActions {
	
	private WebDriver webDriver;
	private WebDriverWait wait;
	
	public ExpectedActions(int seconds) {
		WebDriver driver = TestCase.getWebDriver();
		this.webDriver = driver;
		this.wait = new WebDriverWait(driver, seconds);
	}
	
	public ExpectedActions() {
		WebDriver driver = TestCase.getWebDriver();
		this.webDriver = driver;
		this.wait = new WebDriverWait(driver, 30);
	}
	
	public WebDriver getWebDriver() {
		return this.webDriver;
	}
	
	public WebDriverWait getWait() {
		return this.wait;
	}
	
	public void click(By locator) {
		this.wait.until(ExpectedConditions.elementToBeClickable(locator)).click();
	}
	
	public void doubleClick(By locator) {
		WebElement element = this.wait.until(ExpectedConditions.elementToBeClickable(locator));
		Actions action = new Actions(this.getWebDriver());
		action.doubleClick(element);
		action.perform();
	}
	
	public void sendKeys(By locator, CharSequence... keys) {
		this.wait.until(ExpectedConditions.visibilityOfElementLocated(locator)).sendKeys(keys);
	}
	
	/**
	 * Simulates a right-click of mouse by triggering the mousedown and mouseup
	 * events of JavaScript.
	 * 
	 * @param driver
	 * @param locator
	 * @throws InterruptedException
	 */
	public void contextNodeMenu(By locator)
			throws InterruptedException {

		// Simulating right-click
		TestCase.getJs().executeScript("$('span.dynatree-active').trigger({type:'mousedown',button:2}).trigger({type:'mouseup',button:2})");

	}
	
	public boolean isCurrentUrlContained(String url) {
		return TestCase.getWebDriver().getCurrentUrl().contains(url);
	}

}
