package com.jonas.service;

import com.jonas.dto.BgmDTO;
import com.jonas.pojo.Bgm;
import com.jonas.utils.PagedResult;

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

    /**
     * 添加BGM
     * @param bgm
     */
    void addBgm(Bgm bgm);

    /**
     * 删除视频
     * @param bgmList
     */
    void delBgm(List<Bgm> bgmList);

    /**
     * 获取BGM列表
     * @param bgmDTO
     * @return
     */
    PagedResult getBgmByCondition(BgmDTO bgmDTO);

    /**
     * 根据ID获取BGM
     * @param id
     * @return
     */
    Bgm getBgmById(String id);

}
