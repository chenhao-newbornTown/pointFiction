package com.point.util;

import com.point.constant.Constant;
import com.point.entity.FictionActorBean;
import com.point.entity.FictionBean;
import com.point.entity.FictionDetailBean;
import com.point.entity.PicBean;
import com.point.mongo.FictionRepository;
import com.point.mongo.PicRepostitory;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import java.io.*;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Random;

/**
 * Created by hadoop on 2017-7-20.
 */
public class PublicUtil {

    protected static Logger logger = LoggerFactory.getLogger(PublicUtil.class);
    // 全局数组
    private final static String[] strDigits = {"0", "1", "2", "3", "4", "5", "6", "7", "8", "9", "a", "b", "c", "d", "e", "f"};

    // 返回形式为数字跟字符串
    private static String byteToArrayString(byte bByte) {
        int iRet = bByte;
        if (iRet < 0) {
            iRet += 256;
        }
        int iD1 = iRet / 16;
        int iD2 = iRet % 16;
        return strDigits[iD1] + strDigits[iD2];
    }

    // 转换字节数组为16进制字串
    private static String byteToString(byte[] bByte) {
        StringBuffer sBuffer = new StringBuffer();
        for (int i = 0; i < bByte.length; i++) {
            sBuffer.append(byteToArrayString(bByte[i]));
        }
        return sBuffer.toString();
    }

    public static String GetMD5Code(String strObj) {
        String resultString = null;
        try {
            resultString = new String(strObj);
            MessageDigest md = MessageDigest.getInstance("MD5");
            // md.digest() 该函数返回值为存放哈希值结果的byte数组
            resultString = byteToString(md.digest(strObj.getBytes()));
        } catch (NoSuchAlgorithmException ex) {
            ex.printStackTrace();
        }
        return resultString;
    }

    /**
     * 将字符串前后加上随机数（5以内）个空格（用于AES加密）
     *
     * @param str
     * @return String
     */
    public static String getAddSpaceStr(String str) {

        Random random = new Random();
        int i = random.nextInt(5) + 1;
        int j = random.nextInt(5) + 1;
        StringBuffer FrontTemp = new StringBuffer("");
        StringBuffer TailTemp = new StringBuffer("");
        for (int t = 0; t < i; t++) {
            FrontTemp.append(" ");
        }
        for (int t1 = 0; t1 < j; t1++) {
            TailTemp.append(" ");
        }
        return FrontTemp.toString() + str + TailTemp.toString();
    }

    /**
     * <B>方法名称：</B>makeMD5<BR>
     * <B>概要说明：</B>将密码进行MD5加密<BR>
     *
     * @param password 传入密码
     * @return 经过MD5加密的密码
     */
    public static String makeMD5(String password) {
        return GetMD5Code(password).toUpperCase();
    }


