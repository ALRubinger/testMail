package com.outjected.mail.core.enumurations;

public enum MailHeader
{
   DELIVERY_RECIEPT("Disposition-Notification-To"),
   READ_RECIEPT("Return-Receipt-To");
   
   private String headerValue;
   
   private MailHeader(String headerValue)
   {
      this.headerValue = headerValue;
   }
   public String headerValue()
   {
      return headerValue;
   }
}
