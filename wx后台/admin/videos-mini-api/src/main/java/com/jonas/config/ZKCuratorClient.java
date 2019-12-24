package com.jonas.config;

import com.jonas.dto.BgmDTO;
import com.jonas.pojo.Bgm;
import com.jonas.service.BgmService;
import com.jonas.utils.PagedResult;
import org.apache.commons.io.FileUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.curator.RetryPolicy;
import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.framework.CuratorFrameworkFactory;
import org.apache.curator.framework.recipes.cache.PathChildrenCache;
import org.apache.curator.framework.recipes.cache.PathChildrenCacheEvent;
import org.apache.curator.framework.recipes.cache.PathChildrenCacheListener;
import org.apache.curator.retry.ExponentialBackoffRetry;
import org.apache.zookeeper.CreateMode;
import org.apache.zookeeper.ZooDefs;
import org.aspectj.util.FileUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import java.io.File;
import java.net.URL;
import java.net.URLEncoder;

/**
 * @author cmj
 * @date 2019-04-14 16:41
 */
public class ZKCuratorClient {

    @Autowired
    private BgmService bgmService;

    private CuratorFramework client = null; // zk客户端
    final static Logger log = LoggerFactory.getLogger(ZKCuratorClient.class);
    public static final String zookeeper_server = "47.102.223.176:2181";
    public void init() {
        // 创建zk客户端，如果存在则不需要再次创建
        if (client != null)
            return;
        RetryPolicy retryPolicy = new ExponentialBackoffRetry(1000, 3);
        client = CuratorFrameworkFactory.builder().connectString(zookeeper_server).sessionTimeoutMs(10000)
                .retryPolicy(retryPolicy).namespace("admin").build();
        // 启动客户端
        client.start();
        // 使用命名空间
        client = client.usingNamespace("admin");
        // 启动完毕之后监听bgm路径
        try {
            addChildWatcher("/bgm");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * @Description: 增加或者删除bgm，向zk-server创建子节点，供小程序后端监听
     */
    public void sendBgmOperator(String bgmId, String operObj) {
        try {

            client.create().creatingParentsIfNeeded()
                    .withMode(CreateMode.PERSISTENT)		// 节点类型：持久节点
                    .withACL(ZooDefs.Ids.OPEN_ACL_UNSAFE)			// acl：匿名权限
                    .forPath("/bgm/" + bgmId, operObj.getBytes());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void addChildWatcher(String nodePath) throws Exception {
        final PathChildrenCache cache = new PathChildrenCache(client, nodePath, true);
        cache.start(PathChildrenCache.StartMode.BUILD_INITIAL_CACHE);
        cache.getListenable().addListener(new PathChildrenCacheListener() {
            public void childEvent(CuratorFramework client, PathChildrenCacheEvent event) throws Exception {
                // 1.从数据库获取查询BGM对象，获取路径Path
                if (event.getType().equals(PathChildrenCacheEvent.Type.CHILD_ADDED)){
                    log.info("监听到 CHILD_ADDED事件");
                    String path = event.getData().getPath();
                    String operatorType = new String(event.getData().getData());

                    String[] split = path.split("/");
                    String bgmId = split[split.length-1];
                    Bgm bgm = bgmService.getBgmById(bgmId);
                    if (bgm==null){
                        return;
                    }
                    //获取BGM所在路径
                    String songPath = bgm.getPath();
                    // 2.定义保存到本地的bgm路径
                    String filePath = "/Users/chenmingjun/开发文件夹/Video/AdminBgm" + songPath;
                    // 3.定义下载路径（播放url）
                    System.out.println(songPath);
                    String[] arrPath = songPath.split("\\\\");
                    String finalPath = "/Users/chenmingjun/开发文件夹/Video/Bgm";
                    // 处理 url斜杠以及编码
                    for (int i=0;i<arrPath.length;i++){
                        if (StringUtils.isNotBlank(arrPath[i])){
                            filePath+="/";
                            filePath+= URLEncoder.encode(arrPath[i],"UTF-8");
                        }
                    }
                    String bgmUrl = "http://127.0.0.1:8081"+finalPath;

                    URL url = new URL(bgmUrl);
                    File file = new File(filePath);
                    FileUtils.copyURLToFile(url,file);


                }

                if (event.getType().equals(PathChildrenCacheEvent.Type.INITIALIZED)) {
                    System.out.println(111);
                } else if (event.getType().equals(PathChildrenCacheEvent.Type.CHILD_ADDED)) {
                    System.out.println(222);
                } else if (event.getType().equals(PathChildrenCacheEvent.Type.CHILD_REMOVED)) {
                    System.out.println(333);
                } else if (event.getType().equals(PathChildrenCacheEvent.Type.CHILD_UPDATED)) {
                    System.out.println(444);
                }
            }
        });
    }


    public void addChildWatcher2(String path) throws Exception {
        final PathChildrenCache cache = new PathChildrenCache(client, path, true);
        cache.start();
        cache.getListenable().addListener(new PathChildrenCacheListener() {
            public void childEvent(CuratorFramework client, PathChildrenCacheEvent event) throws Exception {
                if (event.getType().equals(PathChildrenCacheEvent.Type.CHILD_ADDED)) {

                }
            }
        });
    }

}

