package org.eql;

import static org.junit.Assert.*;

import org.junit.Test;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.firefox.FirefoxDriver;

public class TestSelenium_OK {

	@Test
	public void TestSeleniumJUnit () throws InterruptedException{

		System.setProperty("webdriver.gecko.driver", "src/main/resources/geckodriver.exe");

		WebDriver driver = new FirefoxDriver();
	
		//Acces au site
		driver.get("http://192.168.1.58:8090/jpetstore-1.0.5-env2/");
		Thread.sleep(100);
		
		//Acces a la page de login
		driver.findElement(By.xpath("//a[contains(@href,'shop/signonForm.do')]")).click();
		Thread.sleep(100);
		
		
		WebElement champPassword = driver.findElement(By.xpath("//input[@type='password'][@name='password']"));
		WebElement champUsername =  driver.findElement(By.xpath("//input[@type='text'][@name='username']"));
		
		ecrireChampText(champUsername, "j2ee");
		ecrireChampText(champPassword, "j2ee");
		
		//Submit le form
		driver.findElement(By.xpath("//input[@type='image'][@name='update']")).submit();
		Thread.sleep(150);
		
		//Test que ce soit le bon user
		assertTrue("Mauvais utilisateur",driver.findElement(By.xpath("//font[contains(text(),'Welcome')]")).getText().equals("Welcome ABC!"));
		Thread.sleep(100);
		
		//Test presence du bouton signoff
		try {
		assertTrue("Pas de bouton sign out",driver.findElement(By.xpath("//a[contains(@href,'shop/signoff.do')]")).isDisplayed());
		}
		catch(AssertionError ae) {
			System.out.println("pas de bouton sign out");
			throw ae;
		}
		Thread.sleep(100);
		
		//Acces categorie fish
		driver.findElement(By.xpath("//a[contains(@href,'viewCategory.do?categoryId=FISH')]")).click();
		Thread.sleep(100);
		
		//Test que l on soit bien dans la categorie Fish
		assertTrue("Pas dans la catégorie Fish",driver.findElement(By.xpath("//h2[text()='Fish']")).getText().equals("Fish"));
		
		//Koi best fish
		driver.findElement(By.xpath("//a[contains(@href,'viewProduct.do?productId=FI-FW-01')]")).click();
		Thread.sleep(100);
		
		//Add to cart
		driver.findElement(By.xpath("//a[contains(@href,'shop/addItemToCart.do?workingItemId=EST-4')]")).click();
		Thread.sleep(100);
		
		//Test si dans le panier
		assertTrue("Pas dans le Panier",driver.findElement(By.xpath("//h2[.='Shopping Cart']"))!=null);
		Thread.sleep(100);
		
		//Changement de la quantite
		driver.findElement(By.xpath("//input[@type='text'][@name='EST-4']")).clear();
		Thread.sleep(100);
		driver.findElement(By.xpath("//input[@type='text'][@name='EST-4']")).sendKeys("2");
		Thread.sleep(100);
		
		//Update du cart
		driver.findElement(By.xpath("//input[@type='image'][@name='update']")).click();
		
		//Recuperation du prix unitaire et changement de type
		String list_cost = driver.findElement(By.xpath("//form[contains(@action,'shop/updateCartQuantities.do')]/table/tbody/tr[2]/td[6]")).getText();
		System.out.println("Coût unitaire pré traitement : " + list_cost);
		list_cost = list_cost.substring(1);
		list_cost = list_cost.replace(",", ".");
		System.out.println("Coût unitaire post traitement : " + list_cost);
		
		//Recuperation du prix total et changement de type
		String tot_cost = driver.findElement(By.xpath("//form[contains(@action,'shop/updateCartQuantities.do')]/table/tbody/tr[2]/td[7]")).getText();
		System.out.println("Coût total pré traitement : " + tot_cost);
		tot_cost = tot_cost.substring(1);
		tot_cost = tot_cost.replace(",", ".");
		System.out.println("Coût total post traitement : " + tot_cost);
		
		double tot_cost_double = Double.parseDouble(tot_cost);
		double list_cost_double =Double.parseDouble(list_cost);
		
		//Test que le prix total corresponde bien au prix unitaire x2
		assertTrue("Prix erroné",tot_cost_double==(2*list_cost_double));
		
		Thread.sleep(600);
		driver.quit();
	}

	
	public void ecrireChampText(WebElement we, String text) {
		we.clear();
		we.sendKeys(text);
	}
}
