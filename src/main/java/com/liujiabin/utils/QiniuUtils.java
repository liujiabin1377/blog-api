package com.liujiabin.utils;

import com.alibaba.fastjson.JSON;
import com.qiniu.http.Response;
import com.qiniu.storage.Configuration;
import com.qiniu.storage.Region;
import com.qiniu.storage.UploadManager;
import com.qiniu.storage.model.DefaultPutRet;
import com.qiniu.util.Auth;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

@Component
public class QiniuUtils {

    public static  final String url = "http://rcfjj2dc4.hn-bkt.clouddn.com/";

    @Value("-TZLqwiDAU2IvQLJ-O0rDQzsDNz9nrRBCDXdomhG")
    private  String accessKey;
    @Value("GYsElG4BRwdUiW_wu_PyLN952wZNrhnF50NaX8tf")
    private  String accessSecretKey;

    //参数为源文件和上传后的文件名
    public  boolean upload(MultipartFile file,String fileName){

        Configuration cfg = new Configuration(Region.huanan());  //构造一个带指定 Region 对象的配置类(修改)

        UploadManager uploadManager = new UploadManager(cfg);  //...其他参数参考类注释

        String bucket = "liujiabinblog";  //...生成上传凭证，然后准备上传(修改)

        //默认不指定key的情况下，以文件内容的hash值作为文件名
        try {
            byte[] uploadBytes = file.getBytes();
            Auth auth = Auth.create(accessKey, accessSecretKey);
            String upToken = auth.uploadToken(bucket);
                Response response = uploadManager.put(uploadBytes, fileName, upToken);
                //解析上传成功的结果
                DefaultPutRet putRet = JSON.parseObject(response.bodyString(), DefaultPutRet.class);
                return true;
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        return false;
    }
}