package com.outjected.mail.velocity;

import javax.el.ELContext;
import javax.el.ELException;
import javax.el.PropertyNotFoundException;
import javax.inject.Inject;

import org.apache.velocity.context.AbstractContext;
import org.slf4j.Logger;

import com.outjected.mail.annotations.Module;

public class SeamELVelocityContext extends AbstractContext
{
   @Inject
   Logger log;

   @Inject
   @Module
   private ELContext elContext;
   
   public SeamELVelocityContext()
   {
      super();
   }

   @Override
   public boolean internalContainsKey(Object key)
   {
      Object object = null;

      log.info("Checking if EL contains key: " + key.toString());
      try
      {
         object = elContext.getELResolver().getValue(elContext, null, key);
      }
      catch (PropertyNotFoundException e)
      {
         log.debug("EL Does Not Contains Key: " + key);
         return false;
      }
      catch (ELException e)
      {
         log.debug("EL Does Not Contains Key: " + key);
         return false;
      }
      if (object != null)
      {
         log.debug("EL Contains Key: " + key);
         return true;
      }
      else
      {
         log.debug("EL Does Not Contains Key: " + key);
         return false;
      }
   }

   @Override
   public Object internalGet(String key)
   {
      log.info("Getting Object by EL key: " + key.toString());

      Object object = null;

      try
      {
         object = elContext.getELResolver().getValue(elContext, null, key);
      }
      catch (PropertyNotFoundException e)
      {
         log.debug("Key: " + key.toString() + " Not Found!");
         return null;
      }
      catch (ELException e)
      {
         log.debug("EL Exception for Key: " + key.toString());
         e.printStackTrace();
         return null;
      }
      catch (NullPointerException e)
      {
         log.debug("ELContext was null");
         return null;
      }

      if (object != null)
      {
         log.debug("Found Object by EL key: " + key.toString());
      }
      
      return object;
   }

   @Override
   @Deprecated
   public Object[] internalGetKeys()
   {
      return null;
   }

   @Override
   @Deprecated
   public Object internalPut(String key, Object value)
   {
      return null;
   }

   @Override
   @Deprecated
   public Object internalRemove(Object key)
   {
      return null;
   }

}
