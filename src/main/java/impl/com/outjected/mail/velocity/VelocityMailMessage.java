package com.outjected.mail.velocity;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.IOException;
import java.io.StringWriter;

import javax.inject.Inject;
import javax.mail.Session;

import org.apache.velocity.Template;
import org.apache.velocity.app.VelocityEngine;
import org.apache.velocity.exception.MethodInvocationException;
import org.apache.velocity.exception.ParseErrorException;
import org.apache.velocity.exception.ResourceNotFoundException;
import org.apache.velocity.runtime.resource.loader.ResourceLoader;
import org.apache.velocity.runtime.resource.loader.StringResourceLoader;
import org.apache.velocity.runtime.resource.util.StringResourceRepository;
import org.apache.velocity.runtime.resource.util.StringResourceRepositoryImpl;
import org.jboss.weld.extensions.resourceLoader.ResourceProvider;

import com.outjected.exception.SeamMailException;
import com.outjected.exception.SeamTemplatingException;
import com.outjected.mail.annotations.Module;
import com.outjected.mail.annotations.Velocity;
import com.outjected.mail.api.MailMessage;
import com.outjected.mail.core.AttachmentMap;
import com.outjected.mail.core.BaseMailMessage;

@Velocity
public class VelocityMailMessage extends BaseMailMessage implements MailMessage
{
   private VelocityEngine velocityEngine;
   private SeamBaseVelocityContext context;

   private Template textTemplate;
   private Template htmlTemplate;

   @Inject
   private ResourceProvider resourceProvider;

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
   public void setHTMLBodyTextAlt(String htmlTemplatePath, String textTemplatePath) throws SeamMailException
   {
      setHTMLBody(htmlTemplatePath);
      setTextBody(textTemplatePath);
   }

   public void setTemplateHTMLBody(String htmlTemplatePath) throws SeamMailException
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
      
      byte[] buffer = new byte[(int) new File(templatePath).length()];
      
      BufferedInputStream bis = new BufferedInputStream(resourceProvider.loadResourceStream(templatePath));
     
      try
     {
        bis.read(buffer);
     }
     catch (IOException e)
     {
        throw new SeamMailException("Unabled to read template: " + templatePath);
     }      

      try
      {
         resourceProvider.loadResourceStream(templatePath);
         StringResourceRepository s = new StringResourceRepositoryImpl();
         s.putStringResource("templatePath", new String(buffer));
         Template t = new Template();
         t.setResourceLoader(StringResourceLoader.setRepository(templatePath, s));
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
         velocityEngine.evaluate(context, writer, logTag, instream)
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

   @Override
   public void put(String key, Object value)
   {
      context.put(key, value);
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