    public static PicBean saveFile(MultipartFile file) {

        PicBean picBean = new PicBean();
        // 判断文件是否为空
        try {
            // 转存文件
            int random = new Random().nextInt(1000);
            String filename = String.valueOf(System.currentTimeMillis() + random) + ".jpg";
            file.transferTo(new File(Constant.PicPath + File.separator + filename));
            picBean.setPic_name(filename);
            picBean.setPic_upload_time(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date()));
        } catch (Exception e) {
            picBean = null;
            e.printStackTrace();
        }
        return picBean;
    }


    public static PicBean uploadPic(MultipartFile file) {

        BufferedOutputStream stream = null;

        PicBean picBean = new PicBean();
        try {
            byte[] bytes = file.getBytes();
            int random = new Random().nextInt(1000);
            String filename = String.valueOf(System.currentTimeMillis() + random) + file.getOriginalFilename().substring(file.getOriginalFilename().lastIndexOf("."));
            stream = new BufferedOutputStream(new FileOutputStream(new File(Constant.PicPath + File.separator + filename)));
            stream.write(bytes);
            stream.close();


            picBean.setPic_name(filename);
            picBean.setPic_upload_time(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date()));
        } catch (Exception e) {
            stream = null;
            picBean = null;
        }

        return picBean;
    }

    public void uploadXlsx(MultipartFile mfile, String fiction_name, String fiction_author_name, String fiction_pic_path, FictionRepository fictionRepository, MongoTemplate mongoTemplate, String status, String fiction_id) {

        File uploadDir = new File(Constant.PicPath);
        //创建一个目录 （它的路径名由当前 File 对象指定，包括任一必须的父路径。）
        if (!uploadDir.exists()) uploadDir.mkdirs();
        //新建一个文件
        File tempFile = new File(Constant.PicPath + File.separator + fiction_name + ".xlsx");
        //初始化输入流
        InputStream is = null;
        try {
            //将上传的文件写入新建的文件中
            mfile.transferTo(tempFile);

            //根据新建的文件实例化输入流
            is = new FileInputStream(tempFile);

            //根据版本选择创建Workbook的方式
            Workbook wb = null;
            //根据文件名判断文件是2003版本还是2007版本
            wb = new XSSFWorkbook(is);
            if (status.equals("add")) {
                readExcelValue(wb, tempFile, fiction_name, fiction_author_name, fiction_pic_path, fictionRepository, mongoTemplate);
            } else if (status.equals("update")) {
                readupdateExcelValue(wb, tempFile, mongoTemplate, Long.parseLong(fiction_id), fiction_name, fiction_author_name);
            }

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (is != null) {
                try {
                    is.close();
                } catch (IOException e) {
                    is = null;
                    e.printStackTrace();
                }
            }
        }
    }

    private void readupdateExcelValue(Workbook wb, File tempFile, MongoTemplate mongoTemplate, Long fiction_id, String fiction_name, String fiction_author_name) {

        Sheet sheet = wb.getSheetAt(0);

        List<FictionDetailBean> fictionDetailBeanList = new ArrayList<FictionDetailBean>();
        List<FictionActorBean> fictionActorBeanList = new ArrayList<FictionActorBean>();

        List<String> actor_name_list = new ArrayList<String>();

        for (int i = 1; i < sheet.getLastRowNum() + 1; i++) {

            FictionDetailBean fictionDetailBean = new FictionDetailBean();

            Row row = sheet.getRow(i);
            String actor_name = row.getCell(0).getStringCellValue();
            String actor_fiction_detail = row.getCell(1).getStringCellValue();

            if (!actor_name_list.contains(actor_name) && !actor_name.equals("旁白")) {
                actor_name_list.add(actor_name);
                FictionActorBean fictionActorBean = new FictionActorBean();
                fictionActorBean.setFiction_id(fiction_id);
                fictionActorBean.setFiction_actor_id(PublicUtil.makeMD5(String.valueOf(actor_name)));
                fictionActorBean.setFiction_actor_name(actor_name);

                fictionActorBeanList.add(fictionActorBean);
            }

            fictionDetailBean.setFiction_id(fiction_id);
            fictionDetailBean.setActor_fiction_detail(actor_fiction_detail);
            fictionDetailBean.setActor_id(PublicUtil.makeMD5(String.valueOf(actor_name)));
            fictionDetailBean.setActor_name(actor_name);
            fictionDetailBean.setActor_fiction_detail_index(i);
            fictionDetailBean.setFiction_detail_status(1);

            fictionDetailBeanList.add(fictionDetailBean);
        }

        mongoTemplate.insert(fictionDetailBeanList, FictionDetailBean.class);
        mongoTemplate.insert(fictionActorBeanList, FictionActorBean.class);
        mongoTemplate.updateFirst(new Query(Criteria.where("fiction_id").is(fiction_id)), Update.update("fiction_name", fiction_name).set("fiction_author_name", fiction_author_name).set("update_time", String.valueOf(System.currentTimeMillis())).set("update_date", new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date())), FictionBean.class);

        if (tempFile.exists()) {
            tempFile.delete();
        }

    }


    /**
     * 解析Excel里面的数据
     *
     * @param wb
     * @return
     */
    private void readExcelValue(Workbook wb, File tempFile, String fiction_name, String fiction_author_name, String fiction_pic_path, FictionRepository fictionRepository, MongoTemplate mongoTemplate) {

        Sheet sheet = wb.getSheetAt(0);

        List<FictionDetailBean> fictionDetailBeanList = new ArrayList<FictionDetailBean>();
        List<FictionActorBean> fictionActorBeanList = new ArrayList<FictionActorBean>();

        List<String> actor_name_list = new ArrayList<String>();

        FictionBean fictionBean = new FictionBean();

        fictionBean.setFiction_name(fiction_name);
        fictionBean.setFiction_author_id("888888");
        fictionBean.setFiction_author_name(fiction_author_name);
        fictionBean.setUpdate_time(String.valueOf(System.currentTimeMillis()));
        fictionBean.setUpdate_date(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date()));
        fictionBean.setFiction_pic_path(fiction_pic_path);
        fictionBean.setFiction_status(1);
        fictionBean.setFiction_line_num((long) sheet.getLastRowNum());
        fictionBean.setStatus(Constant.FictionStatusDefault);
        fictionBean.setUser_like_count_status("0");

        FictionBean fictionMongoBean = fictionRepository.save(fictionBean);

        long fiction_id = fictionMongoBean.getFiction_id();

        for (int i = 1; i < sheet.getLastRowNum() + 1; i++) {

            FictionDetailBean fictionDetailBean = new FictionDetailBean();

            Row row = sheet.getRow(i);
            String actor_name = row.getCell(0).getStringCellValue();
            String actor_fiction_detail = row.getCell(1).getStringCellValue();

            if (!actor_name_list.contains(actor_name) && !actor_name.equals("旁白")) {
                actor_name_list.add(actor_name);
                FictionActorBean fictionActorBean = new FictionActorBean();
                fictionActorBean.setFiction_id(fiction_id);
                fictionActorBean.setFiction_actor_id(PublicUtil.makeMD5(String.valueOf(actor_name)));
                fictionActorBean.setFiction_actor_name(actor_name);

                fictionActorBeanList.add(fictionActorBean);
            }

            fictionDetailBean.setFiction_id(fiction_id);
            fictionDetailBean.setActor_fiction_detail(actor_fiction_detail);
            fictionDetailBean.setActor_id(PublicUtil.makeMD5(String.valueOf(actor_name)));
            fictionDetailBean.setActor_name(actor_name);
            fictionDetailBean.setActor_fiction_detail_index(i);
            fictionDetailBean.setFiction_detail_status(1);

            fictionDetailBeanList.add(fictionDetailBean);
        }

        mongoTemplate.insert(fictionDetailBeanList, FictionDetailBean.class);
        mongoTemplate.insert(fictionActorBeanList, FictionActorBean.class);

        if (tempFile.exists()) {
            tempFile.delete();
        }

    }

    public final static String getIpAddress(HttpServletRequest request) throws IOException {
        // 获取请求主机IP地址,如果通过代理进来，则透过防火墙获取真实IP地址

        String ip = request.getHeader("X-Forwarded-For");
        if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
            if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
                ip = request.getHeader("Proxy-Client-IP");
            }
            if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
                ip = request.getHeader("WL-Proxy-Client-IP");
            }
            if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
                ip = request.getHeader("HTTP_CLIENT_IP");
            }
            if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
                ip = request.getHeader("HTTP_X_FORWARDED_FOR");
            }
            if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
                ip = request.getRemoteAddr();
            }
        } else if (ip.length() > 15) {
            String[] ips = ip.split(",");
            for (int index = 0; index < ips.length; index++) {
                String strIp = (String) ips[index];
                if (!("unknown".equalsIgnoreCase(strIp))) {
                    ip = strIp;
                    break;
                }
            }
        }
        return ip;
    }

}
