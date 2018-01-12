package de.lalo.jpa.account.boundary;

import de.lalo.jpa.account.control.AccountingService;
import de.lalo.jpa.account.entity.Account;
import de.lalo.jpa.account.entity.Transaction;

import javax.inject.Inject;
import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

/**
 * @author llorenzen
 * @since 08.01.18
 */
@Path("/accounts/{id}")
@Consumes(MediaType.APPLICATION_JSON)
@Produces(MediaType.APPLICATION_JSON)
public class AccountsResource {

    @PathParam("id")
    private String playerId;

    @Inject
    private AccountingService accountingService;

    @GET
    public AccountResponse getAccount() {
        Account account = accountingService.findAccount(playerId);
        if (account == null) {
            throw new WebApplicationException(Response.status(Response.Status.NOT_FOUND).entity("No account for playerId: " + playerId).build());
        }
        return new AccountResponse(account);
    }

    @POST
    @Path("/transactions")
    public TransactionChangedMessage perform(TransactionMessage transactionMessage) {
        TransactionChangedEvent event = accountingService.performTransaction(transactionMessage);
        if (event == null) {
            throw new WebApplicationException(Response.status(Response.Status.CONFLICT).entity(new TransactionFailure(transactionMessage, null)).build());
        }

        Transaction transaction = event.getTransaction();
        return new TransactionChangedMessage(transaction.getId() + "", transaction.getAccount().getPlayerReference(), transaction.getReference(),
                transaction.getPreBalance(), transaction.getPostBalance(), transaction.getAmount(), "success");
    }
}
