package cn.miaonai.nekolevel.utils;

import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.plugin.java.JavaPlugin;
public class configutils {
        private JavaPlugin plugin;
        private FileConfiguration config;

        public configutils(JavaPlugin plugin) {
                this.plugin = plugin;
                loadConfig();
        }

        private void loadConfig() {
                // 如果配置文件不存在，则保存默认配置文件
                plugin.saveDefaultConfig();

                // 获取配置文件实例
                config = plugin.getConfig();
        }

        public String getString(String path) {
                return config.getString(path);
        }

        public int getInt(String path) {
                return config.getInt(path);
        }
        public boolean getBoolean(String path) {
                return config.getBoolean(path);
        }


}
