package testpacakage;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import io.github.bonigarcia.wdm.WebDriverManager;

import java.time.Duration;

public class GenerateCertificate {

    public static void main(String[] args) {
        // Step 1: Initialize WebDriver and login
        WebDriver driver = initializeWebDriver();

        try {
            login(driver);

            // Step 2: Navigate to certificates
            navigateToCertificates(driver);

            // Step 3: Select the certificate type
            selectCertificateType(driver);

            // Step 4: Search and select the student
            searchAndSelectStudent(driver, "Sam");

            // Step 5: Click on generate
            clickGenerate(driver);

            // Step 6: Update remarks
            updateRemarks(driver, "Leaving school for higher studies");

            // Step 7: Generate and download the certificate
            generateAndDownload(driver);

            // Step 8: Validate the history of certificates
            validateHistory(driver, "Sam");

        } finally {
            // Close the browser
            driver.quit();
        }
    }

    private static WebDriver initializeWebDriver() {
        // Set up WebDriver using WebDriverManager
        WebDriverManager.chromedriver().setup();

        // Configure Chrome options
        ChromeOptions options = new ChromeOptions();
        options.addArguments("--disable-extensions", "--disable-user-media-security=true",
                "--allow-file-access-from-files", "--use-fake-device-for-media-stream",
                "--use-fake-ui-for-media-stream", "--disable-popup-blocking",
                "--disable-infobars", "--enable-usermedia-screen-capturing",
                "--disable-dev-shm-usage", "--no-sandbox",
                "--auto-select-desktop-capture-source=Screen 1",
                "--disable-blink-features=AutomationControlled");

        WebDriver driver = new ChromeDriver(options);
        driver.manage().window().maximize();
        return driver;
    }

    private static void login(WebDriver driver) {
        driver.get("https://accounts.teachmint.com/");

        // Assuming phone number and OTP login
        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(30));
        WebElement phoneInput = wait.until(ExpectedConditions.elementToBeClickable(By.xpath("//input[@type='text']")));
        phoneInput.sendKeys("123451");

        driver.findElement(By.id("send-otp-btn-id")).click();
        wait.until(ExpectedConditions.invisibilityOfElementLocated(By.cssSelector("loader")));

        // Enter OTP (Assume OTP: 120992 for example)
        String[] otp = {"1", "2", "0", "9", "9", "2"};
        for (int i = 0; i < otp.length; i++) {
            driver.findElement(By.xpath("//input[@data-group-idx='" + i + "']")).sendKeys(otp[i]);
        }

        driver.findElement(By.id("submit-otp-btn-id")).click();
        wait.until(ExpectedConditions.invisibilityOfElementLocated(By.cssSelector("loader")));

        // Select the correct account
        WebElement accountElement = wait.until(ExpectedConditions.elementToBeClickable(By.xpath("//div[text()='@Automation-2']")));
        accountElement.click();

        wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath("//a[text()='Dashboard']")));
    }

    private static void navigateToCertificates(WebDriver driver) {
        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(10));
        WebElement certificatesMenu = wait.until(ExpectedConditions.elementToBeClickable(By.id("certificatesMenu")));
        certificatesMenu.click();
    }

    private static void selectCertificateType(WebDriver driver) {
        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(10));
        WebElement certificateTypeDropdown = wait.until(ExpectedConditions.elementToBeClickable(By.id("certificateType")));
        certificateTypeDropdown.click();

        WebElement leavingCertificateOption = driver.findElement(By.xpath("//option[@value='leavingCertificate']"));
        leavingCertificateOption.click();
    }

    private static void searchAndSelectStudent(WebDriver driver, String studentName) {
        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(10));
        WebElement searchBox = wait.until(ExpectedConditions.elementToBeClickable(By.id("studentSearchBox")));
        searchBox.sendKeys(studentName);

        WebElement searchButton = driver.findElement(By.id("searchButton"));
        searchButton.click();

        WebElement studentResult = wait.until(ExpectedConditions.elementToBeClickable(By.xpath("//div[text()='" + studentName + "']")));
        studentResult.click();
    }

    private static void clickGenerate(WebDriver driver) {
        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(10));
        WebElement generateButton = wait.until(ExpectedConditions.elementToBeClickable(By.id("generateButton")));
        generateButton.click();
    }

    private static void updateRemarks(WebDriver driver, String remarks) {
        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(10));
        WebElement remarksBox = wait.until(ExpectedConditions.elementToBeClickable(By.id("remarksBox")));
        remarksBox.sendKeys(remarks);
    }

    private static void generateAndDownload(WebDriver driver) {
        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(10));
        WebElement generateAndDownloadButton = wait.until(ExpectedConditions.elementToBeClickable(By.id("generateAndDownloadButton")));
        generateAndDownloadButton.click();
    }

    private static void validateHistory(WebDriver driver, String studentName) {
        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(10));
        WebElement historyMenu = wait.until(ExpectedConditions.elementToBeClickable(By.id("historyMenu")));
        historyMenu.click();

        WebElement historyEntry = wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//div[text()='" + studentName + "']")));
        if (historyEntry.isDisplayed()) {
            System.out.println("Certificate generation history validated for student: " + studentName);
        } else {
            System.out.println("Certificate generation history validation failed for student: " + studentName);
        }
    }
}
