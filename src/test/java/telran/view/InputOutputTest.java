package telran.view;

import static org.junit.jupiter.api.Assertions.*;

import java.time.LocalDate;
import java.util.HashSet;
import java.util.function.Predicate;

import org.junit.jupiter.api.Test;
record User(String username, String password,
		LocalDate dateLastLogin, String phoneNumber, int numberOfLogins) {}
class InputOutputTest {
InputOutput io = new SystemInputOutput();

@Test
void readIntTest() {
    int number = io.readInt("Enter an integer:", "Invalid input. Please enter an integer.");
    io.writeLine("Entered number: " + number);
}
@Test
void readDoubleTest() {
    double number = io.readDouble("Enter a double:", "Invalid input. Please enter a valid double.");
    io.writeLine("Entered number: " + number);
}

@Test
void readNumberRangeTest() {
    double min = 0.0;
    double max = 100.0;
    double number = io.readNumberRange("Enter a number between " + min + " and " + max + ":",
            "Invalid input. Please enter a number within the range.", min, max);
    io.writeLine("Entered number within range: " + number);
}
@Test
void readStringPredicateTest() {
    Predicate<String> predicate = str -> str.length() >= 5; 
    String input = io.readStringPredicate("Enter a string with at least 5 characters:",
            "Invalid input. Please enter a valid string.", predicate);
    io.writeLine("Entered string: " + input);
}

@Test
void readStringOptionsTest() {
    HashSet<String> options = new HashSet<>();
    options.add("yes");
    options.add("no");
    String input = io.readStringOptions("Choose an option:", "Invalid option. Please choose a valid option.", options);
    io.writeLine("Selected option: " + input);
}

@Test
void readIsoDateTest() {
    LocalDate date = io.readIsoDate("Enter a date in ISO format (yyyy-MM-dd):",
            "Invalid date format. Please enter a date in yyyy-MM-dd format.");
    io.writeLine("Entered date: " + date);
}

@Test
void readIsoDateRangeTest() {
    LocalDate from = LocalDate.of(2024, 1, 1);
    LocalDate to = LocalDate.of(2024, 12, 31);

    LocalDate date = io.readIsoDateRange("Enter a date in ISO format within " + from + " and " + to + ":",
            "Invalid date format or date out of range. Please enter a valid date.", from, to);
    io.writeLine("Entered date within range: " + date);
}
	@Test
	void readObjectTest() {
		User user = io.readObject("Enter user in format <username>#<password>#<dateLastLogin>"
				+ "#<phone number>#<number of logins>", "Wrong user input format", str -> {
					String[] tokens = str.split("#");
					return new User(tokens[0], tokens[1],
							LocalDate.parse(tokens[2]), tokens[3], Integer.parseInt(tokens[4]));
				});
		io.writeLine(user);
	}
	
		@Test
	    void readUserByFields() {

	        Predicate<String> usernamePredicate = str -> str.matches("[A-Z][a-z]{5,20}");
	        Predicate<String> passwordPredicate = str -> {
	            if (str.length() < 8) {
	                return false;
	            }
	            boolean hasUpperCase = false;
	            boolean hasLowerCase = false;
	            boolean hasDigit = false;
	            boolean hasSpecialChar = false;
	            for (char ch : str.toCharArray()) {
	                if (Character.isUpperCase(ch)) {
	                    hasUpperCase = true;
	                } else if (Character.isLowerCase(ch)) {
	                    hasLowerCase = true;
	                } else if (Character.isDigit(ch)) {
	                    hasDigit = true;
	                } else if ("#@$*&%!?".indexOf(ch) != -1) {
	                    hasSpecialChar = true;
	                }
	            }

	            return hasUpperCase && hasLowerCase && hasDigit && hasSpecialChar;
	        };
	        Predicate<String> phoneNumberPredicate = str -> str.matches("^\\+972[5][0-9]{8}$");

	        String username = io.readStringPredicate("Enter username (at least 6 ASCII letters, starting with a capital letter):",
	                "Invalid username format. Please enter a valid username.", usernamePredicate);

	        String password = io.readStringPredicate("Enter password (at least 8 characters, including one uppercase, one lowercase, one digit, and one symbol from '#@$*&%'):",
	                "Invalid password format. Please enter a valid password.", passwordPredicate);
	        LocalDate today = LocalDate.now();
	        LocalDate firstday = LocalDate.parse("2000-01-01");
	        LocalDate dateLastLogin = io.readIsoDateRange("Enter date of last login (yyyy-MM-dd format):",
	                "Invalid date format. Please enter a valid date in yyyy-MM-dd format.", firstday,today);

	        String phoneNumber = io.readStringPredicate("Enter phone number (Israel mobile phone format +9725XXXXXXXX):",
	                "Invalid phone number format. Please enter a valid Israel mobile phone number.", phoneNumberPredicate);
	        
	        int numberOfLogins = io.readNumberRange("Enter number of logins (positive number):",
	                "Invalid input. Please enter a positive number of logins.", 0, 1000);

	        User user = new User(username, password, dateLastLogin, phoneNumber, numberOfLogins);
	        io.writeLine("Created User object: " + user);
	}

}
