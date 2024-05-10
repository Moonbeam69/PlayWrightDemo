### Playwrght examples test cases

- Playwright test classes based on Java
- Use CodeGen to generate initial locators
- Use Junit5 script tags which can be used to control execution through pom.xml <groups> & <excludedGroups>
- Inclusion of a test data source
- Use of custom and AssertJ (soft) assertion
- Used caches in workfow file

Note

Builds took appr 20sec to execute locally (Windows/Inteli7) and up to 4 minutes on a (Windows/i5) self-hosted runner. Upon investigation, 
it appears that caching OpenJDK is done in the cloud by GitHub Actions and so offered limited apparent speed advantage over
downloading directly from the OpenJDK distributor (Temurin). I need to ptove still that

- a local (self-hosted) install of OpenJDK or
- relevant Docker image 

remedies this problem. 
