package service;

/**
 * @author zk
 */
public interface SqlConst {

    String[] CLOSE_TABLE_NAME = {"com_user", "com_video_award_log", "com_user_wallet_detail",
            "com_user_wallet", "com_user_vitality_log", "com_user_location", "com_user_basic_log",
            "com_user_addition_log", "com_task_user", "com_task_hold_log", "com_task_finish_user",
            "com_real_name_auth", "com_organization_user", "com_order_violation", "com_news_award_log",
            "com_user_ban_log"};

    String DEFAULT_TABLE_COLUMN = "user_id";

    String _CLOSE_CLEAR_DEL_SQL = "DELETE FROM ?1t WHERE ?2c = ?3v";

    String _CLOSE_RECOVER_UPDATE_SQL_BY_SQL = "UPDATE com_user SET `status` = 0 WHERE id in (?)";

    String _CLOSE_RECOVER_UPDATE_SQL = "UPDATE com_user SET `status` = 0 WHERE id = ?";

    String _CANCEL_RECOVER_UPDATE_SQL = "UPDATE com_user SET `agent_type` = ?1, `agent_region_id` = ?2 WHERE id = ?3";

    String _CLOSE_CLEAR_UPDATE_SQL_BY_IN = "UPDATE com_user SET `status` = 1 WHERE id in (?)";

    String _CLOSE_CLEAR_UPDATE_SQL = "UPDATE com_user SET `status` = 1 WHERE id = ?";

    String _CANCEL_CLEAR_UPDATE_SQL_BY_IN = "UPDATE com_user SET `agent_type` = 0, `agent_region_id` = 0 WHERE id in (?)";

    String _CANCEL_CLEAR_UPDATE_SQL = "UPDATE com_user SET `agent_type` = 0, `agent_region_id` = 0 WHERE id = ?";

    String _OUT_DAY_CLEAR_QUERY_SQL = "SELECT\n" +
            "\tt1.user_id 用户id\n" +
            "\t,t1.id\n" +
            "\t,t1.create_time 最后任务时间\n" +
            "\t,t2.mobile 账号\n" +
            "\t,t2.real_name 姓名\n" +
            "\t,t2.id_card 身份证\n" +
            "FROM\n" +
            "\t(SELECT \n" +
            "\t\tt1.id\n" +
            "\t\t,t1.user_id\n" +
            "\t\t,t1.create_time\n" +
            "\tFROM\n" +
            "\t\tcom_user_wallet_detail t1\n" +
            "\tWHERE\n" +
            "\t\tt1.remark = '答题奖励'\n" +
            "\t\tGROUP BY user_id \n" +
            "\t\tHAVING MAX(create_time) < DATE_SUB(CURDATE(),INTERVAL ? DAY)) t1\n" +
            "\t,com_user t2\n" +
            "WHERE\n" +
            "\tt1.user_id = t2.id";

    String _MULTIPLE_CLEAR_QUERY_SQL = "SELECT \n" +
            "\tid 用户id\n" +
            "\t,mobile 账号\n" +
            "\t,real_name 姓名\n" +
            "\t,id_card 身份证\n" +
            "\t,COUNT(mobile_lock) 相同数量\n" +
            "\t,mobile_lock 设备锁\n" +
            "FROM\n" +
            "\tcom_user t1\n" +
            "WHERE\n" +
            "\tt1.status = 0\n" +
            "\tAND t1.mobile_lock != '' \n" +
            "\tAND t1.mobile_lock IS NOT NULL\n" +
            "\tGROUP BY mobile_lock \n" +
            "\tHAVING COUNT(mobile_lock) >= ?";

    String _PARTNER_CLEAR_QUERY_SQL = "SELECT \n" +
            "\tt2.user_id 用户id\n" +
            "\t,t1.mobile 账号\n" +
            "\t,t1.real_name 姓名\n" +
            "\t,t1.id_card 身份证\n" +
            "\t,t2.basic 基础贡献值\n" +
            "\t,t1.agent_type 代理类型\n" +
            "\t,CASE t1.agent_type\n" +
            "\tWHEN 1 THEN '省'\n" +
            "\tWHEN 2 THEN '市'\n" +
            "\tWHEN 3 THEN '区'\n" +
            "\tELSE '无'\n" +
            "\tEND 代理区域\n" +
            "\t-- ,IF(t1.agent_type == 1,'省',IF(t1.agent_type == 2,'市','区')) agentName\n" +
            "\t,t1.agent_region_id 区域id\n" +
            "\t,t3.`name` 区域名称\n" +
            "FROM\n" +
            "\tcom_user t1\n" +
            "\t,com_user_wallet t2\n" +
            "\t,com_region t3\n" +
            "WHERE\n" +
            "\tt1.id = t2.user_id\n" +
            "\tAND t1.agent_type != 0\n" +
            "\tAND t2.basic < ?\n" +
            "\tAND t1.agent_region_id = t3.id";
}
