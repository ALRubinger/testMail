package com.outjected.mail.core;

import javax.inject.Inject;

import com.outjected.mail.annotations.Velocity;

public class Mail
{
   @Inject
   private BaseMailMessage baseMailMessage;
   
   @Inject @Velocity
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
