package org.totalit.sbms.SyncData;

import org.totalit.sbms.Database.AppDatabase;
import org.totalit.sbms.domain.Branch;
import org.totalit.sbms.domain.Client;
import org.totalit.sbms.domain.ClientInventory;
import org.totalit.sbms.domain.Contact;
import org.totalit.sbms.domain.Notes;
import org.totalit.sbms.domain.ProcumentDocs;
import org.totalit.sbms.domain.VisitPlan;

public class CheckExistAll {
    AppDatabase mdb;

    public CheckExistAll(AppDatabase mdb) {
        this.mdb = mdb;
    }

    public boolean checkClientByRealId(int realId){
        Boolean exists = false;
        Client c = mdb.clientRepository().findOneByRealId(realId);
        if (c!=null){
            exists=true;
        }
        return exists;
    }
    public boolean checkBranchByRealId(int realId){
        Boolean exists = false;
        Branch branch = mdb.branchDao().findOneByRealId(realId);
        if (branch!=null){
            exists=true;
        }
        return exists;
    }
    public boolean checkClientInByRealId(int realId){
        Boolean exists = false;
        ClientInventory c = mdb.clientInventoryDao().getByRealId(realId);
        if (c!=null){
            exists=true;
        }
        return exists;
    }

    public boolean checkVisitPlanByRealId(int realId){
        Boolean exists = false;
        VisitPlan vp = mdb.visitPlanDao().getByRealId(realId);
        if (vp!=null){
            exists=true;
        }
        return exists;
    }
    public boolean checkProcDocsByRealId(int realId){
        Boolean exists = false;
        ProcumentDocs pd = mdb.procumentDocsDao().getByRealId(realId);
        if (pd!=null){
            exists=true;
        }
        return exists;
    }
    public boolean checkContactByRealId(int realId){
        Boolean exists = false;
        Contact c = mdb.contactDao().getByRealId(realId);
        if (c!=null){
            exists=true;
        }
        return exists;
    }
    public boolean checkNotesByRealId(int realId){
        Boolean exists = false;
        Notes n = mdb.notesDao().getByRealId(realId);
        if (n!=null){
            exists=true;
        }
        return exists;
    }

}
