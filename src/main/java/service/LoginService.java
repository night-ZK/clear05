package service;

import cn.hutool.db.Db;
import cn.hutool.db.Entity;
import db.CustomDs;

import java.sql.SQLException;
import java.util.List;

/**
 * @author zk
 */
public class LoginService {
    public static boolean login(String account, String password) throws SQLException {
        List users = Db.use(CustomDs.getCustomDS()).findAll(Entity.create("blade_user").set("account", account).set("real_name", password));
        if (users.size() <= 0) {
            throw new RuntimeException("登录失败");
        }
        return true;
    }
}
