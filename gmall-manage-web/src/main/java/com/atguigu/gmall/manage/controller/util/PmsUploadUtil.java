package com.atguigu.gmall.manage.controller.util;

import org.csource.fastdfs.ClientGlobal;
import org.csource.fastdfs.StorageClient;
import org.csource.fastdfs.TrackerClient;
import org.csource.fastdfs.TrackerServer;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

public class PmsUploadUtil {

    public static String uploadImage ( MultipartFile multipartFile ) {

        String imgUrl = "http://192.168.232.131";
        //上传图片的代码
        //String file = this.getClass ().getResource ( "/tracker.conf" ).getFile ();

        //配置fdfs的全局连接地址
        String tracker = PmsUploadUtil.class.getResource ( "/tracker.conf" ).getPath ( );
        try {
            ClientGlobal.init ( tracker );//这个进程去读取tracker的配置文件
        } catch (Exception e) {
            e.printStackTrace ( );
        }

        TrackerClient trackerClient = new TrackerClient ( );
        //获得一个traServer的实例；
        TrackerServer trackerServer = null;
        try {
            trackerServer = trackerClient.getConnection ( );
        } catch (IOException e) {
            e.printStackTrace ( );
        }
        //通过tracker获取到Storage的客户端
        StorageClient storageClient = new StorageClient ( trackerServer,null );
        //storageClient.append_file ( "E:/Google Dowload/ad.jpg","jpg",null );

        try {
            //获得上传的二进制对象
            byte[] bytes = multipartFile.getBytes ( );

            //获得文件后缀名
            //这个是等于图片的名字加后缀
            String originalFilename = multipartFile.getOriginalFilename ( );
            System.out.println (originalFilename );

            //截取文件的后缀，取最后一个点
            int i = originalFilename.lastIndexOf ( "." );//lastIndexOf这个方法是获取最后一个的意思
            String extName = originalFilename.substring (i);

            //注意这一点，
            String[] uploadInfos = storageClient.upload_file ( bytes,extName,null );

            //这个以后要建一个地址类
            for (String uploadInfo : uploadInfos) {
                imgUrl += "/" + uploadInfo;
            }

        } catch (Exception e) {
            e.printStackTrace ( );
        }

        return imgUrl;
    }
}
