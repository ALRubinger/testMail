package com.outjected.mail.templating.velocity;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.StringWriter;

import javax.inject.Inject;
import javax.mail.Session;

import org.apache.velocity.app.VelocityEngine;
import org.apache.velocity.exception.MethodInvocationException;
import org.apache.velocity.exception.ParseErrorException;
import org.apache.velocity.exception.ResourceNotFoundException;

import com.outjected.mail.annotations.Module;
import com.outjected.mail.annotations.Velocity;
import com.outjected.mail.api.TemplateMailMessage;
import com.outjected.mail.core.AttachmentMap;
import com.outjected.mail.core.BaseMailMessage;
import com.outjected.mail.exception.SeamMailException;
import com.outjected.mail.exception.SeamTemplatingException;
import com.outjected.mail.templating.MailTemplate;

@Velocity
public class VelocityMailMessage extends BaseMailMessage implements TemplateMailMessage
{
   private VelocityEngine velocityEngine;
   private SeamBaseVelocityContext context;

   private MailTemplate textTemplate;
   private MailTemplate htmlTemplate;

   //@Inject
   //private ResourceProvider resourceProvider;

   @Inject
   public VelocityMailMessage(@Module Session session, @Module SeamCDIVelocityContext seamCDIVelocityContext) throws SeamMailException
   {
      super(session);
      velocityEngine = new VelocityEngine();
      velocityEngine.setProperty("runtime.log.logsystem.class", "org.apache.velocity.runtime.log.SimpleLog4JLogSystem");
      context = new SeamBaseVelocityContext(seamCDIVelocityContext);
      put("attachmentMap", new AttachmentMap(super.getAttachments()));
   }

   @Override
   public TemplateMailMessage setTemplateHTMLBodyTextAlt(String htmlTemplatePath, String textTemplatePath) throws SeamMailException
   {
      setTemplateHTMLBody(htmlTemplatePath);
      setTemplateTextBody(textTemplatePath);
      
      return this;
   }

   public TemplateMailMessage setTemplateHTMLBody(String htmlTemplatePath) throws SeamMailException
   {
      try
      {
         htmlTemplate = createTemplate(htmlTemplatePath);
      }
      catch (SeamTemplatingException e)
      {
         throw new SeamMailException("Unable to add HTML template to MimeMessage", e);
      }
      return this;
   }

   public TemplateMailMessage setTemplateTextBody(String textTemplatePath) throws SeamMailException
   {
      try
      {
         textTemplate = createTemplate(textTemplatePath);
      }
      catch (SeamTemplatingException e)
      {
         throw new SeamMailException("Unable to add Text template to MimeMessage", e);
      }
      return this;
   }

   private MailTemplate createTemplate(String templatePath) throws SeamTemplatingException
   {
	   FileInputStream inputStream;
	   try 
	   {
		   inputStream = new FileInputStream(templatePath);
	   } 
	   catch (FileNotFoundException e) 
	   {
		   throw new SeamTemplatingException("Unable to find template " + templatePath ,e);
	   }
	   
	  //InputStream inputStream = resourceProvider.loadResourceStream(templatePath);
	   
      MailTemplate template = new MailTemplate(templatePath, inputStream);
      
      return template;
   }

   private String mergeTemplate(MailTemplate template) throws SeamTemplatingException
   {
      StringWriter writer = new StringWriter();
      try
      {
         velocityEngine.evaluate(context, writer, template.getName(), new InputStreamReader(template.getInputStream()));
      }
      catch (ResourceNotFoundException e)
      {
         throw new SeamTemplatingException("Unable to find template", e);
      }
      catch (ParseErrorException e)
      {
         throw new SeamTemplatingException("Unable to find template", e);
      }
      catch (MethodInvocationException e)
      {
         throw new SeamTemplatingException("Error processing method referenced in context", e);
      }
      catch (IOException e)
      {
         throw new SeamTemplatingException("Error rendering output", e);
      }
      return writer.toString();
   }

   public TemplateMailMessage put(String key, Object value)
   {
      context.put(key, value);
      return this;
   }

   @Override
   public void send() throws SeamMailException
   {
      if (htmlTemplate != null && textTemplate != null)
      {
         try
         {
            super.setHTMLBodyTextAlt(mergeTemplate(htmlTemplate), mergeTemplate(textTemplate));
         }
         catch (SeamTemplatingException e)
         {
            throw new SeamMailException("Error sending message", e);
         }
      }
      else if (htmlTemplate != null)
      {
         try
         {
            super.setHTMLBody(mergeTemplate(htmlTemplate));
         }
         catch (SeamTemplatingException e)
         {
            throw new SeamMailException("Error sending message", e);
         }
      }
      else if (textTemplate != null)
      {
         try
         {
            super.setTextBody(mergeTemplate(textTemplate));
         }
         catch (SeamTemplatingException e)
         {
            throw new SeamMailException("Error sending message", e);
         }
      }
      else
      {
         throw new SeamMailException("No Body was set");
      }
      super.send();
   }
}
