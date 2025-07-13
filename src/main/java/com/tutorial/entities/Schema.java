package com.tutorial.entities;

import java.util.HashMap;
import java.util.Map;

public class Schema implements Entity {
    private Map<String, String> mappedColumns;

    private String catalogName;
    private String schemaName;
    private String schemaOwner;
    private String sqlPath;

    public Map<String, String> getMappedColumns() {
        if (mappedColumns == null) {
            mappedColumns = new HashMap<>();
            mappedColumns.put("catalog_name", "catalogName");
            mappedColumns.put("schema_name", "schemaName");
            mappedColumns.put("schema_owner", "schemaOwner");
            mappedColumns.put("sql_path", "sqlPath");
        }
        return mappedColumns;
    }

    @Override
    public String toString() {
        return String.format("%s - %s - %s", this.catalogName, this.schemaName, this.schemaOwner);
    }

    public String getSqlPath() {
        return sqlPath;
    }

    public void setSqlPath(String sqlPath) {
        this.sqlPath = sqlPath;
    }

    public String getSchemaOwner() {
        return schemaOwner;
    }

    public void setSchemaOwner(String schemaOwner) {
        this.schemaOwner = schemaOwner;
    }

    public String getSchemaName() {
        return schemaName;
    }

    public void setSchemaName(String schemaName) {
        this.schemaName = schemaName;
    }

    public String getCatalogName() {
        return catalogName;
    }

    public void setCatalogName(String catalogName) {
        this.catalogName = catalogName;
    }

    public void setMappedColumns(Map<String, String> mappedColumns) {
        this.mappedColumns = mappedColumns;
    }
}
