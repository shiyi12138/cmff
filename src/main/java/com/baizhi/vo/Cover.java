package com.baizhi.vo;

import lombok.AllArgsConstructor;
        import lombok.Data;
        import lombok.NoArgsConstructor;

        import java.util.Date;
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Cover {
    private String thumbnail;
    private String title;
    private String author;
    private Integer type;
    private Integer set_count;
    private Date create_date;
}
