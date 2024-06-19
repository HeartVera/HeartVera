package com.sparta.heartvera.common.exception;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public enum ErrorCode {

    FAIL(500, "실패했습니다."),
    USER_NOT_FOUND(400, "해당하는 유저를 찾을 수 없습니다."),
    SOCIAL_TOKEN_GET_FAILED(500, "해당하는 소셜 유저 토큰을 가져오는데 실패했습니다."),
    SOCIAL_USER_NOT_FOUND(400, "해당하는 소셜 유저 데이터를 가져오는데 실패했습니다."),
    DUPLICATE_PASSWORD(400, "기존 비밀번호와 동일한 비밀번호입니다."),
    USER_NOT_UNIQUE(400,"중복된 사용자가 존재합니다."),
    EMAIL_NOT_UNIQUE(400,"중복된 이메일이 존재합니다."),
    INCORRECT_PASSWORD(400, "입력하신 비밀번호가 일치하지 않습니다."),
    POST_NOT_FOUND(404,"게시글을 찾을 수 없습니다."),
    POST_NOT_USER(400, "해당 게시글의 작성자가 아닙니다."),
    POST_SAME_USER(400, "해당 게시글의 작성자입니다."),
    POST_EMPTY(204,"먼저 작성하여 소식을 알려보세요!"),
    COMMENT_NOT_FOUND(404,"댓글을 찾을 수 없습니다."),
    COMMENT_NOT_USER(400, "해당 댓글의 작성자가 아닙니다."),
    COMMENT_SAME_USER(400, "해당 댓글의 작성자입니다."),
    TOKEN_EXPIRED(400, "토큰이 만료되었습니다."),
    TOKEN_NOT_FOUND(400, "토큰을 찾을 수 없습니다.");
    private int status;
    private String msg;
}