package ru.clevertec.webservlet.dto.user;

import com.google.gson.annotations.JsonAdapter;
import com.google.gson.annotations.SerializedName;
import ru.clevertec.webservlet.dto.adapter.LocalDateTimeAdapter;

import java.time.LocalDateTime;
import java.util.Set;

public record UserResponse(Long id,
                           String nickname,
                           String password,

                           @JsonAdapter(LocalDateTimeAdapter.class)
                           LocalDateTime registerTime,

                           @SerializedName("role_ids")
                           Set<Long> roleIds) {
}
