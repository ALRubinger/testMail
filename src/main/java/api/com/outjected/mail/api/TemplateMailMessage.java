package com.outjected.mail.api;

import com.outjected.mail.core.MailMessage;
import com.outjected.mail.exception.SeamMailException;

public interface TemplateMailMessage<T extends TemplateMailMessage<T>> extends MailMessage<T>
{
   public T setTemplateText(String templateFileName) throws SeamMailException;
   
   public T setTemplateHTML(String templateFileName) throws SeamMailException;

   public T setTemplateHTMLTextAlt(String htmlTemplateFileName, String textTemplateFileName) throws SeamMailException;

   public T put(String name, Object value);
   
}
