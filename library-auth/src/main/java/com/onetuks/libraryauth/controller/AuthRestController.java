package com.onetuks.libraryauth.controller;

import static com.onetuks.libraryauth.jwt.AuthHeaderUtil.HEADER_AUTHORIZATION;
import static com.onetuks.libraryobject.enums.ClientProvider.GOOGLE;
import static com.onetuks.libraryobject.enums.ClientProvider.KAKAO;
import static com.onetuks.libraryobject.enums.ClientProvider.NAVER;

import com.onetuks.libraryauth.controller.dto.LoginResponse;
import com.onetuks.libraryauth.controller.dto.LogoutResponse;
import com.onetuks.libraryauth.controller.dto.RefreshResponse;
import com.onetuks.libraryauth.jwt.AuthHeaderUtil;
import com.onetuks.libraryauth.jwt.AuthToken;
import com.onetuks.libraryauth.jwt.AuthTokenProvider;
import com.onetuks.libraryauth.service.AuthService;
import com.onetuks.libraryauth.service.OAuth2ClientService;
import com.onetuks.libraryauth.service.dto.LoginResult;
import com.onetuks.libraryauth.service.dto.LogoutResult;
import com.onetuks.libraryauth.service.dto.RefreshResult;
import com.onetuks.libraryauth.util.LoginId;
import com.onetuks.librarydomain.member.service.MemberService;
import com.onetuks.libraryobject.enums.RoleType;
import jakarta.servlet.http.HttpServletRequest;
import java.util.Set;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(path = "/auth")
public class AuthRestController {

  private final AuthTokenProvider authTokenProvider;
  private final OAuth2ClientService oAuth2ClientService;
  private final AuthService authService;
  private final MemberService memberService;

  public AuthRestController(
      AuthTokenProvider authTokenProvider,
      OAuth2ClientService oAuth2ClientService,
      AuthService authService,
      MemberService memberService) {
    this.authTokenProvider = authTokenProvider;
    this.oAuth2ClientService = oAuth2ClientService;
    this.authService = authService;
    this.memberService = memberService;
  }

  /**
   * 포스트맨 카카오 로그인 1. 포스트맨에서 카카오 토큰 수령 2. 서버에 Header Authentication : Bearer {토큰} 전송 3. 서버 JWT 토큰 발급
   *
   * @param request : 카카오 토큰
   * @return : 로그인 응답
   */
  @PostMapping(path = "/postman/kakao")
  public ResponseEntity<LoginResponse> kakaoLoginWithAuthToken(HttpServletRequest request) {
    LoginResult result =
        oAuth2ClientService.loginWithAuthToken(KAKAO, request.getHeader(HEADER_AUTHORIZATION));

    return ResponseEntity.status(HttpStatus.OK).body(LoginResponse.from(result));
  }

  /**
   * 클라이언트 카카오 로그인 1. 클라이언트에서 카카오 인가코드 수령 2. 서버에 Header Authentication: Bearer {인가코드} 전송 3. 서버 JWT
   * 토큰 발급
   *
   * @param request : 카카오 인가코드
   * @return : 로그인 응답
   */
  @PostMapping(path = "/kakao")
  public ResponseEntity<LoginResponse> kakaoLoginWithAuthCode(HttpServletRequest request) {
    LoginResult result =
        oAuth2ClientService.loginWithAuthCode(KAKAO, request.getHeader(HEADER_AUTHORIZATION));

    return ResponseEntity.status(HttpStatus.OK).body(LoginResponse.from(result));
  }

  /**
   * 포스트맨 구글 로그인 1. 포스트맨에서 구글 토큰 수령 2. 서버에 Header Authentication : Bearer {토큰} 전송 3. 서버 JWT 토큰 발급
   *
   * @param request : 구글 토큰
   * @return : 로그인 응답
   */
  @PostMapping(path = "/postman/google")
  public ResponseEntity<LoginResponse> googleLoginWithAuthToken(HttpServletRequest request) {
    LoginResult result =
        oAuth2ClientService.loginWithAuthToken(GOOGLE, request.getHeader(HEADER_AUTHORIZATION));

    return ResponseEntity.status(HttpStatus.OK).body(LoginResponse.from(result));
  }

