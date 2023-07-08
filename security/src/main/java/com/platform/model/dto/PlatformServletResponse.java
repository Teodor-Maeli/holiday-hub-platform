package com.platform.model.dto;

import com.platform.model.PlatformError;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class PlatformServletResponse {

    private PlatformClientResponse platformClientResponse;
    private PlatformError[] errors;
}
