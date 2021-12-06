package com.elsys.ip.springboottimerhomework.dto;

import com.elsys.ip.springboottimerhomework.serializers.SecondsTohhmmssSerializer;
import com.elsys.ip.springboottimerhomework.serializers.YesNoBooleanSerializer;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;

@JsonInclude(JsonInclude.Include.NON_NULL)
public record ResponseDTO(String id, String name,
		@JsonProperty("time") @JsonSerialize(using = SecondsTohhmmssSerializer.class) Integer timeInSeconds,
		@JsonProperty("totalSeconds") Integer totalSeconds, Integer hours, Integer minutes, Integer seconds,
		@JsonSerialize(using = YesNoBooleanSerializer.class) Boolean done) {
}
