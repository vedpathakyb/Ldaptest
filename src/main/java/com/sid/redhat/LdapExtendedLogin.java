/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.sid.redhat;
import java.security.Principal;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import javax.naming.NamingEnumeration;
import javax.naming.NamingException;
import javax.naming.ReferralException;
import javax.naming.directory.Attribute;
import javax.naming.directory.SearchControls;
import javax.naming.directory.SearchResult;
import javax.naming.ldap.InitialLdapContext;
import javax.naming.ldap.LdapContext;
import org.jboss.security.PicketBoxMessages;
import org.jboss.security.auth.spi.LdapExtLoginModule;
import javax.naming.directory.Attributes;
import javax.security.auth.login.LoginException;
import org.jboss.security.SimplePrincipal;

/**
 *
 * @author sidde
 */
public class LdapExtendedLogin extends LdapExtLoginModule{
    private String realUsername;
    private String username;
    private Principal identity;
    
    @Override
    public boolean login() throws LoginException{
        boolean loginOk = false;
        if(!super.loginOk){
            String[] info = getUsernameAndPassword();
            username = info[0];
            String password = info[1];
            
            String expectedPassword = getUsersPassword();
            
            System.out.println(expectedPassword);
            
            if( validatePassword(password, expectedPassword) == false ){
                throw new RuntimeException("Password is not correct");
            }else{
                System.out.println("Password validated correctly");
            }
            loginOk = true;
            super.loginOk=true;
        }
        return loginOk;
    }
    
    @Override
    protected String bindDNAuthentication(InitialLdapContext ctx, String user, Object credential, String baseDN,String filter) throws NamingException{
      
        Pattern p = Pattern.compile("()(\\w+)");
        Matcher m = p.matcher(filter);
        String userAttri = ((m.find()) ? m.group() : null);
        
      SearchControls constraints = new SearchControls();
      constraints.setSearchScope(SearchControls.SUBTREE_SCOPE);
      constraints.setTimeLimit(searchTimeLimit);
      String attrList[] = {distinguishedNameAttribute,userAttri};
      constraints.setReturningAttributes(attrList);
      NamingEnumeration results = null;
      Object[] filterArgs = {user};
      
      LdapContext ldapCtx = ctx;
      boolean referralsLeft = true;
      SearchResult sr = null;
      while (referralsLeft) {
         try {
            results = ldapCtx.search(baseDN, filter, filterArgs, constraints);
            while (results.hasMore()) {
               sr = (SearchResult) results.next();
               break;
            }
            referralsLeft = false;
         }catch (ReferralException e) {
            ldapCtx = (LdapContext) e.getReferralContext();
            if (results != null) {
               results.close();
            }
         }
      }

      if (sr == null){
         results.close();
         throw PicketBoxMessages.MESSAGES.failedToFindBaseContextDN(baseDN);
      }
      System.out.println("Received username as: "+sr.getAttributes().get(userAttri).get());
      this.realUsername = sr.getAttributes().get(userAttri).get().toString();
      identity = createIdentity(realUsername);
              
      String name = sr.getName();
      String userDN = null;
      Attributes attrs = sr.getAttributes();  
      if (attrs != null)
      {
          Attribute dn = attrs.get(distinguishedNameAttribute);
          if (dn != null)
          {
              userDN = (String) dn.get();
          }
      }

      results.close();
      results = null;
      
      return userDN;
    
    }
    
    @Override
    protected Principal createIdentity(String username){
        System.out.println("Identity is now getting created for "+username);
        return new SimplePrincipal(realUsername);
    }
    
    @Override
    protected Principal getIdentity(){
       return identity;
    }
    
    @Override
    protected String getUsername(){   
        return this.username;
    }
    
    
}