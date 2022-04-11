package goingmerry.cent.oauth;

//import goingmerry.cent.jwt.JwtTokenProvider;
import goingmerry.cent.domain.User;
import goingmerry.cent.jwt.JwtTokenProvider;
import goingmerry.cent.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Optional;

@Component
@RequiredArgsConstructor
public class LoginSuccessHandler implements AuthenticationSuccessHandler {

    private final JwtTokenProvider jwtProvider;
    private final UserRepository userRepository;

    private String accessToken;


    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws IOException {
        // 전달받은 인증정보 SecurityContextHolder에 저장
        OAuth2UserDetails oAuthUser = (OAuth2UserDetails) authentication.getPrincipal();
        Optional<User> user = userRepository.findBySocialTypeAndUserId(oAuthUser.getSocialType(), oAuthUser.getUserId());

        accessToken = jwtProvider.createAccessToken(user.get());
        System.out.println(accessToken);
        response.setHeader("Authorization", accessToken);
        response.sendRedirect("/");

    }

}
