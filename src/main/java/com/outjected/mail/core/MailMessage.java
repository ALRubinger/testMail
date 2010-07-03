package com.outjected.mail.core;

import java.util.Collection;
import java.util.Date;

import javax.inject.Inject;
import javax.mail.MessagingException;
import javax.mail.Session;
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeMultipart;

import com.outjected.mail.annotations.Module;
import com.outjected.mail.core.enumurations.ContentDisposition;
import com.outjected.mail.core.enumurations.MailHeader;
import com.outjected.mail.core.enumurations.MessagePriority;
import com.outjected.mail.core.enumurations.RecipientType;

import exception.SeamMailException;

public class MailMessage
{
   private RootMimeMessage rootMimeMessage;
   private String charset;

   @Inject
   public MailMessage(@Module Session session) throws SeamMailException
   {
      rootMimeMessage = new RootMimeMessage(session);
      charset = "UTF-8";
      setSentDate(new Date());
      setMessageID(java.util.UUID.randomUUID().toString());
   }

   public void addRecipient(RecipientType recipientType, EmailContact emailContact) throws SeamMailException
   {
      try
      {
         rootMimeMessage.addRecipient(recipientType.getRecipientType(), MailUtility.getInternetAddress(emailContact));
      }
      catch (MessagingException e)
      {
         throw new SeamMailException("Unable to add recipient " + recipientType + ": " + emailContact.toString() + " to MIME message", e);
      }
   }

   public void addRecipients(RecipientType recipientType, EmailContact[] emailContacts) throws SeamMailException
   {
      try
      {
         rootMimeMessage.addRecipients(recipientType.getRecipientType(), MailUtility.getInternetAddressses(emailContacts));
      }
      catch (MessagingException e)
      {
         throw new SeamMailException("Unable to add " + recipientType + ":  Collection<Recipients>to MIME message", e);
      }
   }

   public void addRecipients(RecipientType recipientType, Collection<EmailContact> emailContacts) throws SeamMailException
   {
      try
      {
         rootMimeMessage.addRecipients(recipientType.getRecipientType(), MailUtility.getInternetAddressses(emailContacts));
      }
      catch (MessagingException e)
      {
      }
   }

   public void setSubject(String subject) throws SeamMailException
   {
      setSubject(subject, "UTF-8");
   }

   public void setSubject(String subject, String charset) throws SeamMailException
   {
      try
      {
         rootMimeMessage.setSubject(subject, charset);
      }
      catch (MessagingException e)
      {
         throw new SeamMailException("Unable to add subject:" + subject + " to MIME message with charset: " + charset, e);
      }
   }

   public void setFrom(EmailContact emailContact) throws SeamMailException
   {
      try
      {
         rootMimeMessage.setFrom(MailUtility.getInternetAddress(emailContact));
      }
      catch (MessagingException e)
      {
         throw new SeamMailException("Unable to add From Address:" + emailContact.getEmailAddress() + " to MIME message with charset: " + emailContact.getCharset(), e);
      }
   }

   public void setSentDate(Date date) throws SeamMailException
   {
      try
      {
         rootMimeMessage.setSentDate(date);
      }
      catch (MessagingException e)
      {
         throw new SeamMailException("Unable to set Sent Date on MimeMessage", e);
      }
   }

   public void setMessageID(String messageId)
   {
      rootMimeMessage.setMessageId(messageId);
   }

   public void setDeliveryReciept(String email) throws SeamMailException
   {
      setHeader(MailHeader.DELIVERY_RECIEPT.headerValue(), "<" + email + ">");
   }

   public void setReadReciept(String email) throws SeamMailException
   {
      setHeader(MailHeader.DELIVERY_RECIEPT.headerValue(), email);
   }

   public void setImportance(MessagePriority messagePriority) throws SeamMailException
   {
      setHeader("X-Priority", messagePriority.getX_priority());
      setHeader("Priority", messagePriority.getPriority());
      setHeader("Importance", messagePriority.getImportance());
   }

   public void setHeader(String name, String value) throws SeamMailException
   {
      try
      {
         rootMimeMessage.setHeader(name, value);
      }
      catch (MessagingException e)
      {
         throw new SeamMailException("Unable to SET Header: + " + name + " to Value: " + value, e);
      }
   }

   public void addHeader(String name, String value) throws SeamMailException
   {
      try
      {
         rootMimeMessage.addHeader(name, value);
      }
      catch (MessagingException e)
      {
         throw new SeamMailException("Unable to ADD Header: + " + name + " to Value: " + value, e);
      }
   }

   public void setTextBody(String text) throws SeamMailException
   {
      MimeMultipart textMultipart = new MimeMultipart();

      try
      {
         textMultipart.addBodyPart(buildTextBodyPart(text));
         rootMimeMessage.setContent(textMultipart);
      }
      catch (MessagingException e)
      {
         throw new SeamMailException("Unable to add TextBody to MimeMessage", e);
      }
   }

   public void setHTMLBody(String html) throws SeamMailException
   {
      MimeMultipart htmlMultipart = new MimeMultipart();

      try
      {
         htmlMultipart.addBodyPart(buildHTMLBodyPart(html));
         rootMimeMessage.setContent(htmlMultipart);
      }
      catch (MessagingException e)
      {
         throw new SeamMailException("Unable to add TextBody to MimeMessage", e);
      }
   }

   public void setHTMLBodyTextAlt(String html, String text) throws SeamMailException
   {
      MimeMultipart mixedMultipart = new MimeMultipart("mixed");
      MimeBodyPart mixedBodyPart = new MimeBodyPart();

      MimeMultipart relatedMultiPart = new MimeMultipart("related");
      MimeBodyPart relatedBodyPart = new MimeBodyPart();

      MimeMultipart alternativeMultiPart = new MimeMultipart("alternative");

      try
      {
         // Text must be the first or some HTML capable clients will fail to render HTML bodyPart.
         alternativeMultiPart.addBodyPart(buildTextBodyPart(text));
         alternativeMultiPart.addBodyPart(buildHTMLBodyPart(html));

         relatedBodyPart.setContent(alternativeMultiPart);

         relatedMultiPart.addBodyPart(relatedBodyPart);

         mixedBodyPart.setContent(relatedMultiPart);
         mixedMultipart.addBodyPart(mixedBodyPart);

         rootMimeMessage.setContent(relatedMultiPart);
      }
      catch (MessagingException e)
      {
         throw new SeamMailException("Unable to build HTML+Text Email", e);
      }
   }

   private MimeBodyPart buildTextBodyPart(String text) throws SeamMailException
   {
      MimeBodyPart textBodyPart = new MimeBodyPart();

      try
      {
         textBodyPart.setDisposition(ContentDisposition.INLINE.headerValue());
         textBodyPart.setText(text, charset);
      }
      catch (MessagingException e)
      {
         throw new SeamMailException("Unable to build TextBodyPart", e);
      }

      return textBodyPart;
   }

   private MimeBodyPart buildHTMLBodyPart(String html) throws SeamMailException
   {
      MimeBodyPart htmlBodyPart = new MimeBodyPart();

      try
      {
         htmlBodyPart.setDisposition(ContentDisposition.INLINE.headerValue());
         htmlBodyPart.setText(html, charset, "html");
         // htmlBodyPart.setHeader("content-transfer-encoding", "quoted-printable");
      }
      catch (MessagingException e)
      {
         throw new SeamMailException("Unable to build HTMLBodyPart", e);
      }

      return htmlBodyPart;
   }

   public MimeMessage getRootMimeMessage()
   {
      return rootMimeMessage;
   }
}
