import { useState } from "react";
import { FurnitureApi } from "../data/FurnitureApi";
import { useNavigate } from "react-router-dom";

function Login(props) {

    const [username, setUsername] = useState("")
    const [password, setPassword] = useState("");
    const navigate = useNavigate();

    const handleCredentials = async (e) => {
        e.preventDefault();
        const currUser = await FurnitureApi.login(username, password);

        if (currUser === null) {
            alert('Invalid username or password!');
            setUsername("");
            setPassword("");
        }
        else {
            alert('Success!');
            props.setLoggedInUser(currUser);
            navigate('/');
        }
    }


    return (
        <div id="loginDiv" style={{textAlign: "center"}}>
            <form onSubmit={handleCredentials}>
                <fieldset>
                    <legend>Login</legend>
                    <label htmlFor="uname" style={{marginRight: "10px"}}>Username</label>
                    <input type="text" id="uname" name="uname" value={username} onChange={(e) => setUsername(e.target.value)} /><br /><br />
                    <label htmlFor="pw" style={{marginRight: "10px"}}>Password</label>
                    <input type="password" id="pw" name="pw" value={password} onChange={(e) => setPassword(e.target.value)} /><br /><br />
                    <input type="submit" value="Login"></input>
                </fieldset>
            </form>
        </div>
    );
}
export default Login;