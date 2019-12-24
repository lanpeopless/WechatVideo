package com.jonas.controller;

import com.jonas.dto.BgmDTO;
import com.jonas.pojo.Bgm;
import com.jonas.pojo.UserInfo;
import com.jonas.service.BgmService;
import com.jonas.utils.JSONResult;
import com.jonas.utils.PagedResult;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.apache.commons.io.IOUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.List;

/**
 * @author cmj
 * @date 2019-03-11 22:40
 */
@RestController
@RequestMapping("/bgm")
@Api(value = "BGM相关业务的接口",tags = {"用户相关业务的controller"})
public class BgmController {
    @Autowired
    private BgmService bgmService;

    @ApiOperation(value = "BGM查询接口", notes = "BGM查询接口")
    @PostMapping(value = "/queryAllBgmList",produces={"application/json;charset=UTF-8"})
    JSONResult queryAllBgmList(){
        List<Bgm> bgms = bgmService.queryBgmList();
        return JSONResult.ok(bgms);
    }

    @ApiOperation(value = "管理员上传BGM的接口", notes = "管理员上传BGM的接口")
    @PostMapping(value = "/admin/uploadBgm",produces={"application/json;charset=UTF-8"})
    JSONResult uploadBgm(@RequestParam("file") MultipartFile[] files) throws Exception{
         {

            //文件实际保存路径--绝对路径
            String filePath = "/Users/chenmingjun/开发文件夹/Video/BGM";
            //文件上传路径--相对路径
            String uploadPathDB = "/";
            FileOutputStream fileOutputStream = null;
            InputStream inputStream = null;

            try {
                if (files !=null && files.length>0){

                    String fileName = files[0].getOriginalFilename();
                    if (StringUtils.isNotBlank(fileName)){
                        //文件上传最终保存的绝对路径
                        String finalFacePath = filePath + uploadPathDB +fileName;
                        //数据库最终保存的相对路径
                        uploadPathDB += fileName;

                        File file = new File(finalFacePath);
                        if (file.getParentFile()!=null || !file.getParentFile().isDirectory()){
                            //如果父级文件夹不存在则创建文件夹
                            file.getParentFile().mkdirs();
                        }
                        fileOutputStream = new FileOutputStream(file);
                        inputStream  = files[0].getInputStream();
                        IOUtils.copy(inputStream,fileOutputStream);
                    }
                }
            } catch (IOException e) {
                e.printStackTrace();
                return JSONResult.errorMsg("上传出错");
            } finally {
                if (fileOutputStream != null){
                    fileOutputStream.flush();
                }
                fileOutputStream.close();
                inputStream.close();
            }

            return JSONResult.ok(uploadPathDB);
        }

    }

    @ApiOperation(value = "BGM入库接口", notes = "BGM入库接口")
    @PostMapping(value = "/admin/addBgm",produces={"application/json;charset=UTF-8"})
    JSONResult addBgm(@RequestBody Bgm bgm){
        System.out.println(bgm);
        bgmService.addBgm(bgm);
        return JSONResult.ok("上传成功");
    }

    @ApiOperation(value = "BGM入库接口", notes = "BGM入库接口")
    @PostMapping(value = "/admin/delBgm",produces={"application/json;charset=UTF-8"})
    JSONResult delBgm(@RequestBody List<Bgm> bgmList){
        bgmService.delBgm(bgmList);
        return JSONResult.ok("删除成功");
    }

    @ApiOperation(value = "BGM查询接口(分页+条件)", notes = "BGM查询接口(分页+条件)")
    @PostMapping(value = "/getBgmByConfition",produces={"application/json;charset=UTF-8"})
    JSONResult getBgmByConfition(@RequestBody BgmDTO bgmDTO){
        System.out.println(bgmDTO.getBgm());
        PagedResult bgmByCondition = bgmService.getBgmByCondition(bgmDTO);
        return JSONResult.ok(bgmByCondition);
    }





}
