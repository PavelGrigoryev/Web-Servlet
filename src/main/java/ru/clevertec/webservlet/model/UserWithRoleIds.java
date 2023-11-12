package ru.clevertec.webservlet.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.Set;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UserWithRoleIds {

    private String nickname;
    private String password;
    private LocalDateTime registerTime;
    private Set<Long> roleIds;

}
