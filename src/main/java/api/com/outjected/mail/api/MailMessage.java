package com.outjected.mail.api;

import java.io.File;

import com.outjected.mail.core.EmailContact;
import com.outjected.mail.core.enumurations.ContentDisposition;
import com.outjected.mail.core.enumurations.MessagePriority;
import com.outjected.mail.core.enumurations.RecipientType;
import com.outjected.mail.exception.SeamMailException;

public interface MailMessage
{
   public MailMessage setFrom(EmailContact emailContact) throws SeamMailException;
   
   public MailMessage addRecipient(RecipientType recipientType, EmailContact emailContact) throws SeamMailException;
   
   public MailMessage from(String name, String address) throws SeamMailException;
   
   public MailMessage to(String name, String address) throws SeamMailException;
   
   public MailMessage cc(String name, String address) throws SeamMailException;

   public MailMessage bcc(String name, String address) throws SeamMailException;

   public MailMessage subject(String string) throws SeamMailException;

   public MailMessage setTextBody(String text) throws SeamMailException;
   
   public MailMessage setHTMLBody(String text) throws SeamMailException;   
   
   public void send() throws SeamMailException;

   public MailMessage importance(MessagePriority messagePriority) throws SeamMailException;

   public MailMessage addAttachment(File file, ContentDisposition attachment) throws SeamMailException;

   public MailMessage addAttachment(String url, String fileName, ContentDisposition contentDisposition) throws SeamMailException;

   public MailMessage deliveryReciept(String address) throws SeamMailException;

   public MailMessage readReciept(String string) throws SeamMailException;
}
