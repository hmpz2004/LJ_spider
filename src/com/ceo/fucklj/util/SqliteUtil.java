package com.ceo.fucklj.util;

import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;

public class SqliteUtil {

    public static final String CREATE_BIZCYCLE_TABLE =
            "CREATE TABLE if not exists `biz_cycle` "
            +"(`id_` int(11) PRIMARY KEY autoincrement ,"
            +"`` varchar(50) NOT NULL,"
            +"`` varchar(50) NOT NULL,"
            +"`` varchar(50) NOT NULL,"
            +"`` varchar(50) NOT NULL,"
            +"`` varchar(50) NOT NULL,"
            +"`` varchar(50) NOT NULL,"
            +"`CAVCASongID` varchar(50) NOT NULL,"
            +"`SongName` varchar(256) NOT NULL,"
            +"`SongNameShort` varchar(50) NOT NULL,"
            +"`SongAddTimeSec` bigint(20) NOT NULL default '0',"
            +"`SongIsRecommend` smallint(6) NOT NULL,"
            +"`SongSingerID` int(11) NOT NULL,"
            +"`SongZoneID` int(11) NOT NULL,"
            +"`SongThemeID` int(11) NOT NULL,"
            +"`SongSingStyleID` int(11) NOT NULL,"
            +"`SongInLocal` smallint(6) NOT NULL default '0',"
            +"`SongCodeValue` int(11) NOT NULL,"
            +"`SongGroupID` int(11) NOT NULL, "
            +"`ServerGroupID` int(11) NOT NULL,"
            +"`SongFileName` varchar(256) NOT NULL default 'file',"
            +"`SongChecked` int(11) NOT NULL default '0');";

    public static final String CREATE_BIZ_CYCLE_XIAOQU_TABLE =
            "CREATE TABLE if not exists `biz_cycle_xiaoqu` "
                    +"(`id_` integer PRIMARY KEY autoincrement ,"
                    +"`community_id` bigint(20) NOT NULL,"
                    +"`community_name` varchar(50) NOT NULL,"
                    +"`cover_pic` varchar(512) NOT NULL,"
                    +"`district_name` varchar(50) NOT NULL,"
                    +"`bizcircle_name` varchar(50) NOT NULL,"
                    +"`building_finish_year` int(10) NOT NULL,"
                    +"`building_type` varchar(50) NOT NULL,"
                    +"`ershoufang_source_count` int(10) NOT NULL,"
                    +"`ershoufang_avg_unit_price` int(11) NOT NULL,"
                    +"`schema` varchar(256) NOT NULL"
                    +");";

    public static final String[] CREATE_BIZ_CYCLE_XIAOQU_TABLE_COLS_ARRAY = {
            "id_",
            "community_id",
            "community_name",
            "cover_pic",
            "district_name",
            "bizcircle_name",
            "building_finish_year",
            "building_type",
            "ershoufang_source_count",
            "ershoufang_avg_unit_price",
            "schema"
    };

    public static final String CREATE_BIZ_CYCLE_CHENGJIAO_TABLE =
            "CREATE TABLE if not exists `biz_cycle_chengjiao` "
                    +"(`id_` integer PRIMARY KEY autoincrement ,"
                    +"`bizcircle_id` bigint(20) NOT NULL,"
                    +"`community_id` bigint(20) NOT NULL,"
                    +"`house_code` bigint(20) NOT NULL,"
                    +"`title` varchar(50) NOT NULL,"
                    +"`kv_house_type` varchar(50) NOT NULL,"
                    +"`cover_pic` varchar(512) NOT NULL,"
                    +"`frame_id` bigint(20) NOT NULL,"
                    +"`blueprint_hall_num` int(2) NOT NULL,"
                    +"`blueprint_bedroom_num` int(2) NOT NULL,"
                    +"`area` float NOT NULL,"
                    +"`price` int(11) NOT NULL,"
                    +"`unit_price` int(11) NOT NULL default '0',"
                    +"`sign_date` date NOT NULL,"
                    +"`sign_timestamp` bigint(20) NOT NULL,"
                    +"`sign_source` varchar(50) NOT NULL,"
                    +"`orientation` varchar(50) NOT NULL,"
                    +"`floor_state` varchar(50) NOT NULL,"
                    +"`building_finish_year` int(11) NOT NULL,"
                    +"`decoration` varchar(50) NOT NULL,"
                    +"`building_type` varchar(50) NOT NULL"
                    +");";

