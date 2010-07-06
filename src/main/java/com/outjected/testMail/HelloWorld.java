package com.outjected.testMail;

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
import com.outjected.mail.core.enumurations.MessagePriority;
import com.outjected.mail.core.enumurations.RecipientType;

public @Model 
class HelloWorld
{
   private String email;
   
   private String text = "This is the alternative text body for mail readers that don't support html";

   @Inject
   private Mail mail;   

   public HelloWorld()
   {
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
      msg.addRecipient(RecipientType.TO, new EmailContact("Cody Lerum", email));
      msg.setSubject("Text Message from Seam Mail - " + java.util.UUID.randomUUID().toString());
      msg.setTextBody(text);
      msg.send();
   }
   
   public void sendHTML() throws SeamMailException, SeamTemplatingException
   {
      VelocityMailMessage msg = mail.createVelocityMailMessage();
      msg.setFrom(new EmailContact("Seam Framework", "seam@jboss.com"));
      msg.addRecipient(RecipientType.TO, new EmailContact("Cody Lerum", email));
      msg.setSubject("HTML Message from Seam Mail - " + java.util.UUID.randomUUID().toString());
      msg.setHTMLBody("src/main/resources/template.vm");
      msg.putInContext("version", "Seam 4");
      msg.setImportance(MessagePriority.HIGH);
      msg.send();
   }
   
   public void sendHTMLwithAlternative() throws SeamMailException, SeamTemplatingException
   {
      VelocityMailMessage msg = mail.createVelocityMailMessage();
      msg.setFrom(new EmailContact("Seam Framework", "seam@jboss.com"));
      msg.addRecipient(RecipientType.TO, new EmailContact("Cody Lerum", email));
      msg.setSubject("HTML+Text Message from Seam Mail - " + java.util.UUID.randomUUID().toString());
      msg.putInContext("version", "Seam 4");
      msg.setHTMLBodyTextAlt("src/main/resources/template.html.vm", "src/main/resources/template.text.vm");
      msg.setImportance(MessagePriority.LOW);
      msg.setDeliveryReciept("cody.lerum@clearfly.net");
      msg.send();
   }   
   
   /*
   public String velTest1() throws SeamTemplatingException
   {     
      StringWriter writer = new StringWriter();
      VelocityEngine ve = new VelocityEngine();
      
      SeamBaseVelocityContext context = new SeamBaseVelocityContext(seamELVelocityContext);
      context.put("version", "Seam 3");
      
      try
      {
         Template t = ve.getTemplate();
         
         t.merge(context, writer);
      }
      catch (ResourceNotFoundException e)
      {
         throw new SeamTemplatingException("Unable to find template", e);
      }
      catch (ParseErrorException e)
      {
         throw new SeamTemplatingException("Unable to parse template", e);
      }
      catch (Exception e)
      {
         throw new SeamTemplatingException("Error while processing template", e);
      }
      return writer.toString();      
   }
   */
}
