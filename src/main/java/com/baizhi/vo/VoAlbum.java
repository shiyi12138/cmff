package com.baizhi.vo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;
@Data
@AllArgsConstructor
@NoArgsConstructor
public class VoAlbum {
    private String thumbnail;
    private String title;
    private String score;
    private String author;
    private String broadcast;
    private String set_count;
    private String brief;
    private Date create_date;
}
