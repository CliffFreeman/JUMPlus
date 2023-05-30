import { useEffect } from "react";
import { useNavigate } from "react-router-dom";
import { FurnitureApi } from "../data/FurnitureApi";

var orderId = 100;

function Cart(props) {

    var sum = 0;
    const navigate = useNavigate();
    const month = ["January","February","March","April","May","June","July","August","September","October","November","December"];
    var discountPercent = 0;
    var cartItems = props.loggedInUser.cart;

    // get the order total
    for (let item of cartItems) {
        sum += item.price;
    }

    // if the order surpasses $2K, add a discount
    if (sum >= 2000) {
        sum *= 0.9;
        discountPercent = 0.1;
    }

    // once the user clicks the button to order their furniture
    const handleClickOrderNow = async (e) => {
        e.preventDefault();

        if (props.loggedInUser.cart.length === 0) {
            alert('Your cart is empty silly...');
            return;
        }
        
        var d = new Date();
        let m = month[d.getMonth()];
        let day = d.getDate();
        let year = d.getFullYear();
        let hours = d.getHours();
        let mins = d.getMinutes();
        props.loggedInUser.orders.push({
            id: orderId++,
            time: `${m} ${day}, ${year} - ${hours}:${mins}`,
            discount: discountPercent === 0.1 ? "Y" : "N",
            total: `$${sum.toFixed(2)}`
        });
        props.loggedInUser.cart = [];
        
        await FurnitureApi.addOrder(props.loggedInUser);
        navigate('/');
    }

    // removing an item from their cart
    const handleRemoveItem = async (prod) => {
        var currItem = 0;

        for (let product of props.loggedInUser.cart) {
            if (product.id === prod.id)
                currItem = product;
                break;
        }

        for (let x = 0; x < props.loggedInUser.cart.length; x++) {
            if (props.loggedInUser.cart[x].id === currItem.id) {
                props.loggedInUser.cart.splice(x, 1);
                break;
            }
        }
        navigate('/cart')
    }

    useEffect(() => {

    }, [props.loggedInUser.cart])

    return (
        <>
            <div id="cart-div" style={{textAlign: "center"}}>
                <table className="table">
                    <thead>
                        <tr>
                            <th scope="col">Qty</th>
                            <th scope="col">Product</th>
                            <th scope="col">Price</th>
                            <th scope="col">Remove?</th>
                        </tr>
                    </thead>
                
                    <tbody>
                        {props.loggedInUser.cart.map((product, i) => {
                            return (
                                <tr key={i}>
                                    <td style={{backgroundColor: "black", color: "white"}}>1</td>
                                    <td style={{backgroundColor: "black", color: "white"}}>{product.item}</td>
                                    <td style={{backgroundColor: "black", color: "white"}}>${product.price}</td>
                                    <td style={{backgroundColor: "black", color: "white"}}><button onClick={() => handleRemoveItem(product)}>Delete</button></td>
                                </tr>
                            );
                        })}
                    </tbody>
                    
                </table>
                <div>
                    {discountPercent === 0.1 ? <h4>Discount: 10%</h4> : <h4>Discount: None</h4>}
                    <h1 style={{textAlign: "center"}}>Cart total: ${sum.toFixed(2)}</h1>
                    <button className="btn btn-primary" onClick={handleClickOrderNow}>Order Now!</button>
                </div>
            </div>
        </>
    );
}
export default Cart;