package main.transaction;

import java.time.LocalDate;

public class TransactionType {
    private String title;
    private String message;
    private double amount;
    private TransType transType;
    private LocalDate nextDate;

    // Sets up the layout for the transaction information
    public TransactionType(String title, String message, double amount, TransType transType, LocalDate startDate){
        this.title = title;
        this.message = message;
        this.amount = amount;
        this.transType = transType;
        this.nextDate = startDate;
    }

    // File not in use due to RECURRING_(DATE) only being vanity for now

/*    public void setTransTitle() {this.title = title;}
    public void setTransMessage() {this.message = message;}
    public void setTransAmount() {this.amount = amount;}
    public void setTransType() {this.transType = transType;}
    public void setTransDate() {this.nextDate = nextDate;}*/

/*    public String getTransTitle() {return title;}
    public String getTransMessage() {return message;}
    public int getTransAmount() {return amount;}
    public TransType getTransType() {return transType;}
    public LocalDate getTransDate() {return nextDate;}*/

/*    public void updateNextDate() {
        if (transType != transType.ONE_TIME) {
            switch (transType) {
                case RECURRING_DAILY:
                    nextDate = nextDate.plusDays(1);
                    break;
                case RECURRING_WEEKLY:
                    nextDate = nextDate.plusWeeks(1);
                    break;
                case RECURRING_MONTHLY:
                    nextDate = nextDate.plusMonths(1);
                    break;
                case RECURRING_YEARLY:
                    nextDate = nextDate.plusYears(1);
                    break;
                case ONE_TIME:
                    nextDate = null;
                    break;
            }
        } else {
            nextDate = null;
        }
    }*/
}
