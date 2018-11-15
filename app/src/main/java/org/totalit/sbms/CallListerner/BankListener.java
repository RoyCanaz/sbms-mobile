package org.totalit.sbms.CallListerner;

import org.totalit.sbms.domain.Bank;

import java.util.List;

public interface BankListener {
    public void  getActiveBank(List<Bank> bankList);
}
