package com.monds.quartzui;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class JobResponse {

    private String status;
    private String triggerState;
}
