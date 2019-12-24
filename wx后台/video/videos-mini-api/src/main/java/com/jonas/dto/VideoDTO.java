package com.jonas.dto;

import com.jonas.pojo.Video;

import java.util.List;

/**
 * @author cmj
 * @date 2019-03-22 12:42
 */
public class VideoDTO {
    private Integer page;
    private Integer pageSize;
    private List<Video> videoList;
    private String currentUserId;
    private String status;
    private String commentId;// 评论的Id

    public String getCommentId() {
        return commentId;
    }

    public void setCommentId(String commentId) {
        this.commentId = commentId;
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

    public List<Video> getVideoList() {
        return videoList;
    }

    public void setVideoList(List<Video> videoList) {
        this.videoList = videoList;
    }

    public String getCurrentUserId() {
        return currentUserId;
    }

    public void setCurrentUserId(String currentUserId) {
        this.currentUserId = currentUserId;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    @Override
    public String toString() {
        return "VideoDTO{" +
                "page=" + page +
                ", pageSize=" + pageSize +
                ", videoList=" + videoList +
                ", currentUserId='" + currentUserId + '\'' +
                ", status='" + status + '\'' +
                ", commentId='" + commentId + '\'' +
                '}';
    }
}
