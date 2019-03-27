/* HashPassword.java
 *
 * Copyright (C) 2012 Universidad de Sevilla
 * 
 * The use of this project is hereby constrained to the conditions of the 
 * TDG Licence, a copy of which you may download from 
 * http://www.tdg-seville.info/License.html
 * 
 */

package es.us.isa.ideas.app.util;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import org.springframework.security.crypto.password.MessageDigestPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;



public class HashPassword {

	public static void main(String[] args) throws IOException {
		PasswordEncoder encoder;
		InputStreamReader stream;
		BufferedReader reader;
		String line, hash;

		encoder = new MessageDigestPasswordEncoder("MD5");
		stream = new InputStreamReader(System.in);
		reader = new BufferedReader(stream);

		System.out.println("Enter passwords to be hashed or <ENTER> to quit");

		line = reader.readLine();
		while (!line.isEmpty()) {
			hash = encoder.encode(line);
			System.out.println(hash);
			line = reader.readLine();
		}
	}

}
