package db;

import cn.hutool.crypto.Mode;
import cn.hutool.crypto.Padding;
import cn.hutool.crypto.symmetric.DES;
import cn.hutool.db.ds.simple.SimpleDataSource;
import service.io.FileService;

import javax.sql.DataSource;

/**
 * @author zk
 */
public class CustomDs {

    private static DES _des;

    private static String[] $customConfig;

    private static SimpleDataSource _sds;

    public static String _offset = "0CoJUm6Qyw8W8zxk";

    public static String _offset_num = "01044169";

    public static DES get_des() {
        return _des;
    }

    static {
        _des = new DES(Mode.CTS, Padding.PKCS5Padding, _offset.getBytes(), _offset_num.getBytes());

        $customConfig = FileService.readFileByLine("");
    }

    public static synchronized DataSource getCustomDS() {
        if (_sds == null) {
            _sds = new SimpleDataSource(_des.decryptStr(_des.decryptStr($customConfig[0])).replace(".{zk@yd}", ""),
                    _des.decryptStr(_des.decryptStr($customConfig[1])).replace(".{zk@yd}", ""),
                    _des.decryptStr(_des.decryptStr($customConfig[2])).replace(".{zk@yd}", ""),
                    _des.decryptStr(_des.decryptStr($customConfig[3])).replace(".{zk@yd}", ""));
        }
        return _sds;
    }
}
