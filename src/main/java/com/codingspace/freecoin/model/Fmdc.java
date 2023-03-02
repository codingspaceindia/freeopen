package com.codingspace.freecoin.model;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import lombok.Data;

@Data
@Document(collection = "fmdc")
public class Fmdc {
    @Id
    String id;
    String refId, userName, token;
}
