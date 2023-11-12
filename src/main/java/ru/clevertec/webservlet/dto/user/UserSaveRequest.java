package ru.clevertec.webservlet.dto.user;

import com.google.gson.annotations.SerializedName;

import java.util.Set;

public record UserSaveRequest(String nickname,
                              String password,

                              @SerializedName("role_ids")
                              Set<Long> roleIds) {
}
