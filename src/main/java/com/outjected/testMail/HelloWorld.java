package com.outjected.testMail;

import java.io.File;

import javax.enterprise.inject.Model;
import javax.inject.Inject;
import javax.validation.constraints.NotNull;

import org.hibernate.validator.constraints.Email;
import org.hibernate.validator.constraints.NotEmpty;

import com.outjected.exception.SeamMailException;
import com.outjected.exception.SeamTemplatingException;
import com.outjected.mail.core.BaseMailMessage;
import com.outjected.mail.core.EmailContact;
import com.outjected.mail.core.Mail;
import com.outjected.mail.core.VelocityMailMessage;
import com.outjected.mail.core.enumurations.ContentDisposition;
import com.outjected.mail.core.enumurations.MessagePriority;
import com.outjected.mail.core.enumurations.RecipientType;

public @Model
class HelloWorld
{
   private String name;
   private String email;

   private String text = "This is the alternative text body for mail readers that don't support html";

   @Inject
   private Mail mail;

   public HelloWorld()
   {
   }

   @NotNull
   @NotEmpty
   public String getName()
   {
      return name;
   }

   public void setName(String name)
   {
      this.name = name;
   }

   @NotNull
   @NotEmpty
   @Email
   public String getEmail()
   {
      return email;
   }

   public void setEmail(String email)
   {
      this.email = email;
   }

   public void sendEmail() throws SeamMailException, SeamTemplatingException
   {
      sendText();
      sendHTML();
      sendHTMLwithAlternative();
   }

   public void sendText() throws SeamMailException
   {
      BaseMailMessage msg = mail.createBaseMailMessage();
      msg.setFrom(new EmailContact("Seam Framework", "seam@jboss.com"));
      msg.addRecipient(RecipientType.TO, new EmailContact(name, email));
      msg.setSubject("Text Message from Seam Mail - " + java.util.UUID.randomUUID().toString());
      msg.setTextBody(text);
      msg.send();
   }

   public void sendHTML() throws SeamMailException, SeamTemplatingException
   {
      VelocityMailMessage msg = mail.createVelocityMailMessage();
      msg.setFrom(new EmailContact("Seam Framework", "seam@jboss.com"));
      msg.addRecipient(RecipientType.TO, new EmailContact(name, email));
      msg.setSubject("HTML Message from Seam Mail - " + java.util.UUID.randomUUID().toString());
      msg.setHTMLBody("src/main/resources/template.html.vm");
      msg.putInContext("version", "Seam 3");
      msg.setImportance(MessagePriority.HIGH);
      msg.send();
   }

   public void sendHTMLwithAlternative() throws SeamMailException, SeamTemplatingException
   {
      VelocityMailMessage msg = mail.createVelocityMailMessage();
      msg.setFrom(new EmailContact("Seam Framework", "seam@jboss.com"));
      msg.addRecipient(RecipientType.TO, new EmailContact(name, email));
      msg.setSubject("HTML+Text Message from Seam Mail - " + java.util.UUID.randomUUID().toString());
      msg.putInContext("version", "Seam 3");
      msg.setHTMLBodyTextAlt("src/main/resources/template.html.vm", "src/main/resources/template.text.vm");
      msg.setImportance(MessagePriority.LOW);
      msg.setDeliveryReciept("cody.lerum@clearfly.net");
      msg.setReadReciept("cody.lerum@clearfly.net");
      msg.addAttachment(new File("src/main/resources/template.html.vm"), ContentDisposition.ATTACHMENT);
      msg.addAttachment("http://www.seamframework.org/themes/sfwkorg/img/seam_icon_large.png", "seamLogo.png", ContentDisposition.INLINE);
      msg.send();
   }
}
