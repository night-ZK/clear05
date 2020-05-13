package service;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.db.Db;
import cn.hutool.db.Entity;
import db.CustomDs;
import service.io.FileService;

import java.io.File;
import java.io.IOException;
import java.sql.SQLException;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @author zk
 */
public class ClearService {

    public static boolean delUseData(List userList) throws SQLException, IOException {
        List<String> sqlList = CollUtil.newArrayList();
//        List<String> sqlList = new ArrayList<>(65535);
        String tableStr = FileService.readFile("clear.tab");
        String[] tableNames = tableStr.split(",");
        Arrays.stream(tableNames).distinct().forEach(t -> {
            String[] tcs = t.split(":");
            String tn = tcs[0];
            String tc = SqlConst.DEFAULT_TABLE_COLUMN;
            if (tcs.length >= 2) {
                tc = tcs[1];
            }
            String finalTc = tc;
            userList.stream().forEach(id -> sqlList.add(SqlConst._CLOSE_CLEAR_DEL_SQL
                    .replace("?1t", tn)
                    .replace("?2c", finalTc)
                    .replace("?3v", id + "")));
        });

        FileService.writeSql("run-" + Thread.currentThread().getId(), sqlList);

//        FileService.writeMultipleFileForSql("run-" + Thread.currentThread().getId(), sqlList);

        int executeSum = Db.use(CustomDs.getCustomDS()).executeBatch(sqlList.toArray(new String[sqlList.size()])).length;
        if (executeSum < sqlList.size()) {
            throw new RuntimeException("有 " + (sqlList.size() - executeSum) + " 条数据删除失败");
        }
        return true;
    }


    public static boolean recoverData(List list) throws SQLException {
        List<String> sqlList = CollUtil.newArrayList();
        list.stream().forEach( id -> sqlList.add(SqlConst._CLOSE_RECOVER_UPDATE_SQL.replace("?", id + "")));
        int executeSum = Db.use(CustomDs.getCustomDS()).executeBatch(sqlList.toArray(new String[sqlList.size()])).length;
        if (executeSum < sqlList.size()) {
            throw new RuntimeException("有 " + (sqlList.size() - executeSum) + " 条数据恢复失败");
        }
        return true;
    }

    public static boolean recoverPartner(List<List> list) throws SQLException {
        List<String> sqlList = CollUtil.newArrayList();
        list.stream().forEach(row -> {
            sqlList.add(SqlConst._CANCEL_RECOVER_UPDATE_SQL
                    .replace("?1", row.get(5) + "")
                    .replace("?2", row.get(7) + "")
                    .replace("?3", row.get(0) + ""));
        });
        System.out.println("sqlList: " + sqlList);
        int executeSum = Db.use(CustomDs.getCustomDS()).executeBatch(sqlList.toArray(new String[sqlList.size()])).length;
        if (executeSum < sqlList.size()) {
            throw new RuntimeException("有 " + (sqlList.size() - executeSum) + " 条数据恢复失败");
        }
        return true;
    }

    public static boolean clearData(List list) throws SQLException {
        List<String> sqlList = CollUtil.newArrayList();
        list.stream().forEach( id -> sqlList.add(SqlConst._CLOSE_CLEAR_UPDATE_SQL.replace("?", id + "")));
        int executeSum = Db.use(CustomDs.getCustomDS()).executeBatch(sqlList.toArray(new String[sqlList.size()])).length;
        if (executeSum < sqlList.size()) {
            throw new RuntimeException("有 " + (sqlList.size() - executeSum) + " 条数据封号失败");
        }
        return true;
    }

    public static boolean cancelPartner(List list) throws SQLException {
        List<String> sqlList = CollUtil.newArrayList();
        list.stream().forEach( id -> sqlList.add(SqlConst._CANCEL_CLEAR_UPDATE_SQL.replace("?", id + "")));
        int executeSum = Db.use(CustomDs.getCustomDS()).executeBatch(sqlList.toArray(new String[sqlList.size()])).length;
        if (executeSum < sqlList.size()) {
            throw new RuntimeException("有 " + (sqlList.size() - executeSum) + " 条数据清除合伙人失败");
        }
        return true;
    }

    public static List selectOutDayData(Integer day) throws SQLException {
        List<Entity> result = Db.use(CustomDs.getCustomDS()).query(SqlConst._OUT_DAY_CLEAR_QUERY_SQL, day);
        List<List> resultValues = result.stream()
                .map(entity -> CollUtil.newArrayList(entity.values()))
                .collect(Collectors.toList());
        resultValues.add(0, CollUtil.newArrayList(result.get(0).getFieldNames()));
        return resultValues;
    }

    public static List selectMultipleData(Integer sum) throws SQLException {
        List<Entity> result = Db.use(CustomDs.getCustomDS()).query(SqlConst._MULTIPLE_CLEAR_QUERY_SQL, sum);
        List<List> resultValues = result.stream()
                .map(entity -> CollUtil.newArrayList(entity.values()))
                .collect(Collectors.toList());
        resultValues.add(0, CollUtil.newArrayList(result.get(0).getFieldNames()));
        return resultValues;
    }

    public static List selectPartnerData(Double basic) throws SQLException {
        List<Entity> result = Db.use(CustomDs.getCustomDS()).query(SqlConst._PARTNER_CLEAR_QUERY_SQL, basic);
        List<List> resultValues = result.stream()
                .map(entity -> CollUtil.newArrayList(entity.values()))
                .collect(Collectors.toList());
        resultValues.add(0, CollUtil.newArrayList(result.get(0).getFieldNames()));
        return resultValues;
    }
}
