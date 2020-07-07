package com.atguigu.gmall.bean;

import javax.persistence.Column;
import javax.persistence.Id;
import java.io.Serializable;

public class PmsBaseCatalog3 implements Serializable {
    @Id
    @Column
    private String id;
    @Column
    private String name;
    @Column
    private String catalog2id;

    public String getId () {
        return id;
    }

    public void setId ( String id ) {
        this.id = id;
    }

    public String getName () {
        return name;
    }

    public void setName ( String name ) {
        this.name = name;
    }

    public String getCatalog2id () {
        return catalog2id;
    }

    public void setCatalog2id ( String catalog2id ) {
        this.catalog2id = catalog2id;
    }
}
