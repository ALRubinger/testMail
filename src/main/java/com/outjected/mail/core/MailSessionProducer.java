package com.outjected.mail.core;

import java.util.Properties;

import javax.enterprise.inject.Produces;
import javax.mail.Session;

import com.outjected.mail.annotations.Module;

public class MailSessionProducer
{
   private String server = "smtp.clearfly.net";

   @Produces @Module
   public Session getMailSession()
   {
      Properties props = new Properties();
      props.put("mail.smtp.host", server);
      return Session.getInstance(props, null);
   }
}
