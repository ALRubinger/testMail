package com.outjected.mail.velocity;

import org.apache.velocity.VelocityContext;

public class SeamBaseVelocityContext extends VelocityContext
{
   public SeamBaseVelocityContext(SeamCDIVelocityContext seamCDIVelocityContext)
   {
      super(seamCDIVelocityContext);
   }
}
