package com.jonas.service.impl;

import com.jonas.mapper.BgmMapper;
import com.jonas.pojo.Bgm;
import com.jonas.service.BgmService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
 /**
 * @author cmj
 * @date 2019-03-11 22:20
 */
 @Service
 public class BgmServiceImpl implements BgmService {

    @Autowired
    private BgmMapper bgmMapper;

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

 }
