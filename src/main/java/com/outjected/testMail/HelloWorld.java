package com.outjected.testMail;

import javax.enterprise.inject.Model;
import javax.inject.Inject;
import javax.validation.constraints.NotNull;

import org.hibernate.validator.constraints.Email;
import org.hibernate.validator.constraints.NotEmpty;

import com.outjected.mail.core.EmailContact;
import com.outjected.mail.core.Mail;
import com.outjected.mail.core.MailMessage;
import com.outjected.mail.core.SeamMailException;
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

   public void sendEmail() throws SeamMailException
   {
      sendText();
      sendHTML();
      sendHTMLwithAlternative();
   }
   
   public void sendText() throws SeamMailException
   {
      MailMessage msg = mail.create();
      msg.setFrom(new EmailContact("Seam Framework", "seam@jboss.com"));
      msg.addRecipient(RecipientType.TO, new EmailContact("Cody Lerum", email));
      msg.setSubject("Text Message from Seam Mail - " + java.util.UUID.randomUUID().toString());
      msg.setTextBody(text);
      mail.send(msg);
   }
   
   public void sendHTML() throws SeamMailException
   {
      MailMessage msg = mail.create();
      msg.setFrom(new EmailContact("Seam Framework", "seam@jboss.com"));
      msg.addRecipient(RecipientType.TO, new EmailContact("Cody Lerum", email));
      msg.setSubject("HTML Message from Seam Mail - " + java.util.UUID.randomUUID().toString());
      msg.setHTMLBody(getHTMLBody());
      msg.setImportance(MessagePriority.HIGH);
      mail.send(msg);
   }
   
   public void sendHTMLwithAlternative() throws SeamMailException
   {
      MailMessage msg = mail.create();
      msg.setFrom(new EmailContact("Seam Framework", "seam@jboss.com"));
      msg.addRecipient(RecipientType.TO, new EmailContact("Cody Lerum", email));
      msg.setSubject("HTML+Text Message from Seam Mail - " + java.util.UUID.randomUUID().toString());
      msg.setHTMLBodyTextAlt(getHTMLBody(), text);
      msg.setImportance(MessagePriority.LOW);
      msg.setDeliveryReciept("cody.lerum@clearfly.net");
      mail.send(msg);
   }
   
   private String getHTMLBody()
   {
      StringBuilder tmp = new StringBuilder();
      tmp.append("<html>\n");
      tmp.append("\t<body>\n");
      tmp.append("\t\t<p><b>Cody,</b></p>\n");
      tmp.append("\t\t<p>This is an example <i>HTML</i> email sent by Seam.</p>\n");
      tmp.append("\t\t<p>It has an alternative text body for mail readers that don't support html.</p>\n");
      tmp.append("\t</body>\n");
      tmp.append("</html>\n");
      return tmp.toString();

   }
}
