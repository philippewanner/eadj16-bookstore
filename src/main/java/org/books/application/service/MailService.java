package org.books.application.service;

import javax.ejb.Remote;

@Remote
public interface MailService {

	void sendEmail(String emailTo, String subject, String text);
}