import { User } from './user.js';
import { Account } from './account-test.js';
import { Transaction } from './transaction.js';

var allUsers = [];
var accountIdTracker = 1000;
var currUser;

// Submit button for creating a 
if (document.getElementById('btnCreateUser')) {

    if ('allUsers' in sessionStorage && sessionStorage.getItem('allUsers') !== "null") {

        document.getElementById('btnCreateUser').addEventListener("click", function() {

            allUsers = JSON.parse(sessionStorage.getItem('allUsers'));
            const infoArr = [];
        
            // get all user info
            let fn = document.getElementById('fname').value;
            let ln = document.getElementById('lname').value;
            let un = document.getElementById('uname').value;
            let pw = document.getElementById('pw').value;
            let confpw = document.getElementById('cpw').value;
            infoArr.push(fn);
            infoArr.push(ln);
            infoArr.push(un);
            infoArr.push(pw);
            infoArr.push(confpw);
        
            // check to see if any are blank...
            for (let piece of infoArr) {
                if (piece === "") {
                    clearAllFieldsAndFocus();
                    alert('No fields can be left blank!');
                    return;
                }
            }
        
            if (pw !== confpw) {
                clearAllFieldsAndFocus();
                alert('Passwords do NOT match...');
                return;
            }
        
            else {
                allUsers.push(new User(fn, ln, un, pw));
                sessionStorage.setItem('allUsers', JSON.stringify(allUsers));
            }
            alert('User created!');
            location.href = '/HTML/index.html';
            }
        );
    }
    else {

        document.getElementById('btnCreateUser').addEventListener("click", function() {
            const infoArr = [];

            // get all user info
            let fn = document.getElementById('fname').value;
            let ln = document.getElementById('lname').value;
            let un = document.getElementById('uname').value;
            let pw = document.getElementById('pw').value;
            let confpw = document.getElementById('cpw').value;
            infoArr.push(fn);
            infoArr.push(ln);
            infoArr.push(un);
            infoArr.push(pw);
            infoArr.push(confpw);

            // check to see if any are blank...
            for (let piece of infoArr) {
                if (piece === "") {
                    clearAllFieldsAndFocus();
                    alert('No fields can be left blank!');
                    return;
                }
            }

            if (pw !== confpw) {
                clearAllFieldsAndFocus();
                alert('Passwords do NOT match...');
                return;
            }

            else {
                allUsers.push(new User(fn, ln, un, pw));
                sessionStorage.setItem('allUsers', JSON.stringify(allUsers));
            }
            alert('User created!');
            location.href = '/HTML/index.html';
            }
        );
    }
}

if (document.getElementById('btnLogin')) {

    document.getElementById('btnLogin').addEventListener("click", function() {

        allUsers = JSON.parse(sessionStorage.getItem('allUsers'));
        let tempUsername = document.getElementById('usernameBox').value;
        let tempPassword = document.getElementById('passwordBox').value;

        for (let user of allUsers) {
            if (user.username === tempUsername && user.password === tempPassword) {
                alert('Login SUCCESSFUL!');
                window.location.href = '/HTML/portal.html';
                sessionStorage.setItem('currUser', JSON.stringify(user));
                return;
            }
        }
        alert('Invalid username or password.');
        document.getElementById('usernameBox').value = '';
        document.getElementById('passwordBox').value = '';
        document.getElementById('usernameBox').focus();
    });
}

if (document.getElementById('btnViewInfo')) {

    document.getElementById('btnViewInfo').addEventListener("click", function() {

        currUser = JSON.parse(sessionStorage.getItem('currUser'));
        alert(`Name: ${currUser.firstName} ${currUser.lastName}\n` +
              `# of Accounts: ${currUser.accounts.length}\n` +
              `User ID: ${currUser.userId}`);
    });
}

if (document.getElementById('btnLastFiveButton')) {

    document.getElementById('btnLastFiveButton').addEventListener("click", function() {
        window.location.href = '/HTML/view_transactions.html';
    });
}

if (document.getElementById('btnTransferButton')) {

    document.getElementById('btnTransferButton').addEventListener('click', function() {

        location.href = '/HTML/transfer.html';
    })
}

if (document.getElementById('btnLogoutButton')) {

    document.getElementById('btnLogoutButton').addEventListener("click", function() {
        currUser = JSON.parse(sessionStorage.getItem('currUser'));
        allUsers = JSON.parse(sessionStorage.getItem('allUsers'));
        let i = 0;
        
        allUsers.forEach(element => {
            if (element.username === currUser.username) {
                i = allUsers.indexOf(element);
                return;
            }
        });
        allUsers.splice(i,1);
        allUsers.push(currUser);
        sessionStorage.setItem('allUsers', JSON.stringify(allUsers));
        sessionStorage.removeItem('currUser');
        window.location.href = '/HTML/login.html';
    });
}

