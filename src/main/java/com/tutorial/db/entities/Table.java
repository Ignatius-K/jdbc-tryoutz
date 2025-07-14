package com.tutorial.db.entities;

import java.util.HashMap;
import java.util.Map;

public class Table implements Entity {
    private Map<String, String> mappedColumns;

    private String tableCatalog;
    private String tableSchema;
    private String tableName;
    private String tableType;
    private String selfReferencingColumnName;
    private String referenceGeneration;
    private String isInsertableInto;

    public Map<String, String> getMappedColumns() {
        if (mappedColumns == null) {
            mappedColumns = new HashMap<>();
            mappedColumns.put("table_catalog", "tableCatalog");
            mappedColumns.put("table_schema", "tableSchema");
            mappedColumns.put("table_name", "tableName");
            mappedColumns.put("table_type", "tableType");
            mappedColumns.put("self_referencing_column_name",  "selfReferencingColumnName");
            mappedColumns.put("reference_generation", "referenceGeneration");
            mappedColumns.put("is_insertable_into", "isInsertableInto");

        }
        return mappedColumns;
    }

    @Override
    public String toString() {
        return String.format("%s -%s -%s", this.getTableCatalog(), this.getTableSchema(), this.getTableName());
    }

    public String getTableCatalog() {
        return tableCatalog;
    }

    public void setTableCatalog(String tableCatalog) {
        this.tableCatalog = tableCatalog;
    }

    public String getTableSchema() {
        return tableSchema;
    }

    public void setTableSchema(String tableSchema) {
        this.tableSchema = tableSchema;
    }

    public String getTableName() {
        return tableName;
    }

    public void setTableName(String tableName) {
        this.tableName = tableName;
    }

    public String getTableType() {
        return tableType;
    }

    public void setTableType(String tableType) {
        this.tableType = tableType;
    }

    public String getSelfReferencingColumnName() {
        return selfReferencingColumnName;
    }

    public void setSelfReferencingColumnName(String selfReferencingColumnName) {
        this.selfReferencingColumnName = selfReferencingColumnName;
    }

    public String getReferenceGeneration() {
        return referenceGeneration;
    }

    public void setReferenceGeneration(String referenceGeneration) {
        this.referenceGeneration = referenceGeneration;
    }

    public String getIsInsertableInto() {
        return isInsertableInto;
    }

    public void setIsInsertableInto(String isInsertableInto) {
        this.isInsertableInto = isInsertableInto;
    }
}
