package piggybit.piggybitmongodb;

import java.util.ArrayList;
import java.util.List;

public class eventLog {
    private long moneyInvested;
    private String currency;
    private String cryptocurrency;
    private int period;

    protected eventLog(){
    }

    public eventLog (long moneyInvested, String currency, String cryptocurrency, int period) {
        this.moneyInvested = moneyInvested;
        this.currency = currency;
        this.cryptocurrency = cryptocurrency;
        this.period = period;
    }

}
