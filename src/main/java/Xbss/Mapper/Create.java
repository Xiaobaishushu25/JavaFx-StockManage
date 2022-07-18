package Xbss.Mapper;

import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Param;

/**
 * @author Xbss
 * @version 1.0
 * @create 2022-07-13-21:53
 * @descirbe
 */
public interface Create {
    @Insert("CREATE TABLE ${tableName} (\n" +
            "  `name` varchar(10) DEFAULT NULL,\n" +
            "\t`date` date\t\tDEFAULT NULL ,\n" +
            "\t`open` varchar(30) DEFAULT NULL,\n" +
            "\t`close` varchar(30) DEFAULT NULL,\n" +
            "\t`high` varchar(30) DEFAULT NULL,\n" +
            "\t`low` varchar(30) DEFAULT NULL,\n" +
            "\t`vol` varchar(40) DEFAULT NULL,\n" +
            "\t`MA5` varchar(30) DEFAULT NULL,\n" +
            "\t`MA10` varchar(30) DEFAULT NULL,\n" +
            "\t`MA20` varchar(30) DEFAULT NULL,\n" +
            "\t`MA60` varchar(30) DEFAULT NULL\n" +
            ") ENGINE=InnoDB DEFAULT CHARSET=utf8;")
    void createTable(@Param("tableName") String tableName);
}
