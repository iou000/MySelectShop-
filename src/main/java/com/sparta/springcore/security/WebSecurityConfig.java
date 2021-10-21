package com.sparta.springcore.security;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

// 처음 스프링이 기동될 때 WebSecurityConfig 클래스를 바라보고 @Bean 함수들을 살펴보고 필요한 내용들을
// Bean으로 담는다는 의미
@Configuration
@EnableWebSecurity // 스프링 Security 지원을 가능하게 함
public class WebSecurityConfig extends WebSecurityConfigurerAdapter {
    @Bean
    public BCryptPasswordEncoder encodePassword() {
        //BCryptPasswordEncoder 객체를 만들어서 IoC컨테이너에 넣어줌.
        return new BCryptPasswordEncoder();
    }


    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http.csrf().disable();
        http.headers().frameOptions().disable();

        http.authorizeRequests()
                // image 폴더를 login 없이 허용
                .antMatchers("/images/**").permitAll()
                // css 폴더를 login 없이 허용
                .antMatchers("/css/**").permitAll()
                .antMatchers("/user/**").permitAll()
                .antMatchers("/h2-console/**").permitAll()
                //그 외 모든 요청은 인증과정 필요
                .anyRequest().authenticated() //어떤 요청이 오든 로그인 과정이 없으면 로그인을 하도록 해줌.
                .and()
                .formLogin() //로그인 페이지에 대해서는	허용해줌.
                .loginPage("/user/login")//로그인이 필요할 때 필요한 페이지의 위치를 지정해줌.
                .failureUrl("/user/login/error") //로그인이 실패했을 때 해당되는 url로 요청됨.
                .defaultSuccessUrl("/") //로그인이 완료되었을 때 이동할 위치
                .permitAll()
                .and()
                .logout() //로그아웃 기능도 허용해줌.
                .logoutUrl("/user/logout")
                .permitAll();
    }
}