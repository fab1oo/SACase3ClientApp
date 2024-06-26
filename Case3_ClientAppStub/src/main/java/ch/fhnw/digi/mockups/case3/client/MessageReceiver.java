package ch.fhnw.digi.mockups.case3.client;

import javax.jms.ConnectionFactory;

import ch.fhnw.digi.mockups.case3.JobAssignmentMessage;
import ch.fhnw.digi.mockups.case3.JobMessage;
import ch.fhnw.digi.mockups.case3.JobRequestMessage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.jms.DefaultJmsListenerContainerFactoryConfigurer;
import org.springframework.context.annotation.Bean;
import org.springframework.jms.annotation.JmsListener;
import org.springframework.jms.config.DefaultJmsListenerContainerFactory;
import org.springframework.jms.support.converter.MappingJackson2MessageConverter;
import org.springframework.jms.support.converter.MessageConverter;
import org.springframework.jms.support.converter.MessageType;
import org.springframework.stereotype.Component;

@Component
public class MessageReceiver {

	@Autowired
	private UI ui;

	@Autowired
	private MessageSender messageSender;

	@JmsListener(destination = "dispo.jobs.new", containerFactory = "myFactory")
	public void getJobMessage(JobMessage jm) {
		// If urgent repair job, then send direct to repair queue
		if (jm.getType() == JobMessage.JobType.Repair) {
			messageSender.sendRepairJob(jm);
		} else {
			ui.addJobToList(jm);
		}

		try {
			Thread.sleep(2000);
		} catch (InterruptedException e) {
		}
	}

	@JmsListener(destination = "dispo.jobs.assignments", containerFactory = "myFactory")
	public void receiveJobAssignmentMessage(JobAssignmentMessage jam) {
		ui.assignJob(jam);
	}
	
	@Bean
	public DefaultJmsListenerContainerFactory myFactory(ConnectionFactory connectionFactory,
			DefaultJmsListenerContainerFactoryConfigurer configurer) {
		DefaultJmsListenerContainerFactory factory = new DefaultJmsListenerContainerFactory();

		configurer.configure(factory, connectionFactory);
		factory.setPubSubDomain(true);
		factory.setMessageConverter(jacksonJmsMessageConverter());

		return factory;
	}

	@Bean // Serialize message content to json/from using TextMessage
	public MessageConverter jacksonJmsMessageConverter() {
		MappingJackson2MessageConverter converter = new MappingJackson2MessageConverter();
		converter.setTargetType(MessageType.TEXT);
		converter.setTypeIdPropertyName("_type");
		return converter;
	}

}
