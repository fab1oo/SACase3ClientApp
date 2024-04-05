package ch.fhnw.digi.mockups.case3.client;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.jms.support.converter.MessageConverter;
import org.springframework.stereotype.Component;

import ch.fhnw.digi.mockups.case3.JobMessage;

@Component
public class MessageSender {


	@Autowired
	private JmsTemplate jmsTemplate;
	@Autowired
	private MessageConverter jacksonJmsMessageConverter;

	void requestJobFromDispo(JobMessage job) {
		
		// FIXME: JobRequestMessage erzeugen und an Broker schicken 

	}

}
