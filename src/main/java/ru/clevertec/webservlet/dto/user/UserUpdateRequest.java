package ru.clevertec.webservlet.dto.user;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public record UserUpdateRequest(String password,

                                @SerializedName("role_ids")
                                List<Long> roleIds) {
}
