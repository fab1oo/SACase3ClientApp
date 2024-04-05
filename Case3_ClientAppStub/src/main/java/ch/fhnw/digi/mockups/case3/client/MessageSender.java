package ch.fhnw.digi.mockups.case3.client;

import ch.fhnw.digi.mockups.case3.JobRequestMessage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.jms.DefaultJmsListenerContainerFactoryConfigurer;
import org.springframework.context.annotation.Bean;
import org.springframework.jms.annotation.JmsListener;
import org.springframework.jms.config.DefaultJmsListenerContainerFactory;
import org.springframework.jms.config.JmsListenerContainerFactory;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.jms.support.converter.MappingJackson2MessageConverter;
import org.springframework.jms.support.converter.MessageConverter;
import org.springframework.jms.support.converter.MessageType;
import org.springframework.stereotype.Component;

import ch.fhnw.digi.mockups.case3.JobMessage;

import javax.annotation.PostConstruct;
import javax.jms.ConnectionFactory;

@Component
public class MessageSender {

	@Autowired
	private JmsTemplate jmsTemplate;
	@Autowired
	private MessageConverter jacksonJmsMessageConverter;


	public void requestJobFromDispo(JobMessage jm) {
		jmsTemplate.setMessageConverter(jacksonJmsMessageConverter);
		jmsTemplate.setPubSubDomain(false); // we want to send to a queue, not a topic

		final String jobNumber = "message" + jm.getJobnumber();

		// publish a new JobRequestMessage to the channel "dispo.jobs.requestAssignment"
		jmsTemplate.convertAndSend("dispo.jobs.requestAssignment\t", new JobRequestMessage(jm.getJobnumber(), "Franz"), m -> {
			m.setStringProperty("someHeaderField", "someImportantValue");
			m.setJMSCorrelationID(jobNumber);
			return m;
		});
	}

	public void sendRepairJob(JobMessage jm) {
		jmsTemplate.setMessageConverter(jacksonJmsMessageConverter);
		jmsTemplate.setPubSubDomain(true); // we want to send to a topic, not a queue

		final String jobNumber = "message" + jm.getJobnumber();

		// publish a new JobMessage to the channel "group8.dispo.jobs.repair"
		jmsTemplate.convertAndSend("group8.dispo.jobs.repair\t", jm, m -> {
			m.setStringProperty("someHeaderField", "someImportantValue");
			m.setJMSCorrelationID(jobNumber);
			return m;
		});
	}

}
