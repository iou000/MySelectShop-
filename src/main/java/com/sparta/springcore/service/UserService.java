package com.sparta.springcore.service;

import com.sparta.springcore.dto.SignupRequestDto;
import com.sparta.springcore.model.User;
import com.sparta.springcore.model.UserRole;
import com.sparta.springcore.repository.UserRepository;
import com.sparta.springcore.security.UserDetailsImpl;
import com.sparta.springcore.security.kakao.KakaoOAuth2;
import com.sparta.springcore.security.kakao.KakaoUserInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class UserService {
    private final PasswordEncoder passwordEncoder;
    private final UserRepository userRepository;
    private final KakaoOAuth2 kakaoOAuth2;
    private final AuthenticationManager authenticationManager;
    private static final String ADMIN_TOKEN = "AAABnv/xRVklrnYxKZ0aHgTBcXukeZygoC";

    @Autowired
    public UserService(UserRepository userRepository, PasswordEncoder passwordEncoder, KakaoOAuth2 kakaoOAuth2, AuthenticationManager authenticationManager) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.kakaoOAuth2 = kakaoOAuth2;
        this.authenticationManager = authenticationManager;
    }


    public User registerUser(SignupRequestDto requestDto) {
        String username = requestDto.getUsername();
        // 회원 ID 중복 확인
        Optional<User> found = userRepository.findByUsername(username);
        if (found.isPresent()) {
            throw new IllegalArgumentException("중복된 사용자 ID 가 존재합니다.");
        }

        // 패스워드 인코딩
        String password = passwordEncoder.encode(requestDto.getPassword());
        String email = requestDto.getEmail();
        // 사용자 ROLE 확인
        UserRole role = UserRole.USER;
        if (requestDto.isAdmin()) {
            if (!requestDto.getAdminToken().equals(ADMIN_TOKEN)) {
                throw new IllegalArgumentException("관리자 암호가 틀려 등록이 불가능합니다.");
            }
            role = UserRole.ADMIN;
        }

        User user = new User(username, password, email, role);
        user = userRepository.save(user);
        return user;
    }

    public void kakaoLogin(String authorizedCode) {
        // 카카오 OAuth2 를 통해 카카오 사용자 정보 조회
        KakaoUserInfo userInfo = kakaoOAuth2.getUserInfo(authorizedCode);
        Long kakaoId = userInfo.getId();
        String nickname = userInfo.getNickname();
        String email = userInfo.getEmail();


        // DB 에 중복된 Kakao Id 가 있는지 확인
        // kakaoUser는 db에서 kakaoId를 가진 회원. 없다면 null
        User kakaoUser = userRepository.findByKakaoId(kakaoId)
                .orElse(null);

        // db에 등록된 Kakao Id가 없다면
        if (kakaoUser == null) {
            //카카오 이메일과 동일한 이메일을 가진 회원이 있는지 확인
            User sameEmailUser = userRepository.findByEmail(email).orElse(null);
            //카카오 이메일과 동일한 이메일을 가진 회원이 있다면
            if(sameEmailUser != null){
                kakaoUser = sameEmailUser;
                // 카카오 이메일이 db에 저장되어있지 않고, 카카오 이메일과 동일한 이메일 회원이 있는 경우
                // 카카오 Id를 회원정보에 저장
                //찾아진 기존 회원 정보에 카카오 Id만 추가해서 저장해준 것임.
                kakaoUser.setKakaoId(kakaoId);
                userRepository.save(kakaoUser);
            }
            // 같은 이메일을 가진 회원이 없다면
            else {
                // 카카오 정보로 회원가입
                // 회원 Id = 카카오 nickname
                String username = nickname;
                // 패스워드 = 카카오 Id + ADMIN TOKEN
                String password = kakaoId + ADMIN_TOKEN;
                // 패스워드 인코딩
                String encodedPassword = passwordEncoder.encode(password);
                // ROLE = 사용자
                UserRole role = UserRole.USER;

                kakaoUser = new User(nickname, encodedPassword, email, role, kakaoId);
                userRepository.save(kakaoUser);
            }
        }

        //강제 로그인 처리
        //카카오 로그인이 된 사용자를 로그인 한 사용자로 인식하도록 만듬.
        //UsernamePasswordAuthenticationToken는 원래 패스워드를 입력해서 username과 password를 통해 스프링 시큐리티가 인증을 하도록 했는데
        //기존 사용자(카카오이메일과 같은 이메일을 사용하는 기존 사용자)의 패스워드를 알 수 없기 때문에 강제로그인을 처리해줘야함
        UserDetailsImpl userDetails = new UserDetailsImpl(kakaoUser);
        Authentication authentication = new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());
        SecurityContextHolder.getContext().setAuthentication(authentication);
    }
}