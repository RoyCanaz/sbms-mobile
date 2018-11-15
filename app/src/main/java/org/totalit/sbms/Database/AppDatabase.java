package org.totalit.sbms.Database;

import android.arch.persistence.room.Database;
import android.arch.persistence.room.Room;
import android.arch.persistence.room.RoomDatabase;
import android.content.Context;

import org.totalit.sbms.dao.BankDao;
import org.totalit.sbms.dao.BranchDao;
import org.totalit.sbms.dao.CategoryDao;
import org.totalit.sbms.dao.ClientDao;
import org.totalit.sbms.dao.ClientInventoryDao;
import org.totalit.sbms.dao.ContactDao;
import org.totalit.sbms.dao.NotesDao;
import org.totalit.sbms.dao.ProcumentDocsDao;
import org.totalit.sbms.dao.QuoteDao;
import org.totalit.sbms.dao.QuoteItemDao;
import org.totalit.sbms.dao.QuoteItemTempDao;
import org.totalit.sbms.dao.QuoteTempDao;
import org.totalit.sbms.dao.RateDao;
import org.totalit.sbms.dao.SupplierDao;
import org.totalit.sbms.dao.UserDao;
import org.totalit.sbms.dao.VisitPlanDao;
import org.totalit.sbms.domain.Bank;
import org.totalit.sbms.domain.Branch;
import org.totalit.sbms.domain.Category;
import org.totalit.sbms.domain.Client;
import org.totalit.sbms.domain.ClientInventory;
import org.totalit.sbms.domain.Contact;
import org.totalit.sbms.domain.Notes;
import org.totalit.sbms.domain.ProcumentDocs;
import org.totalit.sbms.domain.Quote;
import org.totalit.sbms.domain.QuoteItem;
import org.totalit.sbms.domain.Rate;
import org.totalit.sbms.domain.Supplier;
import org.totalit.sbms.domain.User;
import org.totalit.sbms.domain.VisitPlan;
import org.totalit.sbms.domain.temp.QuoteItemTemp;
import org.totalit.sbms.domain.temp.QuoteTemp;

@Database(entities = {User.class, Client.class, ProcumentDocs.class, ClientInventory.class, Branch.class, Contact.class, Category.class, Notes.class, VisitPlan.class, Supplier.class, Rate.class, Bank.class, QuoteTemp.class, QuoteItemTemp.class, Quote.class, QuoteItem.class}, version = 1)
public abstract class AppDatabase extends RoomDatabase {
    private static AppDatabase INSTANCE;

    public abstract UserDao userRepository();
    public abstract ClientDao clientRepository();
    public abstract ProcumentDocsDao procumentDocsDao();
    public abstract ClientInventoryDao clientInventoryDao();
    public abstract BranchDao branchDao();
    public abstract ContactDao contactDao();
    public abstract CategoryDao categoryDao();
    public abstract NotesDao notesDao();
    public abstract VisitPlanDao visitPlanDao();
    public abstract SupplierDao supplierDao();
    public abstract RateDao rateDao();
    public abstract BankDao bankDao();
    public abstract QuoteTempDao quoteTempDao();
    public abstract QuoteItemTempDao quoteItemTempDao();
    public abstract QuoteDao quoteDao();
    public abstract QuoteItemDao quoteItemDao();

    public static AppDatabase getInMemoryDatabase(Context context){
        if(INSTANCE==null){
            INSTANCE =
                    Room.inMemoryDatabaseBuilder(context.getApplicationContext(),
                            AppDatabase.class).allowMainThreadQueries().fallbackToDestructiveMigration().build();
        }
        return INSTANCE;
    }
    public static AppDatabase getFileDatabase(Context context){
        if(INSTANCE == null){
            INSTANCE = Room.databaseBuilder(context.getApplicationContext(),
                    AppDatabase.class, "sbmsdb").allowMainThreadQueries().fallbackToDestructiveMigration().build();
        }
        return INSTANCE;
    }
    public  static void destroyInstance(){
        INSTANCE = null;
    }
}
