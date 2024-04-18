package com.example.weddinginvitation.member.service;

import com.example.weddinginvitation.member.dto.MemberDto;
import com.example.weddinginvitation.member.entity.MemberEntity;
import com.example.weddinginvitation.member.repository.MemberRepository;
import java.util.HashMap;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class MemberService {

    private final MemberRepository memberRepository;
    private final MemberDto memberDto;

    /**
     * @param userInfo 액세스 토큰을 이용해 카카오 이메일,닉네임 데이터 가져옴
     * @return 레퍼지토리 저장
     */
    public MemberEntity kakaoLogin(HashMap<String, Object> userInfo) {

        Optional<MemberEntity> userEmail = memberRepository.findByEmail((String) userInfo.get("email"));
        MemberEntity user = userEmail.orElse(null);

        if (user == null) {
            log.info("회원정보가 없으므로 회원가입 진행하겠습니다.");
            MemberEntity kakaoLoginData = memberDto.toEntity(userInfo);
            return memberRepository.save(kakaoLoginData);
        }
        if (user.getName() == null) {
            log.info("핸드폰번호 인증이 필요합니다.");
        } else {
            log.info("이미 회원가입이 되어있습니다.");
        }
        return user;
    }
/*    public MemberEntity kakaoLogin(String userInfoJson) {
        MemberDto memberDto = MemberDto.fromJson(userInfoJson);

        Optional<MemberEntity> userEmail = memberRepository.findByEmail(memberDto.getEmail());
        MemberEntity user = userEmail.orElse(null);

        if (user == null) {
            log.info("회원정보가 없으므로 회원가입 진행하겠습니다.");
            MemberEntity kakaoLoginData = memberDto.toEntity();
            return memberRepository.save(kakaoLoginData);
        }

        if (user.getName() == null) {
            log.info("핸드폰번호 인증이 필요합니다.");
        } else {
            log.info("이미 회원가입이 되어있습니다.");
        }

        return user;
    }*/


    /**
     * @param memberDto            소셜로그인에서 가져오지 못하는 정보 추가기입
     * @param authenticationNumber 문자인증 번호
     * @return 문자인증 성공 여부
     */
    public MemberEntity sendSmsAuthenticationCode(MemberDto memberDto, String authenticationNumber) {

        Optional<MemberEntity> byEmail = memberRepository.findByEmail(memberDto.getEmail());

        if (byEmail.isPresent()) {
            MemberEntity existingMember = byEmail.get();
            existingMember.setTextAuthenticationNumber(authenticationNumber);
            existingMember.setPhoneNumber(memberDto.getPhoneNumber());
            existingMember.setName(memberDto.getName());
            return memberRepository.save(existingMember);
        } else {
            throw new RuntimeException(memberDto.getEmail() + " : 메일은 카카오 회원가입이 되지 않았습니다.");
        }

    }

    /**
     * @param authenticationNumber 문자인증 확인
     * @return 회원가입 완료
     */
    public MemberEntity sendSmsAuthenticationCodeCheck(String authenticationNumber) {
        Optional<MemberEntity> byTextAuthenticationNumber = memberRepository.findByTextAuthenticationNumber(
                authenticationNumber);
        if (byTextAuthenticationNumber.isPresent()) {
            MemberEntity existingMember = byTextAuthenticationNumber.get();
            existingMember.setTextAuthentication(true);
            return memberRepository.save(existingMember);

        } else {
            throw new RuntimeException("인증번호가 잘못되었습니다.");
        }
    }

}
