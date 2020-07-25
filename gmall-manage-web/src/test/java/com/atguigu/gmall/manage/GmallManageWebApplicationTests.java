package com.atguigu.gmall.manage;

import org.csource.common.MyException;
import org.csource.fastdfs.ClientGlobal;
import org.csource.fastdfs.StorageClient;
import org.csource.fastdfs.TrackerClient;
import org.csource.fastdfs.TrackerServer;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.io.IOException;

@RunWith ( SpringRunner.class )
@SpringBootTest
public class GmallManageWebApplicationTests {

    @Test
    public void contextLoads ()  throws IOException, MyException {
        //String file = this.getClass ().getResource ( "/tracker.conf" ).getFile ();

        //配置fdfs的全局连接地址
        String tracker = GmallManageWebApplicationTests.class.getResource ( "/tracker.conf" ).getPath ();
        ClientGlobal.init ( tracker );//这个进程去读取tracker的配置文件

        TrackerClient trackerClient = new TrackerClient (  );
        //获得一个traServer的实例；
        TrackerServer trackerServer = trackerClient.getConnection();
        //通过tracker获取到Stirage的客户端
        StorageClient storageClient= new StorageClient ( trackerServer,null  );
        //storageClient.append_file ( "E:/Google Dowload/ad.jpg","jpg",null );
        String[] uploadInfos =  storageClient.upload_file ( "E:/Google Dowload/ad.jpg","jpg",null );

        String url = "http://192.168.232.131";
        for (String uploadInfo: uploadInfos) {
            url += "/" + uploadInfo;
        }
        System.out.println (url );

      /*  TrackerClient trackerClient = new TrackerClient (  );
        TrackerServer trackerServer = trackerClient.getConnection();
        StorageClient storageClient = new StorageClient ( trackerServer,null );
        String orginalFilenamem ="";
        String[] upload_file = storageClient.upload_file(orginalFilename, "jpg", null);
        for (int i = 0; i <upload_file.length ; i++) {
            String s = upload_file[1];
            System.out.println ("s = "+s );

        }*/

    }

}
