package com.codingspace.freecoin.model;

import java.util.Date;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Data
@Document(collection = "notice")
public class Notice {
    @Id
    private String id;
    private String userId; // createdByUserId
    private Date createdAt;
    private NoticeType noticeType;
    private String message;
}
