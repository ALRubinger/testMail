package com.outjected.mail.core.enumurations;

public enum ContentDisposition
{
   ATTACHMENT("attachment"),
   INLINE("inline");
   
   private String headerValue;
   
   private ContentDisposition(String headerValue)
   {
      this.headerValue = headerValue;
   }

   public String headerValue()
   {
      return headerValue;
   }
}
