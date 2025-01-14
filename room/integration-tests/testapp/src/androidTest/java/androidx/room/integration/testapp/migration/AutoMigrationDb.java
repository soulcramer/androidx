/*
 * Copyright 2021 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package androidx.room.integration.testapp.migration;


import androidx.annotation.NonNull;
import androidx.room.AutoMigration;
import androidx.room.ColumnInfo;
import androidx.room.Dao;
import androidx.room.Database;
import androidx.room.DeleteColumn;
import androidx.room.DeleteTable;
import androidx.room.Entity;
import androidx.room.ForeignKey;
import androidx.room.Index;
import androidx.room.PrimaryKey;
import androidx.room.Query;
import androidx.room.RenameColumn;
import androidx.room.RenameTable;
import androidx.room.RoomDatabase;
import androidx.room.migration.AutoMigrationCallback;

import java.util.List;

@Database(
        version = AutoMigrationDb.LATEST_VERSION,
        entities = {
                AutoMigrationDb.Entity1.class,
                AutoMigrationDb.Entity2.class,
                AutoMigrationDb.Entity3.class,
                AutoMigrationDb.Entity4.class,
                AutoMigrationDb.Entity5.class,
                AutoMigrationDb.Entity6.class,
                AutoMigrationDb.Entity7.class,
                AutoMigrationDb.Entity8.class,
                AutoMigrationDb.Entity9.class,
                AutoMigrationDb.Entity10.class,
                AutoMigrationDb.Entity11.class,
                AutoMigrationDb.Entity12.class,
                AutoMigrationDb.Entity13_V2.class,
                AutoMigrationDb.Entity14.class,
                AutoMigrationDb.Entity15.class,
                AutoMigrationDb.Entity16.class,
                AutoMigrationDb.Entity17.class,
                AutoMigrationDb.Entity19_V2.class,
                AutoMigrationDb.Entity20_V2.class
        },
        autoMigrations = {
                @AutoMigration(
                        from = 1, to = 2, callback = AutoMigrationDb.SimpleAutoMigration1.class
                )
        },
        exportSchema = true
)
public abstract class AutoMigrationDb extends RoomDatabase {
    static final int LATEST_VERSION = 2;
    abstract AutoMigrationDb.AutoMigrationDao dao();

    /**
     * No change between versions.
     */
    @Entity
    static class Entity1 {
        public static final String TABLE_NAME = "Entity1";
        @PrimaryKey
        public int id;
        public String name;
        @ColumnInfo(defaultValue = "1")
        public int addedInV1;
    }

    /**
     * A new simple column added to Entity 2 with a default value.
     */
    @Entity
    static class Entity2 {
        public static final String TABLE_NAME = "Entity2";
        @PrimaryKey
        public int id;
        public String name;
        @ColumnInfo(defaultValue = "1")
        public int addedInV1;
        @ColumnInfo(defaultValue = "2")
        public int addedInV2;
    }

    /**
     * Added Entity 3 to the schema. No foreign keys, views, indices added.
     */
    @Entity
    static class Entity3 {
        public static final String TABLE_NAME = "Entity3";
        @PrimaryKey
        public int id;
        public String name;
    }

    /**
     * Changing the default value of ‘addedInV1’ in Entity 4.
     */
    @Entity
    static class Entity4 {
        public static final String TABLE_NAME = "Entity4";
        @PrimaryKey
        public int id;
        public String name;
        @ColumnInfo(defaultValue = "2")
        public int addedInV1;
    }

    /**
     * Changing the affinity of ‘addedInV1’ in Entity 5.
     */
    @Entity
    static class Entity5 {
        public static final String TABLE_NAME = "Entity5";
        @PrimaryKey
        public int id;
        public String name;
        @ColumnInfo(defaultValue = "1")
        public String addedInV1;
    }

    /**
     * Changing the nullability of ‘addedInV1’ in Entity 6.
     */
    @Entity
    static class Entity6 {
        public static final String TABLE_NAME = "Entity6";
        @PrimaryKey
        public int id;
        public String name;
        // @ColumnInfo(defaultValue = "1") - now nullable
        public int addedInV1;
    }

    /**
     * No change between versions.
     */
    @Entity
    static class Entity7 {
        public static final String TABLE_NAME = "Entity7";
        @PrimaryKey
        public int id;
        public String name;
        @ColumnInfo(defaultValue = "1")
        public int addedInV1;
    }

    /**
     * Change the primary key of Entity 8.
     */
    @Entity
    static class Entity8 {
        public static final String TABLE_NAME = "Entity8";
        public int id;
        @PrimaryKey
        @NonNull
        public String name;
        @ColumnInfo(defaultValue = "1")
        public int addedInV1;
    }

    /**
     * Add a foreign key to Entity 9.
     */
    @Entity(foreignKeys = {
            @ForeignKey(entity = Entity12.class,
                    parentColumns = "id",
                    childColumns = "id",
                    deferred = true)})
    static class Entity9 {
        public static final String TABLE_NAME = "Entity9";
        @PrimaryKey
        public int id;
        public String name;
        @ColumnInfo(defaultValue = "1")
        public int addedInV1;
    }

    /**
     * Change the foreign key added in Entity 10 to ‘addedInV1’. Add index for addedInV1 on
     * Entity 10. The reference table of the foreign key has been renamed from Entity13 to
     * Entity13_V2.
     */
    @Entity(foreignKeys = {
            @ForeignKey(entity = Entity13_V2.class,
                    parentColumns = "addedInV1",
                    childColumns = "addedInV1",
                    deferred = true)},
            indices = {@Index(value = "addedInV1", unique = true)})
    static class Entity10 {
        public static final String TABLE_NAME = "Entity10";
        @PrimaryKey
        public int id;
        public String name;
        @ColumnInfo(defaultValue = "1")
        public int addedInV1;
    }

    /**
     * Remove the foreign key in Entity 11.
     */
    @Entity
    static class Entity11 {
        public static final String TABLE_NAME = "Entity11";
        @PrimaryKey
        public int id;
        public String name;
        @ColumnInfo(defaultValue = "1")
        public int addedInV1;
    }

    /**
     * Add an index ‘name’ to Entity 12.
     */
    @Entity(indices = {@Index(value = "name", unique = true)})
    static class Entity12 {
        public static final String TABLE_NAME = "Entity12";
        @PrimaryKey
        public int id;
        public String name;
        @ColumnInfo(defaultValue = "1")
        public int addedInV1;
    }

    /**
     * Rename to Entity13_V2, it is a table referenced by the foreign key in Entity10. Change the
     * index added in Entity 13 to ‘addedInV1’.
     */
    @Entity(indices = {@Index(value = "addedInV1", unique = true)})
    static class Entity13_V2 {
        public static final String TABLE_NAME = "Entity13";
        @PrimaryKey
        public int id;
        public String name;
        @ColumnInfo(defaultValue = "1")
        public int addedInV1;
    }

    /**
     * Remove the index ‘name’ added in Entity 14.
     */
    @Entity
    static class Entity14 {
        public static final String TABLE_NAME = "Entity14";
        @PrimaryKey
        public int id;
        public String name;
    }

    /**
     * Deleting the column ‘addedInV1’ from Entity 15.
     */
    @Entity
    static class Entity15 {
        public static final String TABLE_NAME = "Entity15";
        @PrimaryKey
        public int id;
        public String name;
    }

    /**
     * Renaming the column ‘addedInV1’ from Entity 16 to ‘renamedInV2’.
     */
    @Entity
    static class Entity16 {
        public static final String TABLE_NAME = "Entity16";
        @PrimaryKey
        public int id;
        public String name;
        @ColumnInfo(defaultValue = "1")
        public int renamedInV2;
    }

    /**
     * Renaming the column ‘addedInV1’ from Entity 17 to ‘renamedInV2’. Changing the affinity of
     * this column.
     */
    @Entity
    static class Entity17 {
        public static final String TABLE_NAME = "Entity17";
        @PrimaryKey
        public int id;
        public String name;
        @ColumnInfo(defaultValue = "1")
        public String renamedInV2;
    }

    /**
     * Deleted Entity 18.
     *
     * Rename Entity19 to ‘Entity19_V2’.
     */
    @Entity
    static class Entity19_V2 {
        public static final String TABLE_NAME = "Entity19_V2";
        @PrimaryKey
        public int id;
        public String name;
        @ColumnInfo(defaultValue = "1")
        public int addedInV1;
    }

    /**
     * Rename Entity20 to ‘Entity20_V2’. Rename the column ‘addedInV1’ to ‘renamedInV2’. Change
     * the primary key of this table to ‘name’. Change the affinity of the column ‘renamedInV2’.
     * Add new column ‘addedInV2’.
     */
    @Entity
    static class Entity20_V2 {
        public static final String TABLE_NAME = "Entity20_V2";
        public int id;
        @PrimaryKey
        @NonNull
        public String name;
        @ColumnInfo(defaultValue = "1")
        public String renamedInV2;
        @ColumnInfo(defaultValue = "2")
        public int addedInV2;
    }

    @Dao
    interface AutoMigrationDao {
        @Query("SELECT * from Entity1 ORDER BY id ASC")
        List<AutoMigrationDb.Entity1> getAllEntity1s();
    }

    @DeleteTable(deletedTableName = "Entity18")
    @RenameTable(originalTableName = "Entity19", newTableName = "Entity19_V2")
    @RenameTable(originalTableName = "Entity20", newTableName = "Entity20_V2")
    @RenameTable(originalTableName = "Entity13", newTableName = "Entity13_V2")
    @RenameColumn(tableName = "Entity16", originalColumnName = "addedInV1",
            newColumnName = "renamedInV2")
    @RenameColumn(tableName = "Entity17", originalColumnName = "addedInV1",
            newColumnName = "renamedInV2")
    @RenameColumn(tableName = "Entity20", originalColumnName = "addedInV1",
            newColumnName = "renamedInV2")
    @DeleteColumn(tableName = "Entity15", deletedColumnName = "addedInV1")
    interface SimpleAutoMigration1 extends AutoMigrationCallback {
    }
}