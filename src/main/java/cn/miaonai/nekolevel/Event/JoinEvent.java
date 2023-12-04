package cn.miaonai.nekolevel.Event;

import cn.miaonai.nekolevel.NekoLevel;
import cn.miaonai.nekolevel.utils.Log;
import cn.miaonai.nekolevel.utils.NyaSQL;
import cn.miaonai.nekolevel.utils.RandomUID;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;

import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.UUID;

public class JoinEvent implements Listener {
    private final NekoLevel nekoLevel;

    public JoinEvent(NekoLevel nekoLevel) {
        this.nekoLevel = nekoLevel;
    }

    @EventHandler
    public void onPlayerJoin(PlayerJoinEvent event) {
        Player player = event.getPlayer();
        UUID playerUUID = player.getUniqueId();
        NekoLevel.initializeDatabase();
        addPlayer(playerUUID);
    }

    public void addPlayer(UUID playerUUID) {
        try {
            Connection connection = NekoLevel.nyaSQL.getConnection();
            try (Statement statement = connection.createStatement()) {
                String infoQuery = "SELECT uuid FROM nekolevel WHERE uuid = '" + playerUUID + "'";
                if (!statement.executeQuery(infoQuery).next()) {
                    String createInfo = "INSERT INTO nekolevel (uuid, exp, uid) VALUES ('" + playerUUID + "', 0, '" + RandomUID.RandomID() + "')";
                    statement.executeUpdate(createInfo);
                }
            } catch (SQLException e) {
                e.printStackTrace();
                Log.warning("Error creating PlayerInfo!!!");
            } finally {
                NyaSQL.closeConnection();
            }
        } catch (Exception e) {
            Log.warning("无法连接到数据库,插件无法正常运行,请检查config.yml中数据库配置是否正确!!");
        }
    }
}
