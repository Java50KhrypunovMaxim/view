package telran.view;

import java.time.LocalDate;
import java.util.HashSet;
import java.util.function.Function;
import java.util.function.Predicate;

public interface InputOutput {
	String readString(String prompt);

	void writeString(String str);

	default void writeLine(Object obj) {
		writeString(obj.toString() + "\n");
	}

	default <T> T readObject(String prompt, String errorPrompt, Function<String, T> mapper) {
		T res = null;
		boolean running = false;
		do {
			String str = readString(prompt);
			running = false;
			try {
				res = mapper.apply(str);
			} catch (RuntimeException e) {
				writeLine(errorPrompt + " " + e.getMessage());
				running = true;
			}

		} while (running);
		return res;
	}

	
	default Integer readInt(String prompt, String errorPrompt) {
		return readObject(prompt, errorPrompt, Integer::parseInt);

	}

	default Long readLong(String prompt, String errorPrompt) {
		return readObject(prompt, errorPrompt, Long::parseLong);
	}

	default Double readDouble(String prompt, String errorPrompt) {
		return readObject(prompt, errorPrompt, Double::parseDouble);
	}

	default Double readNumberRange(String prompt, String errorPrompt, double min, double max) {
		return readObject(prompt, errorPrompt, string ->{
			double res = Double.parseDouble(string);
			if (res < min) {
				throw new IllegalArgumentException("must be not less than " + min);
			}
			if (res > max) {
				throw new IllegalArgumentException("must be not greater than " + max);
			}
			return res;
		});
		}
	
	default Integer readNumberRange(String prompt, String errorPrompt, int min, int max) {
		return readObject(prompt, errorPrompt, string ->{
			int res = Integer.parseInt(string);
			if (res < min) {
				throw new IllegalArgumentException("must be not less than " + min);
			}
			if (res > max) {
				throw new IllegalArgumentException("must be not greater than " + max);
			}
			return res;
		});
		}
		
	default String readStringPredicate(String prompt, String errorPrompt,
			Predicate<String> predicate) {
		return readObject(prompt, errorPrompt, str -> {
            if (predicate.test(str)) {
                return str;
            } else {
                throw new IllegalArgumentException("Input does not match the predicate.");
            }
        });
    }
	default String readStringOptions(String prompt, String errorPrompt,
			HashSet<String> options) {
		InputOutput io = new SystemInputOutput();
		io.writeLine("Options: ");
        for (String option : options) {
        	io.writeLine(option + ", ");
        }

        return readObject(prompt, errorPrompt, str -> {
            if (options.contains(str)) {
                return str;
            } else {
                throw new IllegalArgumentException("Input does not match any of the given options.");
            }
        });
    }	
	default LocalDate readIsoDate(String prompt, String errorPrompt) {
		return readObject(prompt, errorPrompt, LocalDate::parse);
	}
	default LocalDate readIsoDateRange(String prompt, String errorPrompt, LocalDate from,
			LocalDate to) {
		
		return readObject(prompt, errorPrompt, string ->{
			LocalDate res =LocalDate.parse(string);
			if (res.isBefore(from)) {
				throw new IllegalArgumentException("must be not less than " + from);
			}
			if (res.isAfter(to)) {
				throw new IllegalArgumentException("must be not greater than " + to);
			}
			return res;
		});
	}
	
	}
