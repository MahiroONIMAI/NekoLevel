package cn.miaonai.nekolevel.utils;

import cn.miaonai.nekolevel.NekoLevel;

import java.util.logging.Logger;


public class Log {
    private static Logger logger;

    public static boolean ys() {
        boolean op = false;
        if (op) {
            return false;
        } else {
            logger = NekoLevel.getInstance().getLogger();
            return true;
        }
    }

    public static void info(String msg) {
        logger.info(msg);
    }

    public static void warning(String msg) {
        logger.warning(msg);
    }
}