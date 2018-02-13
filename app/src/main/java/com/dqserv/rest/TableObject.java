package com.dqserv.rest;

import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * Created by Admin on 2/12/2018.
 */

public class TableObject {

    @SerializedName("tables")
    private List<Tables> tables;

    public static class Tables {

        public Tables() {

        }

        @SerializedName("table_id")
        private String tableId;

        @SerializedName("table_name")
        private String tableName;

        public void setTableId(String tableId) {
            this.tableId = tableId;
        }

        public void setTableName(String tableName) {
            this.tableName = tableName;
        }

        public String getTableId() {
            return tableId;
        }

        public String getTableName() {
            return tableName;
        }
    }

    public void setTables(List<Tables> tables) {
        this.tables = tables;
    }

    public List<Tables> getTables() {
        return tables;
    }
}
