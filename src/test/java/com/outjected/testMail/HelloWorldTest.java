/*package com.outjected.testMail;

import javax.inject.Inject;

import junit.framework.Assert;

import org.jboss.arquillian.api.Deployment;
import org.jboss.arquillian.junit.Arquillian;
import org.jboss.shrinkwrap.api.ArchivePaths;
import org.jboss.shrinkwrap.api.ShrinkWrap;
import org.jboss.shrinkwrap.api.spec.JavaArchive;
import org.jboss.shrinkwrap.impl.base.asset.ByteArrayAsset;
import org.jboss.weld.extensions.log.Category;
import org.junit.Ignore;
import org.junit.Test;
import org.junit.runner.RunWith;

import com.dumbster.smtp.SimpleSmtpServer;
import com.outjected.exception.SeamMailException;
import com.outjected.mail.core.BaseMailMessage;
import com.outjected.mail.core.EmailContact;
import com.outjected.mail.core.Mail;
import com.outjected.mail.core.enumurations.RecipientType;
import com.outjected.mail.velocity.SeamBaseVelocityContext;

@RunWith(Arquillian.class)
public class HelloWorldTest
{
   @Inject @com.outjected.mail.annotations.Test
   private Mail mail;

   @Deployment
   public static JavaArchive createTestArchive()
   {
      return ShrinkWrap.create("test.jar", JavaArchive.class)
            .addPackage(Mail.class.getPackage())
            .addPackage(SeamBaseVelocityContext.class.getPackage())
            .addPackage(Category.class.getPackage())
            .addManifestResource(new ByteArrayAsset(new byte[0]), ArchivePaths.create("beans.xml"));
   }

   @Ignore
   @Test
   public void testGetText() throws SeamMailException
   {
      SimpleSmtpServer server = SimpleSmtpServer.start();

      BaseMailMessage msg = mail.createBaseMailMessage();
      msg.setFrom(new EmailContact("Seam Framework", "seam@jboss.com"));
      msg.addRecipient(RecipientType.TO, new EmailContact("Seamy Seamerson", "cody.lerum@gmail.com"));
      msg.setSubject("Text Message from Seam Mail - " + java.util.UUID.randomUUID().toString());
      msg.setTextBody("Test Body");
      msg.send();
      
      Assert.assertTrue(server.getReceivedEmailSize() == 1);
   }
}
*/