if (document.getElementById('btnAddAccountButton')) {
    
    document.getElementById('btnAddAccountButton').addEventListener("click", function() {
        window.location.href = '/HTML/open_account.html';
    });
}

if (document.getElementById('btnOpenAccountButton')) {

    document.getElementById('btnOpenAccountButton').addEventListener('click', function() {

        currUser = JSON.parse(sessionStorage.getItem('currUser'));

        let accType = document.getElementById('types').value;
        let initDeposit = document.getElementById('init-depo').value;

        // add the new account
        currUser.accounts.push(new Account(getRandomNum(), accType, initDeposit));
        

        // add the initial deposit to transactions array
        currUser.accounts[currUser.accounts.length-1].transactions.splice(0, 0, new Transaction(getCurrentDateAndTime(), 'Initial Deposit', Number(initDeposit)));

        // save and store to storage
        alert(`New ${accType} account opened with a starting balance of $${initDeposit}`);
        sessionStorage.setItem('currUser', JSON.stringify(currUser));
        location.href = "/HTML/portal.html";
    });
}

if (document.getElementById('btnDepositButton')) {

    document.getElementById('btnDepositButton').addEventListener('click', function() {
        location.href = '/HTML/deposit.html';
    });
}

if (document.getElementById('btnWithdrawButton')) {

    document.getElementById('btnWithdrawButton').addEventListener('click', function() {
        location.href = '/HTML/withdraw.html';
    });
}

if (window.location.pathname === '/HTML/deposit.html') {

    var i = 0;
    currUser = JSON.parse(sessionStorage.getItem('currUser'));

    // div
    const parentDiv = document.getElementById('deposit-div');
    parentDiv.style.textAlign = 'center';

    // drop down for the accounts
    const accountDropDown = document.createElement('select');
    accountDropDown.setAttribute('id', 'accountDD');
    parentDiv.appendChild(accountDropDown);

    const goButton = document.createElement('button');
    goButton.setAttribute('id', 'goButton');
    const goButtonText = document.createTextNode('Go!');
    goButton.appendChild(goButtonText);
    parentDiv.appendChild(goButton);
    

    currUser.accounts.forEach(account => {
        const info = document.createElement('option');
        info.text = `${account.accountType}, $${account.balance}`;
        info.value = `${i}`;
        accountDropDown.appendChild(info);
        i++;
    });

    const boxLabel = document.createElement('label');
    const labelText = document.createTextNode('Amount');
    const brk = document.createElement('br');
    boxLabel.appendChild(labelText);
    const depoAmount = document.createElement('input');
    depoAmount.setAttribute('type', 'number');
    depoAmount.setAttribute('id', 'depoBox');
    parentDiv.appendChild(brk);
    parentDiv.appendChild(boxLabel);
    parentDiv.appendChild(depoAmount);
    depoAmount.style.marginTop = '50px';
    depoAmount.style.marginLeft = '15px';
    depoAmount.style.width = '75px';

    var accountChoice;

    if (goButton) {
        
        goButton.addEventListener('click', function() {

            accountChoice = accountDropDown.value;
        
            if (depoAmount.value === null || depoAmount.value < 1) {
                alert('You must enter a positive currency amount');
                return;
            }

            var tempUserAccountBalance = Number(currUser.accounts[accountChoice].balance);
            var tempDepositAmount = Number(depoAmount.value);

            currUser.accounts[accountChoice].balance = tempUserAccountBalance + tempDepositAmount;

            let ts = getCurrentDateAndTime();
            let amount = tempDepositAmount;
            
            currUser.accounts[accountChoice].transactions.splice(0, 0, new Transaction(ts, 'Deposit', amount));

            sessionStorage.setItem('currUser', JSON.stringify(currUser));
            alert(`Deposit of $${tempDepositAmount} made!`);
            location.href = '/HTML/portal.html';
        })
    }
}

