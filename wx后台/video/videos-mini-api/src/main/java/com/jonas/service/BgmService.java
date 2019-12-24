package com.jonas.service;

import com.jonas.pojo.Bgm;

import java.util.List;

/**
 * @author cmj
 * @date 2019-03-11 22:20
 */
public interface BgmService {
    /**
     * 查询BGM
     * @return
     */
    List<Bgm> queryBgmList();

    /**
     * 根据id查找相应的BGM
     * @param id
     * @return
     */
    Bgm queryBgmById(String id);
}
