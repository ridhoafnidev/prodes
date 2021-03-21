package com.mrii.prodes.db;

import androidx.room.Database;
import androidx.room.RoomDatabase;
import androidx.room.TypeConverters;

import com.mrii.prodes.db.common.Converters;
import com.mrii.prodes.viewobject.AboutUs;
import com.mrii.prodes.viewobject.Banner;
import com.mrii.prodes.viewobject.Blog;
import com.mrii.prodes.viewobject.ChatHistory;
import com.mrii.prodes.viewobject.ChatHistoryMap;
import com.mrii.prodes.viewobject.City;
import com.mrii.prodes.viewobject.CityMap;
import com.mrii.prodes.viewobject.DeletedObject;
import com.mrii.prodes.viewobject.Image;
import com.mrii.prodes.viewobject.InfoCovid;
import com.mrii.prodes.viewobject.Item;
import com.mrii.prodes.viewobject.ItemCategory;
import com.mrii.prodes.viewobject.ItemCollection;
import com.mrii.prodes.viewobject.ItemCollectionHeader;
import com.mrii.prodes.viewobject.ItemCondition;
import com.mrii.prodes.viewobject.ItemCurrency;
import com.mrii.prodes.viewobject.ItemDealOption;
import com.mrii.prodes.viewobject.ItemFavourite;
import com.mrii.prodes.viewobject.ItemFromFollower;
import com.mrii.prodes.viewobject.ItemHistory;
import com.mrii.prodes.viewobject.ItemLocation;
import com.mrii.prodes.viewobject.ItemMap;
import com.mrii.prodes.viewobject.ItemPaidHistory;
import com.mrii.prodes.viewobject.ItemPriceType;
import com.mrii.prodes.viewobject.ItemSpecs;
import com.mrii.prodes.viewobject.ItemSubCategory;
import com.mrii.prodes.viewobject.ItemType;
import com.mrii.prodes.viewobject.Noti;
import com.mrii.prodes.viewobject.PSAppInfo;
import com.mrii.prodes.viewobject.PSAppSetting;
import com.mrii.prodes.viewobject.PSAppVersion;
import com.mrii.prodes.viewobject.PSCount;
import com.mrii.prodes.viewobject.Rating;
import com.mrii.prodes.viewobject.User;
import com.mrii.prodes.viewobject.UserLogin;
import com.mrii.prodes.viewobject.UserMap;
import com.mrii.prodes.viewobject.messageHolder.Message;


/**
 * Created by Panacea-Soft on 11/20/17.
 * Contact Email : teamps.is.cool@gmail.com
 */

@Database(entities = {
        Image.class,
        User.class,
        UserLogin.class,
        AboutUs.class,
        ItemFavourite.class,
        Noti.class,
        ItemHistory.class,
        Blog.class,
        Banner.class,
        InfoCovid.class,
        Rating.class,
        PSAppInfo.class,
        PSAppVersion.class,
        DeletedObject.class,
        City.class,
        CityMap.class,
        Item.class,
        ItemMap.class,
        ItemCategory.class,
        ItemCollectionHeader.class,
        ItemCollection.class,
        ItemSubCategory.class,
        ItemSpecs.class,
        ItemCurrency.class,
        ItemPriceType.class,
        ItemType.class,
        ItemLocation.class,
        ItemDealOption.class,
        ItemCondition.class,
        ItemFromFollower.class,
        Message.class,
        ChatHistory.class,
        ChatHistoryMap.class,
        PSAppSetting.class,
        UserMap.class,
        PSCount.class,
        ItemPaidHistory.class
}, version = 11, exportSchema = false)
// app version 2.9 = db version 11
// app version 2.8 = db version 10
// app version 2.7 = db version 9
// app version 2.6 = db version 8
// app version 2.5 = db version 7
// app version 2.4 = db version 7
// app version 2.3 = db version 6
// app version 2.2 = db version 6
// app version 2.1 = db version 6
// app version 2.0 = db version 6
// app version 1.9 = db version 6
// app version 1.8 = db version 5
// app version 1.7 = db version 4
// app version 1.6 = db version 4
// app version 1.5 = db version 4
// app version 1.4 = db version 3
// app version 1.3 = db version 3
// app version 1.2 = db version 2
// app version 1.0 = db version 1


@TypeConverters({Converters.class})
public abstract class PSCoreDb extends RoomDatabase {

    abstract public UserDao userDao();

    abstract public UserMapDao userMapDao();

    abstract public HistoryDao historyDao();

    abstract public SpecsDao specsDao();

    abstract public AboutUsDao aboutUsDao();

    abstract public ImageDao imageDao();

    abstract public ItemDealOptionDao itemDealOptionDao();

    abstract public ItemConditionDao itemConditionDao();

    abstract public ItemLocationDao itemLocationDao();

    abstract public ItemCurrencyDao itemCurrencyDao();

    abstract public ItemPriceTypeDao itemPriceTypeDao();

    abstract public ItemTypeDao itemTypeDao();

    abstract public RatingDao ratingDao();

    abstract public NotificationDao notificationDao();

    abstract public BlogDao blogDao();

    abstract public InfoCovidDao infoCovidDao();

    abstract public BannerDao bannerDao();

    abstract public PSAppInfoDao psAppInfoDao();

    abstract public PSAppVersionDao psAppVersionDao();

    abstract public DeletedObjectDao deletedObjectDao();

    abstract public CityDao cityDao();

    abstract public CityMapDao cityMapDao();

    abstract public ItemDao itemDao();

    abstract public ItemMapDao itemMapDao();

    abstract public ItemCategoryDao itemCategoryDao();

    abstract public ItemCollectionHeaderDao itemCollectionHeaderDao();

    abstract public ItemSubCategoryDao itemSubCategoryDao();

    abstract public ChatHistoryDao chatHistoryDao();

    abstract public MessageDao messageDao();

    abstract public PSCountDao psCountDao();

    abstract public ItemPaidHistoryDao itemPaidHistoryDao();


//    /**
//     * Migrate from:
//     * version 1 - using Room
//     * to
//     * version 2 - using Room where the {@link } has an extra field: addedDateStr
//     */
//    public static final Migration MIGRATION_1_2 = new Migration(1, 2) {
//        @Override
//        public void migrate(@NonNull SupportSQLiteDatabase database) {
//            database.execSQL("ALTER TABLE news "
//                    + " ADD COLUMN addedDateStr INTEGER NOT NULL DEFAULT 0");
//        }
//    };

    /* More migration write here */
}