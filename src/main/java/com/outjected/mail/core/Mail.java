package com.outjected.mail.core;

import javax.inject.Inject;

public class Mail
{
   @Inject
   private BaseMailMessage baseMailMessage;

   @Inject 
   private VelocityMailMessage velocityMailMessage;

   public BaseMailMessage createBaseMailMessage()
   {
      return baseMailMessage;
   }

   public VelocityMailMessage createVelocityMailMessage()
   {
      return velocityMailMessage;
   }
}
