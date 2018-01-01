package de.lalo.jpa.account.entity;

import org.apache.commons.lang3.StringUtils;

import java.util.EnumMap;
import java.util.EnumSet;
import java.util.Map;

/**
 * @author llorenzen
 * @since 30.12.17
 */
public enum TransactionState {

    PENDING,
    PERFORMED, // means AUTHORIZED
    //AUTHORIZED, // exported for payout
    SETTLED,
    IN_PROCESS, // sent to provider - waiting for notification
    CANCELED, // means CANCELLED, REFUNDED or CHARGEBACKED
    REJECTED, // means REFUSED
    TIMED_OUT; // the user started a transaction but never performed it at external payment provider

    private static final Map<TransactionState, EnumSet<TransactionState>> validTransitions = new EnumMap<>(TransactionState.class);

    static {
        validTransitions.put(PENDING, EnumSet.of(PERFORMED, REJECTED, TIMED_OUT));
        validTransitions.put(TIMED_OUT, EnumSet.of(PERFORMED, REJECTED));
        validTransitions.put(PERFORMED, EnumSet.of(SETTLED, CANCELED, IN_PROCESS));
        validTransitions.put(IN_PROCESS, EnumSet.of(SETTLED, CANCELED));
        validTransitions.put(SETTLED, EnumSet.of(CANCELED));
        validTransitions.put(CANCELED, EnumSet.of(PENDING));
    }

    public String getKey() {
        String name = TransactionState.class.getName();
        name = StringUtils.replace(name, ".", "_");
        return name + "_" + name();
    }

    @SuppressWarnings("UnusedDeclaration") // used by payoutTx.xhtml (admin)
    public String getPayoutKey() {
        return "zadmin_transaction_payOut_" + name();
    }

    public static boolean isTransitionOk(TransactionState from, TransactionState to) {
        EnumSet<TransactionState> validStates = validTransitions.get(from);
        return validStates != null && validStates.contains(to);
    }
}
