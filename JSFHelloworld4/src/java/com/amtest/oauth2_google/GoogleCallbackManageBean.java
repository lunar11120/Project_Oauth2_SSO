/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.amtest.oauth2_google;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import javax.faces.bean.ManagedBean;
import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

@ManagedBean(name = GoogleCallbackManageBean.CONTROLLER_NAME)
@Component
@Scope(value = "session")
public class GoogleCallbackManageBean {
    
    public static final String CONTROLLER_NAME = "googleCallbackManageBean";

    private String username;
    private String password;
    private boolean isAuthen; 
    private boolean remember;    

    public GoogleCallbackManageBean() {
         isAuthen = Boolean.FALSE;
    }

 //test pass seamless sso jdk1.8   run glassfish 3.1.2  java 1.6
       public String getCodeID_Google() {
           System.out.println("[authenticationControllert] ** listener invoking ---------"); 
         //String code = request.getParameter("code");   
        FacesContext facesContext = FacesContext.getCurrentInstance();
        String code = facesContext.getExternalContext().getRequestParameterMap().get("code");
        System.out.println(code);
        
        String clientId = "469397192090-gjevgcrhmrtuuiajm7amdu9vo79v3bk1.apps.googleusercontent.com";
        String clientSecret = "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";
        String redirectUri = "http://localhost:8080/seamless/callback.jsf";
        try {
            // Exchange code for access token using HttpURLConnection
            URL tokenUrl = new URL("https://oauth2.googleapis.com/token");
            String urlParameters = "code=" + code
                    + "&client_id=" + clientId
                    + "&client_secret=" + clientSecret
                    + "&redirect_uri=" + redirectUri
                    + "&grant_type=authorization_code";
            byte[] postData = urlParameters.getBytes("UTF-8");
            
            System.out.println("getToken with POST :"+urlParameters);
            
            HttpURLConnection tokenConn = (HttpURLConnection) tokenUrl.openConnection();
            tokenConn.setRequestMethod("POST");
            tokenConn.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");
            tokenConn.setDoOutput(true);
            tokenConn.getOutputStream().write(postData);
            BufferedReader tokenIn = new BufferedReader(new InputStreamReader(tokenConn.getInputStream()));
            StringBuilder tokenResp = new StringBuilder();
            String tokenLine;
            while ((tokenLine = tokenIn.readLine()) != null) {
                tokenResp.append(tokenLine);
                System.out.println("Token Detail:"+tokenLine);
            }
            tokenIn.close();
            tokenConn.disconnect();
            
            // Parse access_token from tokenResp (simple string search, not robust JSON parsing)
            String tokenJson = tokenResp.toString();
            String accessToken = null;
            int atIdx = tokenJson.indexOf("\"access_token\":");
            if (atIdx != -1) {
                int start = tokenJson.indexOf('"', atIdx + 16) + 1;
                int end = tokenJson.indexOf('"', start);
                accessToken = tokenJson.substring(start, end);
            }
            if (accessToken == null) {
                throw new Exception("Access token not found");
            }
            // Get user info
            URL userUrl = new URL("https://www.googleapis.com/oauth2/v2/userinfo?access_token=" + accessToken);
            
            
            System.out.println("get Access Token : "+userUrl);
            
            HttpURLConnection userConn = (HttpURLConnection) userUrl.openConnection();
            userConn.setRequestMethod("GET");
            BufferedReader userIn = new BufferedReader(new InputStreamReader(userConn.getInputStream()));
            StringBuilder userContent = new StringBuilder();
            String userLine;
            while ((userLine = userIn.readLine()) != null) {
                userContent.append(userLine);
                System.out.println(userLine);
            }
            userIn.close();
            userConn.disconnect();
            

            System.out.println("authenticationManager ---- login ------");
           ExternalContext  context = FacesContext.getCurrentInstance().getExternalContext();
            context.redirect("index.jsf");
            FacesContext.getCurrentInstance().responseComplete(); 
            

            
         } catch (Exception e) {
            e.printStackTrace();
            //response.sendRedirect("default.jsf");
        }           
        
        return code;
    }    
      
     public void loginByServlet(ExternalContext context) {
        try {
            context = FacesContext.getCurrentInstance().getExternalContext();
   
            context.redirect("index.jsf");
            FacesContext.getCurrentInstance().responseComplete();
        } catch (Exception ex) {
            ex.printStackTrace();
          
        }
    }      
       
       
    
}
