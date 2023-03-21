package com.project.chamong.home;

import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.client.OAuth2AuthorizedClient;
import org.springframework.security.oauth2.client.OAuth2AuthorizedClientService;
import org.springframework.security.oauth2.client.authentication.OAuth2AuthenticationToken;
import org.springframework.security.oauth2.core.OAuth2AccessToken;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
@RequiredArgsConstructor
public class HomeController {
  private final OAuth2AuthorizedClientService authorizedClientService;
  
  @GetMapping("/hello-oauth2")
  public String home(OAuth2AuthenticationToken authentication){
    
    OAuth2User oAuth2User = (OAuth2User) authentication.getPrincipal();
    OAuth2AuthorizedClient authorizedClient = authorizedClientService.loadAuthorizedClient(authentication.getAuthorizedClientRegistrationId(), authentication.getName());
    System.out.println("User's email in Google: " + oAuth2User.getAttributes().get("email"));
    System.out.println(authorizedClient.getClientRegistration().getRedirectUri());
    OAuth2AccessToken accessToken = authorizedClient.getAccessToken();
    System.out.println("Access Token Value: " + accessToken.getTokenValue());
    System.out.println("Access Token Type: " + accessToken.getTokenType().getValue());
    System.out.println("Access Token Scopes: " + accessToken.getScopes());
    System.out.println("Access Token Issued At: " + accessToken.getIssuedAt());
    System.out.println("Access Token Expires At: " + accessToken.getExpiresAt());
    return "hello-oauth2";
  }
}
