package com.cc.api.config;

import com.cc.api.utils.sys.YmlConfig;

/**
 * @Description
 * @Author CandyMuj
 * @Date 2019/12/24 17:13
 * @Version 1.0
 */
public class Configc {

    /**
     * 上方是关于系统或全局的配置 （若只需要框架，此处的配置不建议删除，建议只修改，视具体情况而定）
     */

    // 系统全局编码
    public static final String GLOBAL_ENCODING = YmlConfig.getString(YmlKey.GLOBAL_ENCODING);
    // 是否开启swagger
    public static final boolean GLOBAL_SWAGGER_OPEN = YmlConfig.getBooleanValue(YmlKey.GLOBAL_SWAGGER_OPEN);

    // 全局统一针对日期的处理 默认日期格式定义
    public static final String DEFAULT_DATEFORMAT = "yyyy-MM-dd HH:mm:ss";


    /**
     * 下方写关于自己业务的一些配置，避免建过多的重复文件，造成冗余 （若只需要框架，此处的配置可全部删除，可视具体情况保留需要的）
     */
    // 文件上传到服务器的根路径(后面不要跟斜杠，我在生成文件名的时候加的有不然就重复了不好的）
    public static final String UPLOAD_FILE_PATH = "/upload";

}
