package be.leerstad.helpers;

import org.apache.log4j.Logger;

import javax.activation.DataHandler;
import javax.activation.DataSource;
import javax.activation.FileDataSource;
import javax.mail.*;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeMultipart;
import java.io.InputStream;
import java.util.Properties;


public final class Email {
	
	    private static Logger log = Logger.getLogger("email");
	

	public boolean sendMail (String file, String titel) {
		
		boolean sent = false;
		final String userName;
		final String password;
		final String from;
		final String to;
		final String propertiesName = "mail.properties";
		Properties props = new Properties ();


		
		try (InputStream inputStream = ClassLoader.getSystemResourceAsStream (propertiesName)) {
			
			props.load (inputStream);
			
		} catch (Exception e) {
			log.error (e);
		}
		
		userName = props.getProperty ("mail.smtp.user");
		password = props.getProperty ("mail.smtp.password");

		from = props.getProperty ("mail.smtp.user");
		to = props.getProperty ("Receiver");


        Session session = Session.getInstance (props,
				new Authenticator () {
					protected PasswordAuthentication getPasswordAuthentication () {
						return new PasswordAuthentication (userName, password);
					}
				});
		
		try {
			
			MimeMessage message = new MimeMessage (session);
			message.setFrom (new InternetAddress (from));
			message.addRecipient (Message.RecipientType.TO, new InternetAddress (to));
			message.setSubject ("Report " + titel);
			
			BodyPart messageBodyPart = new MimeBodyPart ();
			messageBodyPart.setText ("Dear  \n\nIn attachment you'll find the requested report  \n\nKind Regards \n\nPeter Hardeel ");
			
			Multipart multipart = new MimeMultipart ();
			
			multipart.addBodyPart (messageBodyPart);
			
			messageBodyPart = new MimeBodyPart ();
			String attachment = file;
			DataSource source = new FileDataSource (attachment);
			messageBodyPart.setDataHandler (new DataHandler (source));
			messageBodyPart.setFileName (attachment);
			multipart.addBodyPart (messageBodyPart);
			
			message.setContent (multipart);
			
			Transport transport = session.getTransport ("smtp");
			transport.connect (userName, password);
			transport.send (message);
			transport.close ();
			sent = true;

			
		} catch (MessagingException mex) {
			log.error (mex);
		}
        log.debug("mail sent with titel: " + titel);
		return sent;
	}
	
}
