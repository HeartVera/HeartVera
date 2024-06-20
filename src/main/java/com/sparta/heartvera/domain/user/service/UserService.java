package com.sparta.heartvera.domain.user.service;

import com.sparta.heartvera.common.exception.CustomException;
import com.sparta.heartvera.common.exception.ErrorCode;
import com.sparta.heartvera.domain.user.dto.UserPwRequestDto;
import com.sparta.heartvera.domain.user.dto.UserRequestDto;
import com.sparta.heartvera.domain.user.dto.UserResponseDto;
import com.sparta.heartvera.domain.user.entity.PasswordHistory;
import com.sparta.heartvera.domain.user.entity.User;
import com.sparta.heartvera.domain.user.repository.PasswordHistoryRepository;
import com.sparta.heartvera.domain.user.repository.UserRepository;
import java.time.LocalDateTime;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class UserService {

  private final UserRepository userRepository;
  private final PasswordEncoder passwordEncoder;
  private final PasswordHistoryRepository passwordHistoryRepository;

  // 사용자 프로필 조회
  public UserResponseDto getUser(Long userSeq) {
    User user = findByUserSeq(userSeq);
    return new UserResponseDto(user);
  }

  // 사용자 프로필 수정
  @Transactional
  public UserResponseDto updateUser(UserRequestDto requestDto, Long userSeq) {
    User user = findByUserSeq(userSeq);
    user.updateUser(requestDto);
    return new UserResponseDto(user);
  }

  // 비밀번호 변경
  @Transactional
  public void updatePassword(UserPwRequestDto requestDto, Long userSeq) {
    User user = findByUserSeq(userSeq);
    String password = requestDto.getPassword();
    String newPassword = requestDto.getNewPassword();

    if (!passwordEncoder.matches(password, user.getUserPassword())) {
      throw new CustomException(ErrorCode.CURRENT_PASSWORD_MATCH);
    }

    if (passwordEncoder.matches(newPassword, user.getUserPassword())) {
      throw new CustomException(ErrorCode.SAME_NEW_PASSWORD);
    }

    List<PasswordHistory> usedPasswords = passwordHistoryRepository.findTop3ByUserOrderByChangedAtDesc(
        user);
    for (PasswordHistory usedPassword : usedPasswords) {
      if (passwordEncoder.matches(newPassword, usedPassword.getPassword())) {
        throw new CustomException(ErrorCode.RECENT_PASSWORD_MATCH);
      }
    }

    if (usedPasswords.size() >= 3) {
      PasswordHistory oldPassword = usedPasswords.get(usedPasswords.size() - 1);
      passwordHistoryRepository.delete(oldPassword);
    }

    PasswordHistory newPasswordHistory = new PasswordHistory(user, passwordEncoder.encode(newPassword), LocalDateTime.now());
    passwordHistoryRepository.save(newPasswordHistory);

    user.updatePassword(passwordEncoder.encode(newPassword));

  }

  private User findByUserSeq(Long userSeq) {
    return userRepository.findById(userSeq).orElseThrow(() ->
        new CustomException(ErrorCode.USER_NOT_FOUND)
    );
  }

}
