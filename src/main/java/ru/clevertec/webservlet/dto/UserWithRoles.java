package ru.clevertec.webservlet.dto;

import com.google.gson.annotations.JsonAdapter;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import ru.clevertec.webservlet.dto.adapter.LocalDateTimeAdapter;

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
    @JsonAdapter(LocalDateTimeAdapter.class)
    private LocalDateTime registerTime;
    private List<Role> roles;

}
