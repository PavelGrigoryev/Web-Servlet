package ru.clevertec.webservlet.dto;

import com.google.gson.annotations.JsonAdapter;
import com.google.gson.annotations.SerializedName;
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
public class UserWithRoleIds {

    private String nickname;
    private String password;
    @JsonAdapter(LocalDateTimeAdapter.class)
    private LocalDateTime registerTime;
    @SerializedName("role_ids")
    private List<Long> roleIds;

}
