package com.outjected.mail.core;

public class FileTools
{
   public static String determineMimeType(String fileName)
   {
      String extension = parseFileExtension(fileName);
      
      if (extension.equals("pdf"))
      {
         return "application/pdf";
      }
      else if (extension.equals("doc"))
      {
         return "application/msword";
      }
      else if (extension.equals("docx"))
      {
         return "application/vnd.openxmlformats-officedocument.wordprocessingml.document";
      }
      else if (extension.equals("xls"))
      {
         return "application/vnd.ms-excel";
      }
      else if (extension.equals("xlsx"))
      {
         return "application/vnd.openxmlformats-officedocument.spreadsheetml.sheet";
      }
      else if (extension.equals("vsd"))
      {
         return "application/visio";
      }
      else if (extension.equals("txt"))
      {
         return "text/plain";
      }
      else if (extension.equals("csv"))
      {
         return "application/csv";
      }
      else if (extension.equals("gif"))
      {
         return "image/gif";
      }
      else if (extension.equals("bmp"))
      {
         return "image/bmp";
      }
      else if (extension.equals("jpg") || extension.equals("jpeg"))
      {
         return "image/jpeg";
      }
      else if (extension.equals("tif") || extension.equals("tiff"))
      {
         return "image/tiff";
      }
      else
      {
         return "application/octet-stream";
      }
   }
   
   private static String parseFileExtension(String name)
   {
      String[] parts = name.split("\\."); 
      return parts[parts.length - 1].toLowerCase();
   }
}
