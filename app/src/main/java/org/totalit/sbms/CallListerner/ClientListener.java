package org.totalit.sbms.CallListerner;

import org.totalit.sbms.domain.Client;

import java.util.List;

public interface ClientListener {
    public void  getClients(List<Client> clientList);
}
