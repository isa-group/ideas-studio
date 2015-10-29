/* HashPassword.java
 *
 * Copyright (C) 2012 Universidad de Sevilla
 * 
 * The use of this project is hereby constrained to the conditions of the 
 * TDG Licence, a copy of which you may download from 
 * http://www.tdg-seville.info/License.html
 * 
 */

package es.us.isa.ideas.utilities;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

import org.springframework.security.authentication.encoding.Md5PasswordEncoder;

public class HashPassword {

	public static void main(String[] args) throws IOException {
		Md5PasswordEncoder encoder;
		InputStreamReader stream;
		BufferedReader reader;
		String line, hash;

		encoder = new Md5PasswordEncoder();
		stream = new InputStreamReader(System.in);
		reader = new BufferedReader(stream);

		System.out.println("Enter passwords to be hashed or <ENTER> to quit");

		line = reader.readLine();
		while (!line.isEmpty()) {
			hash = encoder.encodePassword(line, null);
			System.out.println(hash);
			line = reader.readLine();
		}
	}

}
