package com.example.backend;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;

@EnableScheduling
@SpringBootApplication
public class BackendApplication {

	public static void main(String[] args) {
		loadLocalEnv();
		SpringApplication.run(BackendApplication.class, args);
	}

	/**
	 * Loads Backend/.env for local development before Spring resolves properties.
	 * Real environment variables and JVM -D properties always take precedence.
	 */
	private static void loadLocalEnv() {
		Path envFile = Path.of(".env");
		if (!Files.isRegularFile(envFile)) {
			return;
		}

		try (var lines = Files.lines(envFile, StandardCharsets.UTF_8)) {
			lines.map(String::trim)
					.filter(line -> !line.isEmpty() && !line.startsWith("#"))
					.forEach(line -> {
						int separator = line.indexOf('=');
						if (separator <= 0) return;
						String key = line.substring(0, separator).trim();
						String value = stripQuotes(line.substring(separator + 1).trim());
						if (System.getenv(key) == null && System.getProperty(key) == null) {
							System.setProperty(key, value);
						}
					});
		} catch (IOException exception) {
			System.err.println("Unable to load .env: " + exception.getMessage());
		}
	}

	private static String stripQuotes(String value) {
		if (value.length() >= 2 && ((value.startsWith("\"") && value.endsWith("\""))
				|| (value.startsWith("'") && value.endsWith("'")))) {
			return value.substring(1, value.length() - 1);
		}
		return value;
	}

}
