package org.totalit.sbms.CallListerner;

import org.totalit.sbms.domain.Rate;

import java.util.List;

public interface RateListener {
    public void rates(List<Rate> rates);
}
