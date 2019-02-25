/* AbstractController.java
 *
 * Copyright (C) 2012 Universidad de Sevilla
 * 
 * The use of this project is hereby constrained to the conditions of the 
 * TDG Licence, a copy of which you may download from 
 * http://www.tdg-seville.info/License.html
 * 
 */

package es.us.isa.ideas.app.controllers;

import javax.inject.Inject;

//import org.apache.commons.lang.exception.ExceptionUtils;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.TransactionStatus;
import org.springframework.transaction.support.DefaultTransactionDefinition;
import org.springframework.util.Assert;
import org.springframework.util.ClassUtils;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.servlet.ModelAndView;

@Controller
public class AbstractController {
	
	// Panic handler ----------------------------------------------------------
	
	@ExceptionHandler(Throwable.class)
	public ModelAndView panic(Throwable oops) {
		Assert.notNull(oops);
		
		ModelAndView result;

		result = new ModelAndView("misc/panic");
		result.addObject("name", ClassUtils.getShortName(oops.getClass()));
		result.addObject("message", oops.getMessage());
                result.addObject("message_type", "message.type.error");
		result.addObject("exception", oops);

		return result;
	}
	
	// Transaction management -------------------------------------------------
	
	@Inject
	private PlatformTransactionManager transactionManager;
	private TransactionStatus txStatus;
	
	protected void beginTransaction() {
		Assert.isNull(txStatus);
				
		beginTransaction(false);
	}
	
	protected void beginTransaction(boolean readOnly) {
		Assert.isNull(txStatus);
		
		DefaultTransactionDefinition definition; 
			
		definition = new DefaultTransactionDefinition();
		definition.setIsolationLevel(DefaultTransactionDefinition.ISOLATION_DEFAULT);
		definition.setPropagationBehavior(DefaultTransactionDefinition.PROPAGATION_REQUIRES_NEW);
		definition.setReadOnly(readOnly);		
		txStatus = transactionManager.getTransaction(definition);		
	}		
	
	protected void commitTransaction() throws Throwable {
		Assert.notNull(txStatus);
		
		try {
			transactionManager.commit(txStatus);
			txStatus = null;
		} catch (Throwable oops) {
			throw oops;
		}		
	}
	
	protected void rollbackTransaction() throws Throwable {
		Assert.notNull(txStatus);
		
		try {
			if (!txStatus.isCompleted())
				transactionManager.rollback(txStatus);
			txStatus = null;
		} catch (Throwable oops) {
			throw oops;
		} 
	}

}