if (window.location.pathname === '/HTML/withdraw.html') {

    let i = 0;
    currUser = JSON.parse(sessionStorage.getItem('currUser'));

    // div
    const parentDiv = document.getElementById('withdraw-div');
    parentDiv.style.textAlign = 'center';

    // drop down for the accounts
    const accountDropDown = document.createElement('select');
    accountDropDown.setAttribute('id', 'accountDD');
    parentDiv.appendChild(accountDropDown);

    const goButton1 = document.createElement('button');
    goButton1.setAttribute('id', 'goButton');
    const goButtonText = document.createTextNode('Go!');
    goButton1.appendChild(goButtonText);
    parentDiv.appendChild(goButton1);
    

    currUser.accounts.forEach(account => {
        const info = document.createElement('option');
        info.text = `${account.accountType}, $${account.balance}`;
        info.value = `${i}`;
        accountDropDown.appendChild(info);
        i++;
    });

    const boxLabel = document.createElement('label');
    const labelText = document.createTextNode('Amount');
    const brk = document.createElement('br');
    boxLabel.appendChild(labelText);
    const withdrawAmount = document.createElement('input');
    withdrawAmount.setAttribute('type', 'number');
    withdrawAmount.setAttribute('id', 'withdrawBox');
    parentDiv.appendChild(brk);
    parentDiv.appendChild(boxLabel);
    parentDiv.appendChild(withdrawAmount);
    withdrawAmount.style.marginTop = '50px';
    withdrawAmount.style.marginLeft = '15px';
    withdrawAmount.style.width = '75px';

    var accountChoice;

    if (goButton1) {
        
        goButton1.addEventListener('click', function() {

            accountChoice = accountDropDown.value;
        
            if (withdrawAmount.value === null || withdrawAmount.value < 1) {
                alert('You must enter a positive currency amount');
                return;
            }

            var tempUserAccountBalance = Number(currUser.accounts[accountChoice].balance);
            var tempWithdrawAmount = Number(withdrawAmount.value);
            let ts = getCurrentDateAndTime();
            let amount = tempWithdrawAmount;

            if (tempWithdrawAmount > tempUserAccountBalance) {
                alert('This results in a negative balance...');
                return;
            }

            currUser.accounts[accountChoice].balance = tempUserAccountBalance - tempWithdrawAmount;

            currUser.accounts[accountChoice].transactions.splice(0, 0, new Transaction(ts, 'Withdrawal', amount));
            sessionStorage.setItem('currUser', JSON.stringify(currUser));
            alert(`Withdrawal of $${tempWithdrawAmount} made!`);
            location.href = '/HTML/portal.html';
        })
    }
}

if (window.location.pathname === '/HTML/view_transactions.html') {

    let i = 0;
    currUser = JSON.parse(sessionStorage.getItem('currUser'));

    // div
    let viewDiv = document.getElementById('VT-div');
    viewDiv.style.textAlign = 'center';

    // drop down for user's accounts
    let viewDropDown = document.createElement('select');
    viewDropDown.setAttribute('id', 'viewDD');
    viewDiv.appendChild(viewDropDown);

    let viewButton = document.createElement('button');
    viewButton.setAttribute('id', 'viewButton');
    const viewButtonText = document.createTextNode('View!');
    viewButton.appendChild(viewButtonText);
    viewDiv.appendChild(viewButton);

    currUser.accounts.forEach(account => {
        const info = document.createElement('option');
        info.text = `${account.accountType}, $${account.balance}`;
        info.value = `${i}`;
        viewDropDown.appendChild(info);
        i++;
    });

    let dispDiv = document.createElement('div');
    dispDiv.setAttribute('id', 'disp-div');
    viewDiv.appendChild(dispDiv);
    dispDiv.style.textAlign = 'center';

    let choice;

    document.getElementById('viewButton').addEventListener('click', function() {

        dispDiv.innerHTML = '';

        choice = viewDropDown.value;
        let size;
        let allTransactions = currUser.accounts[choice].transactions;

        if (allTransactions.length >= 5)
            size = 5;
        else {
            size = allTransactions.length;
        }

        for (let x = 0; x < size; x++) {

            let transactionInfo = document.createElement('h3');
            transactionInfo.setAttribute('id', 'infoHeader');
            let displayTime = allTransactions[x].time;
            let displayType = allTransactions[x].type;
            let displayAmount = allTransactions[x].amount;

            let display = `[${displayTime}] ${displayType} of $${displayAmount}`;

            transactionInfo.innerHTML = display;
            dispDiv.appendChild(transactionInfo);
        }
    })
}

if (window.location.pathname === '/HTML/portal.html') {

    currUser = JSON.parse(sessionStorage.getItem('currUser'));
    document.getElementById('name-portion').innerHTML = `${currUser.firstName}!`;
}

