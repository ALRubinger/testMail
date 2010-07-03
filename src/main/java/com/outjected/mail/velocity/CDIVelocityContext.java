package com.outjected.mail.velocity;

import org.apache.velocity.context.AbstractContext;

public class CDIVelocityContext extends AbstractContext
{
   
   public CDIVelocityContext()
   {
      super();
   }

   @Override
   public boolean internalContainsKey(Object key)
   {
      // See if key exists
      return false;
   }

   @Override
   public Object internalGet(String key)
   {
      //Get object for Key
      return null;
   }

   @Override
   public Object[] internalGetKeys()
   {
      //Get All Keys
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
      //This won't work
      return null;
   }

}
