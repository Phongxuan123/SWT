package phongnxk;

import org.junit.jupiter.api.*;
import static org.junit.jupiter.api.Assertions.*;
import java.io.*;
import java.util.*;

public class AccountServiceTest {
    private AccountService service;
    private static final String TEST_DATA = "src/test/resources/test-data.csv";
    private static final String UNIT_TEST_RESULT = "src/test/resources/UnitTest";

    @BeforeEach
    public void setUp() {
        service = new AccountService();
    }

    @Test
    public void testRegisterAccountWithCSV() throws Exception {
        List<String> results = new ArrayList<>();
        try (BufferedReader br = new BufferedReader(new FileReader(TEST_DATA))) {
            String line = br.readLine(); // skip header
            while ((line = br.readLine()) != null) {
                String[] parts = line.split(",");
                String username = parts[0];
                String password = parts[1];
                String email = parts[2];
                boolean expected = Boolean.parseBoolean(parts[3]);
                boolean actual = service.registerAccount(username, password, email);
                results.add(username + "," + password + "," + email + "," + expected + "," + actual);
                assertEquals(expected, actual, "Failed for: " + Arrays.toString(parts));
            }
        }
        // Write results to UnitTest file
        try (BufferedWriter bw = new BufferedWriter(new FileWriter(UNIT_TEST_RESULT))) {
            bw.write("username,password,email,expected,actual\n");
            for (String r : results) {
                bw.write(r + "\n");
            }
        }
    }

    @Test
    public void testIsValidEmail() {
        assertTrue(service.isValidEmail("test@mail.com"));
        assertFalse(service.isValidEmail("invalidmail.com"));
        assertFalse(service.isValidEmail(null));
        assertFalse(service.isValidEmail(""));
    }

    @Test
    public void testRegisterAccount_UsernameNullOrEmpty() {
        assertFalse(service.registerAccount(null, "password123", "user@mail.com"));
        assertFalse(service.registerAccount("", "password123", "user@mail.com"));
    }

    @Test
    public void testRegisterAccount_PasswordNullOrShort() {
        assertFalse(service.registerAccount("user1", null, "user@mail.com"));
        assertFalse(service.registerAccount("user1", "", "user@mail.com"));
        assertFalse(service.registerAccount("user1", "short", "user@mail.com"));
        assertFalse(service.registerAccount("user1", "123456", "user@mail.com"));
        assertTrue(service.registerAccount("user1", "1234567", "user@mail.com"));
    }

    @Test
    public void testRegisterAccount_AllFieldsNullOrEmpty() {
        assertFalse(service.registerAccount(null, null, null));
        assertFalse(service.registerAccount("", "", ""));
    }
}
