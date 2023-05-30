function Orders(props) {
    return (
        <div id="cart-div" style={{textAlign: "center"}}>
            <table className="table">
                <thead>
                    <tr>
                        <th scope="col">ID</th>
                        <th scope="col">Order Date/Time</th>
                        <th scope="col">Discount Applied?</th>
                        <th scope="col">Total</th>
                    </tr>
                </thead>
            
                <tbody>
                    {props.loggedInUser.orders.map((ordr, i) => {
                        return (
                            <tr key={i}>
                                <td style={{backgroundColor: "black", color: "white"}}>{ordr.id}</td>
                                <td style={{backgroundColor: "black", color: "white"}}>{ordr.time}</td>
                                <td style={{backgroundColor: "black", color: "white"}}>{ordr.discount}</td>
                                <td style={{backgroundColor: "black", color: "white"}}>{ordr.total}</td>
                            </tr>
                        );
                    })}
                </tbody>
            </table>
        </div>
    );
}
export default Orders;