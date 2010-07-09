package com.outjected.testMail;

import java.io.File;

import javax.enterprise.inject.Instance;
import javax.enterprise.inject.Model;
import javax.inject.Inject;
import javax.validation.constraints.NotNull;

import org.hibernate.validator.constraints.Email;
import org.hibernate.validator.constraints.NotEmpty;

import com.outjected.mail.annotations.Velocity;
import com.outjected.mail.api.MailMessage;
import com.outjected.mail.api.TemplateMailMessage;
import com.outjected.mail.core.EmailContact;
import com.outjected.mail.core.enumurations.ContentDisposition;
import com.outjected.mail.core.enumurations.MessagePriority;
import com.outjected.mail.core.enumurations.RecipientType;
import com.outjected.mail.exception.SeamMailException;
import com.outjected.mail.exception.SeamTemplatingException;

public @Model
class HelloWorld
{
   private String name;
   private String email;

   private String text = "This is the alternative text body for mail readers that don't support html";

   @Inject @Velocity 
   private Instance<TemplateMailMessage> velocityMailMessage;
   
   @Inject 
   private Instance<MailMessage> baseMailMessage;
   
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

   public void sendText() throws SeamMailException
   {
      MailMessage msg = baseMailMessage.get();
      msg.setFrom(new EmailContact("Seam Framework", "seam@jboss.com"));
      msg.addRecipient(RecipientType.TO, new EmailContact(name, email));
      msg.subject("Text Message from Seam Mail - " + java.util.UUID.randomUUID().toString());
      msg.setText(text);
      msg.send();
   }

   public void sendHTML() throws SeamMailException, SeamTemplatingException
   {
      TemplateMailMessage msg = velocityMailMessage.get();
      msg.setFrom(new EmailContact("Seam Framework", "seam@jboss.com"));
      msg.to(name, email);
      msg.subject("HTML Message from Seam Mail - " + java.util.UUID.randomUUID().toString());
      msg.setTemplateHTML("src/main/resources/template.html.vm");
      msg.put("version", "Seam 3");
      msg.importance(MessagePriority.HIGH);
      msg.addAttachment("http://www.seamframework.org/themes/sfwkorg/img/seam_icon_large.png", "seamLogo.png", ContentDisposition.INLINE);
      msg.send();
   }

   public void sendHTMLwithAlternative() throws SeamMailException, SeamTemplatingException
   {
      TemplateMailMessage msg = velocityMailMessage.get();
      msg.setFrom(new EmailContact("Seam Framework", "seam@jboss.com"));
      msg.addRecipient(RecipientType.TO, new EmailContact(name, email));
      msg.subject("HTML+Text Message from Seam Mail - " + java.util.UUID.randomUUID().toString());
      msg.put("version", "Seam 3");
      msg.setTemplateHTMLTextAlt("src/main/resources/template.html.vm", "src/main/resources/template.text.vm");
      msg.importance(MessagePriority.LOW);
      msg.deliveryReciept("cody.lerum@clearfly.net");
      msg.readReciept("cody.lerum@clearfly.net");
      msg.addAttachment(new File("src/main/resources/template.html.vm"), ContentDisposition.ATTACHMENT);
      msg.addAttachment("http://www.seamframework.org/themes/sfwkorg/img/seam_icon_large.png", "seamLogo.png", ContentDisposition.INLINE);
      msg.send();
   }
}
