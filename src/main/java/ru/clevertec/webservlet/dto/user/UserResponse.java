package ru.clevertec.webservlet.dto.user;

import com.google.gson.annotations.JsonAdapter;
import ru.clevertec.webservlet.dto.adapter.LocalDateTimeAdapter;
import ru.clevertec.webservlet.model.Role;

import java.time.LocalDateTime;
import java.util.List;

public record UserResponse(Long id,
                           String nickname,
                           String password,

                           @JsonAdapter(LocalDateTimeAdapter.class)
                           LocalDateTime registerTime,

                           List<Role> roles) {
}
