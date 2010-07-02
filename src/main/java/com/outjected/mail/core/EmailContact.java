package com.outjected.mail.core;

import java.nio.charset.Charset;

public class EmailContact
{
   private String name;
   private String emailAddress;
   private String charset;
   
   public EmailContact(String name, String emailAddress)
   {
      this.name = name;
      this.emailAddress = emailAddress;
      charset = "UTF-8";
   }
   
   public EmailContact(String name, String emailAddress, String charset) throws SeamMailException
   {
      this.name = name;
      this.emailAddress = emailAddress;
      if(Charset.isSupported(charset))
      {
         this.charset = charset;
      }
      else
      {
         throw new SeamMailException("Unsupported Charset: " + charset);
      }
   }

   public String getName()
   {
      return name;
   }

   public void setName(String name)
   {
      this.name = name;
   }

   public String getEmailAddress()
   {
      return emailAddress;
   }

   public void setEmailAddress(String emailAddress)
   {
      this.emailAddress = emailAddress;
   }

   public String getCharset()
   {
      return charset;
   }

   public void setCharset(String charset)
   {
      this.charset = charset;
   }
   
   @Override
   public String toString()
   {
      return name + " <" + emailAddress + ">"; 
   }
   
}
