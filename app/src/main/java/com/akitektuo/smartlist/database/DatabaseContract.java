package com.akitektuo.smartlist.database;

import android.provider.BaseColumns;

/**
 * Created by Akitektuo on 15.03.2017.
 */

public class DatabaseContract {

    abstract class ListContractEntry implements BaseColumns {
        static final String TABLE_NAME = "list";
        static final String COLUMN_NAME_NUMBER = "number";
        static final String COLUMN_NAME_VALUE = "value";
        static final String COLUMN_NAME_PRODUCT = "product";
    }

    abstract class UsageContractEntry implements BaseColumns {
        static final String TABLE_NAME = "usage";
        static final String COLUMN_NAME_PRODUCTS = "products";
        static final String COLUMN_NAME_PRICES = "prices";
    }
}
