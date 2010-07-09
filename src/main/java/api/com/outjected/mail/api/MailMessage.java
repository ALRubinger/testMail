package com.outjected.mail.api;

import java.io.File;

import com.outjected.exception.SeamMailException;
import com.outjected.mail.core.EmailContact;
import com.outjected.mail.core.enumurations.ContentDisposition;
import com.outjected.mail.core.enumurations.MessagePriority;
import com.outjected.mail.core.enumurations.RecipientType;

public interface MailMessage
{
   public void setFrom(EmailContact emailContact) throws SeamMailException;
   
   public void addRecipient(RecipientType recipientType, EmailContact emailContact) throws SeamMailException;
   
   public void from(String name, String address) throws SeamMailException;
   
   public void to(String name, String address) throws SeamMailException;
   
   public void cc(String name, String address) throws SeamMailException;

   public void bcc(String name, String address) throws SeamMailException;

   public void subject(String string) throws SeamMailException;

   public void setTextBody(String text) throws SeamMailException;
   
   public void send() throws SeamMailException;

   public void put(String name, Object value);

   public void setTemplateHTMLBody(String pathToTemplate) throws SeamMailException;

   public void importance(MessagePriority messagePriority) throws SeamMailException;

   public void addAttachment(File file, ContentDisposition attachment) throws SeamMailException;

   public void addAttachment(String url, String fileName, ContentDisposition contentDisposition) throws SeamMailException;

   public void deliveryReciept(String address) throws SeamMailException;

   public void readReciept(String string) throws SeamMailException;

   public void setTemplateHTMLBodyTextAlt(String htmlTemplate, String textTemplate);
}
