package com.jonas.dto;

import com.jonas.pojo.Bgm;

/**
 * @author cmj
 * @date 2019-04-07 17:31
 */
public class BgmDTO {
    private Bgm bgm;
    private Integer page;
    private Integer pageSize;

    public Bgm getBgm() {
        return bgm;
    }

    public void setBgm(Bgm bgm) {
        this.bgm = bgm;
    }

    public Integer getPage() {
        return page;
    }

    public void setPage(Integer page) {
        this.page = page;
    }

    public Integer getPageSize() {
        return pageSize;
    }

    public void setPageSize(Integer pageSize) {
        this.pageSize = pageSize;
    }

    @Override
    public String toString() {
        return "BgmDTO{" +
                "bgm=" + bgm +
                ", page=" + page +
                ", pageSize=" + pageSize +
                '}';
    }
}
