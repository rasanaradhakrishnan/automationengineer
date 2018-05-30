
package com.automation.engineer.utils;

import org.junit.Assert;

/**
 * 
 * @author Rasana_R
 *
 */

public class Asserts {
	private StringBuffer verificationErrors;
	public void assertTrue(Boolean a, String msg){
        try{
               
               Assert.assertTrue(a.booleanValue());
        
        }catch (Error e){
               verificationErrors.append(e);
             
        }
 }
 
public void assertFalse(Boolean b, String msg) { 
   try { 
	   
   Assert.assertFalse(b.booleanValue()); 
  } catch (Error e){
   verificationErrors.append(e);
}
}
public void assertNotEquals(String msg, Object obj1, Object obj2) { 
   try { 
	   Assert.assertNotEquals(obj1, obj2); 
  } catch (Error e) { 
   verificationErrors.append(e); 
  }
  }
   public void assertEquals(String msg, String s1, String s2) { 
	     try { 
	      Assert.assertEquals(s1, s2); 
	    } catch (Error e) { 
	     verificationErrors.append(e); 
	  
	    }
}
	     public static void fail(String message) {
	     	Assert.fail(message);
	     }
}
	   

