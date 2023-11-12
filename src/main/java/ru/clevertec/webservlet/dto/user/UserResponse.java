package ru.clevertec.webservlet.dto.user;

import com.google.gson.annotations.JsonAdapter;
import com.google.gson.annotations.SerializedName;
import ru.clevertec.webservlet.dto.adapter.LocalDateTimeAdapter;
import ru.clevertec.webservlet.dto.role.RoleResponse;

import java.time.LocalDateTime;
import java.util.List;

public record UserResponse(Long id,
                           String nickname,
                           String password,

                           @JsonAdapter(LocalDateTimeAdapter.class)
                           @SerializedName("register_time")
                           LocalDateTime registerTime,

                           List<RoleResponse> roles) {
}
