package com.luxoft.assignment.util;

import com.luxoft.assignment.model.Elvl;
import com.luxoft.assignment.model.Quote;

public class Util {
    public static void setElvl(boolean isNew, Elvl elvl, Quote quote) {
        if (isNew) {
            elvl.setElvl(quote.getBid() != null ? quote.getBid() : quote.getAsk());
            return;
        }
        if (quote.getBid() == null) {
            elvl.setElvl(quote.getAsk());
            return;
        }
        if (quote.getBid().compareTo(elvl.getElvl()) > 0) {
            elvl.setElvl(quote.getBid());
            return;
        }
        if (quote.getAsk().compareTo(elvl.getElvl()) < 0) {
            elvl.setElvl(quote.getAsk());
        }
    }
}
