/**
 * Project:  TestRealmAdapterActivity
 * Filename: Pet.java
 *
 * Created by Kyno on 4/25/16.
 * Copyright (c) 2016. Bearyinnovative. All rights reserved.
 */
package com.kyno.testrealmadapteractivity;

import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;

public class Pet extends RealmObject {

    public Pet() {
        name = "";
    }

    public Pet(String name) {
        this.name = name;
    }

    @PrimaryKey
    private String name;


    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
