package com.cc.api.utils.sys;

import cn.hutool.core.util.StrUtil;
import org.yaml.snakeyaml.Yaml;

import java.io.InputStream;
import java.util.*;

/**
 * @Description 解析ymlconfig配置文件, 非实时加载，仅加载一次做缓存
 * @Author CandyMuj
 * @Date 2019/12/31 11:42
 * @Version 1.0
 */
public class YmlConfig {

    // 主配置文件
    private static final String MAIN_FILE = "application_main.yml";

    // 用于存放
    private static final Map<String, Object> config = new HashMap<>();


    private YmlConfig() {
    }

    static {
        if (config.size() <= 0) {
            loadYml();
        }
    }


    private static List<String> loadYmlNames() {
        InputStream inputStream = YmlConfig.class.getClassLoader().getResourceAsStream(MAIN_FILE);

        return (ArrayList<String>) new Yaml().loadAs(inputStream, LinkedHashMap.class).get("ymllist");
    }

    private static void loadYml() {
        List<String> ymls = loadYmlNames();
        ymls.forEach(name -> {
            for (Object o : new Yaml().loadAll(YmlConfig.class.getClassLoader().getResourceAsStream(name))) {
                iteratorYml((Map) o, null);
            }
            // 如果是spring的配置，则根据环境获取环境配置；必须要在for下面执行，因为需要先加载application.yml我才能获取到环境配置的值
            if ("application.yml".equals(name)) {
                String active = (String) config.get("spring.profiles.active");
                if (StrUtil.isBlank(active)) {
                    return;
                }
                for (Object o : new Yaml().loadAll(YmlConfig.class.getClassLoader().getResourceAsStream("application-".concat(active).concat(".yml")))) {
                    iteratorYml((Map) o, null);
                }
            }
        });
    }

    private static void iteratorYml(Map map, String key) {
        for (Object o : map.entrySet()) {
            Map.Entry entry = (Map.Entry) o;
            Object key2 = entry.getKey();
            Object value = entry.getValue();
            if (value == null) continue;
            if (value instanceof LinkedHashMap) {
                if (key == null) {
                    iteratorYml((Map) value, key2.toString());
                } else {
                    iteratorYml((Map) value, key + "." + key2.toString());
                }
            } else if (value instanceof List) {
                if (key == null) {
                    config.put(key2.toString(), value);
                }
                if (key != null) {
                    config.put(key + "." + key2.toString(), value);
                }
            } else {
                if (key == null) {
                    config.put(key2.toString(), value.toString());
                }
                if (key != null) {
                    config.put(key + "." + key2.toString(), value.toString());
                }
            }
        }
    }

    private static Object get(String key) {
        Object val = null;

        String env = (String) config.get("spring.profiles.active");
        if (env != null) {
            val = config.get(env.concat(".").concat(key));
        }

        return val == null ? config.get(key) : val;
    }

    public static List<String> getList(String key) {
        return (ArrayList) get(key);
    }

    public static String getString(String key) {
        Object o = get(key);
        if (o == null) {
            return null;
        }

        return o.toString();
    }

    public static Integer getInteger(String key) {
        Object o = get(key);
        if (o == null) {
            return null;
        }

        return Integer.parseInt(o.toString());
    }

    public static int getIntValue(String key) {
        return Optional.ofNullable(getInteger(key)).orElse(0);
    }

    public static Long getLong(String key) {
        Object o = get(key);
        if (o == null) {
            return null;
        }

        return Long.parseLong(o.toString());
    }

    public static long getLongValue(String key) {
        return Optional.ofNullable(getLong(key)).orElse(0L);
    }

    public static Boolean getBoolean(String key) {
        Object o = get(key);
        if (o == null) {
            return null;
        }

        return Boolean.parseBoolean(o.toString());
    }

    public static boolean getBooleanValue(String key) {
        return Optional.ofNullable(getBoolean(key)).orElse(false);
    }

}
