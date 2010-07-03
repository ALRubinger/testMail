package com.outjected.mail.core;

import javax.inject.Inject;
import javax.mail.MessagingException;
import javax.mail.Transport;

import exception.SeamMailException;

public class Mail
{
   @Inject
   private MailMessage  mailMessage;
   
   public MailMessage create()
   {
      return mailMessage;
   }

   public void send(MailMessage msg) throws SeamMailException
   {
      try
      {
         Transport.send(msg.getRootMimeMessage());
      }
      catch (MessagingException e)
      {
         throw new SeamMailException("Message Send Failed!", e);
      }
   }
}