    public static final String[] CREATE_BIZ_CYCLE_CHENGJIAO_TABLE_COLS_ARRAY = {
            "id_",
            "bizcircle_id",
            "community_id",
            "house_code",
            "title",
            "kv_house_type",
            "cover_pic",
            "frame_id",
            "blueprint_hall_num",
            "blueprint_bedroom_num",
            "area",
            "price",
            "unit_price",
            "sign_date",
            "sign_timestamp",
            "sign_source",
            "orientation",
            "floor_state",
            "building_finish_year",
            "decoration",
            "building_type"
    };

    private Connection connection ;

    public SqliteUtil(Connection connection) {
        this.connection = connection ;
    }

    /**
     * @param sql
     * @return boolean
     */
    public boolean createTable(String sql){
        Statement stmt = null ;
        try{
            stmt = this.connection.createStatement() ;
            stmt.executeUpdate(sql) ;
            return true ;
        }catch (Exception e) {
            System.out.println("create table: " + e.getLocalizedMessage());
            connectionRollback(connection) ;
            return false ;
        }
    }

    /**
     * insert一条多个字段值的数据
     * @param table 表名
     * @param params 多个字段值
     * @return boolean
     */
    public boolean insert(String table, String[] params){
        Statement stmt = null ;
        String sql = "insert into " + table  + " values('";
        for(int i = 0 ; i < params.length ;i++){
            if(i == (params.length - 1)){
                sql += (params[i] + "');") ;
            }else{
                sql += (params[i] + "', '") ;
            }
        }
        System.out.println(sql);
        try{
            stmt = this.connection.createStatement() ;
            stmt.executeUpdate(sql) ;
            if(!connection.isClosed()){
                connection.close();
            }
            return true ;
        }catch (Exception e) {
            System.out.println(table + " error : " + e.getLocalizedMessage());
            e.printStackTrace();
            connectionRollback(connection) ;
            return false ;
        }
    }

    public boolean insertAutoCre(String table, String[] cols, String[] values){
        Statement stmt = null ;
        String sql = "insert into " + table  + " (";

        for(int i = 1 ; i < cols.length ; i++) {
            sql += cols[i];
            if(i != (cols.length - 1)) {
                 sql += ",";
            }
        }
        sql += ") values('";
        for(int i = 0 ; i < values.length ;i++){
            if(i == (values.length - 1)){
                sql += (values[i] + "');") ;
            }else{
                sql += (values[i] + "', '") ;
            }
        }
//        System.out.println(sql);
        try{
            stmt = this.connection.createStatement() ;
            stmt.executeUpdate(sql) ;
//            if(!connection.isClosed()){
//                connection.close();
//            }
            return true ;
        }catch (Exception e) {
            System.out.println(table + " error : " + e.getLocalizedMessage());
            e.printStackTrace();
            connectionRollback(connection) ;
            return false ;
        }
    }

    /**
     * @param table
     * @param keyParam
     * @param keyField
     * @param fields
     * @param params
     * @return boolean
     */
    public boolean update(String table, String keyParam, String keyField, String[] fields, String[] params){
        Statement stmt = null ;
        String sql = "update " + table + " set " ;
        for(int i = 0 ; i < fields.length ; i++){
            if(i == (fields.length - 1)){
                sql += (fields[i] + "='" + params[i] + "' where " + keyField + "='" + keyParam +"';") ;
            }else{
                sql += (fields[i] + "='" + params[i] + "', ") ;
            }
        }
        System.out.println(sql);
        try{
            stmt = this.connection.createStatement() ;
            stmt.executeUpdate(sql) ;
            return true ;
        }catch (Exception e) {
            System.out.println( e.getLocalizedMessage());
            connectionRollback(connection) ;
            return false ;
        }

    }

    /**
     * @param table
     * @param key
     * @param keyValue
     * @return boolean
     */
    public boolean delete(String table, String key, String keyValue){
        Statement stmt = null ;
        String sql = "delete from " + table + " where " + key + "='" + keyValue + "';" ;
        System.out.println(sql);
        try{
            stmt = this.connection.createStatement() ;
            stmt.executeUpdate(sql) ;
            return true ;
        }catch (Exception e) {
            System.out.println( e.getLocalizedMessage());
            connectionRollback(connection) ;
            return false ;
        }
    }

    private void connectionRollback(Connection connection){
        try {
            connection.rollback() ;
        } catch (SQLException e) {
            System.out.println(e.getLocalizedMessage()) ;
        }
    }
}