  /**
   * 클라이언트 구글 로그인 1. 클라이언트에서 구글 인가코드 수령 2. 서버에 Header Authentication: Bearer {인가코드} 전송 3. 서버 JWT 토큰
   * 발급
   *
   * @param request : 구글 인가코드
   * @return : 로그인 응답
   */
  @PostMapping(path = "/google")
  public ResponseEntity<LoginResponse> googleLoginWithAuthCode(HttpServletRequest request) {
    LoginResult result =
        oAuth2ClientService.loginWithAuthCode(GOOGLE, request.getHeader(HEADER_AUTHORIZATION));

    return ResponseEntity.status(HttpStatus.OK).body(LoginResponse.from(result));
  }

  /**
   * 포스트맨 네이버 로그인 1. 포스트맨에서 네이버 토큰 수령 2. 서버에 Header Authentication : Bearer {토큰} 전송 3. 서버 JWT 토큰 발급
   *
   * @param request : 네이버 토큰
   * @return : 로그인 응답
   */
  @PostMapping(path = "/postman/naver")
  public ResponseEntity<LoginResponse> naverLoginWithAuthToken(HttpServletRequest request) {
    LoginResult result =
        oAuth2ClientService.loginWithAuthToken(NAVER, request.getHeader(HEADER_AUTHORIZATION));

    return ResponseEntity.status(HttpStatus.OK).body(LoginResponse.from(result));
  }

  /**
   * 클라이언트 네이버 로그인 1. 클라이언트에서 네이버 인가코드 수령 2. 서버에 Header Authentication: Bearer {인가코드} 전송 3. 서버 JWT
   * 토큰 발급
   *
   * @param request : 네이버 인가코드
   * @return : 로그인 응답
   */
  @PostMapping(path = "/naver")
  public ResponseEntity<LoginResponse> naverLoginWithAuthCode(HttpServletRequest request) {
    LoginResult result =
        oAuth2ClientService.loginWithAuthCode(NAVER, request.getHeader(HEADER_AUTHORIZATION));

    return ResponseEntity.status(HttpStatus.OK).body(LoginResponse.from(result));
  }

  /**
   * 서버 JWT 토큰 갱신
   *
   * @param request : 서버 JWT 토큰
   * @param loginId : 로그인 아이디
   * @return : 갱신된 JWT 토큰
   */
  @PutMapping(path = "/refresh")
  public ResponseEntity<RefreshResponse> refreshToken(
      HttpServletRequest request, @LoginId Long loginId) {
    AuthToken authToken = getAuthToken(request);

    RefreshResult result =
        authService.updateAccessToken(authToken, loginId, authToken.getRoleTypes());

    return ResponseEntity.status(HttpStatus.OK).body(RefreshResponse.from(result));
  }

  // TODO : 일반 유저 접근 방지 처리
  /**
   * 멤버 권한 상승
   *
   * @param request : 서버 JWT 토큰
   * @param loginId : 로그인 아이디
   * @return : 권한 상승 응답 (갱신된 JWT 토큰 포함)
   */
  @PutMapping(path = "/promotion")
  public ResponseEntity<RefreshResponse> promoteMember(
      HttpServletRequest request, @LoginId Long loginId) {
    AuthToken authToken = getAuthToken(request);

    Set<RoleType> newRoles = authService.grantAdminRole(authToken);
    RefreshResult result = authService.updateAccessToken(authToken, loginId, newRoles);

    memberService.editAuthorities(loginId, newRoles);

    return ResponseEntity.status(HttpStatus.OK).body(RefreshResponse.from(result));
  }

  /**
   * 로그아웃
   *
   * @param request : 서버 JWT 토큰
   * @return : 로그아웃 응답
   */
  @DeleteMapping("/logout")
  public ResponseEntity<LogoutResponse> logout(HttpServletRequest request) {
    AuthToken authToken = getAuthToken(request);

    LogoutResult result = authService.logout(authToken);

    return ResponseEntity.status(HttpStatus.OK).body(LogoutResponse.from(result));
  }

  /**
   * 회원 탈퇴
   *
   * @param request : 서버 JWT 토큰
   * @param loginId : 회원 아이디
   * @return : 회원 탈퇴 응답
   */
  @DeleteMapping("/withdraw")
  public ResponseEntity<Void> withdrawMember(HttpServletRequest request, @LoginId Long loginId) {
    AuthToken authToken = getAuthToken(request);

    authService.logout(authToken);
    memberService.remove(loginId);

    return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
  }

  private AuthToken getAuthToken(HttpServletRequest request) {
    String accessToken = AuthHeaderUtil.extractAuthToken(request);
    return authTokenProvider.convertToAuthToken(accessToken);
  }
}
