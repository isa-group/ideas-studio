/* QueryDatabase.java
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
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.EntityTransaction;
import javax.persistence.Persistence;
import javax.persistence.Query;

public class QueryDatabase {

	@SuppressWarnings("unchecked")
	public static void main(String[] args) throws IOException {

		EntityManagerFactory emf;
		EntityManager em;
		EntityTransaction et;
		Query query;
		List<Object> result;
		int affected;

		InputStreamReader stream;
		BufferedReader reader;
		String line;

		emf = Persistence.createEntityManagerFactory("persistenceUnit");
		em = emf.createEntityManager();
		et = em.getTransaction();

		stream = new InputStreamReader(System.in);
		reader = new BufferedReader(stream);

		line = reader.readLine();
		while (!line.equals("quit")) {
			try {
				if (!line.isEmpty()) {
					if (line.equals("begin"))
						et.begin();
					else if (line.equals("commit"))
						et.commit();
					else if (line.equals("rollback"))
						et.rollback();
					else {
						query = em.createQuery(line);
						if (line.startsWith("update") || line.startsWith("delete")) {
							affected = query.executeUpdate();
							System.out.println(String.format("%d objects affected", affected));
						} else {
							result = query.getResultList();
							System.out.println(String.format("%d results found", result.size()));
							for (Object obj : result)
								if (!(obj instanceof Object[]))
									System.out.println(obj);
								else {
									for (Object subObj : (Object[]) obj) {
										System.out.print(subObj);
										System.out.print(' ');
									}
									System.out.println();
								}
						}
					}
				}
			} catch (Throwable oops) {
				System.err.println(oops.getMessage());
				// oops.printStackTrace(System.err);
			}
			line = reader.readLine();
		}

	}
}
