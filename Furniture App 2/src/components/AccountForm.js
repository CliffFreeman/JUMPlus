import { useState } from "react";
import { FurnitureApi } from '../data/FurnitureApi';
import './CreateForm.css';

function AccountForm() {

    const [firstName, setFirstName] = useState("");
    const [lastName, setLastName] = useState("");
    const [username, setUsername] = useState("");
    const [password, setPassword] = useState("");

    const handleCreate = async (e) => {
        e.preventDefault();
        const isAdded = await FurnitureApi.addCustomer({
            name: firstName + ' ' + lastName,
            username: username,
            password: password,
            orders: []
        });
        
        if (isAdded === true) {
            alert('Account created!');
        }
        else {
            alert('Username taken...');
        }
        setFirstName("");
        setLastName("");
        setUsername("");
        setPassword("");
        document.getElementById('fname').focus();
    }

    return (
        <div id="form-div" style={{textAlign: "center"}}>
            <form onSubmit={handleCreate}>
                <fieldset>
                    <legend>Create Account</legend>
                    <label htmlFor="fname" style={{marginRight: "10px"}}>First Name</label>
                    <input type="text" id="fname" name="fname" value={firstName} onChange={(e) => setFirstName(e.target.value)} /><br /><br />
                    <label htmlFor="lname" style={{marginRight: "10px"}}>Last Name</label>
                    <input type="text" id="lname" name="lname" value={lastName} onChange={(e) => setLastName(e.target.value)} /><br /><br />
                    <label htmlFor="uname" style={{marginRight: "10px"}}>Username</label>
                    <input type="text" id="uname" name="uname" value={username} onChange={(e) => setUsername(e.target.value)} /><br /><br />
                    <label htmlFor="pw" style={{marginRight: "10px"}}>Password</label>
                    <input type="password" id="pw" name="pw" value={password} onChange={(e) => setPassword(e.target.value)} /><br /><br />
                    <input type="submit" value="Submit"></input>
                </fieldset>
            </form>
        </div>
    );
}
export default AccountForm;