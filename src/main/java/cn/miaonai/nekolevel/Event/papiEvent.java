package cn.miaonai.nekolevel.Event;

import cn.miaonai.nekolevel.NekoLevel;
import cn.miaonai.nekolevel.utils.Log;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import cn.miaonai.nekolevel.utils.NyaSQL;
import org.bukkit.entity.Player;

public class papiEvent {
    private final NekoLevel nekoLevel;

    public papiEvent(NekoLevel nekoLevel) {
        this.nekoLevel = nekoLevel;
    }


    public static String getPlayerEXP(Player player) {
        NekoLevel.initializeDatabase();
        try (Connection connection = NekoLevel.nyaSQL.getConnection()) {
            String query = "SELECT exp FROM nekolevel WHERE uuid = ?";
            try (PreparedStatement statement = connection.prepareStatement(query)) {
                statement.setString(1, player.getUniqueId().toString());

                try (ResultSet resultSet = statement.executeQuery()) {
                    if (resultSet.next()) {
                        return resultSet.getString("exp");
                    }
                }
            }
            NyaSQL.closeConnection();
        } catch (SQLException e) {
            Log.warning("papi调用失败!");
        }

        return null;
    }
}