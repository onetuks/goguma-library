package com.onetuks.libraryauth.oauth.strategy;

import com.onetuks.libraryauth.config.GoogleClientConfig;
import com.onetuks.libraryauth.exception.TokenValidFailedException;
import com.onetuks.libraryauth.oauth.dto.GoogleAuthToken;
import com.onetuks.libraryauth.oauth.dto.GoogleUser;
import com.onetuks.librarydomain.member.model.vo.AuthInfo;
import com.onetuks.libraryobject.config.WebClientConfig;
import com.onetuks.libraryobject.enums.ClientProvider;
import com.onetuks.libraryobject.enums.RoleType;
import com.onetuks.libraryobject.error.ErrorCode;
import com.onetuks.libraryobject.util.URIBuilder;
import java.util.Objects;
import java.util.Set;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.http.HttpStatusCode;
import org.springframework.stereotype.Component;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.reactive.function.BodyInserters;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

@Component
@ComponentScan(basePackageClasses = WebClientConfig.class)
public class GoogleClientProviderStrategy implements ClientProviderStrategy {

  private final WebClient webClient;
  private final URIBuilder uriBuilder;
  private final GoogleClientConfig googleClientConfig;

  public GoogleClientProviderStrategy(
      WebClient webClient, URIBuilder uriBuilder, GoogleClientConfig googleClientConfig) {
    this.webClient = webClient;
    this.uriBuilder = uriBuilder;
    this.googleClientConfig = googleClientConfig;
  }

  @Override
  public AuthInfo getAuthInfo(String authToken) {
    GoogleUser googleUser =
        webClient
            .get()
            .uri(
                googleClientConfig
                    .googleClientRegistration()
                    .getProviderDetails()
                    .getUserInfoEndpoint()
                    .getUri())
            .headers(httpHeaders -> httpHeaders.set("Authorization", authToken))
            .retrieve()
            .onStatus(
                HttpStatusCode::is4xxClientError,
                clientResponse ->
                    Mono.error(new TokenValidFailedException(ErrorCode.UNAUTHORIZED_TOKEN)))
            .onStatus(
                HttpStatusCode::is5xxServerError,
                clientResponse ->
                    Mono.error(new TokenValidFailedException(ErrorCode.OAUTH_CLIENT_SERVER_ERROR)))
            .bodyToMono(GoogleUser.class)
            .block();

    Objects.requireNonNull(googleUser);

    return AuthInfo.builder()
        .socialId(googleUser.getSub())
        .clientProvider(ClientProvider.GOOGLE)
        .roles(Set.of(RoleType.USER))
        .build();
  }

  @Override
  public GoogleAuthToken getOAuth2Token(String authCode) {
    return webClient
        .post()
        .uri(
            builder ->
                uriBuilder.buildUri(
                    googleClientConfig
                        .googleClientRegistration()
                        .getProviderDetails()
                        .getTokenUri(),
                    buildParamsMap()))
        .headers(
            httpHeaders ->
                httpHeaders.set("Content-Type", "application/x-www-form-urlencoded;charset=utf-8"))
        .body(BodyInserters.fromFormData(buildFormData(authCode)))
        .retrieve()
        .onStatus(
            HttpStatusCode::is4xxClientError,
            clientResponse ->
                Mono.error(new TokenValidFailedException(ErrorCode.UNAUTHORIZED_TOKEN)))
        .onStatus(
            HttpStatusCode::is5xxServerError,
            clientResponse ->
                Mono.error(new TokenValidFailedException(ErrorCode.OAUTH_CLIENT_SERVER_ERROR)))
        .bodyToMono(GoogleAuthToken.class)
        .block();
  }

  private MultiValueMap<String, String> buildParamsMap() {
    MultiValueMap<String, String> params = new LinkedMultiValueMap<>();
    params.add("client_id", googleClientConfig.getClientId());
    params.add("client_secret", googleClientConfig.getClientSecret());
    return params;
  }

  private MultiValueMap<String, String> buildFormData(String authCode) {
    MultiValueMap<String, String> formData = new LinkedMultiValueMap<>();
    formData.add("grant_type", "authorization_code");
    formData.add("code", authCode);
    formData.add("redirect_uri", googleClientConfig.googleClientRegistration().getRedirectUri());
    formData.add("state", googleClientConfig.getClientSecret());
    return formData;
  }
}
