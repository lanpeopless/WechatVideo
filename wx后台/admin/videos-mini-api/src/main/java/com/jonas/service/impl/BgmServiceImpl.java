package com.jonas.service.impl;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.jonas.config.ZKCurator;
import com.jonas.config.ZKCuratorClient;
import com.jonas.controller.BasicController;
import com.jonas.dto.BgmDTO;
import com.jonas.enums.BGMOperatorTypeEnum;
import com.jonas.mapper.BgmMapper;
import com.jonas.pojo.Bgm;
import com.jonas.service.BgmService;
import com.jonas.utils.PagedResult;
import org.apache.commons.io.FileUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.io.File;
import java.io.IOException;
import java.util.List;

/**
 * @author cmj
 * @date 2019-03-11 22:20
 */
 @Service
 public class BgmServiceImpl implements BgmService {

    @Autowired
    private BgmMapper bgmMapper;
    @Autowired
    private ZKCuratorClient zkCuratorClient;
    @Autowired
    private BasicController basicController;

    @Transactional(propagation = Propagation.SUPPORTS)

    @Override
    public List<Bgm> queryBgmList() {
        List<Bgm> bgms = bgmMapper.selectAll();
        return bgms;
    }

     @Override
     public Bgm queryBgmById(String id) {
        Bgm bgm=null;
        if (StringUtils.isNotBlank(id)){
            bgm = bgmMapper.selectByPrimaryKey(id);
        }
         return bgm;
     }

     @Override
     public void addBgm(Bgm bgm) {
         String bgmId = bgmMapper.getBgmId();
         bgm.setId(bgmId);
         bgmMapper.insert(bgm);
//         zkCuratorClient.sendBgmOperator(bgmId, BGMOperatorTypeEnum.ADD.type);
     }

     @Override
     public void delBgm(List<Bgm> bgmList) {
        for (Bgm bgm:bgmList){
            if (StringUtils.isNotBlank(bgm.getId())){
                String path = bgmMapper.getBgmById(bgm.getId()).getPath();
                bgmMapper.deleteByPrimaryKey(bgm.getId());
//                zkCuratorClient.sendBgmOperator(bgm.getId(),
//                        BGMOperatorTypeEnum.DELETE.type);
                File file = new File(basicController.BGM_FILE_PATH + path);
                try {
                    FileUtils.forceDelete(file);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
     }

    @Override
    public PagedResult getBgmByCondition(BgmDTO bgmDTO) {
        Integer page = bgmDTO.getPage();
        Integer pageSize = bgmDTO.getPageSize();
        if (page == null){
            page = 1;
        }if (pageSize == null){
            pageSize = 10;
        }
        PageHelper.startPage(page,pageSize);
        List<Bgm> bgmByCondition = bgmMapper.getBgmByCondition(bgmDTO.getBgm());
        PageInfo<Bgm> videoVOList = new PageInfo<>(bgmByCondition);
        PagedResult pagedResult = new PagedResult();
        pagedResult.setPage(page);
        pagedResult.setRecords(videoVOList.getTotal());
        pagedResult.setRows(videoVOList.getList());
        pagedResult.setTotal(videoVOList.getPages());
        return pagedResult;
    }

    @Override
    public Bgm getBgmById(String id) {
        Bgm bgmById=null;
        if (StringUtils.isNotBlank(id)){
            bgmById = bgmMapper.getBgmById(id);
        }
        return bgmById;
    }

}
