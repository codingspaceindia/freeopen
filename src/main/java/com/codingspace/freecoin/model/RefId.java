package com.codingspace.freecoin.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class RefId {
    @JsonProperty("ref id")
    String refId;
}
