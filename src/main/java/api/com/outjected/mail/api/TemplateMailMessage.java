package com.outjected.mail.api;

import com.outjected.mail.exception.SeamMailException;

public interface TemplateMailMessage extends MailMessage
{
   public TemplateMailMessage setTemplateHTMLBody(String pathToTemplate) throws SeamMailException;

   public TemplateMailMessage setTemplateHTMLBodyTextAlt(String htmlTemplate, String textTemplate) throws SeamMailException;

   public TemplateMailMessage put(String name, Object value);
}
