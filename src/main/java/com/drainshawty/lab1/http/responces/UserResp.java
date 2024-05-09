package com.drainshawty.lab1.http.responces;

import com.drainshawty.lab1.model.User;
import lombok.Builder;
import lombok.Data;

import java.io.Serializable;
import java.util.Collections;
import java.util.List;

@Data
@Builder
public class UserResp implements Serializable {

    @Builder.Default public String token = "";
    @Builder.Default public String msg = "";
    @Builder.Default public List<User> users = Collections.emptyList();
}
