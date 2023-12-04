package cn.miaonai.nekolevel.API;

import cn.miaonai.nekolevel.NekoLevel;
import cn.miaonai.nekolevel.utils.Log;
import cn.miaonai.nekolevel.utils.NyaSQL;
import com.google.gson.Gson;
import org.eclipse.jetty.server.handler.AbstractHandler;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class Httpserver {


    public static class ApiHandler extends AbstractHandler {
        private NekoLevel httpserver;

        public ApiHandler(NekoLevel httpserver) {
            this.httpserver = httpserver;
        }

        String uuid,uid;
        int exp;

        @Override
        public void handle(String target, org.eclipse.jetty.server.Request baseRequest, HttpServletRequest request, HttpServletResponse response)
                throws IOException {

            response.setContentType("application/json;charset=utf-8");
            String userKey = request.getParameter("key");

            if (!Httpserver.isValidKey(userKey)) {
                response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
                String errorMessage = "{ \"error\": \"Invalid key\" }";
                response.getWriter().write(errorMessage);
                baseRequest.setHandled(true);
                return;
            }
            String path = request.getRequestURI().toLowerCase();
            String[] pathSegments = path.split("/");
            String msg = "";
            if (pathSegments.length == 4 && pathSegments[1].equals("api") && pathSegments[2].equals("v1")) {
                String str1 = pathSegments[3];
                String Qkey = IsUUIDorUID(str1);
                NekoLevel.initializeDatabase();
                try (Connection connection = NekoLevel.nyaSQL.getConnection()) {
                    String query = "SELECT * FROM nekolevel WHERE "+Qkey+"= ?";
                    try (PreparedStatement statement = connection.prepareStatement(query)) {
                        statement.setString(1, str1);
                        try (ResultSet resultSet = statement.executeQuery()) {
                            if (resultSet.next()) {
                                int qexp =  resultSet.getInt("exp");
                                String quuid = resultSet.getNString("uuid");
                                String quid = resultSet.getNString("uid");
                                exp = qexp;
                                uuid = quuid;
                                uid = quid;
                            }
                        }
                        NyaSQL.closeConnection();
                    }
                } catch (SQLException e) {
                    Log.warning("API查询数据库时出错!!");
                }

                msg = "{ \"msg\": \"ok!\", \"uuid\": "+uuid+", \"uid\": \""+uid+"\", \"exp\": "+exp+" }";
            } else {
                msg = "{ \"error\": \"Invalid path\" }";
            }


            String jsonData = new Gson().toJson(msg);

            response.setStatus(HttpServletResponse.SC_OK);

            PrintWriter writer = response.getWriter();
            writer.write(jsonData);
            writer.close();

            baseRequest.setHandled(true);
        }
    }

    public static boolean isValidKey(String userKey) {
        return userKey != null && userKey.equals(NekoLevel.key);
    }

    public static String IsUUIDorUID(String chaar){
        int leg = chaar.length();
        if (leg <=8) {
             return "uid";
        }else {
            return "uuid";
        }
    }

}