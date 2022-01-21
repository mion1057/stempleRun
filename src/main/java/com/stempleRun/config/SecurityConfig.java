package com.stempleRun.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationSuccessHandler;
import org.springframework.security.web.csrf.CookieCsrfTokenRepository;

@Configuration
@EnableWebSecurity
public class SecurityConfig extends WebSecurityConfigurerAdapter {
   @Override
   protected void configure(HttpSecurity http) throws Exception {
      http
      // post 메소드가 포함된 컨트롤러만 여기다가 컨트롤러의 requestMapping + **을 붙여주고 .permitAll()을 해준다.
         .authorizeRequests()
            .antMatchers("/").permitAll() // 모든 권한을 줌.=로그인 필요 없음.
            .antMatchers("/test/**").permitAll()
            .antMatchers("/member/**").permitAll()
            .antMatchers("/files/**").permitAll()//hasRole("user") // user 권한만 접근 가능.
            .antMatchers("/gallery/**").permitAll()
            .antMatchers("/logout").permitAll()
            .antMatchers("/storymake/aaa").permitAll()
            .antMatchers("/storymake/app_area").permitAll()
            .antMatchers("/storymake/app_area_storyList").permitAll()
            .antMatchers("/storymake/bbb").permitAll()
            .antMatchers("/storymake/storyStart").permitAll()
            .antMatchers("/storymake/app_storyDuring").permitAll()
            .antMatchers("/storymake//app_storyEnd").permitAll()
            .antMatchers("/app_login/login").permitAll()
            .antMatchers("/app_member/app_autoLoginCheck").permitAll()
            .antMatchers("/storymake/app_test").permitAll()
            .antMatchers("/app_post/post").permitAll()
            .antMatchers("/app_post/popular").permitAll()
            .antMatchers("/app_post/latest").permitAll()
            .antMatchers("/app_post/postInfoList").permitAll()
            .antMatchers("/app_post/app_reco").permitAll()
            .antMatchers("/app_post/app_reco_check").permitAll()
            .antMatchers("/bingo/Bingo_app_area").permitAll() //여기서부터 Bingo관련시작
            .antMatchers("/bingo/Bingo_app_list").permitAll()
            .antMatchers("/app_member/app_signup").permitAll()
            .antMatchers("/app_member/app_idcheck").permitAll()
            .antMatchers("/review/app_getList").permitAll()
            .antMatchers("/review/app_getReview").permitAll()
            .antMatchers("/comment/app_list").permitAll()
            .antMatchers("/comment/app_insert").permitAll()
            .antMatchers("/review/app_recommend").permitAll()
            .antMatchers("/review/app_hits").permitAll()
            .antMatchers("/review/app_insert").permitAll()
            .antMatchers("/mypage/userHost").permitAll()
            .antMatchers("/mypage/userStory").permitAll()
            .antMatchers("/mypage/userBingo").permitAll()
            .antMatchers("/mypage/userWeek").permitAll()
            .antMatchers("/mypage/userMonth").permitAll()
            .anyRequest()
            .authenticated() // 로그인 체크함.
            .and()
            .formLogin()
            .loginPage("/login") // 이 줄을 지우면 스프링이 제공하는 폼이 출력됨.
            .defaultSuccessUrl("/loginProc")
            .permitAll()
            .successHandler(successHandler())
            .and()
         .logout()
            .logoutUrl("/logout")
            .logoutSuccessUrl("/")
            .invalidateHttpSession(true)
            .and()
         .csrf()
            .csrfTokenRepository(CookieCsrfTokenRepository.withHttpOnlyFalse());
      
      // 시큐리티가 모든 post요청을 막기 때문에 이렇게 적어줘서 막는걸 피한다.
      http.csrf().ignoringAntMatchers("/culture/save");
      //http.csrf().ignoringAntMatchers("/storymake/manage_fileSave");
      http.csrf().ignoringAntMatchers("/storymake/**");
      http.csrf().ignoringAntMatchers("/bingo/**");
      http.csrf().ignoringAntMatchers("/files");
      http.csrf().ignoringAntMatchers("/gallery/**");
      http.csrf().ignoringAntMatchers("/review/**");
      http.csrf().ignoringAntMatchers("/insertProc");
      http.csrf().ignoringAntMatchers("/updateProc");
      http.csrf().ignoringAntMatchers("/amenity/**");
      http.csrf().ignoringAntMatchers("/comment/**");
      http.csrf().ignoringAntMatchers("/app_login/**");
      http.csrf().ignoringAntMatchers("/app_post/**");
      http.csrf().ignoringAntMatchers("/app_member/**");
      http.csrf().ignoringAntMatchers("/mypage/**");
   }
   
   // css랑 이미지 같은거 무조건 통과시켜주기 위한거
      @Override
      public void configure(WebSecurity web) {
         web.ignoring().antMatchers("/img/**", "/css/**", "/uploads/**", "/js/**");
      }

   public AuthenticationSuccessHandler successHandler() {
      SimpleUrlAuthenticationSuccessHandler handler = new SimpleUrlAuthenticationSuccessHandler();
      return handler;
   }
}
   
