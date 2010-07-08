package com.outjected.mail.core;

import java.util.Properties;

import javax.enterprise.inject.Produces;
import javax.inject.Inject;
import javax.mail.Session;

import org.slf4j.Logger;

import com.outjected.mail.annotations.Module;

public class MailSessionProducer
{
   private String server = "smtp.clearfly.net";

   @Inject
   private Logger log;
   
   @Produces @Module
   public Session getMailSession()
   {
      log.debug("Producing Mail Session");
      Properties props = new Properties();
      props.put("mail.smtp.host", server);
      return Session.getInstance(props, null);
   }
}
