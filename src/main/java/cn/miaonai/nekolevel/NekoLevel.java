package cn.miaonai.nekolevel;

import cn.miaonai.nekolevel.API.Httpserver;
import cn.miaonai.nekolevel.Event.JoinEvent;
import cn.miaonai.nekolevel.utils.Log;
import cn.miaonai.nekolevel.utils.NyaSQL;
import cn.miaonai.nekolevel.utils.configutils;
import cn.miaonai.nekolevel.utils.papi;
import org.bukkit.Bukkit;
import org.bukkit.plugin.java.JavaPlugin;
import org.eclipse.jetty.server.Server;

import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;


public  class NekoLevel extends JavaPlugin  {

    private static Server server;
    public static String key,lumsg,sound,prefix;
    public static Boolean apiServer;
    private static NekoLevel instance;
    public static int apiport,needexp;
    public static NyaSQL nyaSQL;
    private static configutils configutils;
    @Override
    public void onEnable() {
        instance = this;
        NekoLevel.initialize(this);
        Log.ys();
        Log.info("Initializing config...");
        configutils = new configutils(this);
        GetCfg();
        Log.info("Initializing database...");
        initializeDatabase();
        Log.info("Checking SQL...");
        checkSQL();
        Log.info("Initializing JettyAPI Server.....");
        if (apiServer){
            startServer();
        }else {
            Log.info("Not Enable Api ApiService");
        }
        if(Bukkit.getPluginManager().getPlugin("PlaceholderAPI") != null) {
            Log.info("Registering PlaceholderAPI...");
            new papi(this).register();
        }else {
            Log.warning("没有找到PlaceholderAPI,插件可能无法正常运行喵!");
        }
        Log.info("Initializing Events...");
        getServer().getPluginManager().registerEvents(new JoinEvent(this), this);
        Log.info("NekoLevel已加载 , Developer: MahiroNEKO(爱炒菜的真寻酱 , ver1.0)");


    }

    @Override
    public void onDisable() {
        Log.info("NekoLevel Uninstalled  Nya~!!");
        stopServer();
        try {
            NyaSQL.closeConnection();
        } catch (Exception e) {
            Log.warning("关闭数据库时发送致命错误,请检查数据库是否正常");
        }


    }
    public static NekoLevel getInstance() {
        return instance;
    }
    public static void initialize(JavaPlugin plugin) {
        if (instance == null && plugin instanceof NekoLevel) {
            instance = (NekoLevel) plugin;
        }
    }

    public  void GetCfg() {
        try {
            //apiServer
            boolean xapiServer = configutils.getBoolean("apiServer.Enable");
            String xkey = configutils.getString("apiServer.apiKey");
            int xapiport = configutils.getInt("apiServer.port");
            //levelupEvent
            String xlumsg = configutils.getString("levelupEvent.message");
            String xsound = configutils.getString("levelupEvent.sound");
            //prefix
            String xprefix = configutils.getString("levelupEvent.prefix");
            int xneedexp = configutils.getInt("needexp");

            apiServer =xapiServer;
            key =xkey;
            apiport =xapiport;
            lumsg =xlumsg;
            sound =xsound;
            prefix =xprefix;
            needexp = xneedexp;

        } catch (Exception e) {
            Log.warning("读取配置文件失败,请检查config.yml格式是否正确!!");
        }

    }

    public static void initializeDatabase() {
        // 获取数据库配置
        String host = configutils.getString("database.host");
        int port = configutils.getInt("database.port");
        String name = configutils.getString("database.name");
        String username = configutils.getString("database.username");
        String password = configutils.getString("database.password");
        // 初始化数据库管理器并建立连接
        nyaSQL = new NyaSQL(host, port, name, username, password);

    }

    public void checkSQL() {
        try {
            Connection connection = nyaSQL.getConnection();
            try (Statement statement = connection.createStatement()) {
                // 检查是否存在nekolevel表
                String checkTableQuery = "SHOW TABLES LIKE 'nekolevel'";
                if (!statement.executeQuery(checkTableQuery).next()) {
                    // 如果不存在nekolevel表则创建nekolevel表
                    String createTableQuery = "CREATE TABLE nekolevel (" +
                            "uuid VARCHAR(36) PRIMARY KEY," +
                            "exp VARCHAR(255),"+
                            "uid VARCHAR(255))";
                    statement.executeUpdate(createTableQuery);
                    Log.info("Checking SQL... nya~");
                }
            } catch (SQLException e) {
                Log.warning("Error checking/creating 'nekolevel' table Nyaaa!!!!!");
            }finally {
                // 关闭连接
                NyaSQL.closeConnection();
            }
        } catch (Exception e) {
            Log.warning("无法连接到数据库,插件无法正常运行,请检查config.yml中数据库配置是否正确");
        }

    }
    public void startServer() {
        server = new Server(apiport);
        server.setHandler(new Httpserver.ApiHandler(this));

        try {
            server.start();
            Log.info("API Server started on port " + apiport);
        } catch (Exception e) {
            Log.warning("Api服务启动失败,请检查端口是否占用?");
        }
    }

    public void stopServer() {
        try {
            if (server != null && server.isRunning()) {
                server.stop();
                Log.info("API Server stopped.");
            }
        } catch (Exception e) {
            Log.warning("在关闭Api服务时发生致命错误!");
        }
    }
}
