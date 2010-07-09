package com.outjected.mail.api;

import com.outjected.mail.exception.SeamMailException;

public interface TemplateMailMessage extends MailMessage
{
   public TemplateMailMessage setTemplateText(String templateFileName) throws SeamMailException;
   
   public TemplateMailMessage setTemplateHTML(String templateFileName) throws SeamMailException;

   public TemplateMailMessage setTemplateHTMLTextAlt(String htmlTemplateFileName, String textTemplateFileName) throws SeamMailException;

   public TemplateMailMessage put(String name, Object value);
   
}
