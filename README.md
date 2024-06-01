## Getting Started ▶️

1. **Clone the Repository**: 
   ```bash
   git clone https://github.com/muskaan2lamba/FanCodeTask.git

2. Open the Project:
   Open the cloned project in Eclipse IDE.

3. Build the Project:
   Import as Existing Maven project to build the project and resolve dependencies.

## Running Tests
Follow these steps to run the automated tests:

4. **Run TestNG Suite**: 
   - Right-click on the `testng.xml` file and select "Run as TestNG suite."

5. **View Test Results**: 
   - Test results will be displayed in the console, including passes, failures, and skips.
   - An Extent Report named `extent-report.html` will be generated in the root folder with detailed logs.

## About the Framework
### Test Data
- **Constants**: Contains a configuration file with the Base URI and constants for longitude and latitude.

### Test API Endpoints
- **Endpoints**: Defines all the endpoints used in the tests.

### Test Models
- **Model**: Includes models for User and TodoResponse.

### Test Utility Methods
- **Utils**: Houses utility methods for converting responses to arrays and helper methods for checking latitude and longitude, and calculating task completion percentage.

### Test Implementation
Test cases are implemented in the `FancodeTask.java` file in the `com.task.fancode` package.

### Test Results
Test results will be displayed in the console, and an Extent Report (`extent-report.html`) will be generated in the root folder.

## Tools and Technologies Used
- Unirest
- TestNG
- Maven Surefire Plugin
- Extent Reports
- Eclipse
- Jackson API
- Java

## Author

- [Muskan Lamba](https://github.com/muskaan2lamba)
