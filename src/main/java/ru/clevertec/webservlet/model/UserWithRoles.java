package ru.clevertec.webservlet.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import ru.clevertec.webservlet.tables.pojos.Role;

import java.time.LocalDateTime;
import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UserWithRoles {

    private Long id;
    private String nickname;
    private String password;
    private LocalDateTime registerTime;
    private List<Role> roles;

}
