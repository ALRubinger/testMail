package com.outjected.mail.api;

import com.outjected.mail.exception.SeamMailException;

public interface TemplateMailMessage extends MailMessage
{
   public TemplateMailMessage setTemplateTextBody(String templateFileName) throws SeamMailException;
   
   public TemplateMailMessage setTemplateHTMLBody(String templateFileName) throws SeamMailException;

   public TemplateMailMessage setTemplateHTMLBodyTextAlt(String htmlTemplateFileName, String textTemplateFileName) throws SeamMailException;

   public TemplateMailMessage put(String name, Object value);
}
