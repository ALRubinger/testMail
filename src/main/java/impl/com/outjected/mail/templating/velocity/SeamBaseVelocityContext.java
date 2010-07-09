package com.outjected.mail.templating.velocity;

import org.apache.velocity.VelocityContext;

public class SeamBaseVelocityContext extends VelocityContext
{
   public SeamBaseVelocityContext(SeamCDIVelocityContext seamCDIVelocityContext)
   {
      super(seamCDIVelocityContext);
   }
}
