/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.amtest.oauth2_google;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import javax.faces.bean.ManagedBean;
import javax.faces.context.FacesContext;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

@ManagedBean(name = GoogleAuthenticationController.CONTROLLER_NAME)
@Component
@Scope(value = "session")
public class GoogleAuthenticationController {
    
    public static final String CONTROLLER_NAME = "googleAuthenticationController";

    private String username;
    private String password;
    private boolean isAuthen; 
    private boolean remember;    

    public GoogleAuthenticationController() {
         isAuthen = Boolean.FALSE;
    }

     public void login() throws UnsupportedEncodingException, IOException{       
         isAuthen = Boolean.TRUE;
         System.out.println("[authenticationController] --- Invoking login isAuthen = "+isAuthen);
         /*
        // Dev Site seamless
        String clientId = "seamless";
        String clientSecret = "mtb40SgzNU1KNEGRGzwrRZTwxDsxP6h5";  
        String redirectUri = "http://192.168.202.100:28081/seamless";

        String tokenUrl = "https://tiam.nhso.go.th/realms/nhso/protocol/openid-connect/token";
        String authUrl = "https://tiam.nhso.go.th/realms/nhso/protocol/openid-connect/auth"
                + "?response_type=code"
                + "&client_id=" + URLEncoder.encode(clientId, "UTF-8")
                + "&redirect_uri=" + URLEncoder.encode(redirectUri, "UTF-8")
                + "&scope=openid&state=testam01";
        //check Url
        */

         
        String clientId = "469397192090-gjevgcrhmrtuuiajm7amdu9vo79v3bk1.apps.googleusercontent.com";
        String redirectUri = "http://localhost:8080/seamless/callback.jsf";
        String authUrl = "https://accounts.google.com/o/oauth2/auth" +
                "?client_id=" + clientId +
                "&redirect_uri=" + redirectUri +
                "&response_type=code" +
                "&scope=openid%20email%20profile" +
                "&access_type=online";      
        
        
        System.out.println("Go to this URL: " + authUrl);         
        FacesContext.getCurrentInstance().getExternalContext().redirect(authUrl);
         
     }    
    
}
