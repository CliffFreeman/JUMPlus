export class Account {

    constructor(accountId, accountType, balance) {
        this.accountId = accountId;
        this.accountType = accountType;
        this.balance = balance;
    }
    transactions = [];
}