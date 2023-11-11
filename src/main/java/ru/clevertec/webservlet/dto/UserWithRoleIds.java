package ru.clevertec.webservlet.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UserWithRoleIds {

    private String nickname;
    private String password;
    private LocalDateTime registerTime;
    private List<Long> roleIds;

}
