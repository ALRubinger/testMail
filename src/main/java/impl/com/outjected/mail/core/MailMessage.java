package com.outjected.mail.core;

import java.io.File;

import com.outjected.mail.core.enumurations.ContentDisposition;
import com.outjected.mail.core.enumurations.MessagePriority;
import com.outjected.mail.core.enumurations.RecipientType;
import com.outjected.mail.exception.SeamMailException;

public interface MailMessage<T extends MailMessage<T>>
{
   public T setFrom(EmailContact emailContact) throws SeamMailException;
   
   public T addRecipient(RecipientType recipientType, EmailContact emailContact) throws SeamMailException;
   
   public T from(String name, String address) throws SeamMailException;
   
   public T to(String name, String address) throws SeamMailException;
   
   public T cc(String name, String address) throws SeamMailException;

   public T bcc(String name, String address) throws SeamMailException;

   public T subject(String string) throws SeamMailException;

   public T setText(String text) throws SeamMailException;
   
   public T setHTML(String text) throws SeamMailException; 
   
   public T setHTMLTextAlt(String html, String text) throws SeamMailException;
   
   public void send() throws SeamMailException;

   public T importance(MessagePriority messagePriority) throws SeamMailException;

   public T addAttachment(File file, ContentDisposition attachment) throws SeamMailException;

   public T addAttachment(String url, String fileName, ContentDisposition contentDisposition) throws SeamMailException;

   public T deliveryReciept(String address) throws SeamMailException;

   public T readReciept(String string) throws SeamMailException;
}
