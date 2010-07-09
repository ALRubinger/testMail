package com.outjected.mail.core;

import javax.el.ELContext;
import javax.enterprise.inject.Produces;
import javax.faces.context.FacesContext;
import javax.inject.Inject;

import org.slf4j.Logger;

import com.outjected.mail.annotations.Module;

public class ELContextProducer
{
   @Inject
   private Logger log;

   @Produces @Module
   public ELContext getELContext()
   {
      log.debug("Producing ELContext");
      return FacesContext.getCurrentInstance().getELContext();
   }
}
