package com.outjected.mail.core;

import java.io.IOException;
import java.io.StringWriter;

import javax.inject.Inject;
import javax.mail.Session;

import org.apache.velocity.Template;
import org.apache.velocity.app.VelocityEngine;
import org.apache.velocity.exception.MethodInvocationException;
import org.apache.velocity.exception.ParseErrorException;
import org.apache.velocity.exception.ResourceNotFoundException;

import com.outjected.exception.SeamMailException;
import com.outjected.exception.SeamTemplatingException;
import com.outjected.mail.annotations.Module;
import com.outjected.mail.annotations.Velocity;
import com.outjected.mail.velocity.SeamBaseVelocityContext;
import com.outjected.mail.velocity.SeamCDIVelocityContext;

@Velocity
public class VelocityMailMessage extends BaseMailMessage
{
   private VelocityEngine velocityEngine;
   private SeamBaseVelocityContext context;

   private Template textTemplate;
   private Template htmlTemplate;

   @Inject
   public VelocityMailMessage(@Module Session session, @Module SeamCDIVelocityContext seamCDIVelocityContext) throws SeamMailException
   {
      super(session);
      velocityEngine = new VelocityEngine();
      velocityEngine.setProperty("runtime.log.logsystem.class", "org.apache.velocity.runtime.log.SimpleLog4JLogSystem");
      context = new SeamBaseVelocityContext(seamCDIVelocityContext);
      putInContext("attachmentMap", new AttachmentMap(super.getAttachments()));
   }

   @Override
   public void setHTMLBodyTextAlt(String htmlTemplatePath, String textTemplatePath) throws SeamMailException
   {
      setHTMLBody(htmlTemplatePath);
      setTextBody(textTemplatePath);
   }

   @Override
   public void setHTMLBody(String htmlTemplatePath) throws SeamMailException
   {
      try
      {
         htmlTemplate = createTemplate(htmlTemplatePath);
      }
      catch (SeamTemplatingException e)
      {
         throw new SeamMailException("Unable to add HTML template to MimeMessage", e);
      }
   }

   @Override
   public void setTextBody(String textTemplatePath) throws SeamMailException
   {
      try
      {
         textTemplate = createTemplate(textTemplatePath);
      }
      catch (SeamTemplatingException e)
      {
         throw new SeamMailException("Unable to add Text template to MimeMessage", e);
      }
   }

   private Template createTemplate(String templatePath) throws SeamTemplatingException
   {
      Template template = null;

      try
      {
         template = velocityEngine.getTemplate(templatePath);
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

      return template;
   }
   
   private String mergeTemplate(Template template) throws SeamTemplatingException
   {
      StringWriter writer = new StringWriter();      
      try
      {
         template.merge(context, writer);
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

   public void putInContext(String key, Object value)
   {
      context.put(key, value);
   }
   
   @Override
   public void send() throws SeamMailException
   {     
      if(htmlTemplate != null && textTemplate != null)
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
      else if(htmlTemplate != null)
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
      else if(textTemplate != null)
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