if (window.location.pathname === '/HTML/transfer.html') {

    let from = 0;
    let to = 0;
    let theDropDown = document.getElementById('fromAccounts');
    let currFromAccount;
    let fromHeader;
    let toHeader;

    theDropDown.addEventListener('change', function() {

        document.getElementById('toAccounts').innerHTML = '';
        currFromAccount = theDropDown.options[theDropDown.selectedIndex].text;
        for (let person of allUsers) {
            for (let acct of person.accounts) {
    
                if ((theDropDown.options[theDropDown.selectedIndex].text).includes('ID: ' + acct.accountId))
                    continue;
                toHeader = document.createElement('option');
                toHeader.text = `ID: ${acct.accountId}, ${person.firstName} ${person.lastName}, ${acct.accountType}, $${acct.balance}`;
                toHeader.value = `${to}`;
                document.getElementById('toAccounts').appendChild(toHeader);
                to++;
                
            }
        };
    })

    allUsers = JSON.parse(sessionStorage.getItem('allUsers'));
    currUser = JSON.parse(sessionStorage.getItem('currUser'));
    let currUserAccounts = currUser.accounts;
    let currUserIds = [];
    
    currUserAccounts.forEach(account => {
        fromHeader = document.createElement('option');
        fromHeader.text = `ID: ${account.accountId}, ${account.accountType}, $${account.balance}`;
        currUserIds.push(account.accountId);    // account ids for the current user
        fromHeader.value = `${account.accountId}`;
        document.getElementById('fromAccounts').appendChild(fromHeader);
        from++;
    });
    
    for (let person of allUsers) {
        for (let acct of person.accounts) {

            if ((theDropDown.options[theDropDown.selectedIndex].text).includes('ID: ' + acct.accountId))
                continue;
            toHeader = document.createElement('option');
            toHeader.text = `ID: ${acct.accountId}, ${person.firstName} ${person.lastName}, ${acct.accountType}, $${acct.balance}`;
            toHeader.value = `${acct.accountId}`;
            document.getElementById('toAccounts').appendChild(toHeader);
        }
    };

    let transAmount = document.getElementById('transferAmountField');

    if (document.getElementById('proceedWithTransferButton')) {

        document.getElementById('proceedWithTransferButton').addEventListener('click', function() {

            let accountFromIndex;
            let accountToIndex;

            if (transAmount.value === null || transAmount.value < 1) {
                alert('Only positive and non-zero amounts allowed...');
                return;
            }

            transAmount = Number(transAmount.value);
            let fromId = document.getElementById('fromAccounts').value;
            let toId = document.getElementById('toAccounts').value;
            let transferToAcct;
            let transferFromAcct;

            currUser.accounts.forEach(account => {
                if (account.accountId === Number(fromId)) {
                    transferFromAcct = account;
                }
            });

            for (let person of allUsers) {
                for (let acct of person.accounts) {
                    if (acct.accountId === Number(toId)) {
                        transferToAcct = acct;
                        // console.log((transferToAcct));
                    }
                }
            };

            if (transAmount > Number(transferFromAcct.balance)){
                alert('This results in a negative balance in your account');
                return;
            }
            
            transferFromAcct.balance = Number(transferFromAcct.balance) - transAmount;
            

            for (let x = 0; x < currUser.accounts.length; x++) {
                if (currUser.accounts[x].accountId === transferFromAcct.accountId) {
                    accountFromIndex = x;
                    break;
                }
            }

            currUser.accounts[accountFromIndex].transactions.splice(0, 0, new Transaction(getCurrentDateAndTime(), 'Transfer Out', Number(transAmount)));
            
            sessionStorage.setItem('currUser', JSON.stringify(currUser));
            sessionStorage.setItem('allUsers', JSON.stringify(allUsers));
            alert('Transfer of $' + transAmount + " was made!");
        })
        
    }
    

}







// Helper functions
function clearAllFieldsAndFocus() {
    document.getElementById('fname').value = '';
    document.getElementById('lname').value = '';
    document.getElementById('uname').value = '';
    document.getElementById('pw').value = '';
    document.getElementById('cpw').value = '';
    document.getElementById('fname').focus();
}

function getRandomNum() {
    return Math.floor(Math.random() * 901) + 100;
}

function getCurrentDateAndTime() {
    let now = new Date();
    let month = now.toLocaleString('default', {month: 'long'});
    let day = now.getDate();
    let year = now.getFullYear();
    let hours = now.getHours();
    let mins = now.getMinutes();
    let secs = now.getSeconds();
    let timestamp = `${month} ${day}, ${year} - ${hours}:${mins}:${secs}`;
    return timestamp;
}