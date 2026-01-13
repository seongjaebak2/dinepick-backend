package com.dinepick.dinepickbackend.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class MemberUpdateRequest {
    @NotBlank(message = "이름은 필수입니다.")
    @Size(min = 2, max = 20, message = "이름은 2~20자여야 합니다.")
    private String name;

    // ↓↓↓ 비밀번호 변경은 선택 ↓↓↓
    private String currentPassword;

    @Size(min = 4, max = 20, message = "비밀번호는 4~20자여야 합니다.")
    private String newPassword; // null이면 비밀번호 변경 안 함

    private String newPasswordConfirm;
}
